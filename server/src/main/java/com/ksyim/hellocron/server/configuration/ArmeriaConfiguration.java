package com.ksyim.hellocron.server.configuration;

import com.ksyim.hellocron.server.controller.CronController;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.auth.OAuth2Token;
import com.linecorp.armeria.common.util.EventLoopGroups;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import com.linecorp.armeria.server.logging.LoggingService;
import com.linecorp.armeria.spring.AnnotatedServiceRegistrationBean;
import com.linecorp.armeria.spring.ArmeriaServerConfigurator;
import io.netty.channel.EventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

@Configuration
public class ArmeriaConfiguration {

    static final String LINE_MESSAGING_API_TOKEN = requireNonNull(
            System.getenv("LINE_MESSAGING_API_TOKEN"),
            "environment variable `LINE_MESSAGING_API_TOKEN` is not set");

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator() {
        return builder -> {
            builder
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
    public WebClient lineBotClient() {
        return WebClient.builder()
                .decorator(LoggingClient.newDecorator())
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder("https://api.line.me")
                .decorator(LoggingClient.newDecorator())
                .auth(OAuth2Token.of(LINE_MESSAGING_API_TOKEN))
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
