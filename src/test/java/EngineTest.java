import commons.Color;
import commons.Piece;
import game.Board;
import game.Cell;
import game.Move;
import org.junit.Assert;
import org.junit.Test;

public class EngineTest {
    @Test
    public void fenRepresentation() {
        Board board = Board.getStartBoard();
        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", board.fenRepresentation());
        board.makeMove(Move.get(board.getPiece(1, 4), Cell.get(3, 4), false));
        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3", board.fenRepresentation());
        board.makeMove(Move.get(board.getPiece(6, 2), Cell.get(4, 2), false));
        Assert.assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6", board.fenRepresentation());
        board.makeMove(Move.get(board.getPiece(0, 6), Cell.get(2, 5), false));
        Assert.assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq -", board.fenRepresentation());
    }

    @Test
    public void otherBoard() {
        Board board = Board.getStartBoard();
        board.makeMove(Move.get(board.getPiece(1, 4), Cell.get(3, 4), false));
        Engine engine = new Engine();
        board = board.copy();
        board.makeMove(Move.get(board.getPiece(6, 3), Cell.get(5, 3), false));
        board = board.copy();
        board.makeMove(Move.get(board.getPiece(3, 4), Cell.get(4, 4), false));
        board = board.copy();
        board.makeMove(Move.get(board.getPiece(6, 5), Cell.get(4, 5), false));
//        board = board.copy();
//        board.makeMove(board.getPiece(4, 4).getMoveList(board).stream().filter(c -> c.captureMove).filter(c -> c.captureCell.equals(Cell.get(4, 5))).findAny().get());
        final int positions = engine.countAllMoves(board, 2);
        System.out.println(board);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        System.out.println(board.fenRepresentation());
        Assert.assertEquals(729, positions);
    }

    @Test
    public void countMovesAfterMove() {
        Board board = Board.getStartBoard();
        Piece piece = board.getPiece(1, 1);
        board.makeMove(Move.get(piece, Cell.get(2, 1), false));
        piece = board.getPiece(6, 1);
        board.makeMove(Move.get(piece, Cell.get(5, 0), false));
        Engine engine = new Engine();
        System.out.println(board);
        Assert.assertEquals("rnbqkbnr/p1pppppp/p7/8/8/1P6/P1PPPPPP/RNBQKBNR w KQkq -", board.fenRepresentation());
        Assert.assertEquals(21, engine.countAllMoves(board, 1));
    }

    @Test
    public void countMovesAtPosition31() {
        Board board = Board.getBoard("8/2p5/K2p4/1P4kr/1R3p2/8/4P1P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(1563505, engine.countAllMoves(board, 5));
    }

    @Test
    public void countMovesAtPosition32() {
        Board board = Board.getBoard("8/1Kp5/3p4/1P4kr/1R3p2/8/4P1P1/8 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(129425, engine.countAllMoves(board, 4));
    }

    @Test
    public void countMovesAtPosition33() {
        Board board = Board.getBoard("8/1Kp5/3p4/1P3k1r/1R3p2/8/4P1P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(6416, engine.countAllMoves(board, 3, 3));
    }

    @Test
    public void countDoublePawnMovePossibilities() {
        Board board = Board.getBoard("8/1Kp5/3p4/1P3k1r/1R2Pp2/8/6P1/8 b - e3");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(120, engine.countAllMoves(board, 2, 2));
    }

    @Test
    public void countEnpassantPossibilities() {
        Board board = Board.getBoard("8/1Kp5/3p4/1P3k1r/1R6/4p3/6P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(20, engine.countAllMoves(board, 1));
    }


    @Test
    public void countMovesAtPosition34() {
        Board board = Board.getBoard("8/2K5/3p4/1P3k1r/1R3p2/8/4P1P1/8 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(318, engine.countAllMoves(board, 2));
    }

    @Test
    public void countMovesAtPosition35() {
        Board board = Board.getBoard("8/2K5/3p4/1P2k2r/1R3p2/8/4P1P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        Assert.assertEquals(20, engine.countAllMoves(board, 1));
    }

    @Test
    public void countMovesAtPositionRandom() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R4K1R b kq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(77887, positions);
    }

    @Test
    public void countMovesAtPositionInCheck() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q2/PPPBBPpP/R4K1R w kq -");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(188, positions);
    }

    @Test
    public void countMovesAtPositionMovesAfterCheck() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q2/PPPBBPpP/R3K2R b kq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(52, positions);
    }

    @Test
    public void countMovesAtPositionMovesAfterCheck2() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q2/PPPBBPpP/R5KR b kq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(48, positions);
    }

    @Test
    public void queenToEdge() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN2Q/1p2P3/2N4p/PPPBBPPP/R3K2R b KQkq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(95034, positions);
    }

    @Test
    public void queenToDeath() {
        Board board = Board.getBoard("r3k1r1/p1ppqpb1/bn2pnp1/3PN2Q/1p2P3/2N4p/PPPBBPPP/R3K2R w KQq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(2079, positions);
    }

    @Test
    public void queenToDeathBlockCastle() {
        Board board = Board.getBoard("r3k1r1/p1ppqpb1/Bn2pnp1/3PN2Q/1p2P3/2N4p/PPPB1PPP/R3K2R b KQq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(32, positions);
    }

    @Test
    public void queenDeterminedToCommitSuicide() {
        Board board = Board.getBoard("r3k1rQ/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N4p/PPPBBPPP/R3K2R b KQq -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(38, positions);
    }

    @Test
    public void rookEndgameWithKing() {
        Board board = Board.getBoard("8/2p5/3p4/KP4kr/5p2/8/4P1P1/1R6 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 4, 4);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(104371, positions);
    }

    @Test
    public void rookEndgameWithKing8() {
        Board board = Board.getBoard("8/2p5/3p4/KP4kr/5pP1/8/4P3/1R6 b - g3");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(5260, positions);
    }

    @Test
    public void rookEndgameWithKing8_1() {
        Board board = Board.getBoard("8/8/2pp4/KP4kr/5pP1/8/4P3/1R6 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(295, positions);
    }

    @Test
    public void rookEndgameWithKing9() {
        Board board = Board.getBoard("8/2p5/3p4/KP4k1/5pP1/8/4P3/1R5r w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(328, positions);
    }

    @Test
    public void rookEndgameWithKing10() {
        Board board = Board.getBoard("8/2p5/3p4/KP4k1/5pP1/8/4P3/6Rr b - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(16, positions);
    }

    @Test
    public void rookEndgameWithKingPawn() {
        Board board = Board.getBoard("8/2p5/3p4/KP4kr/4Pp2/8/6P1/1R6 b - e3");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3, 3);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(5503, positions);
    }

    @Test
    public void rookEndgameWithKingRookMove() {
        Board board = Board.getBoard("8/2p5/3p4/KP4k1/4Pp2/7r/6P1/1R6 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(412, positions);
    }

    @Test
    public void rookEndgameWithKingRookMoving() {
        Board board = Board.getBoard("7r/2p5/3p4/KP4k1/4Pp2/8/6P1/1R6 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(407, positions);
    }

    @Test
    public void rookEndgameWithKingRookMoveA() {
        Board board = Board.getBoard("8/2p5/8/KP1p2kr/4Pp2/8/6P1/1R6 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(305, positions);
    }

    @Test
    public void rookEndgameWithKing2() {
        Board board = Board.getBoard("8/2p5/3p4/KP4kr/5p2/8/4P1P1/6R1 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(5007, positions);
    }

    @Test
    public void rookEndgameWithKing3() {
        Board board = Board.getBoard("8/2p5/3p4/KP4k1/5p1r/8/4P1P1/6R1 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(271, positions);
    }

    @Test
    public void rookEndgameWithKing4() {
        Board board = Board.getBoard("8/2p5/3p4/KP4k1/5pPr/8/4P3/6R1 b - g3");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(16, positions);
    }

    @Test
    public void rookEndgameWithKing5() {
        Board board = Board.getBoard("8/2p5/3p4/KP4kr/1R3p2/8/4P1P1/8 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(5013, positions);
    }

    @Test
    public void rookEndgameWithKing6() {
        Board board = Board.getBoard("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(191, positions);
    }

    @Test
    public void moveKingInChaos() {
        Board board = Board.getBoard("r6r/Ppppkppp/1b3nbN/nPP5/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 4);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(2539008, positions);
    }

    @Test
    public void moveKingInChaos2() {
        Board board = Board.getBoard("r6r/Ppppkppp/1P3nbN/nP6/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 b - -");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 3);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(9501, positions);
    }

    @Test
    public void moveKingInChaos20() {
        Board board = Board.getBoard("r2k3r/Pppp1ppp/1P3nbN/nP6/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(1475, positions);
    }

    @Test
    public void moveKingInChaos21() {
        Board board = Board.getBoard("r2k3r/PpPp1ppp/5nbN/nP6/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 b - -");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(3, positions);
    }

    @Test
    public void moveKingInChaos0() {
        Board board = Board.getBoard("r6r/Pppp1ppp/1P2knbN/nP6/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 w - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(1614, positions);
    }

    @Test
    public void moveKingInChaos1() {
        Board board = Board.getBoard("r6r/Pppp1ppp/1P2knbN/nP2P3/BB6/q4N2/Pp1P2PP/R2Q1RK1 b - -");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(51, positions);
    }

    @Test
    public void moveKingInChaos3() {
        Board board = Board.getBoard("r6r/Pp1pkppp/1P3nbN/nPp5/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 w - c6");
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 2);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(1550, positions);
    }

    @Test
    public void moveKingInChaos4() {
        Board board = Board.getBoard("r6r/Pp1pkppp/1PP2nbN/n7/BB2P3/q4N2/Pp1P2PP/R2Q1RK1 b - -");
        board.inCheck = true;
        System.out.println(board);
        Engine engine = new Engine();
        final int positions = engine.countAllMoves(board, 1);
        System.out.println("NUMBER OF POSITIONS: " + positions);
        Assert.assertEquals(5, positions);
    }


    @Test
    public void kingCastling() {
        Board board = Board.getBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q2/PPPBBPpP/R3K2R b kq -");
        System.out.println(board);
        System.out.println(board.getKing(Color.BLACK).getMoveList(board).size());
        System.out.println(board.getKing(Color.BLACK).getMoveList(board));
    }
}

/*
e2e4: 120
g2g4: 137


e2e4: 122
g2g4: 139
 */