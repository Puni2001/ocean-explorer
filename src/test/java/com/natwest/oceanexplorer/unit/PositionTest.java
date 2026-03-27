package com.natwest.oceanexplorer.unit;

import com.natwest.oceanexplorer.core.Position;
import com.natwest.oceanexplorer.core.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    @DisplayName("Should create position with coordinates")
    void testCreation() {
        Position pos = new Position(5, 10);
        assertEquals(5, pos.getX());
        assertEquals(10, pos.getY());
    }

    @Test
    @DisplayName("Should move in direction")
    void testMove() {
        Position start = new Position(2, 2);

        assertEquals(new Position(2, 3), start.move(Direction.NORTH));
        assertEquals(new Position(3, 2), start.move(Direction.EAST));
        assertEquals(new Position(2, 1), start.move(Direction.SOUTH));
        assertEquals(new Position(1, 2), start.move(Direction.WEST));
    }

    @Test
    @DisplayName("Should calculate distance correctly")
    void testDistance() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 4);

        assertEquals(5.0, p1.distanceTo(p2), 0.001);
    }

    @Test
    @DisplayName("Should implement equals and hashCode")
    void testEqualsAndHashCode() {
        Position p1 = new Position(2, 3);
        Position p2 = new Position(2, 3);
        Position p3 = new Position(3, 2);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Should format toString correctly")
    void testToString() {
        Position pos = new Position(5, 7);
        assertEquals("(5, 7)", pos.toString());
    }
}