package com.example.cafeteria.exception;

public class OrderNotFinishedException extends Exception {

    public OrderNotFinishedException(String message) {
        super(message);
    }
}