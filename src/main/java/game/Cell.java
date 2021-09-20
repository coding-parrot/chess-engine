package game;

public class Cell {
    public final int row, col;

    private static final Cell[] cells = setBoard();

    private Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Cell get(final int row, final int col) {
        return cells[(row << 3) + col];
    }

    private static Cell[] setBoard() {
        final Cell[] cells = new Cell[64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[(i << 3) + j] = new Cell(i, j);
            }
        }
        return cells;
    }

    @Override
    public String toString() {
        return "(" + row +
                ',' + col +
                ')';
    }

    public String notation() {
        return Character.valueOf((char) (col + 'a')).toString() + (row + 1);
    }
}
