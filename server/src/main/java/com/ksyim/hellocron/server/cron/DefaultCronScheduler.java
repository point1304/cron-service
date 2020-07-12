package com.ksyim.hellocron.server.cron;

import io.netty.channel.EventLoopGroup;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

public class DefaultCronScheduler extends AbstractCronScheduler {

    public DefaultCronScheduler(EventLoopGroup workerGroup) {
        super(workerGroup);
    }

    @Override
    public long getTimeOffsetToNextRunInMillis(CronTask cronTask) {
        Date now = new Date();
        return new CronSequenceGenerator(cronTask.getCronExp()).next(now).getTime() - now.getTime();
    }
}
