package com.natwest.oceanexplorer.unit;

import com.natwest.oceanexplorer.core.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    @DisplayName("Should convert valid characters to commands")
    void testFromCharValid() {
        assertEquals(Command.FORWARD, Command.fromChar('F'));
        assertEquals(Command.FORWARD, Command.fromChar('f'));
        assertEquals(Command.BACKWARD, Command.fromChar('B'));
        assertEquals(Command.LEFT, Command.fromChar('L'));
        assertEquals(Command.RIGHT, Command.fromChar('R'));
    }

    @Test
    @DisplayName("Should throw exception for invalid characters")
    void testFromCharInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Command.fromChar('X'));
        assertThrows(IllegalArgumentException.class, () -> Command.fromChar('1'));
        assertThrows(IllegalArgumentException.class, () -> Command.fromChar(' '));
    }

    @Test
    @DisplayName("Should return correct character representation")
    void testGetChar() {
        assertEquals('F', Command.FORWARD.getChar());
        assertEquals('B', Command.BACKWARD.getChar());
        assertEquals('L', Command.LEFT.getChar());
        assertEquals('R', Command.RIGHT.getChar());
    }
}