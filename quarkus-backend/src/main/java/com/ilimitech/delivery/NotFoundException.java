package com.ilimitech.delivery;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}