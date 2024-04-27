package com.chess.engine.player.ai;

import java.util.Collection;
import java.util.Comparator;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;
import com.dama.engine.player.MoveTransition;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class MiniMax implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private final MoveSorter moveSorter;
	
	public MiniMax(final int searchDepth) {
		this.boardEvaluator= StandartBoardEvaluator.get();
		this.searchDepth=searchDepth;
		this.moveSorter=MoveSorter.SORT;
	}
	
	@Override
	public String toString() {
		return "MiniMax";
	}

	private enum MoveSorter {
		SORT {
			@Override
			Collection<Move> sort(Collection<Move> moves) {
				return Ordering.from(SMART_SORT).immutableSortedCopy(moves);
			}
		};

        public static Comparator<Move> SMART_SORT = new Comparator<Move>() {
            @Override
            public int compare(final Move move1, final Move move2) {
                return ComparisonChain.start()
                        .compareTrueFirst(move1.isAttack(), move2.isAttack())
                        .compare(move2.getMovedPiece().getPieceValue(), move1.getMovedPiece().getPieceValue())
                        .result();
            }
        };		
		
        abstract Collection<Move> sort(Collection<Move> moves);
	}	
	
	@Override
	public Move execute(Board board) {
		Move bestMove = null;
		
		int lowestValue = Integer.MAX_VALUE;
		int highestValue = Integer.MIN_VALUE;
		int currentValue;
        for(final Move move : moveSorter.sort(board.getCurrentPlayer().getLegalMoves())) {
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
            for (Move move : moveSorter.sort(board.getCurrentPlayer().getLegalMoves())) {
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
            for (Move move : moveSorter.sort(board.getCurrentPlayer().getLegalMoves())) {
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
