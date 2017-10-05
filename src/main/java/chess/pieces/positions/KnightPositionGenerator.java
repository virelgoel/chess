package chess.pieces.positions;

import chess.GameState;
import chess.Player;
import chess.Position;

import java.util.HashSet;
import java.util.Set;

import static chess.Direction.*;
import static chess.Direction.South;

/**
 * A position generator specific to the Knight class
 */
public class KnightPositionGenerator extends PiecePositionGenerator {

    public KnightPositionGenerator(Player player, GameState gameState) {
        super(player, gameState);

    }

    @Override
    public Set<Position> generate(Position origin) {
        Set<Position> possible = new HashSet<Position>();

        Position step = origin.step(North);
        if (step != null) {
            step = step.step(North);
            if (step != null) {
                addIfGoodPosition(possible, step.step(East), step.step(West));
            }
        }

        step = origin.step(South);
        if (step != null) {
            step = step.step(South);
            if (step != null) {
                addIfGoodPosition(possible, step.step(East), step.step(West));
            }
        }

        step = origin.step(East);
        if (step != null) {
            step = step.step(East);
            if (step != null) {
                addIfGoodPosition(possible, step.step(North), step.step(South));
            }
        }

        step = origin.step(West);
        if (step != null) {
            step = step.step(West);
            if (step != null) {
                addIfGoodPosition(possible, step.step(North), step.step(South));
            }
        }


        return possible;
    }

    /**
     * Add positions to a given set of positions IFF the position is not null AND the
     * position is not occupied by me
     * @param set The set to add things to
     * @param positions The position(s) to conditionally add to the set
     */
    private void addIfGoodPosition(Set<Position> set, Position... positions) {
        for (Position position : positions) {
            if (position != null && !isOccupied(position, player)) {
                set.add(position);
            }
        }
    }

}
