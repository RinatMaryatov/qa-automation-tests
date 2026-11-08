
package ru.qa.models;

public record AuditEvent(
        Long id,
        String eventId,
        String eventType,
        String occurredAt,
        Long userId,
        String name,
        String email,
        Geolocation geolocation
) {
}
