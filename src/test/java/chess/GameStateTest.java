package chess;

import chess.pieces.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Tests to exercise the GameState logic
 */
public class GameStateTest {

    private GameState gameState;

    @Before
    public void setUp() {
        gameState = new GameState();
    }

    @Test
    public void testInitialState() {
        gameState.reset();

        Set<Piece> whitePieces = gameState.getPiecesOnBoard(Player.White);
        assertNotNull("Should never be null", whitePieces);
        assertEquals("Wrong number of white pieces", 16, whitePieces.size());

        Set<Piece> blackPieces = gameState.getPiecesOnBoard(Player.Black);
        assertNotNull("Should never be null", blackPieces);
        assertEquals("Wrong number of black pieces", 16, blackPieces.size());
    }

    @Test
    public void testSparseBoard() {
        gameState.placePiece(new King(Player.White), "a1");
        gameState.placePiece(new Pawn(Player.White), "b2");
        gameState.placePiece(new King(Player.Black), "h8");
        Rook blackRook = new Rook(Player.Black);
        gameState.placePiece(blackRook, "g7");

        Set<Piece> onBoard = gameState.getPiecesOnBoard(Player.White);
        assertEquals("Wrong number of pieces in sparse initial state", 2, onBoard.size());

        Piece foundPiece = gameState.getPieceAt("g7");
        assertEquals("Wrong piece found at g7", blackRook, foundPiece);
    }

    /**
     * Verify that we can't initialize a game state with two pieces in the same position
     */
    @Test
    public void testDuplicatePosition() {
        King whiteKing = new King(Player.White);
        King blackKing = new King(Player.Black);

        gameState.placePiece(whiteKing, "d5");
        gameState.placePiece(blackKing, "d5");
        assertEquals("Should not be any pieces on the board", 0, gameState.getPiecesOnBoard(Player.White).size());
        assertEquals("Should only be one piece on the board", 1, gameState.getPiecesOnBoard(Player.Black).size());
    }

    @Test
    public void testFindPossibleMoves() {
        King whiteKing = new King(Player.White);
        Pawn whitePawn = new Pawn(Player.White);
        King blackKing = new King(Player.Black);

        gameState.placePiece(whiteKing, "a1");
        gameState.placePiece(whitePawn, "c2");
        gameState.placePiece(blackKing, "h1");

        Map<Piece,Set<Move>> moveMap = gameState.findPossibleMoves();

        assertEquals("Should be entries for white's only pieces", 2, moveMap.size());

        Set<Move> moves = new HashSet<Move>();
        for (Piece piece : moveMap.keySet()) {
            moves.addAll(moveMap.get(piece));
        }

        assertEquals("Should have 5 moves total", 5, moves.size());
    }

    @Test
    public void testPossibleMovesInCheck() {
        King whiteKing = new King(Player.White);
        Pawn whitePawn = new Pawn(Player.White);
        King blackKing = new King(Player.Black);
        Rook blackRook = new Rook(Player.Black);

        gameState.placePiece(whiteKing, "a1");
        gameState.placePiece(whitePawn, "b2");
        gameState.placePiece(blackKing, "h1");
        gameState.placePiece(blackRook, "f5");

        assertFalse("White should not be in check", gameState.isInCheck());
        makeMoves("a1 a2");

        // Move the rook to put White in check
        makeMoves("f5 a5");
        assertTrue("White should be in check", gameState.isInCheck());

        // White should only have two possible moves:  "a2 b3" and "a2 b1"
        Set<Move> kingMoves = gameState.findValidMovesFor(whiteKing);
        assertEquals("The king should have two possible moves", 2, kingMoves.size());

        Set<Move> pawnMoves = gameState.findValidMovesFor(whitePawn);
        assertEquals("The pawn should not have any valid moves", 0, pawnMoves.size());
    }

    /*
        Verify that a fool's checkamte is detected and isGameOver() returns true
    */
    @Test
    public void testFoolsCheckmate() {
        King whiteKing = new King(Player.White);
        Queen whiteQueen = new Queen(Player.White);
        Pawn whitePawn_E2 = new Pawn(Player.White);
        Pawn whitePawn_D2 = new Pawn(Player.White);
        Bishop whiteBishop = new Bishop(Player.White);

        Queen blackQueen = new Queen(Player.Black);

        gameState.placePiece(whiteQueen, "d1");
        gameState.placePiece(whiteKing, "e1");
        gameState.placePiece(whiteBishop, "f1");
        gameState.placePiece(whitePawn_D2, "d2");
        gameState.placePiece(whitePawn_E2, "e2");
        gameState.placePiece(blackQueen, "h4");

        assertTrue("Black should have won with checkmate", gameState.isGameOver());
    }

    /*
        Test that for the Knight's given position on the board, 8 moves are returned and as expected
    */
    @Test
    public void getPossibleKnightMoves() {
        Knight whiteKnight = new Knight(Player.White);
        gameState.placePiece(whiteKnight, "d5");

        Set<Move> moves = gameState.findValidMovesFor(whiteKnight);

        ArrayList<Move> movesList = new ArrayList<Move>(moves);
        
        assertEquals("did not get the expected number of moves", 8, movesList.size());
        assertTrue("d5 c3 is not in the moves list", movesList.toString().contains("d5 c3"));
        assertTrue("d5 e3 is not in the moves list", movesList.toString().contains("d5 e3"));
        assertTrue("d5 c7 is not in the moves list", movesList.toString().contains("d5 c7"));
        assertTrue("d5 e7 is not in the moves list", movesList.toString().contains("d5 e7"));
        assertTrue("d5 b6 is not in the moves list", movesList.toString().contains("d5 b6"));
        assertTrue("d5 b4 is not in the moves list", movesList.toString().contains("d5 b4"));
        assertTrue("d5 f6 is not in the moves list", movesList.toString().contains("d5 f6"));
        assertTrue("d5 f4 is not in the moves list", movesList.toString().contains("d5 f4"));
    }

    @Test
    public void testInvalidMove() {
        King whiteKing = new King(Player.White);
        Queen whiteQueen = new Queen(Player.White);
        King blackKing = new King(Player.Black);
       

        gameState.placePiece(whiteKing, "a1");
        gameState.placePiece(whiteQueen, "d1");
        gameState.placePiece(blackKing, "h8");

        try {
            makeMoves("d1 e3");
            fail("The queen should not have been able to move from d1 to e3");
        } catch (InvalidMoveException ex) {
            // Yay
            return;
        }

        fail("Attempting an invalid move should throw InvalidMoveException");
    }

    @Test
    public void testPutKingInCheck() {
        gameState.placePiece(new King(Player.White), "a1");
        gameState.placePiece(new Pawn(Player.White), "h6");
        gameState.placePiece(new King(Player.Black), "g8");

        assertFalse("The White King is not in check", gameState.isInCheck());
        makeMoves("h6 h7");
        assertTrue("The Black King should be in check", gameState.isInCheck());

        makeMoves("g8 h7");
        assertFalse("The Black King should now not be in check", gameState.isInCheck());
    }

    @Test
    public void testMovePutsKingInCheck() {
        King whiteKing = new King(Player.White);
        Pawn whitePawn = new Pawn(Player.White);
        King blackKing = new King(Player.Black);
        Rook blackRook = new Rook(Player.Black);

        gameState.placePiece(whiteKing, "a2");
        gameState.placePiece(whitePawn, "b2");
        gameState.placePiece(blackKing, "h8");
        gameState.placePiece(blackRook, "h2");

        assertFalse("The White King is not in check", gameState.isInCheck());

        // Try to move the White Pawn, which would put the White King in check.
        Move badMove = new Move("b2 b3");
        boolean executed = gameState.makeMove(badMove);
        assertFalse("Should not be able to make move that places King in check: " + badMove, executed);

        //  Test that the current player is still white
        assertEquals("The current player should still be White", Player.White, gameState.getCurrentPlayer());
        Piece piece = gameState.getPieceAt("b2");
        assertEquals("The White Pawn should be in the same place", piece, whitePawn);
    }

    @Test
    public void testIfGameOver() {
        King whiteKing = new King(Player.White);
        Pawn whitePawn = new Pawn(Player.White);
        Pawn otherWhitePawn = new Pawn(Player.White);
        King blackKing = new King(Player.Black);
        Rook blackRook = new Rook(Player.Black);

        gameState.placePiece(whiteKing, "a1");
        gameState.placePiece(whitePawn, "b2");
        gameState.placePiece(otherWhitePawn, "b1");
        gameState.placePiece(blackKing, "h8");
        gameState.placePiece(blackRook, "g8");
        gameState.toggleCurrentPlayer();

        assertFalse("The game is not yet over", gameState.isGameOver());
    }

    @Test
    public void testIfPlayerCanMoveOutOfCheck() {
        King whiteKing = new King(Player.White);
        Pawn whitePawn = new Pawn(Player.White);
        Knight whiteKnight = new Knight(Player.White);
        King blackKing = new King(Player.Black);
        Rook blackRook = new Rook(Player.Black);

        gameState.placePiece(whiteKing, "a1");
        gameState.placePiece(whitePawn, "b2");
        gameState.placePiece(whiteKnight, "b1");
        gameState.placePiece(blackKing, "h8");
        gameState.placePiece(blackRook, "g8");
        gameState.toggleCurrentPlayer();

        assertFalse("The game is not over", gameState.isGameOver());
        makeMoves("g8 a8");
        assertFalse("The game should not be over", gameState.isGameOver());
    }

    @Test
    public void aggressiveQueen() {
        gameState.reset();

        makeMoves("e2 e4", "d7 d5", "d2 d3", "d5 e4", "d3 e4", "d8 d1");

        assertTrue("The current player should be in check", gameState.isInCheck());
        Piece whiteKing = gameState.getPieceAt("e1");
        Set<Move> kingMoves = gameState.findValidMovesFor(whiteKing);

        assertTrue("The current player should still be in check", gameState.isInCheck());
        assertEquals("The king has only one move", 1, kingMoves.size());
        assertFalse("The game is not over (King can take Queen)", gameState.isGameOver());
    }

    private void makeMoves(String... moves) {
        for (String move : moves) {
            gameState.makeMove(move);
        }
    }

}
