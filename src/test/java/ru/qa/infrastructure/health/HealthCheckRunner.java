
package ru.qa.infrastructure.health;

import io.restassured.response.Response;
import ru.qa.config.ConfigManager;
import ru.qa.config.TestConfig;
import ru.qa.infrastructure.http.HttpClientConfig;

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

        checkReady(
                "Audit Service",
                config.auditBaseUrl(),
                "/api/events"
        );

        System.out.println("All services are READY!");
    }

    private static void checkReady(
            String serviceName,
            String baseUrl,
            String path
    ) {

        System.out.println("Checking: " + serviceName);

        await()
                .atMost(Duration.ofSeconds(90))
                .pollInterval(Duration.ofSeconds(3))
                .until(() -> {

                    try {

                        Response response = given()
                                .spec(HttpClientConfig.forBaseUrl(baseUrl, false))                                .when()
                                .get(path);

                        int statusCode = response.statusCode();

                        System.out.println(
                                serviceName + " -> HTTP " + statusCode
                        );

                        return statusCode == 200;

                    } catch (Exception e) {

                        System.out.println(
                                serviceName + " -> ERROR: " + e.getMessage()
                        );

                        return false;
                    }
                });

        System.out.println(serviceName + " is READY!");
    }
}
