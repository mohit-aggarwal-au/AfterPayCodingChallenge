package com.afterpay.codingchallenge.exception;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String errorMessage) {
        super(errorMessage);
    }
}
