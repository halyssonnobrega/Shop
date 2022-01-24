package com.demo.shop.business.exception;

import org.springframework.http.HttpStatus;

public abstract class GenericCustomException extends RuntimeException {

    private HttpStatus status;

    private String error;

    public GenericCustomException(HttpStatus status, String error, String detailMessage) {
        super(detailMessage);
        this.status = status;
        this.error = error;
    }

    public GenericCustomException(HttpStatus status, String error) {
        this.status = status;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
