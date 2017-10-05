package chess.pieces;

import chess.Player;
import chess.Position;
import chess.pieces.positions.PawnPositionGenerator;
import chess.pieces.positions.PiecePositionGenerator;

import java.util.Set;

/**
 * Represents the Pawn piece type
 */
public class Pawn extends Piece {

    public Pawn(Player player) {
        super(player);
    }

    @Override
    protected Set<PiecePositionGenerator> getPositionGenerators() {
        return arrayToSet(new PiecePositionGenerator[] {
                new PawnPositionGenerator(player, state)
        });
    }

    @Override
    protected char getIdentifierCharacter() {
        return 'p';
    }
}
