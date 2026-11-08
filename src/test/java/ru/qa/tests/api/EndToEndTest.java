
package ru.qa.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.qa.clients.AuditApiClient;
import ru.qa.clients.UsersApiClient;
import ru.qa.data.UserFactory;
import ru.qa.database.UsersDbRepository;
import ru.qa.models.AuditEvent;
import ru.qa.models.UserRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.qa.config.AwaitilityConfig.asyncWait;

public class EndToEndTest {

    private final UsersApiClient usersApiClient = new UsersApiClient();
    private final AuditApiClient auditApiClient = new AuditApiClient();
    private final UsersDbRepository usersDbRepository = new UsersDbRepository();

    private String createdUserEmail;

    @Test
    @DisplayName("E2E — создание пользователя и проверка события в Audit Service")
    public void createUserAndCheckAuditServiceTest() {

        // Шаг 1. Генерируем пользователя
        UserRequest request = step(
                "Подготавливаем случайного пользователя",
                () -> UserFactory.randomUser()
        );

        createdUserEmail = request.data().email();

        // Шаг 2. Создаём пользователя
        Response response = step(
                "Отправляем POST /api/users",
                () -> usersApiClient.createUser(request)
        );

        // Шаг 3. Проверяем HTTP-статус
        step("Проверяем статус 201 Created", () -> {
            assertThat(response.statusCode()).isEqualTo(201);
        });

        // Шаг 4. Ожидаем событие во втором сервисе
        step("Ожидаем событие создания пользователя в Audit Service", () -> {

           asyncWait()
                    .untilAsserted(() -> {

                        // Каждый раз выполняем новый GET
                        Response auditResponse = auditApiClient.getAllEvents();

                        assertThat(auditResponse.statusCode()).isEqualTo(200);

                        // JSON -> Java-массив
                        AuditEvent[] events =
                                auditResponse.as(AuditEvent[].class);

                        // Массив -> List
                        List<AuditEvent> eventsList = Arrays.asList(events);

                        // Находим событие нашего пользователя
                        Optional<AuditEvent> foundEvent = eventsList.stream()
                                .filter(event ->
                                        createdUserEmail.equals(event.email()))
                                .findFirst();

                        // Проверяем, что событие найдено
                        assertThat(foundEvent).isPresent();

                        // Получаем объект из Optional
                        AuditEvent event = foundEvent.orElseThrow();

                        // Сравниваем данные
                        assertThat(event.name())
                                .isEqualTo(request.data().name());

                        assertThat(event.email())
                                .isEqualTo(request.data().email());

                        assertThat(event.geolocation()).isNotNull();

                        assertThat(event.geolocation().country())
                                .isEqualTo(request.data().geolocation().country());

                        assertThat(event.geolocation().city())
                                .isEqualTo(request.data().geolocation().city());

                        assertThat(event.geolocation().street())
                                .isEqualTo(request.data().geolocation().street());
                    });
        });


    }

    @AfterEach
    void cleanup() {

        if (createdUserEmail == null) {
            return;
        }

        step("Удаляем созданного пользователя", () -> {

            usersDbRepository.findByEmail(createdUserEmail)
                    .ifPresent(user -> {

                        Response deleteResponse =
                                usersApiClient.deleteUser(user.id());

                        assertThat(deleteResponse.statusCode())
                                .isIn(200, 204);
                    });
        });

        step("Проверяем удаление пользователя из PostgreSQL", () -> {

            assertThat(usersDbRepository.findByEmail(createdUserEmail))
                    .isEmpty();
        });
    }
}
