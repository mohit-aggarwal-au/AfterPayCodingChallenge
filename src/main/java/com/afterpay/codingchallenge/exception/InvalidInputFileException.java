package com.afterpay.codingchallenge.exception;

public class InvalidInputFileException extends RuntimeException {
    public InvalidInputFileException(String errorMessage) {
        super(errorMessage);
    }
}
