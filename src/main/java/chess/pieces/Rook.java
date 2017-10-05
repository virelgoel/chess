package chess.pieces;

import chess.Player;
import chess.Position;
import chess.pieces.positions.ColumnPositionGenerator;
import chess.pieces.positions.PiecePositionGenerator;
import chess.pieces.positions.RowPositionGenerator;

import java.util.Set;

/**
 * Represents the Rook type
 */
public class Rook extends Piece {

    public Rook(Player player) {
        super(player);
    }

    @Override
    protected Set<PiecePositionGenerator> getPositionGenerators() {
        return arrayToSet(new PiecePositionGenerator[] {
                new ColumnPositionGenerator(player, state),
                new RowPositionGenerator(player, state)
        });
    }

    @Override
    protected char getIdentifierCharacter() {
        return 'r';
    }
}
