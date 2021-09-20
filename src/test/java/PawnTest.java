import commons.Color;
import commons.Piece;
import game.Board;
import game.Cell;
import game.Move;
import org.junit.Assert;
import org.junit.Test;
import pieces.PieceType;

import java.util.stream.Collectors;

public class PawnTest {
    @Test
    public void checkStartingPositions() {
        final Board board = Board.getStartBoard();
        System.out.println(board);
        Assert.assertTrue(board.moveList.isEmpty());
        Assert.assertEquals(16, board.pieces.values().stream().filter(piece -> piece.sameType(PieceType.PAWN)).count());
        Assert.assertEquals(8, board.pieces.values().stream()
                .filter(piece -> piece.sameType(PieceType.PAWN))
                .filter(piece -> piece.color == Color.BLACK)
                .count());
        Assert.assertEquals(8, board.pieces.values().stream()
                .filter(piece -> piece.sameType(PieceType.PAWN))
                .filter(piece -> piece.color == Color.WHITE)
                .count());

    }

    @Test
    public void moveForward() {
        final Board board = new Board();
        board.placePawn(2, 1, Color.WHITE);
        System.out.println(board);
        Assert.assertEquals(1, board.getPiece(2, 1).getMoveList(board).size());
    }

    @Test
    public void moveForwardDouble() {
        final Board board = new Board();
        board.placePawn(1, 1, Color.WHITE);
        System.out.println(board);
        Assert.assertEquals(2, board.getPiece(1, 1).getMoveList(board).size());
    }

    @Test
    public void cantMoveForwardOpponent() {
        final Board board = new Board();
        board.placePawn(2, 1, Color.WHITE);
        board.placePawn(3, 1, Color.BLACK);
        System.out.println(board);
        Assert.assertTrue(board.getPiece(2, 1).getMoveList(board).isEmpty());
    }

    @Test
    public void cantMoveForwardSameColor() {
        final Board board = new Board();
        board.placePawn(2, 1, Color.WHITE);
        board.placePawn(3, 1, Color.WHITE);
        System.out.println(board);
        Assert.assertTrue(board.getPiece(2, 1).getMoveList(board).isEmpty());
    }

    @Test
    public void canCapture() {
        final Board board = new Board();
        board.placePawn(2, 1, Color.WHITE);
        board.placePawn(3, 1, Color.BLACK);
        board.placePawn(3, 2, Color.BLACK);
        System.out.println(board);
        Assert.assertEquals(1, board.getPiece(2, 1).getMoveList(board).size());
        System.out.println(board.getPiece(2, 1).getMoveList(board));
    }

    @Test
    public void canCaptureBlack() {
        final Board board = new Board();
        board.placePawn(6, 6, Color.BLACK);
        board.placePawn(5, 5, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(6, 6).getMoveList(board));
        Assert.assertEquals(3, board.getPiece(6, 6).getMoveList(board).size());
    }

    @Test
    public void canCaptureBlackBlocked() {
        final Board board = new Board();
        board.placePawn(6, 6, Color.BLACK);
        board.placePawn(5, 6, Color.BLACK);
        board.placePawn(5, 5, Color.WHITE);
        System.out.println(board);
        System.out.println(board.getPiece(6, 6).getMoveList(board));
        Assert.assertEquals(1, board.getPiece(6, 6).getMoveList(board).size());
    }

    @Test
    public void canCaptureEnPassant() {
        final Board board = new Board();
        board.placePawn(4, 6, Color.WHITE);
        board.placePawn(4, 5, Color.BLACK);
        board.placePawn(4, 7, Color.BLACK);
        board.moveList.add(Move.get(Piece.get(Color.BLACK, Cell.get(6, 5), PieceType.PAWN), Cell.get(4, 5), false));
        System.out.println(board);
        System.out.println(board.getPiece(4, 6).getMoveList(board));
        Assert.assertEquals(2, board.getPiece(4, 6).getMoveList(board).size());
    }

    @Test
    public void cannotCaptureEnPassantOnNextTurn() {
        final Board board = new Board();
        board.placePawn(4, 6, Color.WHITE);
        board.placePawn(4, 5, Color.BLACK);
        board.placePawn(4, 7, Color.BLACK);
        System.out.println(board);
        System.out.println(board.getPiece(4, 6).getMoveList(board));
        Assert.assertEquals(1, board.getPiece(4, 6).getMoveList(board).size());
    }

    @Test
    public void captureOnEdge() {
        Board board = Board.getStartBoard();
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(1, 0)))
                .filter(c -> c.target.equals(Cell.get(2, 0)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(6, 0)))
                .filter(c -> c.target.equals(Cell.get(5, 0)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(1, 1)))
                .filter(c -> c.target.equals(Cell.get(3, 1)))
                .findAny()
                .get());
        board = board.copy();
        board.makeMove(board.getLegalMoves()
                .stream()
                .filter(c -> c.piece.sameType(PieceType.PAWN))
                .filter(c -> c.piece.position.equals(Cell.get(5, 0)))
                .filter(c -> c.target.equals(Cell.get(4, 0)))
                .findAny()
                .get());
        System.out.println(board);
        System.out.println(board.getLegalMoves().stream().map(Move::toString).collect(Collectors.joining("\n")));
        System.out.println(board.fenRepresentation());
        Assert.assertEquals(20, board.getLegalMoves().size());
    }
}

/*
b4a5
 */
