import game.Board;
import game.Move;

import java.util.List;

public class Engine {

    public int countAllMoves(final Board board, final int depth) {
        return countAllMoves(board, depth, 1000);
    }

    public int countAllMoves(final Board board, final int depth, final int printAt) {
        final List<Move> legalMoves = board.getLegalMoves();
//        if (board.fenRepresentation().equals("8/2p5/K2p4/1P4kr/1R6/6p1/4P3/8 w - -")) {
//            System.out.println("HERE");
//            System.out.println(board);
//            System.out.println(Arrays.deepToString(board.canCastle));
//            System.out.println(legalMoves.stream().map(Move::toString).collect(Collectors.joining("\n")));
//            final List<Move> legalMovezzz = board.getLegalMoves();
//            System.out.println(legalMovezzz);
//        }
        if (legalMoves.isEmpty()) {
            return 0;
        }
        if (depth == 1) {
            return legalMoves.size();
        }
        return legalMoves.stream().mapToInt(move -> {
            final Board copy = board.copy();
            copy.makeMove(move);
            final int countAllMoves = countAllMoves(copy, depth - 1, printAt);
            if (depth == printAt) {// && board.fenRepresentation().equals("8/8/2pp4/1P4kr/K4pP1/8/4P3/1R6 b - -")) {
                System.out.println(getString(move) + ": " + countAllMoves + " " + move +
//                         Arrays.deepToString(copy.canCastle) + " \n" + copy.getKing(Color.BLACK).getMoveList(copy) + " \n" +
                        " " + copy.fenRepresentation());
            }
            return countAllMoves;
        }).sum();
    }

    private String getString(Move move) {
        return move.piece.position.notation() + move.target.notation();
    }
}

/*




b4g4
b4h4











b4g4
b4h4
b4g4
b4h4
 */