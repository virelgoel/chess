package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test the movement of the Bishop
 */
public class BishopTest {

    private Bishop bishop;
    private Position origin;
    private GameState gameState;

    @Before
    public void setUp() {
        bishop = new Bishop(Player.Black);
        origin = new Position("d4");
        gameState = new GameState();
        gameState.placePiece(bishop, origin);
    }

    @Test
    public void testClearPaths() {
        Set<Position> positions = bishop.getNextPositions(origin);
        assertEquals("The bishop should have 13 possible moves", 13, positions.size());

        assertTrue("Positions should include a1", positions.contains(new Position("a1")));
        assertTrue("Positions should include h8", positions.contains(new Position("h8")));
        assertFalse("Positions should not include h1", positions.contains(new Position("h1")));
    }

    @Test
    public void testBlocked() {
        gameState.placePiece(new Pawn(Player.Black), "e5");

        Set<Position> positions = bishop.getNextPositions(origin);
        assertEquals("The bishop should have 9 moves (blocked NE)", 9, positions.size());
        assertFalse("The bishop should not be able to move to h8", positions.contains(new Position("h8")));
        assertTrue("The bishop should be able to move to a1", positions.contains(new Position("a1")));
    }

    @Test
    public void testAttack() {
        gameState.placePiece(new Pawn(Player.White), "e5");

        Set<Position> positions = bishop.getNextPositions(origin);
        assertEquals("The bishop should have 10 moves (attack NE)", 10, positions.size());
        assertFalse("The bishop should not be able to move to h8", positions.contains(new Position("h8")));
        assertTrue("The bishop should be able to move to a1", positions.contains(new Position("a1")));
        assertTrue("The bishop should be able to attack e5", positions.contains(new Position("e5")));
    }
}
