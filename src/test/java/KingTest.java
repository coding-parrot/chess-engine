import commons.Color;
import game.Board;
import org.junit.Assert;
import org.junit.Test;

public class KingTest {
    @Test
    public void moveForward() {
        Board board = new Board();
        board.placeKing(0, 0, Color.BLACK);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(0, 0).getMoveList(board));
        Assert.assertEquals(3, board.getPiece(0, 0).getMoveList(board).size());
    }

    @Test
    public void moveAsKing() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placeKing(3, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void blockedByPawn() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(7, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().noneMatch(c -> c.captureMove));
    }

    @Test
    public void blockedByEnemyPawn() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void notBlockedByEnemyPawn() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(5, 3, Color.WHITE);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(8, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void notBlockedByEnemyPawnInFront() {
        Board board = new Board();
        board.placeKing(3, 3, Color.WHITE);
        board.placePawn(5, 3, Color.BLACK);
        board.placeKing(7, 7, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(6, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPiecesStraight() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.BLACK);
        board.placePawn(3, 4, Color.BLACK);
        board.placeRook(2, 3, Color.BLACK);
        board.placeRook(3, 2, Color.BLACK);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByEnemyStraight() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        board.placeKing(7, 7, Color.WHITE);
        board.inCheck = true;
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(3, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByPiecesDiag() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.BLACK);
        board.placePawn(2, 2, Color.BLACK);
        board.placeRook(4, 2, Color.BLACK);
        board.placeRook(2, 4, Color.BLACK);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByEnemyDiag() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        board.placeKing(7, 7, Color.WHITE);
        board.inCheck = true;
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(2, board.getPiece(3, 3).getMoveList(board).size());
    }

    @Test
    public void surroundedByTeam() {
        Board board = new Board();
        board.placeKing(3, 3, Color.WHITE);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        board.placeKing(7, 7, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).isEmpty());
    }

    @Test
    public void surroundedByEnemy() {
        Board board = new Board();
        board.placeKing(3, 3, Color.BLACK);
        board.placePawn(4, 4, Color.WHITE);
        board.placePawn(2, 2, Color.WHITE);
        board.placeRook(4, 2, Color.WHITE);
        board.placeRook(2, 4, Color.WHITE);
        board.placePawn(4, 3, Color.WHITE);
        board.placePawn(3, 4, Color.WHITE);
        board.placeRook(2, 3, Color.WHITE);
        board.placeRook(3, 2, Color.WHITE);
        board.placeKing(7, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(3, 3).getMoveList(board));
        Assert.assertEquals(1, board.getPiece(3, 3).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(3, 3).getMoveList(board).stream().allMatch(c -> c.captureMove));
    }

    @Test
    public void canCastleLeft() {
        Board board = new Board();
        board.canCastle[0][1] = false;
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(0, 0, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(0, 4).getMoveList(board));
        Assert.assertEquals(6, board.getPiece(0, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(0, 4).getMoveList(board).stream().noneMatch(c -> c.captureMove));
        board.makeMove(board.getPiece(0, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(0, 2).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(0, 2).getMoveList(board).size());
        Assert.assertEquals(11, board.getPiece(0, 3).getMoveList(board).size());
    }

    @Test
    public void canCastleRight() {
        Board board = new Board();
        board.canCastle[0][0] = false;
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(0, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(0, 4).getMoveList(board));
        Assert.assertEquals(6, board.getPiece(0, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(0, 4).getMoveList(board).stream().noneMatch(c -> c.captureMove));
        board.makeMove(board.getPiece(0, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(0, 6).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(0, 6).getMoveList(board).size());
        Assert.assertEquals(12, board.getPiece(0, 5).getMoveList(board).size());
    }

    @Test
    public void canCastleBothSides() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(0, 7, Color.WHITE);
        board.placeRook(0, 0, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(0, 4).getMoveList(board));
        Assert.assertEquals(7, board.getPiece(0, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(0, 4).getMoveList(board).stream().noneMatch(c -> c.captureMove));
    }

    @Test
    public void castleBlockedLeftSide() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(0, 7, Color.WHITE);
        board.placeRook(0, 0, Color.WHITE);
        board.placeBishop(0, 2, Color.WHITE);
        board.placePawn(1, 6, Color.WHITE);
        board.placePawn(1, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(0, 4).getMoveList(board));
        Assert.assertEquals(6, board.getPiece(0, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(0, 4).getMoveList(board).stream().noneMatch(c -> c.captureMove));
        board.makeMove(board.getPiece(0, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(0, 6).getMoveList(board));
        Assert.assertEquals(2, board.getPiece(0, 6).getMoveList(board).size());
        Assert.assertEquals(9, board.getPiece(0, 5).getMoveList(board).size());
    }

    @Test
    public void castleBlockedByCheck() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(0, 7, Color.WHITE);
        board.placeRook(0, 0, Color.WHITE);
        board.placeBishop(0, 2, Color.WHITE);
        board.placePawn(1, 6, Color.WHITE);
        board.placePawn(1, 7, Color.WHITE);
        board.placeRook(5, 5, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(0, 4).getMoveList(board));
        Assert.assertEquals(3, board.getPiece(0, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(0, 4).getMoveList(board).stream().noneMatch(c -> Math.abs(c.piece.position.col - c.target.col) == 2));
    }

    @Test
    public void castleNotBlockedByCheck() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(0, 7, Color.WHITE);
        board.placeRook(0, 0, Color.WHITE);
        board.placeBishop(0, 2, Color.WHITE);
        board.placePawn(1, 5, Color.WHITE);
        board.placePawn(1, 6, Color.WHITE);
        board.placeRook(5, 7, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(0, 4).getMoveList(board));
        Assert.assertEquals(5, board.getPiece(0, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(0, 4).getMoveList(board).stream().anyMatch(c -> Math.abs(c.piece.position.col - c.target.col) == 2));
        board.makeMove(board.getPiece(0, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(0, 6).getMoveList(board));
        Assert.assertTrue(board.getPiece(0, 6).getMoveList(board).isEmpty());
        Assert.assertEquals(2, board.getPiece(0, 5).getMoveList(board).size());
    }

    @Test
    public void castleBlockedByCheckBlack() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(7, 7, Color.BLACK);
        board.placeRook(7, 0, Color.BLACK);
        board.placeBishop(7, 2, Color.BLACK);
        board.placePawn(6, 6, Color.BLACK);
        board.placePawn(6, 7, Color.BLACK);
        board.placeRook(4, 5, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(7, 4).getMoveList(board));
        Assert.assertEquals(3, board.getPiece(7, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(7, 4).getMoveList(board).stream().noneMatch(c -> Math.abs(c.piece.position.col - c.target.col) == 2));
    }

    @Test
    public void castleNotBlockedByCheckBlack() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(7, 7, Color.BLACK);
        board.placeRook(7, 0, Color.BLACK);
        board.placeBishop(7, 2, Color.BLACK);
        board.placePawn(6, 5, Color.BLACK);
        board.placePawn(6, 6, Color.BLACK);
        board.placeRook(4, 7, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(7, 4).getMoveList(board));
        Assert.assertEquals(5, board.getPiece(7, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(7, 4).getMoveList(board).stream().anyMatch(c -> Math.abs(c.piece.position.col - c.target.col) == 2));
        board.makeMove(board.getPiece(7, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(7, 6).getMoveList(board));
        Assert.assertTrue(board.getPiece(7, 6).getMoveList(board).isEmpty());
        Assert.assertEquals(2, board.getPiece(7, 5).getMoveList(board).size());
    }

    @Test
    public void kingInProximity() {
        Board board = new Board();
        board.placeKing(2, 5, Color.WHITE);
        board.placeRook(2, 0, Color.BLACK);
        board.placeKing(4, 4, Color.BLACK);
        board.inCheck = true;
        System.out.println(board);
        System.out.println(board.getPiece(2, 5).getMoveList(board));
        Assert.assertEquals(4, board.getPiece(2, 5).getMoveList(board).size());
    }


    @Test
    public void canCastleAsBlack() {
        Board board = new Board();
        board.canCastle[0][0] = board.canCastle[0][1] = false;
        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(7, 7, Color.BLACK);
        board.placeRook(7, 0, Color.BLACK);
        board.placePawn(6, 3, Color.BLACK);
        board.placePawn(6, 4, Color.BLACK);
        board.placePawn(6, 5, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(7, 4).getMoveList(board));
    }
}
