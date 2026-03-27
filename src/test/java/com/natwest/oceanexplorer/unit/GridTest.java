package com.natwest.oceanexplorer.unit;

import com.natwest.oceanexplorer.core.Grid;
import com.natwest.oceanexplorer.core.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Set;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    @DisplayName("Should create grid with bounds")
    void testGridCreation() {
        Grid grid = new Grid.Builder()
                .withBounds(0, 0, 10, 10)
                .build();

        assertTrue(grid.isValidPosition(new Position(5, 5)));
        assertFalse(grid.isValidPosition(new Position(11, 5)));
        assertFalse(grid.isValidPosition(new Position(5, -1)));
    }

    @Test
    @DisplayName("Should detect obstacles")
    void testObstacles() {
        Set<Position> obstacles = new HashSet<>();
        obstacles.add(new Position(2, 2));
        obstacles.add(new Position(3, 3));

        Grid grid = new Grid.Builder()
                .withBounds(0, 0, 10, 10)
                .withObstacles(obstacles)
                .build();

        assertTrue(grid.isObstacle(new Position(2, 2)));
        assertTrue(grid.isObstacle(new Position(3, 3)));
        assertFalse(grid.isObstacle(new Position(1, 1)));
    }

    @Test
    @DisplayName("Should validate movement to position")
    void testCanMoveTo() {
        Set<Position> obstacles = new HashSet<>();
        obstacles.add(new Position(5, 5));

        Grid grid = new Grid.Builder()
                .withBounds(0, 0, 10, 10)
                .withObstacles(obstacles)
                .build();

        assertTrue(grid.canMoveTo(new Position(1, 1)));
        assertFalse(grid.canMoveTo(new Position(5, 5))); // obstacle
        assertFalse(grid.canMoveTo(new Position(11, 11))); // out of bounds
    }

    @Test
    @DisplayName("Should throw exception for invalid bounds")
    void testInvalidBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Grid.Builder()
                    .withBounds(10, 0, 0, 10)
                    .build();
        });
    }
}