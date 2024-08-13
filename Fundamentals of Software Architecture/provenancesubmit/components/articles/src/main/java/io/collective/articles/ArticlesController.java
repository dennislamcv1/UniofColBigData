package io.collective.articles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.restsupport.BasicHandler;
import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ArticlesController extends BasicHandler {
    private final ArticleDataGateway gateway;

    public ArticlesController(ObjectMapper mapper, ArticleDataGateway gateway) {
        super(mapper);
        this.gateway = gateway;
    }

    @Override
    public void handle(String target, Request request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        get("/articles", List.of("application/json", "text/html"), request, servletResponse, () -> {
            List<ArticleRecord> articles = gateway.findAll();  // Fetch all articles from the gateway
            List<ArticleInfo> articleInfos = articles.stream()
                    .map(ArticleInfo::from)  // Assuming there's a static method `from` to map ArticleRecord to ArticleInfo
                    .collect(Collectors.toList());
            writeJsonBody(servletResponse, articleInfos);  // Write the list of articleInfos to the response body
        });

        get("/available", List.of("application/json"), request, servletResponse, () -> {
            List<ArticleRecord> availableArticles = gateway.findAvailable();  // Fetch available articles from the gateway
            List<ArticleInfo> articleInfos = availableArticles.stream()
                    .map(ArticleInfo::from)
                    .collect(Collectors.toList());
            writeJsonBody(servletResponse, articleInfos);  // Write the list of available articleInfos to the response body
        });
    }
}
