package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the knight's unusual movement patterns
 */
public class KnightTest {

    private Knight knight;
    private GameState gameState;
    private Position origin;

    @Before
    public void setUp() {
        knight = new Knight(Player.Black);
        gameState = new GameState();
        origin = new Position("d5");
        gameState.placePiece(knight, origin);
    }

    @Test
    public void testOccupiedByMe() {
        gameState.placePiece(new Pawn(Player.Black), "e7");
        assertFalse("Cannot move onto my own piece", knight.canMove(origin, new Position("e7")));
    }

    /*
        Test that the knight can move:
            1. East(E) -> East(E) -> South(S)/North(N)
            2. West(W) -> West(W) -> South(S)/North(N)
            3. North(N) -> North(N) -> East(E)/West(W)
            4. South(S) -> South(S) -> East(E)/West(W)
    */
    @Test
    public void testKnightCanMoveInAllDirections() {
        assertTrue("Knight is not able to move E -> E -> S", knight.canMove(origin, new Position("f4")));
        assertTrue("Knight is not able to move E -> E -> N", knight.canMove(origin, new Position("f6")));
        assertTrue("Knight is not able to move W -> W -> S", knight.canMove(origin, new Position("b4")));
        assertTrue("Knight is not able to move W -> W -> N", knight.canMove(origin, new Position("b6")));
        assertTrue("Knight is not able to move N -> N -> E", knight.canMove(origin, new Position("e7")));
        assertTrue("Knight is not able to move N -> N -> W", knight.canMove(origin, new Position("c7")));
        assertTrue("Knight is not able to move S -> S -> E", knight.canMove(origin, new Position("e3")));
        assertTrue("Knight is not able to move S -> S -> W", knight.canMove(origin, new Position("c3")));
    }

    @Test
    public void testOccupiedByOther() {
        gameState.placePiece(new Pawn(Player.White), "e7");
        assertTrue("Should be able to take the other player's piece", knight.canMove(origin, new Position("e7")));
    }
}
