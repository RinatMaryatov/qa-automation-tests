package ru.qa.tests.api;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.qa.clients.UsersApiClient;
import ru.qa.data.UserFactory;
import ru.qa.database.UsersDbRepository;
import ru.qa.models.UserRequest;
import ru.qa.models.db.UserDbRecord;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserCreationTest {

    private final UsersApiClient usersApiClient = new UsersApiClient();

    private final UsersDbRepository usersDbRepository = new UsersDbRepository();

    private String createdUserEmail;

    @Test
    @DisplayName("POST /api/users — успешное создание пользователя")
    public void createUserTest() {

        // Шаг 1. Генерация тестовых данных
        UserRequest request = step(
                "Подготавливаем случайного пользователя",
                () -> UserFactory.randomUser()
        );

        // Сохранили email сгенерированного пользователя для будущего удаления
        createdUserEmail = request.data().email();

        // Шаг 2. Создание пользователя через API
        Response response = step(
                "Отправляем POST /api/users",
                () -> usersApiClient.createUser(request)
        );

        // Шаг 3. Проверка HTTP-ответа
        step("Проверяем статус 201 Created", () -> {
            assertThat(response.statusCode()).isEqualTo(201);
        });

        // Шаг 4. Получаем запись из PostgreSQL
        UserDbRecord savedUser = step(
                "Получаем созданного пользователя из PostgreSQL",
                () -> usersDbRepository
                        .findByEmail(request.data().email())
                        .orElseThrow(() -> new AssertionError(
                                "Пользователь не найден в БД"
                        ))
        );

        // Шаг 5. Сверяем данные
        step("Проверяем сохранённые поля пользователя", () -> {

            assertThat(savedUser.id()).isPositive();

            assertThat(savedUser.name())
                    .isEqualTo(request.data().name());

            assertThat(savedUser.email())
                    .isEqualTo(request.data().email());

            assertThat(savedUser.country())
                    .isEqualTo(request.data().geolocation().country());

            assertThat(savedUser.city())
                    .isEqualTo(request.data().geolocation().city());

            assertThat(savedUser.street())
                    .isEqualTo(request.data().geolocation().street());
        });
    }


    @AfterEach
    void cleanup() {

        if (createdUserEmail == null) {
            return;
        }

        Allure.step("Удаляем созданного пользователя", () -> {

            usersDbRepository.findByEmail(createdUserEmail)
                    .ifPresent(user -> {

                        Response response = usersApiClient.deleteUser(user.id());

                        assertThat(response.statusCode())
                                .isIn(200, 204);
                    });
        });

        Allure.step("Проверяем удаление пользователя из PostgreSQL", () -> {

            assertThat(usersDbRepository.findByEmail(createdUserEmail))
                    .isEmpty();
        });
    }


}
