package commons;

import game.Board;
import game.Cell;
import game.Move;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class Utils {
    public static LegalMoves getMoves(final Board board,
                                      final int maxDistance,
                                      final BiFunction<Boolean, Boolean, Boolean> isLegal,
                                      final Piece piece) {
        final Set<Move> moves = new HashSet<>();
        final Set<Move> guards = new HashSet<>();
        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                boolean straightRow = rowDiff == 0;
                boolean straightCol = colDiff == 0;
                if (isLegal.apply(straightRow, straightCol) && !(straightRow && straightCol)) {
                    int row = piece.position.row + rowDiff, col = piece.position.col + colDiff;
                    for (int i = 1; i <= maxDistance; i++, row = row + rowDiff, col = col + colDiff) {
                        if (Utils.withinBoardLimits(row, col)) {
                            if (board.isEmpty(row, col)) {
                                moves.add(Move.get(piece, Cell.get(row, col), false));
                            } else {
                                if (board.getPiece(row, col).color != piece.color) {
                                    moves.add(Move.get(piece, Cell.get(row, col), true));
                                } else {
                                    guards.add(Move.get(piece, Cell.get(row, col), false));
                                }
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return new LegalMoves(moves, guards);
    }

    public static boolean withinBoardLimits(final int row, final int col) {
        return row < 8 && row >= 0 && col < 8 && col >= 0;
    }
}
