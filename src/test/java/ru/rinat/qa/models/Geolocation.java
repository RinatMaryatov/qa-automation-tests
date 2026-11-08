package ru.rinat.qa.models;

public record Geolocation(
        String country,
        String city,
        String street
) {
}