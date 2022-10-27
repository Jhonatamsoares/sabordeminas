package com.example.cafeteria.exception;

public class DishNotExistException extends Exception {

    public DishNotExistException(String message) {
        super(message);
    }
}