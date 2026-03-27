package com.natwest.oceanexplorer.exceptions;

public class UnsupportedCommandException extends RuntimeException {
    public UnsupportedCommandException(String message) {
        super(message);
    }

    public UnsupportedCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}