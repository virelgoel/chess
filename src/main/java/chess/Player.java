package chess;

/**
 * Which side of the board is being played
 */
public enum Player {
    White, Black;

    public Player other() {
        if (this.equals(White)) {
            return Black;
        } else {
            return White;
        }
    }
}
