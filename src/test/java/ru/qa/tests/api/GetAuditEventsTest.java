package ru.qa.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.qa.clients.AuditApiClient;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetAuditEventsTest {

    AuditApiClient auditApiClient = new AuditApiClient();

    @Test
    @DisplayName("GET /api/events — получение списка всех записей аудита")
    public void getAllEventsTest() {

        // Шаг 1. Отправка GET запроса
        Response response = step ("Отправляем GET /api/events",
                () -> auditApiClient.getAllEvents());
        // Шаг 2. Проверка HTTP-ответа
        step("Проверяем статус 200", () -> {
            assertThat(response.statusCode()).isEqualTo(200);
        });
    }
    }


