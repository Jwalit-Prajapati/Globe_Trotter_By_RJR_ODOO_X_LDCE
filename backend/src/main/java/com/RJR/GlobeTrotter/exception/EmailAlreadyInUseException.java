package com.RJR.GlobeTrotter.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("Email already in use: " + email);
    }
}
