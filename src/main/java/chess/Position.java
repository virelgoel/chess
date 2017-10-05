package chess;

/**
 * Describes a position on the Chess Board
 */
public class Position {

    /**
     * A static position that represents being off the board
     */
    public static final Position OFF_BOARD = new Position();

    private int row;
    private char column;

    /**
     * Constructor used to create the position that represents off the board
     */
    private Position() {
        row = -1;
        column = 'z';
    }

    /**
     * Create a new position object
     *
     * @param column The column
     * @param row The row
     * @throws InvalidPositionException if the given position isn't valid
     */
    public Position(char column, int row) {
        this.row = validateRow(row);
        this.column = validateColumn(column);
    }

    /**
     * Create a new Position object by parsing the string
     * @param colrow The column and row to use.  I.e. "a1", "h7", etc.
     */
    public Position(String colrow) {
        if (colrow.length() != 2) {
            throw new InvalidPositionException("Wrong length string to create a position: " + colrow, 0, ' ');
        }

        char[] chars = colrow.toCharArray();
        this.column = validateColumn(chars[0]);
        int rowFromStr = Character.digit(chars[1], 10);
        this.row = validateRow(rowFromStr);
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }

    public Position step(Direction direction) {
        switch (direction) {
            case North: return safeBuilder(column, row + 1);
            case South: return safeBuilder(column, row - 1);
            case East: return safeBuilder(column + 1, row);
            case West: return safeBuilder(column - 1, row);

            case NorthEast: return safeBuilder(column + 1, row + 1);
            case SouthEast: return safeBuilder(column + 1, row - 1);
            case NorthWest: return safeBuilder(column - 1, row + 1);
            case SouthWest: return safeBuilder(column - 1, row - 1);
            default: throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private Position safeBuilder(int column, int row) {
        try {
            return new Position((char) column, row);
        } catch (InvalidPositionException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "" + column + row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (column != position.column) return false;

        //noinspection RedundantIfStatement
        if (row != position.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + (int) column;
        return result;
    }

    /**
     * Validate that the given row is good.
     * @param row The row number to validate
     * @return The same row number passed in.  Or ...
     * @throws InvalidPositionException If the row is invalid
     */
    private int validateRow(int row) {
        if (row < GameState.MIN_ROW || row > GameState.MAX_ROW) {
            throw new InvalidPositionException("Invalid Row", row, column);
        }

        return row;
    }

    /**
     * Validate that the given position is good
     * @param column The column
     * @throws InvalidPositionException if we don't have a good position
     */
    private char validateColumn(char column) {
        // Force it to upper case
        column = Character.toLowerCase(column);

        if (column < GameState.MIN_COLUMN || column > GameState.MAX_COLUMN) {
            throw new InvalidPositionException("Invalid Column", row, column);
        }

        return column;
    }
}
