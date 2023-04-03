package com.example.demo.util;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.service.ObservationService;
import com.example.demo.modal.Observation;

/**
 * Updates the weather observations with a given period and 'phase'.
 */
@Service
public class CronJob {
    /**
     * Default period for executing the job.
     */
    final static long DEFAULT_PERIOD = TimeUnit.HOURS.toMillis(1);

    /**
     * Default base time.
     */
    final static Instant DEFAULT_BASE_TIME = Instant.EPOCH.plusMillis(TimeUnit.MINUTES.toMillis(15));

    @Autowired
    private ObservationService service;

    public CronJob() {
        this(DEFAULT_PERIOD, DEFAULT_BASE_TIME);
    }

    /**
      * Begins periodical execution of the job.
      * @param periodMillis the interval between two consecutive executions of the job
      * @param baseTime is any of the time instants at which the job should run. This exists to be able to configure the job to execute at a given offset, for example 15 minutes after a full hour.
      */
    public CronJob(long periodMillis, Instant baseTime) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                service.fetchAndStore();
            }
        }, getInitialDelay(baseTime, periodMillis).toEpochMilli(), periodMillis, TimeUnit.MILLISECONDS);
    }

    private static Instant getInitialDelay(Instant baseTime, long period) {
        long delay = Math.floorMod(baseTime.toEpochMilli() - Instant.now().toEpochMilli(), period);

        return Instant.ofEpochMilli(delay);
    }
}
