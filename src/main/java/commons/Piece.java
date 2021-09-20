package commons;

import game.Board;
import game.Cell;
import game.Move;
import pieces.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Piece {
    public final Color color;
    public final Cell position;
    public final PieceType pieceType;
    private static final Piece[][][] pieces = getPieces();

    private static Piece[][][] getPieces() {
        final Piece[][][] pieces = new Piece[2][64][6];
        final Color[] colors = Color.values();
        final PieceType[] pieceTypes = PieceType.values();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 64; j++) {
                for (int k = 0; k < 6; k++) {
                    pieces[i][j][k] = new Piece(colors[i], Cell.get((j >> 3), j & 7), pieceTypes[k]);
                }
            }
        }
        return pieces;
    }

    public static Piece get(Color color, Cell position, PieceType pieceType) {
        return pieces[color.ordinal()][(position.row << 3) + position.col][pieceType.ordinal()];
    }

    private Piece(Color color, Cell position, PieceType pieceType) {
        this.color = color;
        this.position = position;
        this.pieceType = pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return color == piece.color && position.equals(piece.position) && pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, position, pieceType);
    }

    public Set<Move> getMoveList(Board board) {
        switch (pieceType) {
            case BISHOP:
                return Bishop.getMoveList(board, this).moves;
            case KNIGHT:
                return Knight.getMoveList(board, this).moves;
            case ROOK:
                return Rook.getMoveList(board, this).moves;
            case KING:
                return King.getMoveList(board, this).moves;
            case QUEEN:
                return Queen.getMoveList(board, this).moves;
            case PAWN:
                return Pawn.getMoveList(board, this).moves;
            default:
                throw new IllegalStateException();
        }
    }

    public LegalMoves getLegalMoves(Board board) {
        switch (pieceType) {
            case BISHOP:
                return Bishop.getMoveList(board, this);
            case KNIGHT:
                return Knight.getMoveList(board, this);
            case ROOK:
                return Rook.getMoveList(board, this);
            case KING:
                return King.getMoveList(board, this);
            case QUEEN:
                return Queen.getMoveList(board, this);
            case PAWN:
                return Pawn.getMoveList(board, this);
            default:
                throw new IllegalStateException();
        }
    }

    public String getShortForm() {
        switch (pieceType) {
            case BISHOP:
                return color == Color.WHITE ? "♝" : "♗";
            case KNIGHT:
                return color == Color.WHITE ? "♞" : "♘";
            case ROOK:
                return color == Color.WHITE ? "♜" : "♖";
            case KING:
                return color == Color.WHITE ? "♚" : "♔";
            case QUEEN:
                return color == Color.WHITE ? "♛" : "♕";
            case PAWN:
                return color == Color.WHITE ? "♟" : "♙";
            default:
                throw new IllegalStateException();
        }
    }


    @Override
    public String toString() {
        return "(" + getShortForm() +
                ',' + position +
                ')';
    }

    public boolean sameType(PieceType pieceType) {
        return pieceType == this.pieceType;
    }
}
