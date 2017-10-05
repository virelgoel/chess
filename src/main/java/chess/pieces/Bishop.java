package chess.pieces;

import chess.Player;
import chess.Position;
import chess.pieces.positions.DiagonalPositionGenerator;
import chess.pieces.positions.PiecePositionGenerator;

import java.util.Set;

/**
 * Represents the Bishop piece type
 */
public class Bishop extends Piece {

    public Bishop(Player player) {
        super(player);
    }

    @Override
    protected Set<PiecePositionGenerator> getPositionGenerators() {
        return arrayToSet(new PiecePositionGenerator[]{
                new DiagonalPositionGenerator(player, state)
        });
    }

    @Override
    protected char getIdentifierCharacter() {
        return 'b';
    }
}
