package com.collectto.api_collectto.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.collectto.api_collectto.domain.entities.User;

@Service
public class SpringSecurityAuthentication {

    @Autowired
    AuthenticationManager authenticationManager;

    public User authenticate(String email, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(email, password);
        var authentication = authenticationManager.authenticate(authToken);

        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUserDetails) {
            return ((SecurityUserDetails) principal).getUser();
        }
        throw new IllegalStateException("Unexpected principal type: " + principal.getClass()); // Implement better exception handling later
    }
}
