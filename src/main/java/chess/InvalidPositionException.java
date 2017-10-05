package chess;

/**
 * Indicates a position that is off the board.
 */
class InvalidPositionException extends RuntimeException {
    public InvalidPositionException(String msg, int row, char column) {
        super(msg + " (row: " + row + " column: " + column);
    }
}
