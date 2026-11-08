
package ru.rinat.qa.infrastructure.database;

import ru.rinat.qa.config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {

        String url = ConfigManager.getConfig().usersDbUrl();

        String username = System.getenv("DB_USERS_USERNAME");
        String password = System.getenv("DB_USERS_PASSWORD");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Database credentials are missing"
            );
        }

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }
}
