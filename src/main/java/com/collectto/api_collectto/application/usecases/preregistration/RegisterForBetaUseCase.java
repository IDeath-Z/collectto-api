package com.collectto.api_collectto.application.usecases.preregistration;

import com.collectto.api_collectto.application.exceptions.ResourceAlreadyExistsException;
import com.collectto.api_collectto.domain.entities.BetaSignup;
import com.collectto.api_collectto.domain.enums.PreRegisterOrigin;
import com.collectto.api_collectto.domain.ports.BetaSignupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RegisterForBetaUseCase {

    private final BetaSignupRepository betaSignupRepository;

    public record Input(String name, String email, String collects, PreRegisterOrigin source) {}

    public void execute(Input input) {
        if (betaSignupRepository.existsByEmail(input.email()))
            throw new ResourceAlreadyExistsException("Email is already registered for beta");

        BetaSignup betaSignup = BetaSignup.create(
            input.name(),
            input.email(),
            input.collects(),
            input.source()
        );

        betaSignupRepository.save(betaSignup);
    }
}
