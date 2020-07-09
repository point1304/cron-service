package com.ksyim.hellocron.server.configuration;

import com.ksyim.hellocron.server.controller.CronController;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.AnnotatedServiceRegistrationBean;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ArmeriaConfiguration {

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(WebClient webClient) {
        return builder -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup(1);
            workerGroup.scheduleAtFixedRate(() -> {
                System.out.println("working");
            }, 5000, 5000, TimeUnit.MILLISECONDS);
            builder.workerGroup(workerGroup, true)
                    .decorator(LoggingService.newDecorator())
                    .accessLogWriter(AccessLogWriter.combined(), false)
                    .serviceUnder("/docs", new DocService());
        };
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder("http://127.0.0.1:5000")
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
