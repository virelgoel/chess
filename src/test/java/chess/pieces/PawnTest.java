package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test the movement & behavior of the Pawn
 */
public class PawnTest {
    private Pawn pawn;
    private GameState gameState;
    private Position origin;

    @Before
    public void setUp() {
        pawn = new Pawn(Player.Black);
        gameState = new GameState();
        origin = new Position("d7");
        gameState.placePiece(pawn, origin);
    }

    @Test
    public void testNoAttackOption() {
        // This extra piece obviously shouldn't come into play....
        gameState.placePiece(new Pawn(Player.White), "d2");
        Set<Position> possible = pawn.getNextPositions(origin);

        assertEquals("Should have two possible moves", 2, possible.size());

        assertTrue("Should contain the move one step South", possible.contains(new Position("d6")));
        assertTrue("Should contain the move two steps South", possible.contains(new Position("d5")));
    }

    @Test
    public void testBlocked() {
        gameState.placePiece(new Pawn(Player.White), "d6");

        Set<Position> possible = pawn.getNextPositions(origin);
        assertEquals("Should not have any possible moves", 0, possible.size());

        gameState.placePiece(new Pawn(Player.Black), "d6");
        assertEquals("Should not be able to move onto my own piece", 0, pawn.getNextPositions(origin).size());
    }

    @Test
    public void testPartialBlocked() {
        gameState.placePiece(new Pawn(Player.White), "d5");
        Set<Position> possible = pawn.getNextPositions(origin);
        assertEquals("Should only have one possible move", 1, possible.size());
    }

    @Test
    public void testAttackOption() {
        gameState.placePiece(new Pawn(Player.White), "c6");
        Set<Position> possible = pawn.getNextPositions(origin);
        assertEquals("Should not have any possible moves", 3, possible.size());
    }

    @Test
    public void testCannotAttackMyself() {
        gameState.placePiece(new Pawn(Player.Black), "c6");
        Set<Position> possible = pawn.getNextPositions(origin);
        assertEquals("Should not have any possible moves", 2, possible.size());
    }
}
