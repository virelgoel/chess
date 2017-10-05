package chess.pieces.positions;

import chess.Direction;
import chess.GameState;
import chess.Player;
import chess.pieces.Piece;

/**
 * Generator that will find possible positions in a given piece's row
 */
public class RowPositionGenerator extends StraightLinePositionGenerator {
    public RowPositionGenerator(Player player, GameState gameState) {
        super(player, gameState, Direction.East, Direction.West);
    }
}
