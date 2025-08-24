package com.paurus.davor.demo.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class ParallelEventExecutor {

    private static final Logger log = LogManager.getLogger(ParallelEventExecutor.class);

    private final ExecutorService executor;
    private final ConcurrentHashMap<String, Future<?>> taskMap = new ConcurrentHashMap<>();

    private final int terminationTimeout;
    private final TimeUnit timeUnit;

    public ParallelEventExecutor(int threads, int terminationTimeout, TimeUnit timeUnit) {
        this.executor = Executors.newFixedThreadPool(threads);
        this.terminationTimeout = terminationTimeout;
        this.timeUnit = timeUnit;
    }

    public void shutdownAndAwait() {
        try {
            executor.shutdown();
            executor.awaitTermination(terminationTimeout, timeUnit);
        } catch (InterruptedException e) {
            log.error("shutdownAndAwait", e);
        }
    }

    public Future<?> submit(Runnable task, String matchId) {

        waitForPreviousTaskToComplete(matchId);

        Future<?> future = executor.submit(task);
        this.taskMap.put(matchId, future);

        return future;
    }

    private void waitForPreviousTaskToComplete(String matchId) {
        Future<?> previousTask = taskMap.get(matchId);
        if (previousTask != null) {
            try {
                previousTask.get();
            } catch (Exception e) {
                log.error("waitForPreviousTaskToComplete", e);
            }
        }
    }
}
