package com.natwest.oceanexplorer.core;

public enum Command {
    FORWARD('F'),
    BACKWARD('B'),
    LEFT('L'),
    RIGHT('R');

    private final char commandChar;

    Command(char commandChar) {
        this.commandChar = commandChar;
    }

    public char getChar() {
        return commandChar;
    }

    public static Command fromChar(char c) {
        return switch (Character.toUpperCase(c)) {
            case 'F' -> FORWARD;
            case 'B' -> BACKWARD;
            case 'L' -> LEFT;
            case 'R' -> RIGHT;
            default -> throw new IllegalArgumentException("Invalid command: " + c);
        };
    }
}