package com.woopaca.taximate.core.domain.event.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Duration;

@EnableAsync
@Configuration
public class AsynchronousSpringEventConfiguration {

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster(
            @Qualifier("eventHandlerTaskExecutor") TaskExecutor taskExecutor) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor);
        return eventMulticaster;
    }

    @Bean
    public TaskExecutor eventHandlerTaskExecutor() {
        return new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(10)
                .maxPoolSize(50)
                .queueCapacity(200)
                .threadNamePrefix("EventHandler-")
                .keepAlive(Duration.ofHours(1))
                .allowCoreThreadTimeOut(true)
                .build();
    }
}
