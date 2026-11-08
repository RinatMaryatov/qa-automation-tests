
package ru.rinat.qa.config;

import org.aeonbits.owner.ConfigFactory;
import java.util.Map;

public final class ConfigManager {

    private static final TestConfig CONFIG = createConfig();

    private ConfigManager() {
    }

    private static TestConfig createConfig() {

        String proxyEnabled = System.getenv("PROXY_ENABLED");

        if (proxyEnabled == null || proxyEnabled.isBlank()) {
            return ConfigFactory.create(TestConfig.class);
        }

        return ConfigFactory.create(
                TestConfig.class,
                Map.of("proxy.enabled", proxyEnabled)
        );
    }

    public static TestConfig getConfig() {
        return CONFIG;
    }
}
