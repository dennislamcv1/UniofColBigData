package io.collective.start;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.collective.articles.ArticleDataGateway;
import io.collective.articles.ArticleRecord;
import io.collective.articles.ArticlesController;
import io.collective.restsupport.BasicApp;
import io.collective.restsupport.NoopController;
import org.eclipse.jetty.server.handler.HandlerList;
import org.jetbrains.annotations.NotNull;

import io.collective.workflow.WorkScheduler;
import io.collective.workflow.WorkFinder;
import io.collective.workflow.Worker;
import io.collective.workflow.EndpointTask;


import java.util.List;
import java.util.TimeZone;


public class App extends BasicApp {
    private static ArticleDataGateway articleDataGateway = new ArticleDataGateway(List.of(
            new ArticleRecord(10101, "Programming Languages InfoQ Trends Report - October 2019 4", true),
            new ArticleRecord(10106, "Ryan Kitchens on Learning from Incidents at Netflix, the Role of SRE, and Sociotechnical Systems", true)
    ));

    @Override
    public void start() {
    super.start();

    // Initialize the WorkFinder and Workers
    WorkFinder<EndpointTask> finder = new MyEndpointTaskFinder(); // Replace with your actual finder implementation
    List<Worker<EndpointTask>> workers = List.of(new MyEndpointTaskWorker()); // Replace with your actual worker implementations

    // Create the WorkScheduler with a 300ms polling interval
    WorkScheduler<EndpointTask> scheduler = new WorkScheduler<>(finder, workers, 300);

    // Start the WorkScheduler
    scheduler.start();

    // Optionally, add a shutdown hook to gracefully stop the scheduler
    Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
}

    public App(int port) {
        super(port);
    }

    @NotNull
    @Override
    protected HandlerList handlerList() {
        HandlerList list = new HandlerList();
        list.addHandler(new ArticlesController(new ObjectMapper(), articleDataGateway));
        list.addHandler(new NoopController());
        return list;
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String port = System.getenv("PORT") != null ? System.getenv("PORT") : "8881";
        App app = new App(Integer.parseInt(port));
        app.start();
    }
}
