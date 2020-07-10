package com.ksyim.hellocron.server.cron;

import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
abstract public class AbstractCronScheduler implements CronScheduler {

    abstract protected EventLoopGroup getEventLoopGroup();

    protected void scheduleNextTick(CronTask cronTask) {
        long offset = getTimeOffsetToNextRunInMillis(cronTask);
        EventLoopGroup workerGroup = getEventLoopGroup();

        log.info("task scheduled [eventName={}, after={}]", cronTask.getEventName(), offset);

        workerGroup.schedule(() -> {
            log.info("task dispatched [eventName={}]", cronTask.getEventName());

            cronTask.getTask().run();
            scheduleNextTick(cronTask);
        }, offset, TimeUnit.MILLISECONDS);
    }

    public void register(String eventName, String CronExpression, Runnable task) {
        scheduleNextTick(new CronTask(eventName, CronExpression, task));
    }
}
