package com.smartJob.exception;

public class AlreadyRegisterException extends RuntimeException {
    public AlreadyRegisterException(String message) {
        super(message);
    }
}
