import commons.Color;
import game.Board;
import game.Cell;
import game.Move;
import org.junit.Assert;
import org.junit.Test;
import pieces.PieceType;

import java.util.List;
import java.util.NoSuchElementException;

public class BoardTest {

    @Test
    public void startingBoard() {
        Board board = Board.getStartBoard();
        final List<Move> legalMoves = board.getLegalMoves();
        Assert.assertEquals(20, legalMoves.size());
    }

    @Test
    public void blockCheck() {
        Board board = new Board();
        board.placeKing(2, 5, Color.WHITE);
        board.placeRook(2, 0, Color.BLACK);
        board.placeKing(4, 4, Color.BLACK);
        board.placeBishop(1, 1, Color.WHITE);
        board.inCheck = true;
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertEquals(6, board.getLegalMoves().size());
    }

    @Test
    public void captureAndReturnCheck() {
        Board board = new Board();
        board.placeKing(2, 5, Color.WHITE);
        board.placeRook(2, 2, Color.BLACK);
        board.placeKing(4, 4, Color.BLACK);
        board.placeBishop(1, 3, Color.WHITE);
        board.inCheck = true;
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertEquals(6, board.getLegalMoves().size());
        Assert.assertFalse(board.isDraw());
        Assert.assertEquals(-2, board.evaluation(board.getLegalMoves().size()), 0.3);
        board.makeMove(board.getLegalMoves().stream().filter(c -> c.captureMove).findAny().get());
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.inCheck);
        Assert.assertEquals(Color.BLACK, board.playerToMove);
        Assert.assertTrue(board.isDraw());
        Assert.assertEquals(4, board.getLegalMoves().size());
    }

    @Test
    public void checkMate() {
        Board board = new Board();
        board.placeKing(7, 5, Color.WHITE);
        board.placeRook(7, 2, Color.BLACK);
        board.placeRook(6, 3, Color.BLACK);
        board.placeKing(4, 4, Color.BLACK);
        board.inCheck = true;
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.getLegalMoves().isEmpty());
        Assert.assertEquals(Integer.MIN_VALUE, board.evaluation(board.getLegalMoves().size()), 0.0001);
    }

    @Test
    public void staleMate() {
        Board board = new Board();
        board.placeKing(7, 5, Color.WHITE);
        board.placeRook(6, 2, Color.BLACK);
        board.placeRook(3, 4, Color.BLACK);
        board.placeKing(7, 7, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.getLegalMoves().isEmpty());
        Assert.assertEquals(0, board.evaluation(board.getLegalMoves().size()), 0.000001);
    }

    @Test
    public void checkMateIn1() {
        Board board = new Board();
        board.placeKing(7, 5, Color.WHITE);
        board.placeRook(6, 2, Color.BLACK);
        board.placeRook(3, 4, Color.BLACK);
        board.placeKing(7, 7, Color.BLACK);
        board.placeQueen(0, 6, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertEquals(21, board.getLegalMoves().size());
        Assert.assertEquals(-1, board.evaluation(board.getLegalMoves().size()), 0.3);
    }

    @Test(expected = NoSuchElementException.class)
    public void castleFail() {
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
        board.makeMove(board.getPiece(4, 7).getMoveList(board).stream().filter(c -> c.captureMove).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(7, 4).getMoveList(board));
        board.makeMove(board.getPiece(7, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
    }

    @Test(expected = NoSuchElementException.class)
    public void castleFailWithKnightCapture() {
        Board board = new Board();
        board.placeKing(0, 4, Color.WHITE);
        board.placeKnight(6, 5, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);
        board.placeRook(7, 7, Color.BLACK);
        board.placeRook(7, 0, Color.BLACK);
        board.placeBishop(7, 2, Color.BLACK);
        board.placePawn(6, 6, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(7, 4).getMoveList(board));
        Assert.assertEquals(5, board.getPiece(7, 4).getMoveList(board).size());
        Assert.assertTrue(board.getPiece(7, 4).getMoveList(board).stream().anyMatch(c -> Math.abs(c.piece.position.col - c.target.col) == 2));
        board.makeMove(board.getPiece(6, 5).getMoveList(board).stream().filter(c -> c.captureMove).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(7, 4).getMoveList(board));
        board.makeMove(board.getPiece(7, 4).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.col - c.target.col) == 2).findAny().get());
    }

    @Test
    public void pawnIsPinned() {
        Board board = new Board();
        board.placeKing(4, 0, Color.WHITE);
        board.placeRook(3, 1, Color.WHITE);
        board.placePawn(4, 1, Color.WHITE);
        board.placePawn(1, 4, Color.WHITE);
        board.placePawn(1, 6, Color.WHITE);
        board.placeKing(3, 7, Color.BLACK);
        board.placeRook(4, 7, Color.BLACK);
        board.placePawn(3, 5, Color.BLACK);
        board.placePawn(6, 2, Color.BLACK);
        board.placePawn(5, 3, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(4, 1).getMoveList(board));
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.getLegalMoves().stream().filter(move -> move.piece.sameType(PieceType.PAWN)).noneMatch(move -> move.target.row == 5));
        board.makeMove(board.getPiece(1, 4).getMoveList(board).stream().findFirst().get());
        System.out.println(board);
        System.out.println(board.getPiece(6, 2).getMoveList(board));
        board.makeMove(board.getPiece(6, 2).getMoveList(board).stream().filter(c -> Math.abs(c.piece.position.row - c.target.row) == 2).findAny().get());
        System.out.println(board);
        System.out.println(board.getPiece(4, 1).getMoveList(board));
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.getLegalMoves().stream().filter(move -> move.piece.sameType(PieceType.PAWN)).noneMatch(move -> move.target.row == 5 && move.captureMove));
    }

    @Test
    public void doubleCheck() {
        Board board = new Board();
        board.placeKing(4, 0, Color.WHITE);
        board.placeRook(3, 1, Color.WHITE);
        board.placeKing(3, 7, Color.BLACK);
        board.placeRook(1, 0, Color.BLACK);
        board.placeKnight(2, 0, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(4, 0).getMoveList(board));
        System.out.println(board.getLegalMoves());
        board.playerToMove = Color.BLACK;
        board.makeMove(Move.get(board.getPiece(2, 0), Cell.get(3, 2), false));
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.getLegalMoves().stream().allMatch(move -> move.piece.sameType(PieceType.KING)));
    }

    @Test
    public void pawnPromotion() {
        Board board = new Board();
        board.placeKing(4, 0, Color.WHITE);
        board.placeKing(3, 7, Color.BLACK);
        board.placePawn(1, 1, Color.BLACK);
        board.playerToMove = Color.BLACK;
        System.out.println(board);
        System.out.println(board.getPiece(1, 1).getMoveList(board));
        System.out.println(board.getLegalMoves());
        board.makeMove(Move.get(board.getPiece(1, 1), Cell.get(0, 1), false, null, PieceType.QUEEN));
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.playerPieces.get(Color.BLACK).stream().anyMatch(piece -> piece.sameType(PieceType.QUEEN)));
    }

    @Test
    public void pawnPromoteToKnight() {
        Board board = new Board();
        board.placeKing(4, 0, Color.WHITE);
        board.placeKing(3, 7, Color.BLACK);
        board.placePawn(1, 1, Color.BLACK);
        board.playerToMove = Color.BLACK;
        System.out.println(board);
        System.out.println(board.getPiece(1, 1).getMoveList(board));
        System.out.println(board.getLegalMoves());
        board.makeMove(Move.get(board.getPiece(1, 1), Cell.get(0, 1), false, null, PieceType.KNIGHT));
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertTrue(board.playerPieces.get(Color.BLACK).stream().anyMatch(piece -> piece.sameType(PieceType.KNIGHT)));
    }

    @Test
    public void kingAndQueenOut() {
        Board board = Board.getStartBoard();
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(1, 3)))
                .filter(c -> c.target.equals(Cell.get(2, 3)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(6, 4)))
                .filter(c -> c.target.equals(Cell.get(5, 4)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.KING))
                .filter(c -> c.piece.position.equals(Cell.get(0, 4)))
                .filter(c -> c.target.equals(Cell.get(1, 3)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.QUEEN))
                .filter(c -> c.piece.position.equals(Cell.get(7, 3)))
                .filter(c -> c.target.equals(Cell.get(6, 4)))
                .findAny()
                .get());
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertEquals(23, board.getLegalMoves().size());
    }

    @Test
    public void kingAndKnightOut() {
        Board board = Board.getStartBoard();
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(1, 3)))
                .filter(c -> c.target.equals(Cell.get(2, 3)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(6, 4)))
                .filter(c -> c.target.equals(Cell.get(5, 4)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.KING))
                .filter(c -> c.piece.position.equals(Cell.get(0, 4)))
                .filter(c -> c.target.equals(Cell.get(1, 3)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.KNIGHT))
                .filter(c -> c.piece.position.equals(Cell.get(7, 6)))
                .filter(c -> c.target.equals(Cell.get(5, 5)))
                .findAny()
                .get());
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertEquals(23, board.getLegalMoves().size());
    }

    @Test
    public void kingAndPawnOut() {
        Board board = Board.getStartBoard();
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(1, 3)))
                .filter(c -> c.target.equals(Cell.get(2, 3)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(6, 6)))
                .filter(c -> c.target.equals(Cell.get(4, 6)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.KING))
                .filter(c -> c.piece.position.equals(Cell.get(0, 4)))
                .filter(c -> c.target.equals(Cell.get(1, 3)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(4, 6)))
                .filter(c -> c.target.equals(Cell.get(3, 6)))
                .findAny()
                .get());
        System.out.println(board);
        System.out.println(board.getLegalMoves());
        Assert.assertEquals(22, board.getLegalMoves().size());
    }
}
