import commons.Color;
import game.Board;
import org.junit.Assert;
import org.junit.Test;

public class RookTest {
    @Test
    public void moveForward() {
        Board board = new Board();
        board.placeRook(0, 0, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(0, 0).getMoveList(board));
        Assert.assertEquals(14, board.getPiece(0, 0).getMoveList(board).size());
    }

    @Test
    public void moveAsRook() {
        Board board = new Board();
        board.placeRook(3, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(14, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void blockedByPawn() {
        Board board = new Board();
        board.placeRook(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(10, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().noneMatch(c -> c.captureMove));
    }

    @Test
    public void blockedByEnemyPawn() {
        Board board = new Board();
        board.placeRook(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(11, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPieces() {
        Board board = new Board();
        board.placeRook(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        board.placePawn(3, 4, Color.BLACK);
        board.placeRook(2, 3, Color.BLACK);
        board.placeRook(3, 2, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).isEmpty());
    }

    @Test
    public void surroundedByEnemy() {
        Board board = new Board();
        board.placeRook(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().allMatch(c -> c.captureMove));
    }
}
