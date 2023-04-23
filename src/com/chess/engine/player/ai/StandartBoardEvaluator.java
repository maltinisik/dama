package com.chess.engine.player.ai;

import com.dama.engine.board.Board;
import com.dama.engine.pieces.Piece;
import com.dama.engine.player.Player;
import com.google.common.annotations.VisibleForTesting;

public final class StandartBoardEvaluator
        implements BoardEvaluator {

    private final static int WIN_BONUS = 10000;
    private final static int MOBILITY_MULTIPLIER = 2;
    private final static int POSITIONAL_MULTIPLIER = 25;
    private static final StandartBoardEvaluator INSTANCE = new StandartBoardEvaluator();

    private StandartBoardEvaluator() {
    }

    public static StandartBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board,
                        final int depth) {
        return score(board.getWhitePlayer(), depth) - score(board.getBlackPlayer(), depth);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return
               ("White Mobility : " + mobility(board.getWhitePlayer()) + "\n") +
                "White winThreats : " + winThreats(board.getWhitePlayer(), depth) + "\n" +
                "White total piece value : " + totalPieceValue(board.getWhitePlayer()) + "\n" +
                "---------------------\n" +
                "Black Mobility : " + mobility(board.getBlackPlayer()) + "\n" +
                "Black winThreats : " + winThreats(board.getBlackPlayer(), depth) + "\n" +
                "Black total piece value : " + totalPieceValue(board.getBlackPlayer()) + "\n" +
                "Final Score = " + evaluate(board, depth);
    }

    @VisibleForTesting
    private static int score(final Player player,
                             final int depth) {
        return mobility(player) + winThreats(player, depth) + totalPieceValue(player);
    }

    private static int totalPieceValue(Player player) {
    	int total = 0;
    	for (Piece piece : player.getActivePieces()) {
    		total += piece.getPiecePositionalValue(POSITIONAL_MULTIPLIER);
		}
        
		return total;
	}

	private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) {
        return (int)((player.getLegalMoves().size() * 10.0f) / player.getOpponent().getLegalMoves().size());
    }

    private static int winThreats(final Player player,
                                   final int depth) {
        return player.isInWin() ? WIN_BONUS  * depthBonus(depth) : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : 100 * depth;
    }

}