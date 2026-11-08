package ru.rinat.qa.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")

public interface TestConfig extends Config{

    @Key("users.base.url")
    String usersBaseUrl();

    @Key("audit.base.url")
    String auditBaseUrl();

    @Key("proxy.enabled")
    boolean proxyEnabled();

    @Key("proxy.host")
    String proxyHost();

    @Key("proxy.port")
    int proxyPort();
}
