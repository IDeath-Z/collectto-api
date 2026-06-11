package com.collectto.api_collectto.domain.entities.fixtures;

import net.datafaker.Faker;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.User;

public record UserFakeData() {
    private static final Faker faker = new Faker(Locale.of("pt", "BR"));

    public static User generate() {
        return buildData(true);
    }

    public static User generateInactive() {
        return buildData(false);
    }

    private static User buildData(Boolean isActive) {
        return new User(
            UUID.randomUUID(),
            faker.name().fullName(),
            faker.internet().username(),
            faker.internet().emailAddress(),
            faker.internet().password(8, 16),
            faker.lorem().sentence(10), 
            "https://api.dicebear.com/8.x/avataaars/svg?seed=" + faker.internet().username(),
            "https://picsum.photos/seed/" + UUID.randomUUID().toString() + "/800/400",
            faker.number().numberBetween(0, 10000), 
            faker.number().numberBetween(0, 500),
            isActive, 
            LocalDate.now()
                    .minusYears(faker.number().numberBetween(18, 50))
                    .minusDays(faker.number().numberBetween(1, 365)),
            Instant.now().minusSeconds(faker.number().numberBetween(1000, 1000000)) 
        );
    }
}