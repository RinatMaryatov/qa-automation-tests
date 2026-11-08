
package ru.rinat.qa.clients;

import io.restassured.response.Response;
import ru.rinat.qa.config.ConfigManager;
import ru.rinat.qa.infrastructure.http.HttpClientConfig;

import static io.restassured.RestAssured.given;

public class UsersApiClient {

    public Response getAllUsers() {

        String baseUrl = ConfigManager.getConfig().usersBaseUrl();

        return given()
                .spec(HttpClientConfig.forBaseUrl(baseUrl))
                .when()
                .get("/api/users");
    }
}
