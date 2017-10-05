package chess.pieces;

import chess.Player;
import chess.Position;
import chess.pieces.positions.ColumnPositionGenerator;
import chess.pieces.positions.DiagonalPositionGenerator;
import chess.pieces.positions.PiecePositionGenerator;
import chess.pieces.positions.RowPositionGenerator;

import java.util.Set;

/**
 * Represents the Queen type
 */
public class Queen extends Piece {

    public Queen(Player player) {
        super(player);

    }

    @Override
    protected Set<PiecePositionGenerator> getPositionGenerators() {
        return arrayToSet(new PiecePositionGenerator[]{
                new RowPositionGenerator(player, state),
                new ColumnPositionGenerator(player, state),
                new DiagonalPositionGenerator(player, state)
        });
    }

    @Override
    protected char getIdentifierCharacter() {
        return 'q';
    }
}
