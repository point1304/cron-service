package com.ksyim.hellocron.server.cron;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CronTask {
    private String eventName;
    private String cronExp;
    private Runnable task;
}
