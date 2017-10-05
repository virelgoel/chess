package chess.pieces.positions;

import chess.GameState;
import chess.Player;
import chess.Position;
import chess.pieces.Piece;

import java.util.Set;

/**
 * Abstract class that provides the ability to generate a set of positions.
 */
public abstract class PiecePositionGenerator {

    final GameState state;
    final Player player;

    protected PiecePositionGenerator(Player player, GameState state) {
        this.state = state;
        this.player = player;
    }

    /**
     * Generate the positions that are relevant to this generator.
     * @return A set of positions that the piece may move to.  If there are no moves,
     * this must return an empty set.
     * @param origin The position to generate valid next positions for
     */
    public abstract Set<Position> generate(Position origin);

    /**
     * Utility method for subclasses to use to determine if a given position is occupied.
     * @param position The position in question
     * @return True if the position is occupied by another piece
     */
    protected boolean isEmpty(Position position) {
        return !isOccupied(position, null);
    }

    /**
     * Utility method for subclasses to use to determine if a given position is occupied
     * by a given player
     * @param position The position in question
     * @param player The player to determine if they are occupying the position.
     * @return True if the position is occupied by a piece owned by the given player.
     */
    boolean isOccupied(Position position, Player player) {
        Piece foundPiece = state.getPieceAt(position);
        return foundPiece != null &&
                (player == null || foundPiece.getPlayer().equals(player));
    }
}
