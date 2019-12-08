package com.afterpay.codingchallenge.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String errorMessage) {
        super(errorMessage);
    }
}
