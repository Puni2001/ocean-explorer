package com.natwest.oceanexplorer.unit;

import com.natwest.oceanexplorer.core.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    @DisplayName("Should turn left correctly")
    void testTurnLeft() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
    }

    @Test
    @DisplayName("Should turn right correctly")
    void testTurnRight() {
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }

    @Test
    @DisplayName("Should return opposite direction")
    void testOpposite() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite());
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite());
        assertEquals(Direction.WEST, Direction.EAST.opposite());
        assertEquals(Direction.EAST, Direction.WEST.opposite());
    }

    @Test
    @DisplayName("Should have correct movement deltas")
    void testMovementDeltas() {
        assertEquals(0, Direction.NORTH.getDx());
        assertEquals(1, Direction.NORTH.getDy());

        assertEquals(1, Direction.EAST.getDx());
        assertEquals(0, Direction.EAST.getDy());

        assertEquals(0, Direction.SOUTH.getDx());
        assertEquals(-1, Direction.SOUTH.getDy());

        assertEquals(-1, Direction.WEST.getDx());
        assertEquals(0, Direction.WEST.getDy());
    }
}