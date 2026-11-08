package ru.qa.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.qa.clients.UsersApiClient;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetUsersTest {

    UsersApiClient usersApiClient = new UsersApiClient();

    @Test
    @DisplayName("GET /api/users — получение списка всех пользователей")
    public void getAllUsersTest(){

        // Шаг 1. Отправка GET запроса
        Response response = step ("Отправляем GET /api/users",
                () -> usersApiClient.getAllUsers());
        // Шаг 2. Проверка HTTP-ответа
        step("Проверяем статус 200", () -> {
            assertThat(response.statusCode()).isEqualTo(200);
        });
    }

}
