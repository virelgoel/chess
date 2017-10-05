package chess.pieces.positions;

import chess.Direction;
import chess.GameState;
import chess.Player;
import chess.pieces.Piece;

/**
 * Generator for diagonal movement
 */
public class DiagonalPositionGenerator extends StraightLinePositionGenerator {
    public DiagonalPositionGenerator(Player player, GameState state) {
        super(player, state, Direction.NorthEast, Direction.NorthWest, Direction.SouthEast, Direction.SouthWest);
    }
}
