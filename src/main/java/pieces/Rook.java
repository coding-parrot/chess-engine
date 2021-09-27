package pieces;

import commons.LegalMoves;
import commons.Piece;
import commons.Utils;
import game.Board;

import java.util.function.BinaryOperator;


public class Rook {
    public static final BinaryOperator<Boolean> movement = (rowStraight, colStraight) -> rowStraight || colStraight;
    public static final int MAX_DISTANCE = 7;

    public static LegalMoves getMoveList(Board board, Piece piece) {
        return Utils.getMoves(board, MAX_DISTANCE, movement, piece);
    }
}
