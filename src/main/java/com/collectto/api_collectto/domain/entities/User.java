package com.collectto.api_collectto.domain.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String bio;
    private final String profilePictureUrl;
    private final int followersCount;
    private final int followingCount;
    private final boolean isActive;
    private final LocalDate birthdayDate;
    private final Instant creationDate;

    public User(UUID id, String name, String username, String email, String passwordHash, String bio,
            String profilePictureUrl, int followersCount, int followingCount, boolean isActive,
            LocalDate birthdayDate, Instant creationDate) {

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name is required");
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username is required");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email is required");
        if (passwordHash == null || passwordHash.isBlank())
            throw new IllegalArgumentException("Password is required");
        if (birthdayDate == null || birthdayDate.toString().isBlank())
            throw new IllegalArgumentException("Birthday date is required");

        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.isActive = isActive;
        this.birthdayDate = birthdayDate;
        this.creationDate = creationDate;
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
}
