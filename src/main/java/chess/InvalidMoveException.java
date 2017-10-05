package chess;

/**
 * An exception that indicates an impossible move
 */
public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(Move move) {
        this(move.getOrigin(), move.getDestination());
    }

    private InvalidMoveException(Position origin, Position destination) {
        super("Cannot move from " + origin + " to " + destination);
    }

    public InvalidMoveException() {
        super("Invalid Move Specified");
    }
}
