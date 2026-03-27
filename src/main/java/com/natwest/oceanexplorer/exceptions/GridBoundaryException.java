package com.natwest.oceanexplorer.exceptions;

public class GridBoundaryException extends RuntimeException {
    public GridBoundaryException(String message) {
        super(message);
    }

    public GridBoundaryException(String message, Throwable cause) {
        super(message, cause);
    }
}