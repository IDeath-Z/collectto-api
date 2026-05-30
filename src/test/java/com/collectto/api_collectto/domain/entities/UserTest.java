package com.collectto.api_collectto.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.collectto.api_collectto.domain.entities.fixtures.UserFakeData;

import converters.InstantConverter;
import converters.LocalDateConverter;
import converters.UUIDConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final String CSV_PATH = "/entities/invalid_users.csv";

    @Nested
    @DisplayName("createNewUser()")
    class CreateNewUser {

        @Test
        @DisplayName("should create successfully with valid data")
        void shouldCreateSuccessfully() {
            // Arrange
            UserFakeData fake = UserFakeData.generate();

            // Act
            User user = User.createNewUser(fake.name(), fake.username(),
                fake.email(), fake.passwordHash(), fake.birthdayDate());

            // Assert
            assertAll(
                () -> assertNotNull(user.getId()),
                () -> assertEquals(fake.name(), user.getName()),
                () -> assertEquals(fake.username(), user.getUsername()),
                () -> assertEquals(fake.email(), user.getEmail()),
                () -> assertEquals(fake.passwordHash(), user.getPasswordHash()),
                () -> assertNull(user.getBio()),
                () -> assertNull(user.getProfilePictureUrl()),
                () -> assertNull(user.getProfileBackgroundUrl()),
                () -> assertEquals(0, user.getFollowersCount()),
                () -> assertEquals(0, user.getFollowingCount()),
                () -> assertTrue(user.isActive()),
                () -> assertEquals(fake.birthdayDate(), user.getBirthdayDate()),
                () -> assertNotNull(user.getCreationDate())
            );
        }

        @ParameterizedTest(name = "should fail: {9}")
        @CsvFileSource(resources = CSV_PATH, numLinesToSkip = 1, nullValues = {"null"})
        @DisplayName("should throw when data is invalid")
        void shouldThrowWhenDataIsInvalid(@ConvertWith(UUIDConverter.class) UUID id, String name, String username, String email, 
            String passwordHash, int followersCount, int followingCount, @ConvertWith(LocalDateConverter.class) LocalDate birthday,
            @ConvertWith(InstantConverter.class) Instant creationDate, String expectedMessage) {

            assertThrows(IllegalArgumentException.class, () ->
                new User(id, name, username, email, passwordHash,
                    null, null, null, followersCount, followingCount,
                    true, birthday, creationDate));
        }
    }

    @Nested
    @DisplayName("updateProfile()")
    class UpdateProfile {

        private User user;

        @BeforeEach
        void setUp() {
            UserFakeData fake = UserFakeData.generate();
            user = User.createNewUser(
                fake.name(), 
                fake.username(),
                fake.email(), 
                fake.passwordHash(), 
                fake.birthdayDate()
            );
        }

        @Test
        @DisplayName("should update mutable fields and preserve immutable ones")
        void shouldUpdateSuccessfully() {
            UserFakeData newFake = UserFakeData.generate();

            User updated = user.updateProfile(
                newFake.name(), 
                null, 
                newFake.bio(),
                newFake.profilePictureUrl(), 
                null, 
                null
            );

            assertAll(
                () -> assertEquals(newFake.name(), updated.getName()),
                () -> assertEquals(newFake.bio(), updated.getBio()),
                () -> assertEquals(newFake.profilePictureUrl(), updated.getProfilePictureUrl()),
                () -> assertEquals(user.getUsername(), updated.getUsername()),
                () -> assertEquals(user.getId(), updated.getId()),
                () -> assertEquals(user.getBirthdayDate(), updated.getBirthdayDate()),
                () -> assertNotSame(user, updated)
            );
        }

        @Test
        @DisplayName("should remove profile and background pictures when a empty string is passed")
        void shouldRemovePictures() {

            User userWithImages = user.updateProfile(
                null, 
                null, 
                null, 
                UserFakeData.generate().profilePictureUrl(), 
                UserFakeData.generate().profileBackgroundUrl(), 
                null);

            User updated = userWithImages.updateProfile(
                null, 
                null, 
                null,
                "", 
                "", 
                null
            );

            assertAll(
                () -> assertEquals(userWithImages.getName(), updated.getName()),
                () -> assertEquals(userWithImages.getBio(), updated.getBio()),
                () -> assertNull(updated.getProfilePictureUrl()),
                () -> assertNull(updated.getProfileBackgroundUrl()),
                () -> assertEquals(userWithImages.getUsername(), updated.getUsername()),
                () -> assertEquals(userWithImages.getId(), updated.getId()),
                () -> assertEquals(userWithImages.getBirthdayDate(), updated.getBirthdayDate()),
                () -> assertNotSame(userWithImages, updated)
            );
        }

        @Test
        @DisplayName("should throw when name is empty")
        void shouldThrowWhenNameIsEmpty() {
            assertThrows(IllegalArgumentException.class, () ->
                user.updateProfile(
                    "", 
                    null, 
                    null, 
                    null, 
                    null, 
                    null
                )
            );
        }
    }

    @Nested
    @DisplayName("updatePassword()")
    class UpdatePassword {

        private User user;

        @BeforeEach
        void setUp() {
            UserFakeData fake = UserFakeData.generate();
            user = User.createNewUser(
                fake.name(), 
                fake.username(),
                fake.email(), 
                fake.passwordHash(), 
                fake.birthdayDate()
            );
        }

        @Test
        @DisplayName("should change password and return new instance")
        void shouldUpdateSuccessfully() {
            String newHash = UserFakeData.generate().passwordHash();

            User updated = user.updatePassword(newHash);

            assertAll(
                () -> assertEquals(newHash, updated.getPasswordHash()),
                () -> assertNotEquals(user.getPasswordHash(), updated.getPasswordHash()),
                () -> assertEquals(user.getId(), updated.getId()),
                () -> assertNotSame(user, updated)
            );
        }
    }

    @Nested
    @DisplayName("activate()")
    class Activate {

        @Test
        @DisplayName("should return same instance when already active")
        void shouldReturnSameWhenAlreadyActive() {
            UserFakeData fake = UserFakeData.generate();
            User user = User.createNewUser(
                fake.name(), 
                fake.username(),
                fake.email(), 
                fake.passwordHash(), 
                fake.birthdayDate()
            );

            assertSame(user, user.activate());
        }

        @Test
        @DisplayName("should return a new instance with isActive true when user is inactive")
        void shouldActivateInactiveUser() {
            UserFakeData fake = UserFakeData.generate();
            User inactiveUser = new User(
                fake.id(), 
                fake.name(), 
                fake.username(), 
                fake.email(), 
                fake.passwordHash(),
                null, 
                null, 
                null,
                0, 
                0, 
                false,
                fake.birthdayDate(), 
                fake.creationDate()
            );

            User activatedUser = inactiveUser.activate();

            assertAll(
                () -> assertTrue(activatedUser.isActive(), "User should now be active"),
                () -> assertNotSame(inactiveUser, activatedUser, "Should create a new instance"),
                () -> assertEquals(inactiveUser.getId(), activatedUser.getId(), "ID must remain the same")
            );
        }
    }
}