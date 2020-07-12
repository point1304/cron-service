package com.ksyim.hellocron.server.configuration;

import com.ksyim.hellocron.server.cron.CronScheduler;
import com.ksyim.hellocron.server.cron.DefaultCronScheduler;
import com.linecorp.armeria.common.CommonPools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CronConfiguration {

    @Bean
    public CronScheduler getCronScheduler() {
        return new DefaultCronScheduler(CommonPools.workerGroup());
    }
}