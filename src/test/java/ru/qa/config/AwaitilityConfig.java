package ru.qa.config;

import org.awaitility.core.ConditionFactory;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public final class AwaitilityConfig {

    private AwaitilityConfig() {
    }

    public static ConditionFactory asyncWait() {

        return await()
                .pollInSameThread()
                .atMost(Duration.ofSeconds(60))
                .pollInterval(Duration.ofSeconds(2));
    }
}
