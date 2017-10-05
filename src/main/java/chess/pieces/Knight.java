package chess.pieces;

import chess.Player;
import chess.Position;
import chess.pieces.positions.KnightPositionGenerator;
import chess.pieces.positions.PiecePositionGenerator;

import java.util.Set;


/**
 * Represents the Knight type
 */
public class Knight extends Piece {

    public Knight(Player player) {
        super(player);
    }

    @Override
    protected Set<PiecePositionGenerator> getPositionGenerators() {
        return arrayToSet(new PiecePositionGenerator[] {
                new KnightPositionGenerator(player, state)
        });
    }

    @Override
    protected char getIdentifierCharacter() {
        return 'n';
    }
}
