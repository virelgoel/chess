package chess.pieces.positions;

import chess.Direction;
import chess.GameState;
import chess.Player;

/**
 * A position generator that will move a limited number of steps
 */
public class KingPositionGenerator extends StraightLinePositionGenerator {

    public KingPositionGenerator(Player player, GameState gameState) {
        super(player, gameState, Direction.North, Direction.NorthEast, Direction.East, Direction.SouthEast,
                Direction.South, Direction.SouthWest, Direction.West, Direction.NorthWest);
        setMaxNumSteps(1);
    }

}
