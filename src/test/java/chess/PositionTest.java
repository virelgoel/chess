package chess;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Position class
 */
public class PositionTest {

    private Position position;

    @Before
    public void setUp() {
        position = new Position("d4");
    }

    @Test
    public void testInvalidRow() {
        try {
            position = new Position('A', 0);
        } catch (InvalidPositionException ex) {
            return;
        }

        fail("Did not detect invalid row");
    }

    @Test
    public void testInvalidColumn() {
        try {
            position = new Position('I', 1);
        } catch (InvalidPositionException ex) {
            return;
        }

        fail("Did not detect invalid row");
    }

    @Test
    public void testStringLocation() {
        position = new Position("g3");
        assertEquals("Did not generate the expected position string", "g3", position.toString());
    }

    @Test
    public void testForceLowerCaseColumn() {
        position = new Position("a1");
        assertEquals("Did not generate the expected position string", "a1", position.toString());
    }

    @Test
    public void testCreateFromString() throws Exception {
        position = new Position("a1");

        assertEquals("Position has wrong column", 'a', position.getColumn());
        assertEquals("Position has wrong row", 1, position.getRow());
    }

    @Test
    public void testCreateFromIncorrectString() throws Exception {
        try {
            position = new Position("ThisIsTooLong");
            fail("Should not be able to create a Position from a long string");
        } catch (InvalidPositionException ex) {
            // Moving on ...
        }

        try {
            position = new Position("4z");
            fail("Should not be able to create a Position that is off the board");
        } catch (InvalidPositionException ex) {
            // Yay
        }
    }
}
