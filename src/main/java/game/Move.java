package game;

import commons.Piece;
import pieces.PieceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Move {
    public final Piece piece;
    public final Cell target;
    public final boolean captureMove;
    public final Cell captureCell;
    public final PieceType promoteTo;
    private final int id;
    private static final Map<Integer, Move> moveMap = new HashMap<>();

    public static int count = 0;

    public static Move get(Piece piece, Cell end, boolean captureMove) {
        return get(piece, end, captureMove, captureMove ? end : null, null);
    }

    public static Move get(Piece piece, Cell end, boolean captureMove, Cell captureCell) {
        return get(piece, end, captureMove, captureMove ? captureCell : null, null);
    }

    public static Move get(Piece piece, Cell end, boolean captureMove, Cell captureCell, PieceType promoteTo) {
        final int colorIndex = piece.color.ordinal() << 6;
        final int positionIndex = (piece.position.row << 3) + piece.position.col;
        final int pieceIndex = (colorIndex + positionIndex) * 6 + piece.pieceType.ordinal();
        final int travelIndex = (end.row << 3) + end.col;
        final int captureIndex = captureMove ? ((captureCell.row << 3) + captureCell.col) + 1 : 0;
        final int promotionIndex = promoteTo == null ? 0 : promoteTo.ordinal() + 1;
        final int index = (((pieceIndex << 9) + travelIndex) * 65 + captureIndex) * 7 + promotionIndex;
        if (!moveMap.containsKey(index)) {
            moveMap.put(index, new Move(piece, end, captureMove, captureCell, promoteTo, index));
        }
        return moveMap.get(index);
    }

    private Move(Piece piece, Cell target, boolean captureMove, Cell captureCell, PieceType promoteTo, final int id) {
        count++;
        this.piece = piece;
        this.target = target;
        this.captureMove = captureMove;
        this.captureCell = captureCell;
        this.promoteTo = promoteTo;
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + printMove(piece, target) +
                ", captureMove=" + captureMove +
                ", captureCell=" + captureCell +
                ", promoteTo=" + promoteTo +
                '}';
    }

    private String printMove(Piece piece, Cell cell) {
        return piece.getShortForm() + "," + piece.position.notation() + cell.notation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return id == move.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
