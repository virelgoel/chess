package chess.pieces;

import chess.*;
import chess.pieces.positions.PiecePositionGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test the logic of the AbstractPieceTest
 */
public class PieceTest {

    /**
     * The code under test
     */
    private Piece testPiece;

    private Position testPiecePosition;

    private GameState gameState;


    @Before
    public void setUp() throws Exception {
        testPiece = makePiece();
        gameState = new GameState();
        testPiecePosition = new Position("d4");
        gameState.placePiece(testPiece, testPiecePosition);

    }

    /**
     * This test method returns a White piece that can move in straight lines to any
     * place on the board that is not occupied.  Basically, a pacifist queen.
     * @return A piece useful for testing
     */
    private Piece makePiece() {

        return new Piece(Player.White) {
            @Override
            protected Set<PiecePositionGenerator> getPositionGenerators() {
                Set<PiecePositionGenerator> generators = new HashSet<PiecePositionGenerator>(1);
                generators.add(new PiecePositionGenerator(Player.White, testPiece.getGameState()) {
                    @Override
                    public Set<Position> generate(Position origin) {
                        Set<Position> positions = new HashSet<Position>();
                        for (int row = GameState.MIN_ROW; row <= GameState.MAX_ROW; row++) {
                            for (char col = GameState.MIN_COLUMN; col  <= GameState.MAX_COLUMN; col++) {
                                Position toCheck = new Position(col, row);
                                if (isEmpty(toCheck)) {
                                    positions.add(toCheck);
                                }
                            }
                        }
                        return positions;
                    }
                });

                return generators;
            }

            @Override
            protected char getIdentifierCharacter() {
                return 'd';
            }
        };
    }

    @Test
    public void testCanMoveTo() throws Exception {
        assertTrue("Should be able to move to any position that is not occupied", testPiece.canMove(testPiecePosition, new Position('e', 5)));
    }

    @Test
    public void testCanNotMoveToOccupied() throws Exception {
        // Create an initial game state
        Position occupiedPosition = new Position("a1");
        gameState.placePiece(makePiece(), occupiedPosition);
        assertFalse("Should not be able to an occupied position", testPiece.canMove(testPiecePosition, occupiedPosition));
    }

    @Test
    public void testIsPathClearToOccupiedDestination() throws Exception {
        Position destination = new Position("d8");
        gameState.placePiece(makePiece(), destination);

        Set<Position> positions = testPiece.getNextPositions(destination);
        assertFalse("The destination position is already occupied", positions.contains(destination));
    }
}
