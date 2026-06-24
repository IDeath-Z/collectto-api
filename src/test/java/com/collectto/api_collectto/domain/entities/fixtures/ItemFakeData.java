package com.collectto.api_collectto.domain.entities.fixtures;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import com.collectto.api_collectto.domain.entities.Item;

import net.datafaker.Faker;

public final class ItemFakeData {

    private static final Faker faker = new Faker(Locale.of("pt", "BR"));

    private ItemFakeData() {}

    public static Item generate () {
        return new Item(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            faker.book().title(),
            faker.lorem().sentence(),
            LocalDate.now()
                .minusYears(faker.number().numberBetween(18, 50))
                .minusDays(faker.number().numberBetween(1, 365)),
            null,
            null,
            null,
            0,
            0,
            null,
            false,
            null,
            null
        );
    }
}
