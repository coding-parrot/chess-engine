package pieces;

import commons.Color;
import commons.LegalMoves;
import commons.Piece;
import game.Board;
import game.Cell;
import game.Move;

import java.util.HashSet;
import java.util.Set;

public class Pawn {

    public static LegalMoves getMoveList(Board board, Piece piece) {
        final Set<Move> moves = new HashSet<>(8);
        final Set<Move> guards = new HashSet<>(8);
        final int rowDiff = piece.color == Color.WHITE ? 1 : -1;
        final int row = piece.position.row + rowDiff;
        if (row < 8 && row >= 0) {
            PieceType[] pieces = null;
            if (row == 7 || row == 0) {
                pieces = new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
            }
            if (board.isEmpty(row, piece.position.col)) {
                addMoves(Cell.get(row, piece.position.col), false, pieces, moves, piece);
            }
            for (int i = -1; i <= 1; i = i + 2) {
                final int col = piece.position.col + i;
                if (col < 8 && col >= 0) {
                    final Piece diag = board.getPiece(row, col);
                    if (diag != null && diag.color != piece.color) {
                        addMoves(Cell.get(row, col), true, pieces, moves, piece);
                    }
                    if (diag == null || diag.color == piece.color) {
                        guards.add(Move.get(piece, Cell.get(row, col), false));
                    }
                }
            }
            if (piece.position.row == (piece.color == Color.BLACK ? 6 : 1)) {
                if (board.isEmpty(row, piece.position.col)
                        && board.isEmpty(piece.position.row + 2 * rowDiff, piece.position.col)) {
                    moves.add(Move.get(piece, Cell.get(piece.position.row + 2 * rowDiff, piece.position.col), false));
                }
            }
        }
        addEnPassant(board, rowDiff, moves, piece);
        return new LegalMoves(moves, guards);
    }

    private static void addEnPassant(Board board, int rowDiff, Set<Move> moves, Piece piece) {
        if (!board.moveList.isEmpty()) {
            final Move lastMove = board.moveList.get(board.moveList.size() - 1);
            if (allowsEnPassant(lastMove, piece) && piece.position.row == (piece.color == Color.BLACK ? 3 : 4)) {
                final int colDiff = lastMove.target.col - piece.position.col;
                final int col = piece.position.col + colDiff;
                if (Math.abs(colDiff) == 1 && col < 8 && col >= 0) {
                    final Piece diag = board.getPiece(piece.position.row, col);
                    if (diag != null && diag.sameType(PieceType.PAWN) && diag.color != piece.color) {
                        final Move enPassant = Move.get(piece, Cell.get(piece.position.row + rowDiff, col), true, lastMove.target);
                        moves.add(enPassant);
                    }
                }
            }
        }
    }

    private static boolean allowsEnPassant(final Move move, Piece piece) {
        return move.piece.sameType(PieceType.PAWN) && Math.abs(move.piece.position.row - move.target.row) == 2 && Math.abs(move.target.col - piece.position.col) == 1;
    }

    private static void addMoves(Cell destination, boolean capture, PieceType[] pieces, Set<Move> moves, Piece piece) {
        if (pieces == null) {
            moves.add(Move.get(piece, destination, capture));
        } else {
            for (final PieceType pieceType : pieces) {
                moves.add(Move.get(piece, destination, capture, capture ? destination : null, pieceType));
            }
        }
    }
}
