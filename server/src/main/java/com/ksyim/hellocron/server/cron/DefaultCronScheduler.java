package com.ksyim.hellocron.server.cron;

import com.linecorp.armeria.common.CommonPools;
import io.netty.channel.EventLoopGroup;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DefaultCronScheduler extends AbstractCronScheduler {

    protected EventLoopGroup getEventLoopGroup() {
        return CommonPools.workerGroup();
    }

    public long getTimeOffsetToNextRunInMillis(CronTask cronTask) {
        Date now = new Date();
        return new CronSequenceGenerator(cronTask.getCronExp()).next(now).getTime() - now.getTime();
    }
}