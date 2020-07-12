package com.ksyim.hellocron.server.cron;

import com.linecorp.armeria.common.Flags;
import io.netty.channel.EventLoopGroup;
import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: implement jitter logic
public class JitterCronScheduler extends AbstractCronScheduler {

    private static final long DELAY_CAP = 60000L;
    private static final long BASE = 2000L;
    private static final AtomicInteger TASK_COUNT = new AtomicInteger();

    private final int NUM_OF_WORKERS;

    public JitterCronScheduler(EventLoopGroup workerGroup) {
        super(workerGroup);
        NUM_OF_WORKERS = Flags.numCommonWorkers();
    }

    public JitterCronScheduler(EventLoopGroup workerGroup, int numOfWorkers) {
        super(workerGroup);
        NUM_OF_WORKERS = numOfWorkers;
    }

    @Override
    public void register(String eventName, String CronExpression, Runnable task) {
        TASK_COUNT.addAndGet(1);

        super.register(eventName, CronExpression, task);
    }

    @Override
    public long getTimeOffsetToNextRunInMillis(CronTask cronTask) {
        Date now = new Date();
        long originalOffset = new CronSequenceGenerator(cronTask.getCronExp()).next(now).getTime() - now.getTime();

        return originalOffset + getCappedFullJitterDelayInMillis();
    }

    private long getCappedFullJitterDelayInMillis() {
        return ThreadLocalRandom.current().nextLong(
                (long) Math.min(DELAY_CAP, BASE * Math.pow(2, (double) TASK_COUNT.get() / NUM_OF_WORKERS)));
    }
}
