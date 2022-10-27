package com.example.cafeteria.exception;

public class OrderAlreadyDeliverException extends Exception {

    public OrderAlreadyDeliverException(String message) {
        super(message);
    }
}