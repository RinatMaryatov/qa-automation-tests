
package ru.rinat.qa.infrastructure.health;

import ru.rinat.qa.config.ConfigManager;
import ru.rinat.qa.config.TestConfig;
import ru.rinat.qa.infrastructure.HttpClientConfig;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public final class HealthCheckRunner {

    private HealthCheckRunner() {
    }

    public static void main(String[] args) {

        TestConfig config = ConfigManager.getConfig();

        checkReady(
                "Users Service",
                config.usersBaseUrl(),
                "/api/users"
        );
    }

    private static void checkReady(
            String serviceName,
            String baseUrl,
            String path
    ) {

        System.out.println("Check service " + serviceName);

        await()
                .atMost(Duration.ofSeconds(90))
                .pollInterval(Duration.ofSeconds(3))
                .ignoreExceptions()
                .until(() -> {

                    int status = given()
                            .spec(HttpClientConfig.forBaseUrl(baseUrl))
                            .when()
                            .get(path)
                            .statusCode();

                    return status == 200;
                });

        System.out.println(serviceName + " is ready");
    }
}
