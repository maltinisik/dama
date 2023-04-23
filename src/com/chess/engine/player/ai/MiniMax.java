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
        			         min(moveTransition.getTransitionBoard(),this.searchDepth-1) :
        			         max(moveTransition.getTransitionBoard(),this.searchDepth-1);
        	 
        	  if (board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highestValue) {
        		  highestValue = currentValue;
        		  bestMove = move;
			  } else if(board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowestValue){
        		  lowestValue = currentValue;
        		  bestMove = move;
			  }
		  }
        }		
		
		return bestMove;
	}

	public int min(Board board, int depth) {
		if (depth==0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		
		int lowestValue = Integer.MAX_VALUE;
        for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
          final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
          if (moveTransition.getMoveStatus().isDone()) {
        	  final int currentValue = max(moveTransition.getTransitionBoard(),depth-1);
        	  if (currentValue<=lowestValue) {
        		  lowestValue = currentValue;
  			  }
		  }
        }
		
		return lowestValue;
	}

	public int max(Board board, int depth) {
		if (depth==0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		
		int highestValue = Integer.MIN_VALUE;
        for(final Move move : board.getCurrentPlayer().getLegalMoves()) {
          final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
          if (moveTransition.getMoveStatus().isDone()) {
        	  final int currentValue = min(moveTransition.getTransitionBoard(),depth-1);
        	  if (currentValue>=highestValue) {
        		  highestValue = currentValue;
  			  }
		  }
        }
		
		return highestValue;
	}

	private boolean isEndGameScenario(Board board) {
		return board.getCurrentPlayer().isInLoss()  || board.getCurrentPlayer().isInDraw();
	}
}
