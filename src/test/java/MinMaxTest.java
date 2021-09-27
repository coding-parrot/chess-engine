import commons.Color;
import game.Board;
import game.Cell;
import org.junit.Assert;
import org.junit.Test;
import pieces.PieceType;

public class MinMaxTest {

    @Test
    public void mateInOne() {
        Board board = new Board();
        board.placeKing(7, 0, Color.BLACK);
        board.placeKing(1, 0, Color.WHITE);
        board.placeRook(6, 4, Color.WHITE);
        board.placeRook(2, 5, Color.WHITE);
        System.out.println(board.fenRepresentation());
        System.out.println(board);
        Engine engine = new Engine();
        final Evaluation bestMove = engine.minMax(board, 1, 1);
        System.out.println(bestMove.getMove());
        System.out.println(bestMove.getScore());
        board.makeMove(bestMove.getMove());
        System.out.println(board);
        Assert.assertEquals(bestMove.getMove().target, Cell.get(7, 5));
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.ROOK));
    }

    @Test
    public void mateInThree() {
        Board board = Board.getBoard("7k/4K1pp/7N/8/8/8/8/B7 w - - 0 1");
        System.out.println(board);
        Engine engine = new Engine();
        final Evaluation bestMove = engine.minMax(board, 5, 5);
        System.out.println(bestMove.getMove());
        System.out.println(bestMove.getScore());
        board.makeMove(bestMove.getMove());
        System.out.println(board);
        Assert.assertEquals(bestMove.getMove().target, Cell.get(5, 5));
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.BISHOP));
    }

    @Test
    public void mateInTwo() {
        Board board = Board.getBoard("7k/4K2p/5p1N/8/8/8/8/8 w - - 0 2");
        System.out.println(board);
        Engine engine = new Engine();
        final Evaluation bestMove = engine.minMax(board, 3, 3);
        System.out.println(bestMove.getMove());
        System.out.println(bestMove.getScore());
        board.makeMove(bestMove.getMove());
        System.out.println(board);
        Assert.assertEquals(bestMove.getMove().target, Cell.get(7, 5));
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.KING));
    }

    @Test
    public void complexPosition() {
        Board board = Board.getBoard("r4rk1/1bp1q1p1/1p5p/3n4/p2Ppp2/2P3QN/PPB2PPP/R3R1K1 w - - 0 22");
        System.out.println(board);
        Engine engine = new Engine();
        final OutCome bestMove = engine.iterativeDeepening(board, 10);
        System.out.println(bestMove.getMove());
        System.out.println(bestMove.getScore());
        board.makeMove(bestMove.getMove());
        System.out.println(board);
        Assert.assertEquals(Cell.get(5, 6), bestMove.getMove().target);
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.QUEEN));
    }

    @Test
    public void winQueen() {
        Board board = Board.getBoard("3r1k2/5p2/p4N1p/1p4p1/2pq4/6P1/1P3Q1P/4R1K1 w - - 2 33");
        System.out.println(board);
        Engine engine = new Engine();
        final OutCome bestMove = engine.iterativeDeepening(board, 20);
        System.out.println(bestMove.getMove());
        System.out.println(bestMove.getScore());
        board.makeMove(bestMove.getMove());
        System.out.println(board);
        Assert.assertEquals(Cell.get(7, 4), bestMove.getMove().target);
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.ROOK));
    }

    @Test
    public void endGamePgn() {
        Board board = Board.getBoard("2k5/8/5R2/8/8/8/3K4/4R3 w - - 5 6");
        System.out.println(board);
        Engine engine = new Engine();
        final OutCome bestMove = engine.iterativeDeepening(board, 20);
        System.out.println(bestMove.getMove());
        Assert.assertEquals(Integer.MIN_VALUE, bestMove.getScore(), 0.000001);
    }

    @Test
    public void hardMateIn3() {
        Board board = Board.getBoard("8/8/2P1Q3/3P3R/3k4/8/2K5/8 w - - 0 1");
        System.out.println(board);
        Engine engine = new Engine();
        final OutCome bestMove = engine.iterativeDeepening(board, 20);
        System.out.println(bestMove.getMove());
        Assert.assertEquals(Cell.get(6, 5), bestMove.getMove().target);
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.QUEEN));
        Assert.assertEquals(Integer.MIN_VALUE, bestMove.getScore(), 0.000001);
    }

    @Test
    public void mateIn4() {
        Board board = Board.getBoard("7R/r1p1q1pp/3k4/1p1n1Q2/3N4/8/1PP2PPP/2B3K1 w - - 1 0");
        System.out.println(board);
        Engine engine = new Engine();
        final OutCome bestMove = engine.iterativeDeepening(board, 20);
        System.out.println(bestMove.getMove());
        Assert.assertEquals(Cell.get(7, 3), bestMove.getMove().target);
        Assert.assertTrue(bestMove.getMove().piece.sameType(PieceType.ROOK));
        Assert.assertEquals(Integer.MIN_VALUE, bestMove.getScore(), 0.000001);
    }
}
