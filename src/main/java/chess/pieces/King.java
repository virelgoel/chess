package chess.pieces;

import chess.Player;
import chess.pieces.positions.KingPositionGenerator;
import chess.pieces.positions.PiecePositionGenerator;

import java.util.Set;

/**
 * Represents the King
 */
public class King extends Piece {

    /**
     * Create a new King
     * @param player The side the piece belongs to
     */
    public King(Player player) {
        super(player);
    }

    @Override
    public char getIdentifierCharacter() {
        return 'k';
    }

    @Override
    protected Set<PiecePositionGenerator> getPositionGenerators() {
        return arrayToSet(new PiecePositionGenerator[] {
                new KingPositionGenerator(player, state)
        });
    }
}
