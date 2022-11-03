package com.example.cafeteria.exception;

public class OrderAlreadyCheckoutException extends Exception {

    public OrderAlreadyCheckoutException(String message) {
        super(message);
    }
}