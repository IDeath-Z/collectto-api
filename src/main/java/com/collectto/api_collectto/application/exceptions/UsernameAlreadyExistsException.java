package com.collectto.api_collectto.application.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}
