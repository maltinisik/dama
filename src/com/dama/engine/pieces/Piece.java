package com.dama.engine.pieces;

import java.util.List;

import com.dama.engine.board.Board;
import com.dama.engine.board.BoardUtils;
import com.dama.engine.board.Move;

public abstract class Piece {
   protected final PieceType pieceType;
   protected final int piecePostion;
   protected final Alliance pieceAllience;
   private final int cachedHashCode;
   
   Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAllience) {
	   this.pieceType = pieceType;
	   this.piecePostion = piecePosition;
	   this.pieceAllience = pieceAllience;
	   this.cachedHashCode=computeHashCode();
   }
   
   private int computeHashCode() {
	   int result = pieceType.hashCode();
	   result = 31 * result * pieceAllience.hashCode();
	   result = 31 * result + piecePostion;
	   
	   return result;
   }

  public abstract List<Move> calculateLegalMoves(final Board board);

   public boolean hasAttackMove(final Board board) {
	  return Move.hasAttackMoveInSameLevel(this.calculateLegalMoves(board));
   }
  
   public Alliance getPieceAlliance() {
	   return this.pieceAllience;
   }
   
   public Integer getPiecePosition() {
		return piecePostion;
   }
   
   public PieceType getPieceType() {
	   return pieceType;
   }
   
   public abstract Piece movePiece(Move move);
   
   public int getPieceValue() {
	   return this.getPieceType().getPieceValue();
   }

   public abstract int getPiecePositionalValue();
   
   public enum PieceType {
	   PAWN("P",100) {
		public boolean isQueen() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return true;
		}
	},
	   QUEEN("Q",2000) {
		@Override
		public boolean isQueen() {
			return false;
		}

		@Override
		public boolean isPawn() {
			return false;
		}
	   };
	   
	   private String pieceName;
	   private int pieceValue;
	   
	   PieceType(final String pieceName, int pieceValue) {
		   this.pieceName = pieceName;
		   this.pieceValue= pieceValue;
	   }
	   
	   public String toString() {
		   return pieceName;
	   }
	   
	   public int getPieceValue() {
		   return pieceValue;
	   }

	   public abstract boolean isQueen();
	
	   public abstract boolean isPawn();
   }
  
   @Override
   public boolean equals(final Object other) {
	   if (this==other) {return true;};
	   
	   if (!(other instanceof Piece)) {return false;}
	   
	   final Piece piece = (Piece) other;
	   
	   if (this.pieceAllience==piece.pieceAllience && this.getPiecePosition()==piece.getPiecePosition() && this.getPieceType()==piece.getPieceType()) {return true;}
	   
   	   return false;
   }
   
   @Override
   public int hashCode() {
	   return this.cachedHashCode;
   }
   
}