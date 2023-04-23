package com.dama.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;
import com.dama.engine.pieces.Alliance;
import com.dama.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
   protected final Board board;
   protected final Collection<Move> legalMoves;
   protected final Collection<Move> opponentMoves;
   
   public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
	super();
	this.board = board;
	this.legalMoves =  ImmutableList.copyOf(Iterables.concat(legalMoves)) ;
	this.opponentMoves = opponentMoves;
   }
 
   protected static Collection<Move> calculateAttackOnTile(int piecePosition, Collection<Move> moves) {
	   final List<Move> attackMoves = new ArrayList<>();
	   for (final Move move : moves) {
		   if (piecePosition==move.getDestinationCoordinate()) {
				 attackMoves.add(move);
		   }

	   }
   	   return ImmutableList.copyOf(attackMoves);
   }
   
   public boolean isMoveLegal(Move move) {
	return this.legalMoves.contains(move);
   }
   
   public MoveTransition makeMove(final Move move) {
	   if (!isMoveLegal(move)) {
		 return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
	   }
	   
	   final Board transitionBoard = move.execute();
	   
	   return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
   }
   
   public abstract Collection<Piece> getActivePieces();
   
   public abstract Alliance getAlliance();
   
   public abstract Player getOpponent();

   public abstract int getPieceCount();

   public Collection<Move> getLegalMoves() {
		return legalMoves;
   }

	public boolean isInWin() {
		return this.getOpponent().getPieceCount()==0;
	}

	public boolean isInLoss() {
		return this.getPieceCount()==0;
	}
	
	public boolean isInDraw() {
		return  this.getPieceCount()==1 && this.getOpponent().getPieceCount()==1;
	}
}