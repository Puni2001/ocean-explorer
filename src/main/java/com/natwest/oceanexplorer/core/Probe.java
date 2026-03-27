package com.natwest.oceanexplorer.core;

import com.natwest.oceanexplorer.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class Probe {
    private static final Logger logger = LoggerFactory.getLogger(Probe.class);

    private volatile Position position;
    private volatile Direction direction;
    private final Grid grid;
    private final List<Position> visitedPositions;
    private final List<Position> currentPath;
    private final Set<Position> uniquePositions;

    public Probe(Position startPosition, Direction startDirection, Grid grid) {
        validateInitialization(startPosition, grid);

        this.position = startPosition;
        this.direction = startDirection;
        this.grid = grid;
        this.visitedPositions = new CopyOnWriteArrayList<>();
        this.currentPath = new CopyOnWriteArrayList<>();
        this.uniquePositions = Collections.newSetFromMap(new ConcurrentHashMap<>());

        recordPosition(startPosition);
        logger.info("Probe initialized at {} facing {}", startPosition, startDirection);
    }

    private void validateInitialization(Position position, Grid grid) {
        if (!grid.canMoveTo(position)) {
            String error = String.format("Cannot place probe at %s - position invalid or occupied", position);
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
    }


    public void executeCommand(Command command) {
        logger.trace("Executing command: {} at position {} facing {}", command, position, direction);

        switch (command) {
            case FORWARD -> move(DirectionType.FORWARD);
            case BACKWARD -> move(DirectionType.BACKWARD);
            case LEFT -> turnLeft();
            case RIGHT -> turnRight();
            default -> throw new UnsupportedCommandException("Unknown command: " + command);
        }
    }

    private void move(DirectionType type) {
        Position newPosition = type == DirectionType.FORWARD ?
                position.move(direction) :
                position.move(direction.opposite());

        if (!grid.isValidPosition(newPosition)) {
            throw new GridBoundaryException(
                    String.format("Cannot move to %s - outside grid boundaries", newPosition)
            );
        }

        if (grid.isObstacle(newPosition)) {
            throw new ObstacleCollisionException(
                    String.format("Cannot move to %s - obstacle detected", newPosition)
            );
        }

        position = newPosition;
        recordPosition(position);
        logger.debug("Moved {} to {}", type, position);
    }

    private void turnLeft() {
        direction = direction.turnLeft();
        logger.trace("Turned left, now facing {}", direction);
    }

    private void turnRight() {
        direction = direction.turnRight();
        logger.trace("Turned right, now facing {}", direction);
    }

    private void recordPosition(Position pos) {
        visitedPositions.add(pos);
        currentPath.add(pos);
        uniquePositions.add(pos);
    }


    public Position getPosition() {
        return position;
    }


    public Direction getDirection() {
        return direction;
    }


    public List<Position> getVisitedPositions() {
        return Collections.unmodifiableList(visitedPositions);
    }

    public List<Position> getCurrentPath() {
        return Collections.unmodifiableList(currentPath);
    }


    public int getUniquePositionsCount() {
        return uniquePositions.size();
    }


    public void resetPathTracking() {
        currentPath.clear();
        currentPath.add(position);
        logger.info("Path tracking reset for probe");
    }

    private enum DirectionType {
        FORWARD, BACKWARD
    }
}