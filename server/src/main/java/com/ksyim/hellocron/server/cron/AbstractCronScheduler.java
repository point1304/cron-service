package com.ksyim.hellocron.server.cron;

import com.linecorp.armeria.common.CommonPools;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
abstract public class AbstractCronScheduler implements CronScheduler {

    protected final EventLoopGroup workerGroup;

    public AbstractCronScheduler() {
        this.workerGroup = CommonPools.workerGroup();
    }

    public AbstractCronScheduler(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    protected void scheduleNextTick(CronTask cronTask) {
        long offset = getTimeOffsetToNextRunInMillis(cronTask);
        EventLoopGroup workerGroup = getWorkerGroup();

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
