package chess.pieces.positions;

import chess.Direction;
import chess.GameState;
import chess.Player;
import chess.Position;
import chess.pieces.Piece;

import java.util.*;

/**
 * A class that can generate a set of positions in a given set of directions.
 */
public abstract class StraightLinePositionGenerator extends PiecePositionGenerator {

    private final List<Direction> directions;
    private Integer maxNumSteps;

    StraightLinePositionGenerator(Player player, GameState gameState, Direction... directions) {
        super(player, gameState);
        this.directions = Arrays.asList(directions);
        maxNumSteps = null;
    }

    @Override
    public Set<Position> generate(Position origin) {
        Set<Position> positions = new HashSet<Position>();

        // Generate positions for every direction provided to this generator
        for (Direction direction : directions) {
            positions.addAll(generate(origin, direction));
        }

        return positions;
    }

    void setMaxNumSteps(Integer maxNumSteps) {
        this.maxNumSteps = maxNumSteps;
    }

    Collection<Position> generate(Position origin, Direction direction) {
        Set<Position> positions = new HashSet<Position>();

        // Take a step in this direction until we are blocked.
        Position current = origin.step(direction);
        Piece pieceAtCurrent;
        int numSteps = 0;
        while (current != null) {
            if (maxNumSteps != null && numSteps >= maxNumSteps) {
                break;
            }

            pieceAtCurrent = state.getPieceAt(current);
            if (pieceAtCurrent == null) {
                // No piece here; must be a possible position
                positions.add(current);

                // ... take the next step
                current = current.step(direction);
            } else {
                // There is a piece here.  If it's the other player's piece, this is
                // still a good move
                if (!player.equals(pieceAtCurrent.getPlayer())) {
                    positions.add(current);
                }

                // Either way, we cannot move beyond this step
                current = null;
            }
            numSteps++;
        }

        return positions;
    }
}
