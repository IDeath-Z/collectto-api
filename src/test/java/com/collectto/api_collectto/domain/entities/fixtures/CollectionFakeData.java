package com.collectto.api_collectto.domain.entities.fixtures;

import java.time.Instant;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Collection;
import com.collectto.api_collectto.domain.enums.Visibility;

import net.datafaker.Faker;

public record CollectionFakeData() {
    private static final Faker faker = new Faker();

    public static Collection generate() {
        return new Collection(
            UUID.randomUUID(),
            UUID.randomUUID(),
            faker.book().title(),
            faker.lorem().sentence(),
            "https://picsum.photos/seed/" + UUID.randomUUID() + "/800/400",
            Visibility.values()[faker.random().nextInt(Visibility.values().length)],
            faker.number().numberBetween(0, 500),
            faker.lorem().words(faker.number().numberBetween(1, 5)),
            faker.bool().bool(),
            Instant.now().minusSeconds(faker.number().numberBetween(1000, 1000000)),
            Instant.now().minusSeconds(faker.number().numberBetween(0, 500)) 
        );
    }
}
