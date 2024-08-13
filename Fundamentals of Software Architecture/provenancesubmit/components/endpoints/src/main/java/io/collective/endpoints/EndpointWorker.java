package io.collective.endpoints;

import io.collective.articles.ArticleDataGateway;
import io.collective.restsupport.RestTemplate;
import io.collective.workflow.Worker;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class EndpointWorker implements Worker<EndpointTask> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate template;
    private final ArticleDataGateway gateway;

    public EndpointWorker(RestTemplate template, ArticleDataGateway gateway) {
        this.template = template;
        this.gateway = gateway;
    }

    @NotNull
    @Override
    public String getName() {
        return "ready";
    }

    @Override
    public void execute(EndpointTask task) throws IOException {
        String response = template.get(task.getEndpoint(), task.getAccept());
        gateway.clear();

        // Deserialize the RSS feed
        RSS rss = new XmlMapper().readValue(response, RSS.class);

        // Map RSS items to ArticleInfo objects
        List<ArticleInfo> articleInfos = rss.getChannel().getItems().stream()
                .map(item -> new ArticleInfo(
                        item.getTitle(), 
                        item.getDescription(), 
                        item.getLink(), 
                        item.getPubDate()
                ))
                .collect(Collectors.toList());

        // Save ArticleInfo objects to the gateway
        gateway.saveAll(articleInfos);
    }
}
