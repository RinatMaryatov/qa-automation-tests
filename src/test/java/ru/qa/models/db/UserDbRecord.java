package ru.qa.models.db;

public record UserDbRecord(
        Long id,
        String name,
        String email,
        String country,
        String city,
        String street
) {
}
