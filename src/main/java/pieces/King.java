package pieces;

import commons.*;
import game.Board;
import game.Cell;
import game.Move;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class King {

    public static final BiFunction<Boolean, Boolean, Boolean> movement = (rowStraight, colStraight) -> true;
    public static final int MAX_DISTANCE = 1;

    public static LegalMoves getMoveList(Board board, Piece piece) {
        final LegalMoves legalMoves = Utils.getMoves(board, MAX_DISTANCE, movement, piece);
        filterIllegalMoves(board, legalMoves.moves, piece);
        return legalMoves;
    }

    private static void filterIllegalMoves(Board board, Set<Move> moves, Piece king) {
        final Set<Cell> illegalSquares = getIllegalSquares(board, Color.opponent(king.color),
                moves.stream().map(move -> move.target).collect(Collectors.toSet()), king.position);
        moves.removeIf(move -> illegalSquares.contains(move.target));
        if (board.inCheck) {
            moves.removeIf(move -> xRay(move, board, king));
        } else {
            final Move[] castles = castlingMoves(board, king, moves);
            for (int i = 0; i < castles.length && castles[i] != null; i++) {
                moves.add(castles[i]);
            }
        }
    }

    private static Set<Cell> getIllegalSquares(final Board board,
                                               final Color opponentColor,
                                               final Set<Cell> positions,
                                               final Cell kingPosition) {
        final Set<Cell> illegalSquares = new HashSet<>();
        final List<Piece> pieces = board.playerPieces.get(opponentColor).stream()
                .filter(piece -> withinKingRange(kingPosition, piece.pieceType, piece.position))
                .sorted(Comparator.comparingInt(piece -> -Board.approxValue.get(piece.pieceType)))
                .collect(Collectors.toList());
        for (final Cell kingMove : positions) {
            for (final Piece piece : pieces) {
                final int rowDiff = Math.abs(piece.position.row - kingMove.row);
                final int colDiff = Math.abs(piece.position.col - kingMove.col);
                final int diagDiff = Math.abs(Math.abs(piece.position.col - piece.position.row) - Math.abs(kingMove.col - kingMove.row));
                final int revDiff = Math.abs(piece.position.col + piece.position.row - (kingMove.col + kingMove.row));
                switch (piece.pieceType) {
                    case BISHOP:
                        if (diagDiff <= 0 || revDiff <= 0) {
                            if (board.getMoveList(piece).contains(Move.get(piece, kingMove, false))
                                    || board.getGuardList(piece).contains(Move.get(piece, kingMove, false))) {
                                illegalSquares.add(kingMove);
                            }
                        }
                        break;
                    case ROOK:
                        if (rowDiff <= 0 || colDiff <= 0) {
                            if (board.getMoveList(piece).contains(Move.get(piece, kingMove, false))
                                    || board.getGuardList(piece).contains(Move.get(piece, kingMove, false))) {
                                illegalSquares.add(kingMove);
                            }
                        }
                        break;
                    case KING:
                        if ((rowDiff == 1 || colDiff == 1) && (rowDiff <= 1 && colDiff <= 1)) {
                            for (int i = -1; i <= 1; i++) {
                                for (int j = -1; j <= 1; j++) {
                                    final int row = piece.position.row + i;
                                    final int col = piece.position.col + j;
                                    if (Utils.withinBoardLimits(row, col)) {
                                        illegalSquares.add(Cell.get(row, col));
                                    }
                                }
                            }
                        }
                        break;
                    case PAWN:
                        if (rowDiff == 1 && colDiff == 1 &&
                                (opponentColor.equals(Color.WHITE) ? (kingMove.row > piece.position.row) : (kingMove.row < piece.position.row))) {
                            illegalSquares.add(kingMove);
                        }
                        break;
                    case KNIGHT:
                        if (rowDiff <= 2 && colDiff <= 2 && rowDiff + colDiff == 3) {
                            if (board.getMoveList(piece).contains(Move.get(piece, kingMove, false))
                                    || board.getGuardList(piece).contains(Move.get(piece, kingMove, false))) {
                                illegalSquares.add(kingMove);
                            }
                        }
                        break;
                    case QUEEN:
                        if (rowDiff <= 0 || colDiff <= 0 || diagDiff <= 0 || revDiff <= 0) {
                            if (board.getMoveList(piece).contains(Move.get(piece, kingMove, false))
                                    || board.getGuardList(piece).contains(Move.get(piece, kingMove, false))) {
                                illegalSquares.add(kingMove);
                            }
                        }
                        break;
                }
                if (illegalSquares.contains(kingMove)) {
                    break;
                }
            }
        }
        return illegalSquares;
    }

    public static boolean withinKingRange(final Cell kingPosition,
                                          final PieceType pieceType,
                                          final Cell piecePosition) {
        final int rowDiff = Math.abs(piecePosition.row - kingPosition.row);
        final int colDiff = Math.abs(piecePosition.col - kingPosition.col);
        final int diagDiff = Math.abs(Math.abs(piecePosition.col - piecePosition.row) - Math.abs(kingPosition.col - kingPosition.row));
        final int revDiff = Math.abs(piecePosition.col + piecePosition.row - (kingPosition.col + kingPosition.row));
        switch (pieceType) {
            case BISHOP:
                return diagDiff <= 2 || revDiff <= 2;
            case ROOK:
                return rowDiff <= 1 || colDiff <= 1;
            case KING:
                return (rowDiff == 2 || colDiff == 2) && (rowDiff <= 2 && colDiff <= 2);
            case PAWN:
                return rowDiff <= 2 && colDiff <= 2;
            case KNIGHT:
                return rowDiff <= 3 && colDiff <= 3 && rowDiff + colDiff <= 5;
            case QUEEN:
                return rowDiff <= 1 || colDiff <= 1 || diagDiff <= 2 || revDiff <= 2;
        }
        return false;
    }

    private static Move[] castlingMoves(final Board board, final Piece king, final Set<Move> kingMoves) {
        final Move[] castleMoves = new Move[2];
        int castles = 0;
        final int row = king.color.ordinal() * 7;
        if (king.position.row == row) {
            for (int i = 0; i <= 1; i++) {
                if (board.canCastle[king.color.ordinal()][i]) {
                    final Piece rook = board.getPiece(row, i * 7);
                    if (rook != null && rook.sameType(PieceType.ROOK)) {
                        final int colDiff = i == 0 ? -1 : 1;
                        final int col = 4 + colDiff;
                        final boolean clear = board.isEmpty(row, col)
                                && kingMoves.stream()
                                .map(move -> move.target)
                                .anyMatch(cell -> Cell.get(row, col).equals(cell));
                        final Cell kingCastleCell = Cell.get(row, col + colDiff);
                        if (clear && board.isEmpty(row, col + colDiff) && !getIllegalSquares(board, Color.opponent(king.color), Collections.singleton(kingCastleCell), kingCastleCell).contains(kingCastleCell)) {
                            if (i == 1 || board.isEmpty(row, 1)) {
                                castleMoves[castles++] = Move.get(king, Cell.get(row, 4 + 2 * colDiff), false);
                            }
                        }
                    }
                }
            }
        }
        return castleMoves;
    }

    private static boolean xRay(Move move, Board board, Piece king) {
        final Line line = new Line(move.piece.position, move.target);
        for (int index = 1; index < 8; index++) {
            final int row = move.piece.position.row + index * line.rowDiff, col = move.piece.position.col + index * line.colDiff;
            if (Utils.withinBoardLimits(row, col)) {
                if (!board.isEmpty(row, col)) {
                    final Piece piece = board.getPiece(row, col);
                    return piece.color != king.color && (piece.sameType(line.minorPieceType) || piece.sameType(PieceType.QUEEN));
                }
            } else {
                break;
            }
        }
        return false;
    }
}
