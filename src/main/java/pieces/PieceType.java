package pieces;

public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

    public char getLetter() {
        switch (ordinal()) {
            case 0:
                return 'p';
            case 1:
                return 'n';
            case 2:
                return 'b';
            case 3:
                return 'r';
            case 4:
                return 'q';
            case 5:
                return 'k';
        }
        throw new RuntimeException();
    }
}
