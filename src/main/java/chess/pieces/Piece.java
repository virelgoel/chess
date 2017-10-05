package chess.pieces;

import chess.*;
import chess.pieces.positions.PiecePositionGenerator;

import java.util.*;

/**
 * A partial implementation of the Piece interface that provides logic to examine the piece's relationship
 * to other pieces in the same game.
 */
public abstract class Piece {

    /**
     * The player that owns this piece
     */
    final Player player;

    /**
     * The state of the game that this piece is involved in (if any)
     */
    GameState state = null;

    /**
     * The set of position generators to use with this piece
     */
    private Set<PiecePositionGenerator> positionGenerators;

    /**
     * Build this piece
     * @param player The player who owns this piece
     */
    Piece(Player player) {
        this.player = player;
    }

    /**
     * Get the player who owns this piece
     * @return The owning player
     */
    public final Player getPlayer() {
        return player;
    }

    public final GameState getGameState() {
        if (state == null) {
            throw new IllegalStateException("Piece " + this + " is not a part of of a game state");
        }

        return state;
    }

    /**
     * Associate this piece with a game
     * @param gameState The state to use
     */
    public final void setGameState(GameState gameState) {
        this.state = gameState;
        positionGenerators = getPositionGenerators();
    }

    /**
     * Utility method to convert an array of things to a set of things.
     * @param objs The elements to put into the Set
     * @param <T> The types to put into the set
     * @return The set of things
     */
    <T> Set<T> arrayToSet(T[] objs) {
        return new HashSet<T>(Arrays.asList(objs));
    }

    /**
     * Get the identifier for the piece.  Black pieces are upper-case; White pieces are lower-case
     * @return The identifier
     */
    public final char getIdentifier() {
        char id = getIdentifierCharacter();

        if (player.equals(Player.Black)) {
            return Character.toUpperCase(id);
        } else {
            return Character.toLowerCase(id);
        }
    }

    /**
     * Convenience method for determining if a given position is a part of the next
     * possible positions for this set.
     *
     * @param origin The origin to start rom
     * @param destination The destination to consider
     * @return True IFF the position is valid for this piece to move to.
     */
    public final boolean canMove(Position origin, Position destination) {
        Set<Position> possible = getNextPositions(origin);
        return possible.contains(destination);
    }

    /**
     * Get the set of positions that this piece can legally move to next.  The default implementation
     * will cycle through the members of 'positionGenerators' and generate the full set of legal moves
     * from those generators.
     * @return The set of positions that this piece may move to.
     * @param origin The position to start from
     */
    public Set<Position> getNextPositions(Position origin) {
        Set<Position> positions = new HashSet<Position>();

        for (PiecePositionGenerator generator : positionGenerators) {
            positions.addAll(generator.generate(origin));
        }

        return positions;
    }

    /**
     * Sub-classes will want to override this in order to provide the set of
     * PositionGenerators they use to create possible next moves.
     * This method is guaranteed to be called only after the game state has been called.
     * @return The position generators to use to determine the moves possible for
     * the piece at any time.
     */
    protected abstract Set<PiecePositionGenerator> getPositionGenerators();

    /**
     * Sub-classes must provide a unique character that identifies their piece type.
     * @return The identifying character.
     */
    protected abstract char getIdentifierCharacter();


    @Override
    public String toString() {
        return player + " " + getIdentifier();
    }
}
