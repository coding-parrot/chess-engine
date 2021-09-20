package commons;

public enum Color {
    WHITE, BLACK;

    public static Color opponent(Color playerToMove) {
        return playerToMove == BLACK ? WHITE : BLACK;
    }
}
