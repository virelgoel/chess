package chess.pieces;

import chess.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests for the Queen
 */
public class QueenTest {

    private Queen queen;
    private Position origin;
    private GameState gameState;

    @Before
    public void setUp() {
        queen = new Queen(Player.Black);
        origin = new Position("d5");
        gameState = new GameState();
        gameState.placePiece(queen, origin);
    }

    @Test
    public void testClearPaths() {
        Set<Position> positions = queen.getNextPositions(origin);

        assertEquals("The queen should have 27 possible moves", 27, positions.size());
        assertTrue("Positions should include d1", positions.contains(new Position("d1")));
        assertTrue("Positions should include g8", positions.contains(new Position("g8")));
        assertFalse("Positions should not include g1", positions.contains(new Position("g1")));
    }

    @Test
    public void testBlocked() {
        gameState.placePiece(new Pawn(Player.Black), "e4");

        Set<Position> positions = queen.getNextPositions(origin);
        assertEquals("The queen should have 23 possible moves (blocked SE)", 23, positions.size());
        assertTrue("Positions should include d1", positions.contains(new Position("d1")));
        assertTrue("Positions should include g8", positions.contains(new Position("g8")));
        assertFalse("Positions should not include g1", positions.contains(new Position("g1")));
        assertFalse("Positions should not include e4", positions.contains(new Position("e4")));
        assertFalse("Positions should not include f3", positions.contains(new Position("f3")));
    }

    @Test
    public void testAttack() {
        gameState.placePiece(new Pawn(Player.White), "e4");

        Set<Position> positions = queen.getNextPositions(origin);
        assertEquals("The queen should have 24 possible moves (attack SE)", 24, positions.size());
        assertTrue("Positions should include d1", positions.contains(new Position("d1")));
        assertTrue("Positions should include g8", positions.contains(new Position("g8")));
        assertTrue("Positions should include e4", positions.contains(new Position("e4")));
        assertFalse("Positions should not include g1", positions.contains(new Position("g1")));
        assertFalse("Positions should not include f3", positions.contains(new Position("f3")));

    }

    @Test
    public void testInvalidMove() {
        try {
            gameState.makeMove(new Move("d5 c7"));
            fail("Queen should not be able to move from d5 to c7");
        } catch (InvalidMoveException ex) {
            // Yay.
        }
    }
}
