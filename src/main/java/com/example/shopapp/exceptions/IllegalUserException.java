package com.example.shopapp.exceptions;

public class IllegalUserException extends RuntimeException{
    public IllegalUserException(String message) {
        super(message);
    }
}
