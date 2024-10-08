package com.woopaca.taximate.core.domain.party.service.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class ConcurrencyTest {

    protected void executeTasksInParallel(IntConsumer consumer, int executionCount) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(executionCount);
        CountDownLatch countDownLatch = new CountDownLatch(executionCount);

        IntStream.rangeClosed(1, executionCount)
                .forEach(i -> executorService.execute(() -> {
                    try {
                        consumer.accept(i - 1);
                    } catch (Exception e) {
                        log.error("Error: ", e);
                    } finally {
                        countDownLatch.countDown();
                    }
                }));

        countDownLatch.await();
        executorService.shutdown();
    }
}
