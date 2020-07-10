package com.ksyim.hellocron.server.configuration;

import com.ksyim.hellocron.server.controller.CronController;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.util.EventLoopGroups;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.AnnotatedServiceRegistrationBean;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.netty.channel.EventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArmeriaConfiguration {

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(EventLoopGroup workerGroup) {
        return builder -> {
            builder.workerGroup(workerGroup, true)
                    .decorator(LoggingService.newDecorator())
                    .accessLogWriter(AccessLogWriter.combined(), false)
                    .serviceUnder("/docs", new DocService());
        };
    }

    @Bean
    public EventLoopGroup getEventLoopWorkerGroup() {
        return EventLoopGroups.newEventLoopGroup(4);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder("http://localhost:5008")
                .decorator(LoggingClient.newDecorator())
                .build();
    }

    @Bean
    public AnnotatedServiceRegistrationBean annotatedService(CronController cronService) {
        return new AnnotatedServiceRegistrationBean()
                .setPathPrefix("/")
                .setService(cronService)
                .setServiceName("cron-service");
    }
}
