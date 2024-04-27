package com.chess.engine.player.ai;

import com.dama.engine.board.Board;
import com.dama.engine.pieces.Piece;
import com.dama.engine.player.Player;
import com.google.common.annotations.VisibleForTesting;

public final class StandartBoardEvaluator
        implements BoardEvaluator {

    private final static int WIN_BONUS = 100000;
    private final static int MOBILITY_MULTIPLIER = 1;
    private static final StandartBoardEvaluator INSTANCE = new StandartBoardEvaluator();

    private StandartBoardEvaluator() {
    }

    public static StandartBoardEvaluator get() {
        return INSTANCE;
    }

    @Override
    public int evaluate(final Board board,
                        final int depth) {
    	System.out.println(evaluationDetails(board,depth));
        return score(board.getWhitePlayer(), depth) - score(board.getBlackPlayer(), depth);
    }

    public String evaluationDetails(final Board board, final int depth) {
        return
               (
                "White winThreats : " + winThreats(board.getWhitePlayer(), depth) + "\n" +
                "White total piece value : " + totalPieceValue(board.getWhitePlayer()) + "\n" +
                "White total piece value : " + totalPiecePositionalValue(board.getWhitePlayer()) + "\n" +
                "---------------------\n" +
                "Black winThreats : " + winThreats(board.getBlackPlayer(), depth) + "\n" +
                "Black total piece value : " + totalPieceValue(board.getBlackPlayer()) + "\n" +
                "Black total piece value : " + totalPiecePositionalValue(board.getBlackPlayer()) + "\n");
    }

    @VisibleForTesting
    private static int score(final Player player,
                             final int depth) {
        return winThreats(player, depth) + totalPieceValue(player) + totalPiecePositionalValue(player);
    }

    private static int totalPieceValue(Player player) {
    	int total = 0;
    	for (Piece piece : player.getActivePieces()) {
    		total += piece.getPieceValue();
		}
        
		return total;
	}

    private static int totalPiecePositionalValue(Player player) {
    	int total = 0;
//    	for (Piece piece : player.getActivePieces()) {
//    		total += piece.getPiecePositionalValue();
//		}
        
		return total;
	}    
    
    private static int winThreats(final Player player,
                                   final int depth) {
        return player.isInWin() ? WIN_BONUS : 0;
    }

    private static int depthBonus(final int depth) {
        return depth == 0 ? 1 : 100 * depth;
    }

}