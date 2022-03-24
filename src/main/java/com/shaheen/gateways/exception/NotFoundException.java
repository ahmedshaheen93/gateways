package com.shaheen.gateways.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    private static final long serialVersionUID = 332188626719993408L;

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
