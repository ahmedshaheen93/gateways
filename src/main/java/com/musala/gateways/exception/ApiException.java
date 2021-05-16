package com.musala.gateways.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    private static final long serialVersionUID = 449151071379057001L;

    protected ApiException(String message) {
        super(message);
    }

    protected abstract HttpStatus getHttpStatus();
}
