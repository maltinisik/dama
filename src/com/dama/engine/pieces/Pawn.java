package com.dama.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.dama.engine.board.Board;
import com.dama.engine.board.BoardUtils;
import com.dama.engine.board.Move;
import com.dama.engine.board.Move.AttackMove;
import com.dama.engine.board.Move.PawnMove;
import com.dama.engine.board.Move.PawnPromotion;
import com.dama.engine.player.MoveDirection;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATES = {8,-1,1,-2,2,16};
	
	public Pawn(int piecePostion, Alliance pieceAllience) {
		super(PieceType.PAWN,piecePostion, pieceAllience);
	}

	@Override
	public List<Move> calculateLegalMoves(Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate;
			if (candidateCoordinateOffset == 8 || candidateCoordinateOffset == 16 ) {
				candidateDestinationCoordinate = this.piecePostion + (candidateCoordinateOffset* this.pieceAllience.getDirection());	
			}
			else {
				candidateDestinationCoordinate = this.piecePostion + (candidateCoordinateOffset);				
			}
			
			if (MoveDirection.checkIsOppositeDirection(board,this,candidateCoordinateOffset)) {
				continue;
			}
			
			if (isFirstColumnExclusion(this.piecePostion, candidateCoordinateOffset) ||
			    isEightColumnExclusion(this.piecePostion, candidateCoordinateOffset)) {
				continue;
			}
			
			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			
			if (candidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied() ) {
				if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
				} else {
					legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
				}
			}
			else if (candidateCoordinateOffset == 1 && !board.getTile(candidateDestinationCoordinate).isTileOccupied() ) {
				if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
				} else {
					legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
				}
			}
			else if (candidateCoordinateOffset == -1 && !board.getTile(candidateDestinationCoordinate).isTileOccupied() ) {
				if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
				} else {
					legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
				}
			}
			else if (candidateCoordinateOffset == 16 || candidateCoordinateOffset == -16)
			{
				final int behindCandidateDestinationCoordinate = this.piecePostion + (this.pieceAllience.getDirection()*8);
				final int behindOfBehindCandidateDestinationCoordinate = behindCandidateDestinationCoordinate + (this.pieceAllience.getDirection()*8);
				
				if (board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
					board.getTile(behindCandidateDestinationCoordinate).getPiece().getPieceAlliance()!=this.pieceAllience &&
					!board.getTile(behindOfBehindCandidateDestinationCoordinate).isTileOccupied())
				{
					if (this.getPieceAlliance().isPawnPromotionSquare(candidateDestinationCoordinate)) {
					    legalMoves.add(new Move.PawnPromotionAttack(new Move.AttackMove(board, this, candidateDestinationCoordinate, board.getTile(behindCandidateDestinationCoordinate).getPiece(),MoveDirection.getMoveDirection(candidateCoordinateOffset))));
					}
					else 
					{
						AttackMove attackMove = new Move.AttackMove(board, this, candidateDestinationCoordinate, board.getTile(behindCandidateDestinationCoordinate).getPiece(),MoveDirection.getMoveDirection(candidateCoordinateOffset));
					    //if (!board.isTransientBoard()) {
						attackMove = Move.calculateNextAttackMoves(this,attackMove);							
						//}
					    legalMoves.add(attackMove);
					}
				}
			}
			else if (candidateCoordinateOffset == 2 || candidateCoordinateOffset == -2 )
			{
				final int behindCandidateDestinationCoordinate = this.piecePostion + (candidateCoordinateOffset/2);
				final int behindOfBehindCandidateDestinationCoordinate = behindCandidateDestinationCoordinate + (candidateCoordinateOffset/2);
				
				if (board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
					board.getTile(behindCandidateDestinationCoordinate).getPiece().getPieceAlliance()!=this.pieceAllience &&
					!board.getTile(behindOfBehindCandidateDestinationCoordinate).isTileOccupied()) 
				{
					//hedef tas 1. veya 8. kolonda ise bunu pass gec
					if (isFirstColumnExclusion(behindCandidateDestinationCoordinate, candidateCoordinateOffset) ||
								isEightColumnExclusion(behindCandidateDestinationCoordinate, candidateCoordinateOffset)) {
					   continue;
					}
					
					AttackMove attackMove = new Move.AttackMove(board, this, candidateDestinationCoordinate, board.getTile(behindCandidateDestinationCoordinate).getPiece(),MoveDirection.getMoveDirection(candidateCoordinateOffset));
				    //if (!board.isTransientBoard()) {
					attackMove = Move.calculateNextAttackMoves(this,attackMove);
					//}
				    legalMoves.add(attackMove);
				}
			}
		}
		
		return ImmutableList.copyOf(Move.eliminateNonAttackAndMaxCapturedMovesInSameLevel(legalMoves));
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -2 || candidateOffset == -1);
	}

	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 2 ||candidateOffset == 1);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	
	@Override
	public Pawn movePiece(Move move) {
		Pawn pawn = new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
		return pawn;
	}

	public Queen getPromotionPiece() {
		 return new Queen(this.piecePostion, this.pieceAllience);
	}
}