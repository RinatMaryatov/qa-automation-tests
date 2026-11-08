
package ru.rinat.qa.data;

import net.datafaker.Faker;
import ru.rinat.qa.models.Geolocation;
import ru.rinat.qa.models.UserData;
import ru.rinat.qa.models.UserRequest;

import java.util.Locale;
import java.util.UUID;

public final class UserFactory {



    private UserFactory() {
    }

    public static UserRequest randomUser() {

        Faker faker = new Faker(Locale.forLanguageTag("ru-RU"));

        String country = faker.address().country();
        String city = faker.address().city();
        String street = faker.address().streetName();
        String name = faker.name().fullName();
        String email = UUID.randomUUID() + "@" + faker.internet().domainName();

        Geolocation geo = new Geolocation(
               country,
                city,
                street
        );

        UserData data = new UserData(name, email, geo);

        return new UserRequest(data);
    }
}
