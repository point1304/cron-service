package com.ksyim.hellocron.server.configuration;

import com.ksyim.hellocron.server.controller.CronController;
import com.linecorp.armeria.client.ClientFactory;
import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.client.logging.LoggingClient;
import com.linecorp.armeria.common.CommonPools;
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
            "env variable `LINE_MESSAGING_API_TOKEN` is not set");
    static final int PORT = Integer.parseInt(
            requireNonNull(System.getenv("PORT"), "env variable `PORT` is not set"));

    @Bean
    public ArmeriaServerConfigurator armeriaServerConfigurator(EventLoopGroup loopGroup) {
        return builder -> {
            builder.http(PORT)
                    .workerGroup(loopGroup, false)
                    .decorator(LoggingService.newDecorator())
                    .accessLogWriter(AccessLogWriter.combined(), false)
                    .serviceUnder("/docs", new DocService());
        };
    }

    @Bean
    public EventLoopGroup getEventLoopWorkerGroup() {
        String env = requireNonNull(
                System.getenv("ARMERIA_ENV"), "env variable `ARMERIA_ENV` is not set");

        if (env.equals("production")) { return CommonPools.workerGroup(); }
        else if (env.equals("development")) { return EventLoopGroups.newEventLoopGroup(1); }
        else {
            throw new RuntimeException(String.format(
                    "env variable `ARMERIA_ENV` must either be `production` or `development` but " +
                            "`%s` was given.", env));
        }
    }

    @Bean
    public ClientFactory clientFactory(EventLoopGroup loopGroup) {
        return ClientFactory.builder()
                .workerGroup(loopGroup, false)
                .build();
    }

    @Bean
    public WebClient lineBotClient() {
        return WebClient.builder()
                .decorator(LoggingClient.newDecorator())
                .build();
    }

    @Bean
    public WebClient webClient(ClientFactory factory) {
        return WebClient.builder("http://localhost:3000")
                .factory(factory)
                .decorator(LoggingClient.newDecorator())
                .auth(OAuth2Token.of(LINE_MESSAGING_API_TOKEN))
                .build();
    }

    @Bean
    public AnnotatedServiceRegistrationBean annotatedService(CronController cronService) {
        return new AnnotatedServiceRegistrationBean()
                .setPathPrefix("/line")
                .setService(cronService)
                .setServiceName("lint-bot");
    }
}
