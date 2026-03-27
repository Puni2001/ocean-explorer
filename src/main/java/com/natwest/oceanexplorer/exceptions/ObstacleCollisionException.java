package com.natwest.oceanexplorer.exceptions;

public class ObstacleCollisionException extends RuntimeException {
    public ObstacleCollisionException(String message) {
        super(message);
    }

    public ObstacleCollisionException(String message, Throwable cause) {
        super(message, cause);
    }
}