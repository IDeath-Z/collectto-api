package com.collectto.api_collectto.domain.ports;

import java.time.Instant;

public interface TokenProvider {
    
    String generate(String subject, Instant expiration);
    String validate(String token);
}
