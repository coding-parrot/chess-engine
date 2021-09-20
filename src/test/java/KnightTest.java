import commons.Color;
import game.Board;
import org.junit.Assert;
import org.junit.Test;

public class KnightTest {
    @Test
    public void moveForward() {
        Board board = new Board();
        board.placeKnight(0, 0, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(0, 0).getMoveList(board));
        Assert.assertEquals(2, board.getPiece(0, 0).getMoveList(board).size());
    }

    @Test
    public void moveAsKnight() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void blockedByPawn() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        board.placePawn(5, 4, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(7, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().noneMatch(c -> c.captureMove));
    }

    @Test
    public void blockedByEnemyPawn() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(5, 4, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void notBlockedByEnemyPawn() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(5, 3, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPiecesFriendly() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(5, 4, Color.BLACK);
        board.placePawn(4, 5, Color.BLACK);
        board.placeRook(2, 5, Color.BLACK);
        board.placeRook(1, 2, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByEnemyPieces() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(5, 4, Color.WHITE);
        board.placePawn(4, 5, Color.WHITE);
        board.placeRook(2, 5, Color.WHITE);
        board.placeRook(1, 2, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPiecesDiag() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.BLACK);
        board.placePawn(2, 2, Color.BLACK);
        board.placeRook(4, 2, Color.BLACK);
        board.placeRook(2, 4, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByEnemyDiag() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByTeam() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.WHITE);
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
    }

    @Test
    public void surroundedByEnemy() {
        Board board = new Board();
        board.placeKnight(3, 3, Color.BLACK);
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
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }
}
