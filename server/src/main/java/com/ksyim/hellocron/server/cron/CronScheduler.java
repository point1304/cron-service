package com.ksyim.hellocron.server.cron;

public interface CronScheduler {
    void register(String eventName, String cronExp, Runnable task);
    long getTimeOffsetToNextRunInMillis(CronTask cronTask);
}
