package ru.rinat.qa.models;

public record UserData(
        String name,
        String email,
        Geolocation geolocation
) {
}