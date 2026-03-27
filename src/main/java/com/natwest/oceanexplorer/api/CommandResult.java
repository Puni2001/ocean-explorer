package com.natwest.oceanexplorer.api;

import com.natwest.oceanexplorer.core.Command;
import com.natwest.oceanexplorer.core.Direction;
import com.natwest.oceanexplorer.core.Position;

public class CommandResult {
    private final Command command;
    private final Position previousPosition;
    private final Position newPosition;
    private final Direction previousDirection;
    private final Direction newDirection;
    private final long executionTimeNanos;
    private final boolean successful;
    private final String errorMessage;

    public CommandResult(Command command, Position previousPosition, Position newPosition,
                         Direction previousDirection, Direction newDirection,
                         long executionTimeNanos, boolean successful, String errorMessage) {
        this.command = command;
        this.previousPosition = previousPosition;
        this.newPosition = newPosition;
        this.previousDirection = previousDirection;
        this.newDirection = newDirection;
        this.executionTimeNanos = executionTimeNanos;
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    // Getters
    public Command getCommand() { return command; }
    public Position getPreviousPosition() { return previousPosition; }
    public Position getNewPosition() { return newPosition; }
    public Direction getPreviousDirection() { return previousDirection; }
    public Direction getNewDirection() { return newDirection; }
    public long getExecutionTimeNanos() { return executionTimeNanos; }
    public boolean isSuccessful() { return successful; }
    public String getErrorMessage() { return errorMessage; }
}