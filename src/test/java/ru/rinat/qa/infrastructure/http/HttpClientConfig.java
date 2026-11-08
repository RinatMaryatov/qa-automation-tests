
package ru.rinat.qa.infrastructure.http;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ProxySpecification;
import io.restassured.specification.RequestSpecification;
import ru.rinat.qa.config.ConfigManager;
import ru.rinat.qa.config.TestConfig;

public final class HttpClientConfig {

    private HttpClientConfig() {
    }

    public static RequestSpecification forBaseUrl(String baseUrl) {

        TestConfig config = ConfigManager.getConfig();
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri(baseUrl);
        builder.setContentType(ContentType.JSON);
        builder.setAccept(ContentType.JSON);

        if (config.proxyEnabled()) {

            String proxyUser = System.getenv("PROXY_USER");
            String proxyPassword = System.getenv("PROXY_PASSWORD");

            if (proxyUser == null || proxyUser.isBlank()
                    || proxyPassword == null || proxyPassword.isBlank()) {
                throw new IllegalStateException(
                        "Проки активен, но PROXY_USER или PROXY_PASSWORD отсутствует"
                );
            }

            ProxySpecification proxy = ProxySpecification
                    .host(config.proxyHost())
                    .withPort(config.proxyPort())
                    .withAuth(proxyUser, proxyPassword);

            builder.setProxy(proxy);
        }

        return builder.build();
    }
}
