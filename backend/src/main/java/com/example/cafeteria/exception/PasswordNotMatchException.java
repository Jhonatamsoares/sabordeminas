package com.example.cafeteria.exception;

public class PasswordNotMatchException extends Exception {

    public PasswordNotMatchException(String message) {
        super(message);
    }
}