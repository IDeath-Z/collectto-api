package com.collectto.api_collectto.domain.ports;

import com.collectto.api_collectto.domain.entities.BetaSignup;

public interface BetaSignupRepository {
    
    BetaSignup save(BetaSignup signup);
    boolean existsByEmail(String email);
}
