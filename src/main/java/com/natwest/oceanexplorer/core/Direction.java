package com.natwest.oceanexplorer.core;

public enum Direction {
    NORTH(0, 1, "↑"),
    EAST(1, 0, "→"),
    SOUTH(0, -1, "↓"),
    WEST(-1, 0, "←");

    private final int dx;
    private final int dy;
    private final String arrow;

    Direction(int dx, int dy, String arrow) {
        this.dx = dx;
        this.dy = dy;
        this.arrow = arrow;
    }

    public Direction turnLeft() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public int getDx() { return dx; }
    public int getDy() { return dy; }
    public String getArrow() { return arrow; }

    @Override
    public String toString() {
        return name() + " " + arrow;
    }
}