package com.riglerr.data.exceptions;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {
        super();
    }
    public InvalidCommandException(String message) {
        super(message);
    }
}
