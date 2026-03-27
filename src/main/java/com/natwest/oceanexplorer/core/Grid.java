package com.natwest.oceanexplorer.core;

import java.util.HashSet;
import java.util.Set;

public class Grid {
    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;
    private final Set<Position> obstacles;

    private Grid(Builder builder) {
        this.minX = builder.minX;
        this.minY = builder.minY;
        this.maxX = builder.maxX;
        this.maxY = builder.maxY;
        this.obstacles = new HashSet<>(builder.obstacles);
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= minX && position.getX() <= maxX &&
                position.getY() >= minY && position.getY() <= maxY;
    }

    public boolean isObstacle(Position position) {
        return obstacles.contains(position);
    }

    public boolean canMoveTo(Position position) {
        return isValidPosition(position) && !isObstacle(position);
    }

    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public Set<Position> getObstacles() { return new HashSet<>(obstacles); }

    public static class Builder {
        private int minX = 0;
        private int minY = 0;
        private int maxX = Integer.MAX_VALUE;
        private int maxY = Integer.MAX_VALUE;
        private Set<Position> obstacles = new HashSet<>();

        public Builder withBounds(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }

        public Builder withObstacles(Set<Position> obstacles) {
            this.obstacles = obstacles != null ? new HashSet<>(obstacles) : new HashSet<>();
            return this;
        }

        public Grid build() {
            if (minX > maxX || minY > maxY) {
                throw new IllegalArgumentException("Invalid grid bounds: min must be less than max");
            }
            return new Grid(this);
        }
    }
}