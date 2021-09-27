import game.Board;
import game.Move;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IntenseTest {
    @Test
    public void countMovesAtPosition4() {
        Board board = Board.getBoard("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(422333, engine.countAllMoves(board, 4));
        Assert.assertEquals(15833292, engine.countAllMoves(board, 5));
        Assert.assertEquals(706045033, engine.countAllMoves(board, 6));
    }

    @Test
    public void countMovesAtPosition4MinMax() {
        Board board = Board.getBoard("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        Engine.nodesEvaluated = 0;
        final Evaluation evaluation = engine.minMax(board, 4, 4);
        System.out.println(evaluation + " \nNODES: " + Engine.nodesEvaluated);
    }

    @Test
    public void countMovesAtPosition4AlphaBeta() {
        Board board = Board.getBoard("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        Engine.nodesEvaluated = 0;
        final OutCome evaluation = engine.alphaBeta(board, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, 4);
        System.out.println(evaluation + " \nNODES: " + Engine.nodesEvaluated);
    }

    @Test
    public void countMovesAtPosition5() {
        Board board = Board.getBoard("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(62379, engine.countAllMoves(board, 3));
        Assert.assertEquals(2103487, engine.countAllMoves(board, 4));
        Assert.assertEquals(89941194, engine.countAllMoves(board, 5));
    }

    @Test
    public void countMovesAtPosition6() {
        Board board = Board.getBoard("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 5);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(164075551, positions);
    }

    @Test
    public void rookEndgame() {
        Board board = Board.getBoard("8/2p5/3p4/KP5r/5p1k/8/4P1P1/1R6 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 5);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(1160678, positions);
    }

    @Test
    public void rookEndgameLowDepth() {
        Board board = Board.getBoard("8/2p5/3p4/KP5r/5p1k/8/4P1P1/1R6 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 4);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(69665, positions);
    }


    @Test
    public void countMovesAtPosition2() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(97862, engine.countAllMoves(board, 3));
        Assert.assertEquals(4085603, engine.countAllMoves(board, 4));
        Assert.assertEquals(193690690, engine.countAllMoves(board, 5));
    }

    @Test
    public void countMovesAtPosition3() {
        Board board = Board.getBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(674624, engine.countAllMoves(board, 5));
        Assert.assertEquals(11030083, engine.countAllMoves(board, 6));
        Assert.assertEquals(178633661, engine.countAllMoves(board, 7));
    }


    @Test
    public void countMovesAtPosition30() {
        Board board = Board.getBoard("8/2p5/K2p4/1P5r/1R3p1k/8/4P1P1/8 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(3653, engine.countAllMoves(board, 3));
        Assert.assertEquals(59028, engine.countAllMoves(board, 4));
        Assert.assertEquals(968724, engine.countAllMoves(board, 5));
        Assert.assertEquals(16022983, engine.countAllMoves(board, 6));
    }

    @Test
    public void countMovesAtPosition301() {
        Board board = Board.getBoard("8/2p5/K2p4/1P4kr/1R3p2/8/4P1P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(270, engine.countAllMoves(board, 2));
        Assert.assertEquals(4666, engine.countAllMoves(board, 3, 3));
    }

    @Test
    public void countMovesAtPosition302() {
        Board board = Board.getBoard("8/2p5/K2p4/1P4kr/1R3pP1/8/4P3/8 b - g3");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(257, engine.countAllMoves(board, 2));
    }

    @Test
    public void countMovesAtPosition303() {
        Board board = Board.getBoard("8/2p5/K2p4/1P4kr/1R6/6p1/4P3/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(16, engine.countAllMoves(board, 1));
    }

    @Test
    public void countMoves() {
        Board board = Board.getStartBoard();
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 6);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(119060324, positions);
        System.out.println("NUMBER OF MOVES: " + Move.count);
    }

    @Test
    public void chaoticPosition() {
        Board board = Board.getBoard("r3k2r/Pppp1ppp/1b3nbN/nPP5/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 b kq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 5);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(92063670, positions);
    }
}
/*
b4g4
b4h4
 */