
package ru.qa.clients;

import io.restassured.response.Response;
import ru.qa.config.ConfigManager;
import ru.qa.infrastructure.http.HttpClientConfig;
import ru.qa.models.UserRequest;

import static io.restassured.RestAssured.given;

public class UsersApiClient {

    public Response getAllUsers() {

        String baseUrl = ConfigManager.getConfig().usersBaseUrl();

        return given()
                .spec(HttpClientConfig.forBaseUrl(baseUrl))
                .when()
                .get("/api/users");
    }

    public Response createUser(UserRequest request) {

        String baseUrl = ConfigManager.getConfig().usersBaseUrl();

        return given()
                .spec(HttpClientConfig.forBaseUrl(baseUrl))
                .body(request)
                .when()
                .post("/api/users");
    }

    public Response deleteUser(long id) {
        String baseUrl = ConfigManager.getConfig().usersBaseUrl();

        return given()
                .spec(HttpClientConfig.forBaseUrl(baseUrl))
                .when()
                .delete("/api/users/{id}", id);
    }

}
