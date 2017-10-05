package chess.pieces.positions;

import chess.Direction;
import chess.GameState;
import chess.Player;
import chess.pieces.Piece;

/**
 * Position generator that will find all the possible positions a piece may
 * move to in a column.
 */
public class ColumnPositionGenerator extends StraightLinePositionGenerator {
    public ColumnPositionGenerator(Player player, GameState gameState) {
        super(player, gameState, Direction.North, Direction.South);
    }
}
