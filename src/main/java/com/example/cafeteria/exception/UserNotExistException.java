package com.example.cafeteria.exception;

public class UserNotExistException extends Exception {

    public UserNotExistException(String message) {
        super(message);
    }
}