package ru.qa.models;

public record UserData(
        String name,
        String email,
        Geolocation geolocation
) {
}