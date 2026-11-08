package ru.rinat.qa.config;

import org.aeonbits.owner.ConfigFactory;

public final class ConfigManager {

    private static final TestConfig CONFIG =
            ConfigFactory.create(TestConfig.class);

    private ConfigManager() {
    }

    public static TestConfig getConfig() {
        return CONFIG;
    }
}
