package com.chess.engine.player.ai;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;
import com.dama.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
	
	public MiniMax(final int searchDepth) {
		this.boardEvaluator= StandartBoardEvaluator.get();
		this.searchDepth=searchDepth;
	}
	
	@Override
	public String toString() {
		return "MiniMax";
	}
	
	@Override
	public Move execute(Board board) {
		Move bestMove = null;
		
		int lowestValue = Integer.MAX_VALUE;
		int highestValue = Integer.MIN_VALUE;
		int currentValue;
        for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
          final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
          if (moveTransition.getMoveStatus().isDone()) {
        	  
              currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
              		minimaxAlphaBeta(moveTransition.getTransitionBoard(),this.searchDepth - 1, highestValue,lowestValue,false) :
              	    minimaxAlphaBeta(moveTransition.getTransitionBoard(),this.searchDepth - 1, highestValue,lowestValue,true);
              
        	  if (board.getCurrentPlayer().getAlliance().isWhite() && currentValue > highestValue) {
        		  highestValue = currentValue;
        		  bestMove = move;
			  } else if(board.getCurrentPlayer().getAlliance().isBlack() && currentValue < lowestValue){
        		  lowestValue = currentValue;
        		  bestMove = move;
			  }
		  }
        }		
		
		return bestMove;
	}

    public int minimaxAlphaBeta(final Board board, final int depth, int alpha, int beta, boolean maximizingPlayer) {
    	if (depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board,depth);
	    }
        
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getCurrentPlayer().getLegalMoves()) {
   		        final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
                int eval = minimaxAlphaBeta(moveTransition.getTransitionBoard(), depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            	final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
                int eval = minimaxAlphaBeta(moveTransition.getTransitionBoard(), depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }
    
	private boolean isEndGameScenario(Board board) {
		return board.getCurrentPlayer().isInLoss()  || board.getCurrentPlayer().isInDraw();
	}
}
