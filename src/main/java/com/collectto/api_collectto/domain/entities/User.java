package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.collectto.api_collectto.domain.shared.DomainValidator;

public class User {

    private final UUID id;
    private final String name;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String bio;
    private final String profilePictureUrl;
    private final String profileBackgroundUrl;
    private final int followersCount;
    private final int followingCount;
    private final boolean isActive;
    private final LocalDate birthdayDate;
    private final Instant creationDate;

    public User(UUID id, String name, String username, String email, String passwordHash, String bio,
        String profilePictureUrl, String profileBackgroundUrl, int followersCount, int followingCount, boolean isActive,
        LocalDate birthdayDate, Instant creationDate) {
        this.id = DomainValidator.requireNonNull(id, "User ID is required");
        this.name = DomainValidator.requireNonBlank(name, "Name is required");
        this.username = DomainValidator.requireNonBlank(username, "Username is required");
        this.email = DomainValidator.requireNonBlank(email, "Email is required");
        this.passwordHash = DomainValidator.requireNonBlank(passwordHash, "Password is required");
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.profileBackgroundUrl = profileBackgroundUrl;
        this.followersCount = DomainValidator.requireNonNegative(followersCount, "Followers count cannot be negative");
        this.followingCount = DomainValidator.requireNonNegative(followingCount, "Following count cannot be negative");
        this.isActive = isActive;
        this.birthdayDate = DomainValidator.requireNonNull(birthdayDate, "Birthday date is required");
        this.creationDate = DomainValidator.requireNonNull(creationDate, "Creation date is required");
    }

    public static User createNewUser(String name, String username, String email, String passwordHash, LocalDate birthdayDate) {
        return new User(
            UUID.randomUUID(),
            name,
            username,
            email,
            passwordHash,
            null,
            null,
            null,
            0,
            0,
            true,
            birthdayDate,
            Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getProfileBackgroundUrl() {
        return profileBackgroundUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDate getBirthdayDate() {
        return birthdayDate;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public User activate() {
        if (this.isActive)
            return this;

        return new User(
            this.id,
            this.name,
            this.username,
            this.email,
            this.passwordHash,
            this.bio,
            this.profilePictureUrl,
            this.profileBackgroundUrl,
            this.followersCount,
            this.followingCount,
            true,
            this.birthdayDate,
            this.creationDate
        );
    }

    public User updateProfile(String name, String username, String bio, String profilePictureUrl,
        String profileBackgroundUrl, LocalDate birthdayDate) {
        String processPicture = (profilePictureUrl != null && profilePictureUrl.isEmpty()) ? null : 
            (profilePictureUrl != null) ? profilePictureUrl : this.profilePictureUrl;

        String processBackground = (profileBackgroundUrl != null && profileBackgroundUrl.isEmpty()) ? null : 
            (profileBackgroundUrl != null) ? profileBackgroundUrl : this.profileBackgroundUrl;
        
        return new User(
            this.id,
            name != null ? name : this.name,
            username != null ? username : this.username,
            this.email,
            this.passwordHash,
            bio != null ? bio : this.bio,
            processPicture,
            processBackground,
            this.followersCount,
            this.followingCount,
            this.isActive,
            birthdayDate != null ? birthdayDate : this.birthdayDate,
            this.creationDate
        );
    }

    public User updatePassword(String newPasswordHash) {
        return new User(
            this.id,
            this.name,
            this.username,
            this.email,
            newPasswordHash,
            this.bio,
            this.profilePictureUrl,
            this.profileBackgroundUrl,
            this.followersCount,
            this.followingCount,
            this.isActive,
            this.birthdayDate,
            this.creationDate
        );
    }
}
