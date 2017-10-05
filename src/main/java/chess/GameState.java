package chess;

import chess.pieces.*;

import java.util.*;

/**
 * Class that represents the current state of the game.  Basically, what pieces are in which positions on the
 * board.
 */
public class GameState {

    public static final int MIN_ROW = 1;
    public static final int MAX_ROW = 8;
    public static final char MIN_COLUMN = 'a';
    public static final char MAX_COLUMN = 'h';

    private Player currentPlayer = Player.White;

    private final Map<Position, Piece> positionToPieceMap;
    private final Map<Piece, Position> pieceToPositionMap;

    /**
     * The most recently executed move
     */
    private Move lastMove;

    /**
     * Create the game state.
     */
    public GameState() {
        positionToPieceMap = new HashMap<Position, Piece>();
        pieceToPositionMap = new HashMap<Piece, Position>();
    }

    /**
     * Call to initialize the game state into the starting positions
     */
    public void reset() {
        // White Pieces
        placePiece(new Rook(Player.White), new Position("a1"));
        placePiece(new Knight(Player.White), new Position("b1"));
        placePiece(new Bishop(Player.White), new Position("c1"));
        placePiece(new Queen(Player.White), new Position("d1"));
        placePiece(new King(Player.White), new Position("e1"));
        placePiece(new Bishop(Player.White), new Position("f1"));
        placePiece(new Knight(Player.White), new Position("g1"));
        placePiece(new Rook(Player.White), new Position("h1"));
        placePiece(new Pawn(Player.White), new Position("a2"));
        placePiece(new Pawn(Player.White), new Position("b2"));
        placePiece(new Pawn(Player.White), new Position("c2"));
        placePiece(new Pawn(Player.White), new Position("d2"));
        placePiece(new Pawn(Player.White), new Position("e2"));
        placePiece(new Pawn(Player.White), new Position("f2"));
        placePiece(new Pawn(Player.White), new Position("g2"));
        placePiece(new Pawn(Player.White), new Position("h2"));

        // Black Pieces
        placePiece(new Rook(Player.Black), new Position("a8"));
        placePiece(new Knight(Player.Black), new Position("b8"));
        placePiece(new Bishop(Player.Black), new Position("c8"));
        placePiece(new Queen(Player.Black), new Position("d8"));
        placePiece(new King(Player.Black), new Position("e8"));
        placePiece(new Bishop(Player.Black), new Position("f8"));
        placePiece(new Knight(Player.Black), new Position("g8"));
        placePiece(new Rook(Player.Black), new Position("h8"));
        placePiece(new Pawn(Player.Black), new Position("a7"));
        placePiece(new Pawn(Player.Black), new Position("b7"));
        placePiece(new Pawn(Player.Black), new Position("c7"));
        placePiece(new Pawn(Player.Black), new Position("d7"));
        placePiece(new Pawn(Player.Black), new Position("e7"));
        placePiece(new Pawn(Player.Black), new Position("f7"));
        placePiece(new Pawn(Player.Black), new Position("g7"));
        placePiece(new Pawn(Player.Black), new Position("h7"));
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void toggleCurrentPlayer() {
        this.currentPlayer = currentPlayer.other();
    }

    /**
     * Place the given piece in the given position
     * @param piece The piece
     * @param colrow The column/row that indicates the position. I.e. "a1"
     * @return The piece that was replaced, if any
     */
    public Piece placePiece(Piece piece, String colrow) {
        Position position = new Position(colrow);
        return placePiece(piece, position);
    }

    /**
     * Place the given piece in the given position
     * @param piece The piece
     * @param position The position to place the piece
     * @return The piece that was replaced, if any
     */
    public Piece placePiece(Piece piece, Position position) {
        piece.setGameState(this);

        Position originalPosition = getPositionOf(piece);
        if (originalPosition != null) {
            positionToPieceMap.remove(originalPosition);
        }

        Piece replaced = positionToPieceMap.put(position, piece);
        if (replaced != null) {
            pieceToPositionMap.put(replaced, Position.OFF_BOARD);
        }

        pieceToPositionMap.put(piece, position);

        return replaced;
    }

    public Piece getPieceAt(String colrow) {
        Position position = new Position(colrow);
        return getPieceAt(position);
    }

    public Piece getPieceAt(Position position) {
        return positionToPieceMap.get(position);
    }

    private Position getPositionOf(Piece piece) {
        if (piece == null) {
            return null;
        }

        return pieceToPositionMap.get(piece);
    }

    public Set<Piece> getPiecesOnBoard(Player player) {
        HashSet<Piece> pieces = new HashSet<Piece>(positionToPieceMap.values());
        Set<Piece> playerPieces = filterPieces(pieces, player);
        return Collections.unmodifiableSet(playerPieces);
    }

    /**
     * Determine if the given space is "covered" by a given player's piece.
     * @param position  The destination in question.
     * @param player The player whose pieces to check
     * @return True if the other player has the given position covered.
     */
    private boolean isCoveredBy(Position position, Player player) {
        Set<Piece> otherPieces = getPiecesOnBoard(player);
        for (Piece piece : otherPieces) {
            Position pieceOrigin = getPositionOf(piece);
            Set<Position> possible = piece.getNextPositions(pieceOrigin);
            if (possible.contains(position)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find the collection of possible moves for the current player
     * @return A map of moves from Piece --> Set of Moves.  If a piece has no
     * possible moves, it still has an entry in the map, but that value is an empty Set.
     */
    public Map<Piece, Set<Move>> findPossibleMoves() {
        Map<Piece, Set<Move>> moveMap = new HashMap<Piece, Set<Move>>();

        // For each piece the player has on the board, add all of the possible
        // moves it could have.
        Set<Piece> pieces = getPiecesOnBoard(currentPlayer);
        for (Piece piece : pieces) {
            moveMap.put(piece, findValidMovesFor(piece));
        }

        return moveMap;
    }

    /**
     * Find all the valid moves that a given piece could make.
     * @param piece The piece whose moves to get
     * @return The set of possible moves.
     */
    public Set<Move> findValidMovesFor(Piece piece) {
        Set<Move> moves = new HashSet<Move>();
        Position start = getPositionOf(piece);

        Set<Position> possible = piece.getNextPositions(start);
        for (Position position : possible) {
            Move move = new Move(start, position);
            if (!wouldBeInCheckAfter(move)) {
                moves.add(move);
            }
        }

        return Collections.unmodifiableSet(moves);
    }

    /**
     * Make the move indicated by the move string (i.e. "e2 e4")
     * @param moveStr The move string
     * @return True if the move was executed.  False if the move was not executed because it
     * would put the current player in check.
     */
    public boolean makeMove(String moveStr) {
        Move move = new Move(moveStr);
        return makeMove(move);
    }

    /**
     * Make a move.
     * @param move The move to make.
     * @return True if the move was executed.  False if the move was not executed because the
     * players king would (still) be in check.
     * @throws InvalidMoveException If the move does not make sense.  For instance, it references
     * a piece that isn't owned by the current player, or there is no piece at the
     * move starting position, or the referenced piece can't move to the destination.
     */
    public boolean makeMove(Move move) throws InvalidMoveException {
        boolean inCheck = doMakeMove(move);

        if (inCheck) {
            // This was not a valid move; move everything back
            revert(move);
            return false;
        } else {
            // It's now the other player's move
            currentPlayer = currentPlayer.other();
            return true;
        }
    }

    /**
     * Method to make a move and determine if the current player is in check.  This does
     * NOT change the current player.
     * @param move The move to make
     * @return True if after this move is made the current player is in check.
     * @throws InvalidMoveException If the move does not make sense.  For instance, it references
     * a piece that isn't owned by the current player, or there is no piece at the
     * move starting position.
     */
    private boolean doMakeMove(Move move) {
        Position origin = move.getOrigin();
        Piece movingPiece = getPieceAt(origin);
        if (movingPiece == null || !currentPlayer.equals(movingPiece.getPlayer())) {
            throw new InvalidMoveException(move);
        }

        Set<Position> validPositions = movingPiece.getNextPositions(origin);
        Position destination = move.getDestination();

        if (!validPositions.contains(destination)) {
            throw new InvalidMoveException(move);
        }

        // Otherwise we are good to make the move

        // Remove the piece from its original place
//        positionToPieceMap.remove(origin);

        // Place it in its new position, removing the old piece (if any)
        Piece replaced = placePiece(movingPiece, destination);
        move.record(movingPiece, replaced);
        this.lastMove = move;

        return isInCheck();
    }

    /**
     * Revert this move in the game state.  This is used when checking to see if the game
     * is over.
     * @param move The move to revert.  This *must* be the most recently-made move.
     * @throws IllegalArgumentException If you provide a move that isn't the most recently
     * made move.
     */
    void revert(Move move) {
        if (move == null || !move.equals(lastMove)) {
            throw new IllegalArgumentException("Cannot revert: " + move);
        }

        Piece replaced = move.getReplacedPiece();
        if (replaced != null) {
            placePiece(replaced, move.getDestination());
        }

        // Put the piece back in its original spot
        placePiece(move.getMovedPiece(), move.getOrigin());
    }

    /**
     * Determine if the current player's King is in check
     * @return True if the current player's King is in check
     */
    public boolean isInCheck() {
        Position kingPosition = findCurrentKing();
        Piece king = getPieceAt(kingPosition);
        return king != null && isCoveredBy(kingPosition, currentPlayer.other());
    }

    /**
     * Determine if the game is over.  If so, then the current player is the loser.
     * @return True if the game is over.
     */
    public boolean isGameOver() {
        if (isInCheck()) {
            // Find if the current player can make any moves that will get
            // the king out of check
            Map<Piece, Set<Move>> moveMap = findPossibleMoves();

            Set<Move> moves = new HashSet<Move>();
            for (Set<Move> current : moveMap.values()) {
                moves.addAll(current);
            }

            // Try all the moves to see if we can find one that gets us out of check
            for (Move move : moves) {
                boolean inCheck = wouldBeInCheckAfter(move);
                if (!inCheck) {
                    // We have a move that will get us out of check
                    return false;
                }
            }
            //ran out of all the moves but couldn't find one that will get us out of check, checkmate!
            return true;
        }
        return false;
    }

    /**
     * Find the king of the current player
     * @return The King, or null if one cannot be found
     */
    private Position findCurrentKing() {
        Set<Piece> pieces = getPiecesOnBoard(currentPlayer);
        // Find the king
        King king = null;
        for (Piece piece : pieces) {
            if (piece instanceof King) {
                king = (King) piece;
                break;
            }
        }

        return getPositionOf(king);
    }

    private Set<Piece> filterPieces(Set<Piece> pieces, Player player) {
        HashSet<Piece> playerPieces = new HashSet<Piece>();
        for (Piece piece : pieces) {
            if (piece.getPlayer().equals(player)) {
                playerPieces.add(piece);
            }
        }
        return playerPieces;
    }

    /**
     * Determine if the current player would be in check after they made this move
     * @param move The move to try
     * @return True if, after the move was made, the player would be in check
     */
    private boolean wouldBeInCheckAfter(Move move) {
        boolean putsInCheck = doMakeMove(move);
        revert(move);

        return putsInCheck;
    }
}
