package pieces;

import commons.LegalMoves;
import commons.Piece;
import commons.Utils;
import game.Board;
import game.Cell;
import game.Move;

import java.util.HashSet;
import java.util.Set;

public class Knight {
    public static final int[][] diff = new int[][]{
            {2, 1}, {1, 2},
            {2, -1}, {1, -2},
            {-2, -1}, {-1, -2},
            {-2, 1}, {-1, 2}
    };

    public static LegalMoves getMoveList(Board board, Piece piece) {
        final Set<Move> moves = new HashSet<>(8);
        final Set<Move> guards = new HashSet<>(8);
        for (final int[] coordinate : diff) {
            final int row = piece.position.row + coordinate[0], col = piece.position.col + coordinate[1];
            if (Utils.withinBoardLimits(row, col)) {
                if (board.isEmpty(row, col)) {
                    moves.add(Move.get(piece, Cell.get(row, col), false));
                } else if (board.getPiece(row, col).color != piece.color) {
                    moves.add(Move.get(piece, Cell.get(row, col), true));
                } else {
                    guards.add(Move.get(piece, Cell.get(row, col), false));
                }
            }
        }
        return new LegalMoves(moves, guards);
    }
}