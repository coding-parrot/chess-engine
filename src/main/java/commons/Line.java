package commons;

import game.Cell;
import pieces.PieceType;

public class Line {
    public final int rowDiff;
    public final int colDiff;
    public final boolean isStraight;
    public final PieceType minorPieceType;

    public Line(final Cell first, final Cell second) {
        int rowDistance = Math.abs(first.row - second.row);
        int colDistance = Math.abs(first.col - second.col);
        isStraight = rowDistance == 0 || colDistance == 0 || rowDistance == colDistance;
        rowDiff = Integer.compare(first.row, second.row);
        colDiff = Integer.compare(first.col, second.col);
        minorPieceType = rowDiff == 0 || colDiff == 0 ? PieceType.ROOK : PieceType.BISHOP;
    }
}
