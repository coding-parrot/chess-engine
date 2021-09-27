package commons;

import game.Move;

import java.util.Set;

public class LegalMoves {
    public final Set<Move> moves;
    public final Set<Move> guards;

    public LegalMoves(Set<Move> moves, Set<Move> guards) {
        this.moves = moves;
        this.guards = guards;
    }
}
