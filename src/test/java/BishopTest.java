import commons.Color;
import game.Board;
import org.junit.Assert;
import org.junit.Test;

public class BishopTest {
    @Test
    public void moveForward() {
        Board board = new Board();
        board.placeBishop(0, 0, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(0, 0).getMoveList(board));
        Assert.assertEquals(7, board.getPiece(0, 0).getMoveList(board).size());
    }

    @Test
    public void moveAsBishop() {
        Board board = new Board();
        board.placeBishop(3, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(13, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void blockedByPawn() {
        Board board = new Board();
        board.placeBishop(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(9, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().noneMatch(c -> c.captureMove));
    }

    @Test
    public void blockedByEnemyPawn() {
        Board board = new Board();
        board.placeBishop(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(10, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPieces() {
        Board board = new Board();
        board.placeBishop(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.BLACK);
        board.placePawn(2, 2, Color.BLACK);
        board.placeRook(4, 2, Color.BLACK);
        board.placeRook(2, 4, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).isEmpty());
    }

    @Test
    public void surroundedByEnemy() {
        Board board = new Board();
        board.placeBishop(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().allMatch(c -> c.captureMove));
    }
}
