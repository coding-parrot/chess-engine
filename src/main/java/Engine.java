import commons.Color;
import game.Board;
import game.Move;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class Engine {
    public static int nodesEvaluated;
    public Map<Long, Result> transpositionTable = new HashMap<>();

    public int countAllMoves(final Board board, final int depth) {
        return countAllMoves(board, depth, 1000);
    }

    public int countAllMoves(final Board board, final int depth, final int printAt) {
        final List<Move> legalMoves = board.getLegalMoves();
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
            if (depth == printAt) {
                System.out.println(getString(move) + ": " + countAllMoves + " " + move +
                        " " + copy.fenRepresentation());
            }
            return countAllMoves;
        }).sum();
    }

    public Evaluation minMax(final Board board, final int depth, final int printAt) {
        final List<Move> legalMoves = board.getLegalMoves();
        nodesEvaluated++;
        if (legalMoves.isEmpty() || depth == 0) {
            return new Evaluation(null, -board.evaluation(legalMoves.size()));
        }
        Evaluation bestMove = null;
        for (Move move : legalMoves) {
            final Board copy = board.copy();
            copy.makeMove(move);
            final Evaluation eval = minMax(copy, depth - 1, printAt);
            if (depth == printAt) {
                System.out.println(getString(move) + ": " + eval.getScore() + " " + move +
                        " " + copy.fenRepresentation());
            }
            if (bestMove == null || bestMove.getScore() > -eval.getScore()) {
                bestMove = new Evaluation(move, -eval.getScore());
                if (bestMove.getScore() < 0 && bestMove.getScore() + Integer.MAX_VALUE < 0.0001) {
                    return bestMove;
                }
            }
        }
        return bestMove;
    }

    public OutCome alphaBeta(final Board board, final int depth, double alpha, double beta, final int printAt) {
        final List<Move> legalMoves = board.getLegalMoves();
        nodesEvaluated++;
        if (legalMoves.isEmpty() || depth == 0) {
            final double score;
            if (transpositionTable.containsKey(board.zobristHash)) {
                score = transpositionTable.get(board.zobristHash).eval;
            } else {
                score = board.evaluation(legalMoves.size());
                transpositionTable.put(board.zobristHash, new Result(score, 0));
            }
            return new OutCome(board, null, -score);
        }
        final List<OutCome> outComes = legalMoves.stream().map(move -> {
            final Board changedBoard = board.copy();
            changedBoard.makeMove(move);
            final double score;
            if (transpositionTable.containsKey(changedBoard.zobristHash)) {
                score = transpositionTable.get(changedBoard.zobristHash).eval;
            } else {
                score = changedBoard.evaluation(changedBoard.getLegalMoves().size());
                transpositionTable.put(changedBoard.zobristHash, new Result(score, 0));
            }
            return new OutCome(changedBoard, move, score);
        }).sorted(Comparator.comparingDouble(OutCome::getScore)).collect(Collectors.toList());
        OutCome bestOutCome = null;
        for (final OutCome outCome : outComes) {
            final OutCome eval = alphaBeta(outCome.getBoard(), depth - 1, alpha, beta, printAt);
            if (depth == printAt) {
                System.out.println(getString(outCome.getMove()) + ": " + eval.getScore() + " " + outCome +
                        " " + outCome.getBoard().fenRepresentation());
            }
            if (bestOutCome == null || bestOutCome.getScore() > -eval.getScore()) {
                bestOutCome = new OutCome(board, outCome.getMove(), -eval.getScore());
                if (bestOutCome.getScore() < 0 && bestOutCome.getScore() + Integer.MAX_VALUE < 0.0001) {
                    return bestOutCome;
                }
            }
            if (board.playerToMove.equals(Color.WHITE)) {
                if (alpha < -bestOutCome.getScore()) {
                    alpha = -bestOutCome.getScore();
                }
            } else {
                if (beta > bestOutCome.getScore()) {
                    beta = bestOutCome.getScore();
                }
            }
            if (alpha >= beta) {
                break;
            }
        }
        if (!transpositionTable.containsKey(board.zobristHash) || transpositionTable.get(board.zobristHash).depth < depth) {
            transpositionTable.put(board.zobristHash, new Result(-bestOutCome.getScore(), depth));
        }
        return bestOutCome;
    }

    public OutCome iterativeDeepening(final Board board, final long time) {
        transpositionTable = new HashMap<>();
        final long start = System.currentTimeMillis();
        int depth = 1;
        OutCome evaluation = alphaBeta(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1000);
        while (System.currentTimeMillis() - start < time * 1000 && Math.abs(evaluation.getScore()) - Integer.MAX_VALUE < 0.0001) {
            depth++;
            System.out.println("DEPTH: " + depth + " EVAL: " + evaluation + " TIME: " + ((System.currentTimeMillis() - start) / 1000));
            try {
                int finalDepth = depth;
                evaluation = CompletableFuture.supplyAsync(() -> alphaBeta(board, finalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1000)).get(time * 1000 - (System.currentTimeMillis() - start), TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                System.out.println("TIMEOUT: " + depth);
                break;
            }
        }
        return evaluation;
    }

    private String getString(Move move) {
        return move.piece.position.notation() + move.target.notation();
    }
}

class Evaluation {
    private final Move move;
    private final double score;

    public Evaluation(Move move, double score) {
        this.move = move;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public String toString() {
        return "{" +
                "move=" + move +
                ", score=" + score +
                '}';
    }
}

class OutCome {
    private final Board board;
    private final Move move;
    private final double score;

    public OutCome(Board board, Move move, double score) {
        this.move = move;
        this.score = score;
        this.board = board;
    }

    public double getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "{" +
                "board=\n" + board +
                ", move=" + move +
                ", score=" + score +
                '}';
    }
}


class Result {
    final double eval;
    final int depth;

    public Result(double eval, int depth) {
        this.eval = eval;
        this.depth = depth;
    }
}