package game;

import commons.*;
import pieces.Knight;
import pieces.PieceType;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Board {
    public final Map<Cell, Piece> pieces;
    public final Map<Color, List<Piece>> playerPieces;
    public final Map<Cell, Set<Move>> moves;
    public final Map<Cell, Set<Move>> guards;
    public final List<Move> moveList;
    private final long[] positions;
    private int positionIndex;
    public final boolean[][] canCastle;
    public long zobristHash;
    public Color playerToMove;
    boolean isThreeFoldRepetition;
    int halfMoves;
    boolean fiftyMoveDraw;
    public boolean inCheck;
    public Move previousMove;
    private final Piece[] kings;
    public static final Map<PieceType, Integer> approxValue = new HashMap<>();
    private static long[][][] zobrist = new long[6][2][64];
    private static long zobristSwitchPlayer;
    public static long[] zobristCastle = new long[4];
    private static long[] zobristEnpassantFiles = new long[8];

    static {
        approxValue.put(PieceType.PAWN, 1);
        approxValue.put(PieceType.KNIGHT, 3);
        approxValue.put(PieceType.BISHOP, 3);
        approxValue.put(PieceType.ROOK, 5);
        approxValue.put(PieceType.QUEEN, 9);
        approxValue.put(PieceType.KING, 0);
        final Random random = new Random();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 64; k++) {
                    zobrist[i][j][k] = random.nextLong();
                }
            }
        }
        zobristSwitchPlayer = random.nextLong();
        for (int i = 0; i < 4; i++) {
            zobristCastle[i] = random.nextLong();
        }
        for (int i = 0; i < 8; i++) {
            zobristEnpassantFiles[i] = random.nextLong();
        }
    }

    public Board() {
        pieces = new HashMap<>();
        moves = new HashMap<>();
        guards = new HashMap<>();
        moveList = new ArrayList<>();
        canCastle = new boolean[][]{{true, true}, {true, true}};
        positions = new long[6];
        positionIndex = 0;
        playerPieces = new HashMap<>();
        playerPieces.put(Color.WHITE, new ArrayList<>());
        playerPieces.put(Color.BLACK, new ArrayList<>());
        playerToMove = Color.WHITE;
        kings = new Piece[2];
    }

    public boolean isEmpty(final int row, final int col) {
        return pieces.get(Cell.get(row, col)) == null;
    }

    public Piece getPiece(final int row, final int col) {
        return pieces.get(Cell.get(row, col));
    }

    public Set<Move> getMoveList(final Piece piece) {
        final Cell cell = piece.position;
        if (pieces.get(cell) != null) {
            populateMoves(cell);
            return moves.get(cell);
        } else {
            throw new NullPointerException();
        }
    }

    public Set<Move> getGuardList(final Piece piece) {
        final Cell cell = piece.position;
        if (pieces.get(cell) != null) {
            populateMoves(cell);
            return guards.get(cell);
        } else {
            throw new NullPointerException();
        }
    }

    private void populateMoves(Cell cell) {
        if (!moves.containsKey(cell) || !guards.containsKey(cell)) {
            final LegalMoves legalMoves = pieces.get(cell).getLegalMoves(this);
            moves.put(cell, legalMoves.moves);
            guards.put(cell, legalMoves.guards);
        }
    }

    public static Board getStartBoard() {
        final Board board = new Board();
        for (int i = 0; i < 8; i++) {
            board.placePawn(1, i, Color.WHITE);
            board.placePawn(6, i, Color.BLACK);
        }
        board.placeRook(0, 0, Color.WHITE);
        board.placeRook(0, 7, Color.WHITE);
        board.placeRook(7, 0, Color.BLACK);
        board.placeRook(7, 7, Color.BLACK);

        board.placeKnight(0, 1, Color.WHITE);
        board.placeKnight(0, 6, Color.WHITE);
        board.placeKnight(7, 1, Color.BLACK);
        board.placeKnight(7, 6, Color.BLACK);

        board.placeBishop(0, 2, Color.WHITE);
        board.placeBishop(0, 5, Color.WHITE);
        board.placeBishop(7, 2, Color.BLACK);
        board.placeBishop(7, 5, Color.BLACK);

        board.placeQueen(0, 3, Color.WHITE);
        board.placeQueen(7, 3, Color.BLACK);

        board.placeKing(0, 4, Color.WHITE);
        board.placeKing(7, 4, Color.BLACK);

        return board;
    }

    public void placePawn(final int row, final int col, final Color color) {
        final Cell position = Cell.get(row, col);
        final Piece pawn = Piece.get(color, position, PieceType.PAWN);
        pieces.put(position, pawn);
        playerPieces.get(color).add(pawn);
        updateHashForAddition(pawn);
    }

    public void placeRook(final int row, final int col, final Color color) {
        final Cell position = Cell.get(row, col);
        final Piece rook = Piece.get(color, position, PieceType.ROOK);
        pieces.put(position, rook);
        playerPieces.get(color).add(rook);
        updateHashForAddition(rook);
    }

    public void placeBishop(final int row, final int col, final Color color) {
        final Cell position = Cell.get(row, col);
        final Piece bishop = Piece.get(color, position, PieceType.BISHOP);
        pieces.put(position, bishop);
        playerPieces.get(color).add(bishop);
        updateHashForAddition(bishop);
    }

    public void placeQueen(final int row, final int col, final Color color) {
        final Cell position = Cell.get(row, col);
        final Piece queen = Piece.get(color, position, PieceType.QUEEN);
        pieces.put(position, queen);
        playerPieces.get(color).add(queen);
        updateHashForAddition(queen);
    }

    public void placeKing(final int row, final int col, final Color color) {
        final Cell position = Cell.get(row, col);
        final Piece king = Piece.get(color, position, PieceType.KING);
        kings[color.ordinal()] = king;
        pieces.put(position, king);
        playerPieces.get(color).add(king);
        updateHashForAddition(king);
    }

    public void placeKnight(final int row, final int col, final Color color) {
        final Cell position = Cell.get(row, col);
        final Piece knight = Piece.get(color, position, PieceType.KNIGHT);
        pieces.put(position, knight);
        playerPieces.get(color).add(knight);
        updateHashForAddition(knight);
    }

    public List<Move> getLegalMoves() {
        final Piece king = getKing(playerToMove);
        //better to check for pin from king than discoveries
        if (inCheck) {
            final Set<Piece> attackers = attackingKing(king);
            final List<Move> legalMoves = new ArrayList<>(getMoveList(king));
            if (attackers.size() < 2) {
                final Set<Piece> pinnedToKing = pinnedToKing(king).keySet();
                final List<Piece> movableExceptKing = playerPieces.get(playerToMove).stream()
                        .filter(piece -> !pinnedToKing.contains(piece))
                        .filter(piece -> !(piece.sameType(PieceType.KING)))
                        .collect(Collectors.toList());
                final Piece attacker = new ArrayList<>(attackers).get(0);
                final Set<Cell> cellsWithCheck = rayOfCheck(king, attacker);
                if (attacker.sameType(PieceType.PAWN)) {
                    //enPassant can capture a checking pawn
                    movableExceptKing.stream()
                            .filter(piece -> piece.sameType(PieceType.PAWN))
                            .map(this::getMoveList)
                            .flatMap(Collection::stream)
                            .filter(move -> move.captureMove)
                            .filter(move -> move.captureCell.equals(attacker.position))
                            .filter(move -> !move.captureCell.equals(move.target))
                            .forEach(legalMoves::add);
                }
                legalMoves.addAll(movableExceptKing.stream()
                        .map(this::getMoveList)
                        .flatMap(Collection::stream)
                        .filter(move -> cellsWithCheck.contains(move.target))
                        .collect(Collectors.toList()));
            }
            return legalMoves;
        } else {
            final Map<Piece, Piece> pinnedToKing = pinnedToKing(king);
            final List<Move> normalMoves = playerPieces.get(playerToMove).stream()
                    .filter(piece -> !pinnedToKing.containsKey(piece))
                    .map(this::getMoveList)
                    .flatMap(Collection::stream)
                    .filter(move -> !illegalEnPassant(move, king))
                    .collect(Collectors.toList());
            final List<Move> pinnedPieceMoves = pinnedToKing.entrySet()
                    .stream()
                    .filter(entry -> !entry.getKey().sameType(PieceType.KNIGHT))
                    .map(this::getMovesWithinPin)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            normalMoves.addAll(pinnedPieceMoves);
            return normalMoves;
        }
    }

    public Piece getKing(Color color) {
        return kings[color.ordinal()];
    }

    private List<Move> getMovesWithinPin(Map.Entry<Piece, Piece> pinnedEntry) {
        final Piece pinnedPiece = pinnedEntry.getKey();
        final Piece attacker = pinnedEntry.getValue();
        final Piece king = getKing(pinnedPiece.color);
        final Set<Move> moves = getMoveList(pinnedPiece);
        final Line line = new Line(pinnedPiece.position, attacker.position);
        final Line inverse = new Line(pinnedPiece.position, king.position);
        if (line.isStraight) {
            return moves.stream().filter(move -> {
                final Line otherLine = new Line(move.piece.position, move.target);
                final boolean towardsAttacker = otherLine.isStraight && otherLine.rowDiff == line.rowDiff && otherLine.colDiff == line.colDiff;
                final boolean towardsKing = otherLine.isStraight && otherLine.rowDiff == inverse.rowDiff && otherLine.colDiff == inverse.colDiff;
                return towardsAttacker || towardsKing;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private boolean illegalEnPassant(final Move move, final Piece king) {
        if (move.piece.sameType(PieceType.PAWN) && move.captureMove && move.captureCell != move.target) {
            //does not expose an attack on the king.
            //The king is not discovered with the capture
            //ignore the captured and capturing pawn, and then check if the ray is a check.
            final Set<Piece> ignorePawns = new HashSet<>();
            ignorePawns.add(move.piece);
            ignorePawns.add(getPiece(move.captureCell.row, move.captureCell.col));
            final Line line = new Line(move.captureCell, king.position);
            if (line.isStraight) {
                for (int row = king.position.row + line.rowDiff, col = king.position.col + line.colDiff; Utils.withinBoardLimits(row, col); row = row + line.rowDiff, col = col + line.colDiff) {
                    if (!isEmpty(row, col) && !ignorePawns.contains(getPiece(row, col))) {
                        final Piece piece = getPiece(row, col);
                        return piece.color != king.color && (piece.sameType(line.minorPieceType) || piece.sameType(PieceType.QUEEN));
                    } else if (move.target.equals(Cell.get(row, col))) {
                        break;
                    }
                }
            }
        }
        return false;
    }

    private Set<Cell> rayOfCheck(Piece king, Piece attacker) {
        final Set<Cell> stopCheck = new HashSet<>();
        if (attacker.sameType(PieceType.QUEEN) || attacker.sameType(PieceType.ROOK) || attacker.sameType(PieceType.BISHOP)) {
            final Line line = new Line(attacker.position, king.position);
            if (line.isStraight) {
                final int rowLowLimit = Math.min(attacker.position.row, king.position.row),
                        rowHighLimit = Math.max(attacker.position.row, king.position.row),
                        colLowLimit = Math.min(attacker.position.col, king.position.col),
                        colHighLimit = Math.max(attacker.position.col, king.position.col);
                for (int i = 1; i < 8; i++) {
                    final int row = king.position.row + i * line.rowDiff, col = king.position.col + i * line.colDiff;
                    if (row >= rowLowLimit && row <= rowHighLimit && col >= colLowLimit && col <= colHighLimit) {
                        stopCheck.add(Cell.get(row, col));
                    }
                }
                stopCheck.remove(king.position);
            }
        }
        stopCheck.add(attacker.position);
        return stopCheck;
    }

    private Map<Piece, Piece> pinnedToKing(Piece king) {
        final Map<Piece, Piece> blockingChecks = new HashMap<>();
        final int kingRow = king.position.row, kingCol = king.position.col;
        final Color color = king.color;
        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                if (!(rowDiff == 0 && colDiff == 0)) {
                    final PieceType type = rowDiff == 0 || colDiff == 0 ? PieceType.ROOK : PieceType.BISHOP;
                    final Reference<Piece> blockingPiece = new Reference<>();
                    for (int k = 1; k < 8; k++) {
                        final int row = kingRow + (rowDiff * k), col = kingCol + (colDiff * k);
                        if (Utils.withinBoardLimits(row, col)) {
                            if (stopFindingPin(row, col, color, blockingPiece, blockingChecks, PieceType.QUEEN, type)) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return blockingChecks;
    }

    public boolean stopFindingPin(int row, int col, Color color,
                                  Reference<Piece> blockingPiece, Map<Piece, Piece> blockingChecks,
                                  PieceType... attackerTypes) {
        if (!isEmpty(row, col)) {
            final Piece piece = getPiece(row, col);
            if (blockingPiece.object == null) {
                if (piece.color == color) {
                    blockingPiece.object = piece;
                    return false;
                } else {
                    return true;
                }
            } else {
                if (piece.color != color && Arrays.stream(attackerTypes).anyMatch(piece::sameType)) {
                    blockingChecks.put(blockingPiece.object, piece);
                }
                return true;
            }
        }
        return false;
    }

    private Set<Piece> attackingKing(Piece king) {
        final HashSet<Piece> attackers = new HashSet<>();
        final int kingRow = king.position.row, kingCol = king.position.col;
        final Color color = king.color;
        for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
            for (int colDiff = -1; colDiff <= 1; colDiff++) {
                if (!(rowDiff == 0 && colDiff == 0) && attackers.size() < 2) {
                    final PieceType type = rowDiff == 0 || colDiff == 0 ? PieceType.ROOK : PieceType.BISHOP;
                    for (int row = kingRow + rowDiff, col = kingCol + colDiff; Utils.withinBoardLimits(row, col); row = row + rowDiff, col = col + colDiff) {
                        if (!isEmpty(row, col)) {
                            final Piece piece = getPiece(row, col);
                            if (piece.color != color && (piece.sameType(PieceType.QUEEN) || piece.sameType(type))) {
                                attackers.add(piece);
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (attackers.size() < 2) {
            final int pawnAttackRow = color == Color.BLACK ? kingRow - 1 : kingRow + 1;
            if (pawnAttackRow >= 0 && pawnAttackRow < 8) {
                final Piece leftPawn = kingCol > 0 ? getPiece(pawnAttackRow, kingCol - 1) : null;
                final Piece rightPawn = kingCol < 7 ? getPiece(pawnAttackRow, kingCol + 1) : null;
                if (rightPawn != null && rightPawn.sameType(PieceType.PAWN) && rightPawn.color != king.color) {
                    attackers.add(rightPawn);
                } else if (leftPawn != null && leftPawn.sameType(PieceType.PAWN) && leftPawn.color != king.color) {
                    attackers.add(leftPawn);
                }
            }
            for (int i = 0; i < Knight.diff.length && attackers.size() < 2; i++) {
                final int row = kingRow + Knight.diff[i][0], col = kingCol + Knight.diff[i][1];
                if (Utils.withinBoardLimits(row, col)) {
                    final Piece piece = getPiece(row, col);
                    if (piece != null && piece.sameType(PieceType.KNIGHT) && piece.color != king.color) {
                        attackers.add(piece);
                    }
                }
            }
        }
        return attackers;
    }

    private void updateHashForRemove(Piece piece) {
        zobristHash ^= zobrist[piece.pieceType.ordinal()][piece.color.ordinal()][(piece.position.row << 3) + piece.position.col];
    }

    private void updateHashForAddition(Piece piece) {
        zobristHash ^= zobrist[piece.pieceType.ordinal()][piece.color.ordinal()][(piece.position.row << 3) + piece.position.col];
    }

    public void makeMove(Move move) {
        //remove captured pieces
        final Set<Cell> affectedCells = new HashSet<>();
        if (move.captureMove) {
            final Piece delete = pieces.remove(move.captureCell);
            playerPieces.get(Color.opponent(move.piece.color)).remove(delete);
            updateHashForRemove(delete);
            affectedCells.add(move.captureCell);
        }
        pieces.remove(move.piece.position);
        playerPieces.get(move.piece.color).remove(move.piece);
        updateHashForRemove(move.piece);
        affectedCells.add(move.piece.position);
        for (final Cell affectedCell : affectedCells) {
            //todo: don't remove all the moves and guards, just the ones affected
            updateForClearCell(affectedCell);
        }
        updateForBlockedCell(move);
        for (final Piece king : kings) {
            updateKingMoves(move, king);
        }
        removeUnusedEnpassant();
        moveOrPromote(move);
        postMoveUpdates(move);
    }

    private void updateKingMoves(Move move, Piece king) {
        final Cell target = move.target;
        final Cell start = move.piece.position;
        final Cell capture = move.captureCell;
        removeMoves(king.position);
        //add guard moves in vicinity
        //occupied cell allows more moves ->
        //occupied cell allows less moves -> Opponent long piece moved into square. Remove all squares affected.
        //free cell allows more moves -> Impossible
        //free cell allows less moves -> Opponent long piece on other side, remove squares affected.
        //nearby moves of pawn -> My pawns have no affect. Enemy can guard.
        //knight moves -> My knight has no affect. Enemy can guard.
        //king moves -> If it's my kind, remove moves. Else can guard.
    }

    private void updateForClearCell(final Cell affectedCell) {
        longRangeUpdate(affectedCell, (piece) -> {
                    moves.get(piece.position).remove(Move.get(piece, affectedCell, true));
                    guards.get(piece.position).remove(Move.get(piece, affectedCell, false));
                    moves.get(piece.position).add(Move.get(piece, affectedCell, false));
                },
                (m, position) -> moves.get(position).add(m),
                (m, position) -> guards.get(position).add(m));
        for (final int[] coordinate : Knight.diff) {
            final int row = affectedCell.row + coordinate[0];
            final int col = affectedCell.col + coordinate[1];
            if (Utils.withinBoardLimits(row, col)) {
                if (!isEmpty(row, col)) {
                    final Piece knight = getPiece(row, col);
                    if (knight.sameType(PieceType.KNIGHT)) {
                        if (moves.get(knight.position) != null) {
                            moves.get(knight.position).remove(Move.get(knight, affectedCell, true));
                            guards.get(knight.position).remove(Move.get(knight, affectedCell, false));
                            moves.get(knight.position).add(Move.get(knight, affectedCell, false));
                        }
                    }
                }
            }
        }
        for (int i = -2; i <= 2; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    final int row = affectedCell.row + i;
                    final int col = affectedCell.col + j;
                    if (Utils.withinBoardLimits(row, col)) {
                        if (!isEmpty(row, col)) {
                            final Piece pawn = getPiece(row, col);
                            if (pawn.sameType(PieceType.PAWN) && (pawn.color.equals(Color.BLACK) ? i > 0 : i < 0)) {
                                removeMoves(pawn.position);
                            }
                        }
                    }
                }
            }
        }
    }

    private void longRangeUpdate(final Cell affectedCell,
                                 final Consumer<Piece> initialOperation,
                                 final BiConsumer<Move, Cell> moveUpdateOperation,
                                 final BiConsumer<Move, Cell> guardUpdateOperation) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                final PieceType type = (i == 0 || j == 0) ? PieceType.ROOK : PieceType.BISHOP;
                if (i != 0 || j != 0) {
                    for (int k = 0; k < 8; k++) {
                        final int row = affectedCell.row + i * k;
                        final int col = affectedCell.col + j * k;
                        if (Utils.withinBoardLimits(row, col)) {
                            if (!isEmpty(row, col)) {
                                final Piece piece = getPiece(row, col);
                                if (piece.sameType(PieceType.QUEEN) || piece.sameType(type)) {
                                    if (moves.get(piece.position) != null) {
                                        initialOperation.accept(piece);
                                        for (int l = 1; l < 8 - k; l++) {
                                            final int clearedRow = affectedCell.row - i * l;
                                            final int clearedCol = affectedCell.col - j * l;
                                            if (Utils.withinBoardLimits(clearedRow, clearedCol)) {
                                                final Cell visibleCell = Cell.get(clearedRow, clearedCol);
                                                if (isEmpty(clearedRow, clearedCol)) {
                                                    moveUpdateOperation.accept(Move.get(piece, visibleCell, false), piece.position);
                                                } else {
                                                    final Piece nowVisiblePiece = getPiece(clearedRow, clearedCol);
                                                    if (nowVisiblePiece.color.equals(piece.color)) {
                                                        guardUpdateOperation.accept(Move.get(piece, visibleCell, false), piece.position);
                                                    } else {
                                                        moveUpdateOperation.accept(Move.get(piece, visibleCell, true), piece.position);
                                                    }
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    private void updateForBlockedCell(final Move move) {
        final Cell affectedCell = move.target;
        longRangeUpdate(affectedCell, (piece) -> {
                    moves.get(piece.position).remove(Move.get(piece, affectedCell, true));
                    moves.get(piece.position).remove(Move.get(piece, affectedCell, false));
                    guards.get(piece.position).remove(Move.get(piece, affectedCell, false));
                    if (move.piece.color.equals(piece.color)) {
                        guards.get(piece.position).add(Move.get(piece, affectedCell, false));
                    } else {
                        moves.get(piece.position).add(Move.get(piece, affectedCell, true));
                    }
                },
                (m, position) -> moves.get(position).remove(m),
                (m, position) -> guards.get(position).remove(m));
        for (final int[] coordinate : Knight.diff) {
            final int row = affectedCell.row + coordinate[0];
            final int col = affectedCell.col + coordinate[1];
            if (Utils.withinBoardLimits(row, col)) {
                if (!isEmpty(row, col)) {
                    final Piece knight = getPiece(row, col);
                    if (knight.sameType(PieceType.KNIGHT)) {
                        if (moves.get(knight.position) != null) {
                            moves.get(knight.position).remove(Move.get(knight, affectedCell, true));
                            moves.get(knight.position).remove(Move.get(knight, affectedCell, false));
                            guards.get(knight.position).remove(Move.get(knight, affectedCell, false));
                            if (move.piece.color.equals(knight.color)) {
                                guards.get(knight.position).add(Move.get(knight, affectedCell, false));
                            } else {
                                moves.get(knight.position).add(Move.get(knight, affectedCell, true));
                            }
                        }
                    }
                }
            }
        }
        removeMoves(affectedCell);
        for (int i = -2; i <= 2; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0 || j != 0) && (Math.abs(i) + Math.abs(j) <= 2)) {
                    final int row = affectedCell.row + i;
                    final int col = affectedCell.col + j;
                    if (Utils.withinBoardLimits(row, col)) {
                        if (!isEmpty(row, col)) {
                            final Piece pawn = getPiece(row, col);
                            if (pawn.sameType(PieceType.PAWN) && (pawn.color == Color.BLACK ? i > 0 : i < 0)) {
                                removeMoves(pawn.position);
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeUnusedEnpassant() {
        if (!moveList.isEmpty()) {
            final Move previousMoveOfCurrentPlayer = moveList.get(moveList.size() - 1);
            if (PieceType.PAWN.equals(previousMoveOfCurrentPlayer.piece.pieceType)) {
                final Piece pawnLastMoved = previousMoveOfCurrentPlayer.piece;
                if (Math.abs(previousMoveOfCurrentPlayer.target.row - pawnLastMoved.position.row) == 2) {
                    final int row = previousMoveOfCurrentPlayer.target.row;
                    for (int j = -1; j <= 1; j = j + 2) {
                        int col = previousMoveOfCurrentPlayer.target.col + j;
                        if (Utils.withinBoardLimits(row, col)) {
                            if (!isEmpty(row, col)) {
                                final Piece pawn = getPiece(row, col);
                                if (pawn.sameType(PieceType.PAWN) && pawn.color != previousMoveOfCurrentPlayer.piece.color) {
                                    removeMoves(pawn.position);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeMoves(Cell position) {
        moves.remove(position);
        guards.remove(position);
    }

    private void moveOrPromote(Move move) {
        if (move.piece.sameType(PieceType.PAWN) && move.target.row == (playerToMove == Color.BLACK ? 0 : 7)) {
            switch (move.promoteTo) {
                case QUEEN:
                    placeQueen(move.target.row, move.target.col, playerToMove);
                    break;
                case ROOK:
                    placeRook(move.target.row, move.target.col, playerToMove);
                    break;
                case KNIGHT:
                    placeKnight(move.target.row, move.target.col, playerToMove);
                    break;
                case BISHOP:
                    placeBishop(move.target.row, move.target.col, playerToMove);
                    break;
                default:
                    throw new AssertionError();
            }
        } else {
            final Piece changedPiece = Piece.get(move.piece.color, move.target, move.piece.pieceType);
            pieces.put(move.target, changedPiece);
            playerPieces.get(move.piece.color).add(changedPiece);
            updateHashForAddition(changedPiece);
            if (changedPiece.pieceType == PieceType.KING) {
                kings[changedPiece.color.ordinal()] = changedPiece;
            }
        }
    }

    public Board copy() {
        final Board board = new Board();
        pieces.forEach(board.pieces::put);
        playerPieces.forEach((color, pieces) -> board.playerPieces.put(color, new ArrayList<>(pieces)));
        moves.forEach((cell, moves) -> board.moves.put(cell, new HashSet<>(moves)));
        guards.forEach((cell, moves) -> board.guards.put(cell, new HashSet<>(moves)));
        System.arraycopy(positions, 0, board.positions, 0, positions.length);
        board.positionIndex = positionIndex;
        board.moveList.addAll(moveList);
        System.arraycopy(kings, 0, board.kings, 0, 2);
        for (int i = 0; i < 2; i++) {
            System.arraycopy(canCastle[i], 0, board.canCastle[i], 0, 2);
        }
        board.previousMove = previousMove;
        board.zobristHash = zobristHash;
        board.playerToMove = playerToMove;
        board.isThreeFoldRepetition = isThreeFoldRepetition;
        board.halfMoves = halfMoves;
        board.fiftyMoveDraw = fiftyMoveDraw;
        board.inCheck = inCheck;
        return board;
    }

    //todo: idea: Should we compare a list of positions and choose the best? We do not evaluate positions in isolation,
    // but rather rank them by comparing the top 20 positions possible
    public double evaluation(int availableMoves) {
        if (availableMoves == 0) {
            if (inCheck) {
                return Integer.MIN_VALUE;
            } else {
                return 0;
            }
        }
        if (isDraw()) {
            return 0;
        }
        return heuristic() + availableMoves / 100.0;
    }

    private int heuristic() {
        return playerPieces.get(playerToMove).stream().mapToInt(piece -> approxValue.get(piece.pieceType)).sum()
                - playerPieces.get(Color.opponent(playerToMove)).stream().mapToInt(piece -> approxValue.get(piece.pieceType)).sum();
    }

    public boolean isDraw() {
        if (isThreeFoldRepetition || fiftyMoveDraw) {
            return true;
        }
        final List<Piece> currentPieces = playerPieces.get(playerToMove);
        final List<Piece> opponentPieces = playerPieces.get(Color.opponent(playerToMove));
        final boolean playerCannotWin = insufficientMaterial(currentPieces);
        final boolean opponentCannotWin = insufficientMaterial(opponentPieces);
        return (playerCannotWin && opponentCannotWin);
    }

    private boolean insufficientMaterial(List<Piece> opponentPieces) {
        if (opponentPieces.size() == 1) {
            return true;
        } else if (opponentPieces.size() == 2) {
            return opponentPieces.stream().filter(piece -> piece.sameType(PieceType.KNIGHT) || piece.sameType(PieceType.BISHOP)).count() == 1;
        } else if (opponentPieces.size() == 3) {
            return opponentPieces.stream().filter(piece -> piece.sameType(PieceType.KNIGHT)).count() == 2;
        }
        return false;
    }

    private void postMoveUpdates(Move move) {
        moveList.add(move);
        if (move.piece.sameType(PieceType.PAWN) && Math.abs(move.piece.position.row - move.target.row) == 2) {
            zobristHash ^= zobristEnpassantFiles[move.target.col];
        }
        if (previousMove != null && previousMove.piece.sameType(PieceType.PAWN) && Math.abs(previousMove.piece.position.row - previousMove.target.row) == 2) {
            zobristHash ^= zobristEnpassantFiles[previousMove.target.col];
        }
        previousMove = move;
        zobristHash ^= zobristSwitchPlayer;
        markDraw(move);
        castlingAllowance(move);
        inCheck = lookForChecks(move);
        playerToMove = Color.opponent(move.piece.color);
    }

    private void castlingAllowance(Move move) {
        if (move.piece.sameType(PieceType.KING)) {
            final int color = move.piece.color.ordinal();
            if (canCastle[color][0]) {
                zobristHash ^= zobristCastle[color * 2];
            }
            if (canCastle[color][1]) {
                zobristHash ^= zobristCastle[color * 2 + 1];
            }
            canCastle[color][1] = canCastle[color][0] = false;
            //take castling into account
            if (move.target.row == move.piece.position.row && Math.abs(move.piece.position.col - move.target.col) == 2) {
                final int row = color * 7;
                if (move.target.col == 6) {
                    makeMove(Move.get(getPiece(row, 7), Cell.get(row, 5), false));
                } else {
                    makeMove(Move.get(getPiece(row, 0), Cell.get(row, 3), false));
                }
            }
        } else {
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= 1; j++) {
                    final Cell corner = Cell.get(i * 7, j * 7);
                    if (move.piece.position.equals(corner) || (move.captureMove && move.captureCell.equals(corner))) {
                        if (canCastle[i][j]) {
                            if (move.piece.position.equals(Cell.get(i * 7, j * 7))) {
                                if (canCastle[i][j]) {
                                    zobristHash ^= zobristCastle[i * 2 + j];
                                }
                                canCastle[i][j] = false;
                            }
                        }
                    }
                }
            }
        }
    }

    private void markDraw(Move move) {
        positions[positionIndex % positions.length] = zobristHash;
        positionIndex++;
        int frequency = 0;
        for (final long position : positions) {
            if (position == zobristHash) {
                frequency++;
                if (frequency >= 3) {
                    isThreeFoldRepetition = true;
                    break;
                }
            }
        }
        if (move.captureMove || move.piece.sameType(PieceType.PAWN)) {
            halfMoves = 0;
        } else {
            halfMoves++;
            if (halfMoves == 100) {
                fiftyMoveDraw = true;
            }
        }
    }

    private boolean lookForChecks(Move move) {
        final Set<Move> moveList = getMoveList(pieces.get(move.target));
        for (final Move possibleMove : moveList) {
            if (possibleMove.captureMove && pieces.get(possibleMove.target).sameType(PieceType.KING)) {
                return true;
            }
        }
        final Piece king = kings[Color.opponent(playerToMove).ordinal()];
        return discoveredCheck(king, move.piece.position) || (move.captureMove && move.captureCell != move.target && discoveredCheck(king, move.captureCell));
    }

    private boolean discoveredCheck(Piece king, Cell coordinate) {
        //knight and pawn can't give discoveries
        final Line line = new Line(coordinate, king.position);
        if (line.isStraight) {
            for (int row = king.position.row + line.rowDiff, col = king.position.col + line.colDiff; Utils.withinBoardLimits(row, col); row = row + line.rowDiff, col = col + line.colDiff) {
                if (!isEmpty(row, col)) {
                    final Piece piece = pieces.get(Cell.get(row, col));
                    return piece.color != king.color && (piece.sameType(PieceType.QUEEN) || piece.sameType(line.minorPieceType));
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (isEmpty(i, j)) {
                    stringBuilder.append((i + j) % 2 == 0 ? "◻" : "◼");
                } else {
                    stringBuilder.append(getPiece(i, j).getShortForm());
                }
            }
            stringBuilder.append('\n');
        }
        stringBuilder.append("{\npieces=")
                .append(pieces).append('\n')
                .append("moveList=").append(moveList).append('\n')
                .append("inCheck: ").append(inCheck).append("\n}");
        return stringBuilder.toString();
    }

    public String fenRepresentation() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j < 8; j++) {
                if (isEmpty(i, j)) {
                    count++;
                } else {
                    if (count > 0) {
                        stringBuilder.append(count);
                        count = 0;
                    }
                    final Piece piece = getPiece(i, j);
                    char letter = piece.pieceType.getLetter();
                    if (piece.color == Color.WHITE) {
                        letter = (char) (letter - 'a' + 'A');
                    }
                    stringBuilder.append(letter);
                }
            }
            if (count > 0) {
                stringBuilder.append(count);
            }
            if (i > 0) {
                stringBuilder.append('/');
            }
        }
        stringBuilder.append(' ')
                .append(playerToMove == Color.WHITE ? 'w' : 'b')
                .append(' ')
                .append(castlingAllowanceFen(canCastle))
                .append(' ');
        if (!moveList.isEmpty()) {
            final Move lastMove = moveList.get(moveList.size() - 1);
            if (lastMove.piece.sameType(PieceType.PAWN) && Math.abs(lastMove.piece.position.row - lastMove.target.row) == 2) {
                stringBuilder.append((char) (lastMove.target.col + 'a')).append((lastMove.target.row + (lastMove.piece.color == Color.BLACK ? 2 : 0)));
            } else {
                stringBuilder.append('-');
            }
        } else {
            stringBuilder.append('-');
        }
        return stringBuilder.toString();
    }

    public static Board getBoard(String fen) {
        Board board = new Board();
        String[] boardFen = fen.split(" ");
        String[] rows = boardFen[0].split("/");
        for (int i = 0; i < 8; i++) {
            int index = 0;
            for (int j = 0; j < rows[i].length(); j++) {
                final char current = rows[i].charAt(j);
                if (Character.isDigit(current)) {
                    index += current - '0';
                } else {
                    final Color color = Character.isUpperCase(current) ? Color.WHITE : Color.BLACK;
                    final int row = 7 - i;
                    switch (Character.toLowerCase(current)) {
                        case 'p':
                            board.placePawn(row, index, color);
                            break;
                        case 'n':
                            board.placeKnight(row, index, color);
                            break;
                        case 'b':
                            board.placeBishop(row, index, color);
                            break;
                        case 'r':
                            board.placeRook(row, index, color);
                            break;
                        case 'q':
                            board.placeQueen(row, index, color);
                            break;
                        case 'k':
                            board.placeKing(row, index, color);
                            break;
                    }
                    index++;
                }
            }
        }
        board.playerToMove = boardFen[1].equals("w") ? Color.WHITE : Color.BLACK;
        if (board.playerToMove == Color.BLACK) {
            board.zobristHash ^= zobristSwitchPlayer;
        }
        board.canCastle[0][1] = boardFen[2].contains("K");
        board.canCastle[0][0] = boardFen[2].contains("Q");
        board.canCastle[1][1] = boardFen[2].contains("k");
        board.canCastle[1][0] = boardFen[2].contains("q");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (!board.canCastle[i][j]) {
                    board.zobristHash ^= zobristCastle[i * 2 + j];
                }
            }
        }
        if (!boardFen[3].equals("-")) {
            int rowDiff = boardFen[3].charAt(1) == '6' ? -1 : 1;
            final int col = boardFen[3].charAt(0) - 'a';
            final int row = rowDiff == -1 ? 4 : 3;
            final Piece pawn = board.getPiece(row, col);
            board.moveList.add(Move.get(Piece.get(pawn.color, Cell.get(rowDiff == -1 ? 6 : 1, col), PieceType.PAWN), pawn.position, false));
            board.zobristHash ^= zobristEnpassantFiles[col];
        }
        return board;
    }

    private String castlingAllowanceFen(boolean[][] canCastle) {
        String result = "";
        if (canCastle[0][1]) {
            result = result + "K";
        }
        if (canCastle[0][0]) {
            result = result + "Q";
        }
        if (canCastle[1][1]) {
            result = result + "k";
        }
        if (canCastle[1][0]) {
            result = result + "q";
        }
        return result.equals("") ? "-" : result;
    }

    private static class Reference<T> {
        T object;
    }
}