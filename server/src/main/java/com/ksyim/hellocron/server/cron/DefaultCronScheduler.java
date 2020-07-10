package com.ksyim.hellocron.server.cron;

import io.netty.channel.EventLoopGroup;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class DefaultCronScheduler extends AbstractCronScheduler {

    private final EventLoopGroup workerGroup;

    protected EventLoopGroup getEventLoopGroup() {
        return workerGroup;
    }

    public long getTimeOffsetToNextRunInMillis(CronTask cronTask) {
        Date now = new Date();
        return new CronSequenceGenerator(cronTask.getCronExp()).next(now).getTime() - now.getTime();
    }
}
