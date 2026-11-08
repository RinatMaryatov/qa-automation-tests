package ru.rinat.qa.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import ru.rinat.qa.clients.UsersApiClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsersApiTest {

    UsersApiClient usersApiClient = new UsersApiClient();

    @Test
    public void firstTest(){
        Response response = usersApiClient.getAllUsers();
        assertThat(response.statusCode()).isEqualTo(200);
    }

}
