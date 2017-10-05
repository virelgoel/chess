package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test moving and exercising the King
 */
public class KingTest {

    private King king;
    private Position origin;
    private GameState gameState;

    @Before
    public void setUp() {
        king = new King(Player.Black);
        origin = new Position("d4");
        gameState = new GameState();
        gameState.placePiece(king, "d4");
    }

    @Test
    public void testClearPaths() {
        Set<Position> positions = king.getNextPositions(origin);

        assertEquals("The king should have 8 possible moves", 8, positions.size());
        assertTrue("The king should be able to move to c5", positions.contains(new Position("c5")));

        assertFalse("The king should not be able to move to e6", positions.contains(new Position("e6")));
    }

    @Test
    public void testBlocked() {
        gameState.placePiece(new Pawn(Player.Black), "d5");

        Set<Position> positions = king.getNextPositions(origin);
        assertEquals("The king should have 7 possible moves (blocked North)", 7, positions.size());
        assertTrue("The king should be able to move to e5", positions.contains(new Position("e5")));

        assertFalse("The king should not be able to move to d5", positions.contains(new Position("d5")));
    }

    @Test
    public void testAttack() {
        gameState.placePiece(new Pawn(Player.White), "d5");

        Set<Position> positions = king.getNextPositions(origin);
        assertEquals("The king should have 8 possible moves", 8, positions.size());
        assertTrue("The king should be able to move to d5", positions.contains(new Position("d5")));
    }
}
