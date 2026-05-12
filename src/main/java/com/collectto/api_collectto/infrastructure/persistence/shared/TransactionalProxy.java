package com.collectto.api_collectto.infrastructure.persistence.shared;

import java.util.function.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// To guarantee the atomicity of the operations, use to call the use cases methods in controllers.

@Component
public class TransactionalProxy {

    @Transactional
    public <T> T execute(Supplier<T> action) {
        return action.get(); 
    }

    @Transactional
    public void execute(Runnable action) {
        action.run();
    }

    @Transactional(readOnly = true)
    public <T> T executeReadOnly(Supplier<T> action) {
        return action.get();
    }
}
