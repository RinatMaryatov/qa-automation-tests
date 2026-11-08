
package ru.rinat.qa.database;

import ru.rinat.qa.infrastructure.database.DatabaseConnection;
import ru.rinat.qa.models.db.UserDbRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersDbRepository {

    public Optional<UserDbRecord> findByEmail(String email) {

        String sql = """
                SELECT id, name, email, country, city, street
                FROM public.app_users
                WHERE email = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    return Optional.empty();
                }

                UserDbRecord user = new UserDbRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("street")
                );

                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to query users database", e
            );
        }
    }
}
