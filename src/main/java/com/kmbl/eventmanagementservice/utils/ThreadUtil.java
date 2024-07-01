package com.kmbl.eventmanagementservice.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadUtil {

    public static ThreadFactory newThreadFactory(String prefix) {
        return new ThreadFactoryBuilder().setNameFormat(prefix + "-%d").build();
    }

    public static void shutdownExecutor(@Nullable ExecutorService executor) {
        if (executor == null) {
            return;
        }
        try {
            executor.shutdown();
            var terminated = executor.awaitTermination(5000L, TimeUnit.MILLISECONDS);
            if (!terminated) {
                log.info("Executor service hasn't shut down in allotted time. Forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
