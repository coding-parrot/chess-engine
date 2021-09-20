package pieces;

import commons.LegalMoves;
import commons.Piece;
import commons.Utils;
import game.Board;
import game.Move;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class Queen {
    public static final BiFunction<Boolean, Boolean, Boolean> movement = (rowStraight, colStraight) -> true;
    public static final int MAX_DISTANCE = 7;

    public static LegalMoves getMoveList(Board board, Piece piece) {
        return Utils.getMoves(board, MAX_DISTANCE, movement,  piece);
    }
}
