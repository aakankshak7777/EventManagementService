package com.kmbl.eventmanagementservice.unittest.utils;

import com.kmbl.eventmanagementservice.utils.ThreadUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ThreadUtilTest {

    @Test
    void newThreadFactory_ValidPrefix_ReturnsThreadFactory() {
        var threadFactory = ThreadUtil.newThreadFactory("test-prefix");
        Assertions.assertNotNull(threadFactory);
        var thread = threadFactory.newThread(() -> {});
        Assertions.assertTrue(thread.getName().startsWith("test-prefix-"));
    }

    @Test
    void shutdownExecutor_NullExecutor_DoesNothing() {
        ThreadUtil.shutdownExecutor(null);
    }

    @Test
    void shutdownExecutor_ExecutorShutdownsInTime_Success() throws InterruptedException {
        var executor = Mockito.mock(ExecutorService.class);
        Mockito.when(executor.awaitTermination(5000L, TimeUnit.MILLISECONDS)).thenReturn(true);
        ThreadUtil.shutdownExecutor(executor);
        Mockito.verify(executor).shutdown();
        Mockito.verify(executor).awaitTermination(5000L, TimeUnit.MILLISECONDS);
    }

    @Test
    void shutdownExecutor_ExecutorDoesNotShutdownInTime_ForceShutdown() throws InterruptedException {
        // Arrange
        var executor = Mockito.mock(ExecutorService.class);
        Mockito.when(executor.awaitTermination(5000L, TimeUnit.MILLISECONDS)).thenReturn(false);
        ThreadUtil.shutdownExecutor(executor);
        Mockito.verify(executor).shutdown();
        Mockito.verify(executor).awaitTermination(5000L, TimeUnit.MILLISECONDS);
        Mockito.verify(executor).shutdownNow();
    }

    @Test
    void shutdownExecutor_InterruptedException_ThrowsRuntimeException() throws InterruptedException {
        var executor = Mockito.mock(ExecutorService.class);
        Mockito.when(executor.awaitTermination(5000L, TimeUnit.MILLISECONDS)).thenThrow(new InterruptedException());
        Assertions.assertThrows(RuntimeException.class, () -> ThreadUtil.shutdownExecutor(executor));
    }
}
