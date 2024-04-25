package com.dama.engine.pieces;


import java.util.ArrayList;
import java.util.List;

import com.dama.engine.board.Board;
import com.dama.engine.board.BoardUtils;
import com.dama.engine.board.Move;
import com.dama.engine.board.NextAttackMove;
import com.dama.engine.board.Move.AttackMove;
import com.dama.engine.board.Move.MajorMove;
import com.dama.engine.board.Tile;
import com.dama.engine.player.MoveDirection;
import com.google.common.collect.ImmutableList;

public class Queen extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATES = {-8,-1,1,8};
	
	public Queen(int piecePostion, Alliance pieceAllience) {
		super(PieceType.QUEEN,piecePostion, pieceAllience);
	}	
	
	@Override
	public List<Move> calculateLegalMoves(Board board) {
	
		final List<Move> legalMoves = new ArrayList<>();

		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePostion;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
					isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}
				
				if (MoveDirection.checkIsOppositeDirection(board,this,candidateCoordinateOffset)) {
					break;
				}
				
				candidateDestinationCoordinate += candidateCoordinateOffset;
				if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					  final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					  
					  if (!candidateDestinationTile.isTileOccupied()) {
						  legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					  }
					  else {
						  //hedef tas 1. veya 8. kolonda ise bunu pass gec
						  if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
								isEightColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
								break;
						  }
							
						  final Piece pieceAtDestination =  candidateDestinationTile.getPiece();
						  final Alliance pieceDestinateAlliance = pieceAtDestination.getPieceAlliance();

						  if (this.pieceAllience == pieceDestinateAlliance) { //ayni renk tas ile karsilastiginda o yonde ilerlemeyi durdur
							  break;
						  }
						  
						  //su anda farkli renkte bir tas ile karsilasildi, bu tasin o yondeki 1 arkasi bos mu?
						  int behindCandidateDestinationCoordinate = candidateDestinationCoordinate + candidateCoordinateOffset;
						  
						  //Bu karenin gecerli olup olmadigini kontrol et
						  if (!BoardUtils.isValidTileCoordinate(behindCandidateDestinationCoordinate)) {
							  break;
						  }
						  
						  if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()) 
						  {
							  AttackMove attackMove = new Move.AttackMove(board, this, behindCandidateDestinationCoordinate, board.getTile(candidateDestinationCoordinate).getPiece(),MoveDirection.getMoveDirection(candidateCoordinateOffset));								  
							  //if (!board.isTransientBoard()) {
								  attackMove = Move.calculateNextAttackMoves(this,attackMove);
							      
//							  try {
//								NextAttackMove nextAttackMoveFuture = new NextAttackMove(this,attackMove);
//								nextAttackMoveFuture.start();
//								nextAttackMoveFuture.join();
//								attackMove = nextAttackMoveFuture.attackMove;
//							  }
//							  catch(InterruptedException ex) 
//							  { System.err.println(ex);}	
							  
							  //}
							  legalMoves.add(attackMove);

							  //bir sonraki kareden baska varsa bos kareye koyabilir bunlari da AttackMove olarak eklemek gerekiyor
							  //bunun icin hangi yonde ise 
							  while(!(isFirstColumnExclusion(behindCandidateDestinationCoordinate, candidateCoordinateOffset) || isEightColumnExclusion(behindCandidateDestinationCoordinate, candidateCoordinateOffset))) {
								  behindCandidateDestinationCoordinate = behindCandidateDestinationCoordinate + candidateCoordinateOffset;
				
								  //kare gecersiz ise cik
								  if (!BoardUtils.isValidTileCoordinate(behindCandidateDestinationCoordinate)) {
									break;
								  }
								  
								  final Tile candidateDestinationTileNext = board.getTile(behindCandidateDestinationCoordinate);
								  
								  //eger herhangi bir tas ile karsilasirsa o yonde aramayi durdur
								  if (candidateDestinationTileNext.isTileOccupied()) {
									break;
								  }
								  
								  AttackMove attackMoveNext = new Move.AttackMove(board, this, behindCandidateDestinationCoordinate, board.getTile(candidateDestinationCoordinate).getPiece(),MoveDirection.getMoveDirection(candidateCoordinateOffset));								  
								  //if (!board.isTransientBoard()) {
									  attackMoveNext = Move.calculateNextAttackMoves(this,attackMoveNext);
//									  try {
//										NextAttackMove nextAttackMoveFuture = new NextAttackMove(this,attackMoveNext);
//										nextAttackMoveFuture.start();
//										nextAttackMoveFuture.join();
//										attackMoveNext = nextAttackMoveFuture.attackMove;
//									  }
//									  catch(InterruptedException ex) 
//									  { System.err.println(ex);}									  
									  
									  
								  //}
								  legalMoves.add(attackMoveNext);
								  
								  //+1 olarak ilerlerken 8.kolonda ise cik
								  //-1 olarak  ilerlerken 1.kolonda ise cik						  
							  } 
						  }

						  //rakip ya da kendi tasini buldugunda her durumda next aramalari kes
						  //zaten kendi tasi ise durdurmak gerekiyor
						  break;
					  }
					}
			}
		}		

		return ImmutableList.copyOf(Move.eliminateNonAttackAndMaxCapturedMovesInSameLevel(legalMoves));	
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
	}

	private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
	}
	
	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}
	
	@Override
	public Queen movePiece(Move move) {
		Queen queen = new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
		return queen;
	}
	
	public int getPiecePositionalValue() {
		   return this.getPieceType().getPieceValue();
	}
}
