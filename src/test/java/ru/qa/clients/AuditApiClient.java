package ru.qa.clients;

import io.restassured.response.Response;
import ru.qa.config.ConfigManager;
import ru.qa.infrastructure.http.HttpClientConfig;

import static io.restassured.RestAssured.given;

public class AuditApiClient {

    public Response getAllEvents() {

        String baseUrl = ConfigManager.getConfig().auditBaseUrl();

        return given()
                .spec(HttpClientConfig.forBaseUrl(baseUrl))
                .when()
                .get("/api/events");
    }

}
