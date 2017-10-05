package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;


import static org.junit.Assert.*;

/**
 * Testing the movement & behavior of the Rook
 */
public class RookTest {

    private Rook rook;
    private Position origin;
    private GameState gameState;

    @Before
    public void setUp() {
        rook = new Rook(Player.White);
        gameState = new GameState();
        origin = new Position("d4");
        gameState.placePiece(rook, origin);
    }

    @Test
    public void testClearPath() {
        gameState.placePiece(new Rook(Player.Black), "c3");

        Set<Position> positions = rook.getNextPositions(origin);
        assertEquals("Should have clear moves all over the board", 14, positions.size());

        assertTrue("Should be able to move in the column", positions.contains(new Position("d6")));
        assertTrue("Should be able to move in the row", positions.contains(new Position("a4")));
    }

    @Test
    public void testBlockedPath() {
        gameState.placePiece(new Pawn(Player.White), "d3");

        Set<Position> positions = rook.getNextPositions(origin);
        assertEquals("Should have clear moves in three directions (blocked N)", 11, positions.size());

        assertTrue("Should be able to move in the column", positions.contains(new Position("d6")));
        assertTrue("Should be able to move in the row", positions.contains(new Position("a4")));
        assertTrue("Should be able to move in the row", positions.contains(new Position("g4")));
        assertFalse("Should not be able to move South", positions.contains(new Position("d1")));
    }

    @Test
    public void testAttack() {
        gameState.placePiece(new Pawn(Player.Black), "d7");

        Set<Position> positions = rook.getNextPositions(origin);
        assertEquals("Should be able to move clearly in 3 directions (attack N) ", 13, positions.size());

        assertTrue("Should be able to attack d7", positions.contains(new Position("d7")));
        assertTrue("Should be able to move in the row", positions.contains(new Position("a4")));
        assertTrue("Should be able to move in the row", positions.contains(new Position("g4")));
        assertTrue("Should not be able to move South", positions.contains(new Position("d1")));
        assertFalse("Should not be able to move to d8", positions.contains(new Position("d8")));
    }

}
