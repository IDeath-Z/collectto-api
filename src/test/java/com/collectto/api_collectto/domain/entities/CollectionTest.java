package com.collectto.api_collectto.domain.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.collectto.api_collectto.domain.entities.fixtures.CollectionFakeData;
import com.collectto.api_collectto.domain.enums.Visibility;

import converters.InstantConverter;
import converters.UUIDConverter;
import converters.VisibilityConverter;

public class CollectionTest {

    private static final String CSV_PATH = "/entities/invalid_collections.csv";

    @Nested
    @DisplayName("createNewCollection()")
    class CreateNewCollection {

        @Test
        @DisplayName("should create successfully with valid data")
        void shouldCreateSuccessfully() {

            Collection fake = CollectionFakeData.generate();

            Collection collection = Collection.createNewCollection(
                fake.getUserId(),
                fake.getName(),
                fake.getDescription(),
                fake.getCoverImageUrl(),
                fake.getVisibility(),
                fake.getTags()
            );

            assertAll(
                () -> assertNotNull(collection.getId()),
                () -> assertEquals(fake.getUserId(), collection.getUserId()),
                () -> assertEquals(fake.getName(), collection.getName()),
                () -> assertEquals(fake.getDescription(), collection.getDescription()),
                () -> assertEquals(fake.getCoverImageUrl(), collection.getCoverImageUrl()),
                () -> assertEquals(fake.getVisibility(), collection.getVisibility()),
                () -> assertEquals(0, collection.getFollowersCount()),
                () -> assertEquals(fake.getTags(), collection.getTags()),
                () -> assertTrue(collection.isActive()),
                () -> assertNotNull(collection.getCreatedAt()),
                () -> assertNotNull(collection.getUpdatedAt())
            );
        }

        @ParameterizedTest(name = "should fail: {7}")
        @CsvFileSource(resources = CSV_PATH, numLinesToSkip = 1, nullValues = {"null"})
        void shouldFailWithInvalidData(
            @ConvertWith(UUIDConverter.class) UUID id,
            @ConvertWith(UUIDConverter.class) UUID userId,
            String name,
            @ConvertWith(VisibilityConverter.class) Visibility visibility,
            int followersCount,
            @ConvertWith(InstantConverter.class) Instant createdAt,
            @ConvertWith(InstantConverter.class) Instant updatedAt,
            String expectedMessage) {

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Collection(id, userId, name, null, null, visibility, followersCount,
                    null, true, createdAt, updatedAt));
                
            assertEquals(expectedMessage, ex.getMessage());
        }
    }

    @Nested
    @DisplayName("updateCollection()")
    class UpdateCollection {

        private Collection collection;

        @BeforeEach
        void setUp() {
            collection = CollectionFakeData.generate();
        }

        @Test
        @DisplayName("should update successfully with valid data")
        void shouldUpdateSuccessfully() {
            Collection fake = CollectionFakeData.generate();

            Collection updated = collection.updateCollection(
                fake.getName(),
                fake.getDescription(),
                fake.getCoverImageUrl(),
                fake.getVisibility(),
                fake.getTags()
            );

            assertAll(
                () -> assertEquals(fake.getName(), updated.getName()),
                () -> assertEquals(fake.getDescription(), updated.getDescription()),
                () -> assertEquals(fake.getCoverImageUrl(), updated.getCoverImageUrl()),
                () -> assertEquals(fake.getVisibility(), updated.getVisibility()),
                () -> assertEquals(fake.getTags(), updated.getTags()),
                () -> assertEquals(collection.getId(), updated.getId()),
                () -> assertEquals(collection.getUserId(), updated.getUserId()),
                () -> assertEquals(collection.getFollowersCount(), updated.getFollowersCount()),
                () -> assertEquals(collection.isActive(), updated.isActive()),
                () -> assertEquals(collection.getCreatedAt(), updated.getCreatedAt()),
                () -> assertNotEquals(collection.getUpdatedAt(), updated.getUpdatedAt()),
                () -> assertNotSame(collection, updated)
            );
        }

        @Test
        @DisplayName("should preserve existing values when null is passed")
        void shouldPreserveExistingValuesWhenNullIsPassed() {
            Collection updated = collection.updateCollection(
                null,
                null,
                null,
                null,
                null
            );

            assertAll(
                () -> assertEquals(collection.getName(), updated.getName()),
                () -> assertEquals(collection.getDescription(), updated.getDescription()),
                () -> assertEquals(collection.getCoverImageUrl(), updated.getCoverImageUrl()),
                () -> assertEquals(collection.getVisibility(), updated.getVisibility()),
                () -> assertEquals(collection.getTags(), updated.getTags())
            );
        }

        @Test
        @DisplayName("should remove cover image when empty string is passed")
        void shouldRemoveCoverImageWhenEmptyStringIsPassed() {
            Collection updated = collection.updateCollection(
                null,
                null,
                "",
                null,
                null
            );

            assertNull(updated.getCoverImageUrl());
        }

        @Test
        @DisplayName("should throw when name is empty")
        void shouldThrowWhenNameIsEmpty() {
            assertThrows(IllegalArgumentException.class, () ->
                collection.updateCollection(
                    "",
                    null,
                    null,
                    null,
                    null
                )
            );
        }
    }
}
