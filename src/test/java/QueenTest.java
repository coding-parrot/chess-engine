import commons.Color;
import game.Board;
import org.junit.Assert;
import org.junit.Test;

public class QueenTest {
    @Test
    public void moveForward() {
        Board board = new Board();
        board.placeQueen(0, 0, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(0, 0).getMoveList(board));
        Assert.assertEquals(21, board.getPiece(0, 0).getMoveList(board).size());
    }

    @Test
    public void moveAsQueen() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(27, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void blockedByPawn() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(23, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().noneMatch(c -> c.captureMove));
    }

    @Test
    public void blockedByEnemyPawn() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(24, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPiecesStraight() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        board.placePawn(3, 4, Color.BLACK);
        board.placeRook(2, 3, Color.BLACK);
        board.placeRook(3, 2, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(13, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByEnemyStraight() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(17, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPiecesDiag() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.BLACK);
        board.placePawn(2, 2, Color.BLACK);
        board.placeRook(4, 2, Color.BLACK);
        board.placeRook(2, 4, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(14, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByEnemyDiag() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(18, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByTeam() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.WHITE);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).isEmpty());
    }

    @Test
    public void surroundedByEnemy() {
        Board board = new Board();
        board.placeQueen(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().allMatch(c -> c.captureMove));
    }
}
