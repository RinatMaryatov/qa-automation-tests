package ru.rinat.qa.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rinat.qa.clients.UsersApiClient;
import ru.rinat.qa.data.UserFactory;
import ru.rinat.qa.database.UsersDbRepository;
import ru.rinat.qa.models.UserRequest;
import ru.rinat.qa.models.db.UserDbRecord;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserCreationTest {

    private final UsersApiClient usersApiClient = new UsersApiClient();

    private final UsersDbRepository usersDbRepository =
            new UsersDbRepository();

    @Test
    @DisplayName("POST /api/users — успешное создание пользователя")
    public void createUserTest() {


        // Шаг 1. Генерация тестовых данных
        UserRequest request = step(
                "Подготавливаем случайного пользователя",
                () -> UserFactory.randomUser()
        );

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



}
