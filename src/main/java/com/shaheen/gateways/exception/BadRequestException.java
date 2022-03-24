package com.shaheen.gateways.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    private static final long serialVersionUID = -3439141591380860356L;

    public BadRequestException(String message) {
        super(message);
    }


    @Override
    protected HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
