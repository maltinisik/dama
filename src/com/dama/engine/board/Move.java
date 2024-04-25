package com.dama.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dama.engine.board.Board.Builder;
import com.dama.engine.pieces.Alliance;
import com.dama.engine.pieces.Pawn;
import com.dama.engine.pieces.Piece;
import com.dama.engine.pieces.Queen;
import com.dama.engine.player.MoveDirection;
import com.dama.engine.player.MoveExecution;

public abstract class Move {
  final Board board;
  final Piece movedPiece;
  final int destinationCoordinate;
  
  private static final Move NULL_MOVE = new NullMove();

  private Move(Board board, Piece movedPiece, int destinationCoordinate) {
	super();
	this.board = board;
	this.movedPiece = movedPiece;
	this.destinationCoordinate = destinationCoordinate;
  }
  
  public static final class MajorMove extends Move {
	  public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
         super(board, movedPiece, destinationCoordinate);
	  }
	  
	  @Override
	  public boolean equals(Object otherObject) {
		return this==otherObject || (otherObject instanceof MajorMove && super.equals(otherObject));
	  }
	  
	  @Override
	  public String toString() {
		 return super.toString();
	  }
	  
  }
  
  public static final class PawnPromotion extends Move {
	  final Move decoratedMove;
	  final Pawn promotedPawn;
	  public PawnPromotion(final Move decoratedMove) {
         super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
         
         this.decoratedMove=decoratedMove;
         this.promotedPawn=(Pawn)decoratedMove.getMovedPiece();
	  }
	  
	  @Override
	  public Board execute() {
		    
		    final Board pawnMovedBoard = this.decoratedMove.execute();
		    final Builder builder = new Builder();
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
					builder.setPiece(piece);	
			}
		    
		    //promote the piece
		    Piece lastMovedPiece = this.promotedPawn.getPromotionPiece().movePiece(this);
		    builder.setPiece(lastMovedPiece);
		    builder.setMoveExecution(new MoveExecution(this,lastMovedPiece));
		    
		    builder.setPiece(lastMovedPiece);
		    builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
		    
			return builder.build();
    }
  }  

  public static final class PawnPromotionAttack extends AttackMove {
	  final AttackMove decoratedMove;
	  final Pawn promotedPawn;
	  public PawnPromotionAttack(final AttackMove decoratedMove) {
         super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate(),decoratedMove.attackedPiece,decoratedMove.moveDirection);
         
         this.decoratedMove=decoratedMove;
         this.promotedPawn=(Pawn)decoratedMove.getMovedPiece();
	  }
	  
	  @Override
	  public Board execute() {
		    
		    final Board pawnMovedBoard = this.decoratedMove.execute();
		    final Builder builder = new Builder();
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
				if (piece!=this.attackedPiece) {
					builder.setPiece(piece);	
				}
		    }
		    
		    //promote the piece
		    Piece lastMovedPiece = this.promotedPawn.getPromotionPiece().movePiece(this);
		    builder.setPiece(lastMovedPiece);
		    builder.setMoveExecution(new MoveExecution(this,lastMovedPiece));
		    builder.setPiece(lastMovedPiece);
		    builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
		    
			return builder.build();
    }
  }  

  public static final class PawnPromotionAttackWithNextAttack extends AttackMove {
	  final AttackMove decoratedMove;
	  final Pawn promotedPawn;
	  public PawnPromotionAttackWithNextAttack(final AttackMove decoratedMove) {
         super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate(),decoratedMove.attackedPiece,decoratedMove.moveDirection);
         
         this.decoratedMove=decoratedMove;
         this.promotedPawn=(Pawn)decoratedMove.getMovedPiece();
	  }
	  
	  @Override
	  public Board execute() {
		    
		    final Board pawnMovedBoard = this.decoratedMove.execute();
		    final Builder builder = new Builder();
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
				if (!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
		    
		    for (Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
				if (piece!=this.attackedPiece) {
					builder.setPiece(piece);	
				}
		    }
		    
		    //promote the piece
		    
		    Piece lastMovedPiece = this.promotedPawn.getPromotionPiece().movePiece(this);
		    builder.setPiece(lastMovedPiece);
		    builder.setMoveExecution(new MoveExecution(this,lastMovedPiece));
		    
		    builder.setPiece(lastMovedPiece);
		    builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
		    
			return builder.build();
    }
  }  
  
  public static final class PawnMove extends Move {
	  public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
         super(board, movedPiece, destinationCoordinate);
	  }
  }
  
  public static class AttackMove extends Move {
	  final Piece attackedPiece;
	  AttackMove nextAttackMove;
	  final MoveDirection moveDirection;
	  public AttackMove(Board board, Piece movedPiece, int destinationCoordinate,Piece attackedPiece,MoveDirection moveDirection) {
         super(board, movedPiece, destinationCoordinate);
	     this.attackedPiece=attackedPiece;
         this.nextAttackMove=null;
         this.moveDirection=moveDirection;
	  }
	  
	  @Override
	  public int hashCode() {
		  return this.attackedPiece.hashCode() + super.hashCode();
	  }
	  
	  @Override
	  public boolean equals(final Object object) {
		  if(this==object) return true;
		  
		  if (!(object instanceof AttackMove)) {return false;}
		  
		  AttackMove otherAttackMove = (AttackMove) object;
		  
		  return super.equals(otherAttackMove) && otherAttackMove.getAttackedPiece().equals(this.getAttackedPiece());
	  }
	  
	  @Override
	  public boolean isAttack() {
		  return true;
	  }
	  
	  @Override
	  public Piece getAttackedPiece() {
		  return this.getAttackedPiece();
	  }
	  
	  public void setNextAttackMove(AttackMove attackMove) {
		  this.nextAttackMove= attackMove;
	  }
	  
	  public AttackMove getNextAttackMove() {
		  return this.nextAttackMove;
	  }
	  
	  public MoveDirection getMoveDirection() {
		  return this.moveDirection;
	  }
	  
	  public boolean hasMoreAttackMove() {
		  return this.nextAttackMove!=null;
	  }
	  
	  @Override
	  public Board execute() {
		  final Builder builder = new Builder();
		  
		  for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if (piece!=this.movedPiece) {
				  builder.setPiece(piece);	
			}
		  }
		  
		  for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			if (piece!=this.attackedPiece) {
			    builder.setPiece(piece);	
			}
		  }
		  
	    //move the moved piece
		Piece lastMovedPiece = movedPiece.movePiece(this);  
	    builder.setPiece(lastMovedPiece);
	    builder.setMoveExecution(new MoveExecution(this,lastMovedPiece));
	    if (this.nextAttackMove!=null) {
		    builder.setMoveMaker(board.getCurrentPlayer().getAlliance());
		}
	    else {
		    builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
	    }
	    
		return builder.build();
	  }
	  
	  public int getCountOfCapturedPiecesWithAttackMoves() {
			List<Piece> capturedPieces= new ArrayList<Piece>();
		    capturedPieces.add(this.attackedPiece);
			AttackMove nextAttackMove = this.getNextAttackMove();
			
			while (nextAttackMove!=null) {
				if (!capturedPieces.contains(nextAttackMove.attackedPiece)) {
				    capturedPieces.add(nextAttackMove.attackedPiece);					
				}
				nextAttackMove = nextAttackMove.getNextAttackMove();
			}
			
			return capturedPieces.size();
	  }

	 //transient execute
	 public Board execute(Piece piece) {
		final Builder builder = new Builder();
        
		Collection<Piece> currentPlayerActivePieces;
		Collection<Piece> opponentPlayerActivePieces;

		if( piece.getPieceAlliance()==Alliance.WHITE ) {
			currentPlayerActivePieces = this.board.getWhitePieces();
			opponentPlayerActivePieces = this.board.getBlackPieces();
		}
		else {
			currentPlayerActivePieces = this.board.getBlackPieces();
			opponentPlayerActivePieces = this.board.getWhitePieces();			
		}
		
		for (Piece currentPiece : currentPlayerActivePieces) {
			if (currentPiece!=this.movedPiece) {
				  builder.setPiece(currentPiece);	
			}
		}
		  
		for (Piece opponentPiece : opponentPlayerActivePieces) {
			if (opponentPiece!=this.attackedPiece) {
			    builder.setPiece(opponentPiece);	
			}
		}
		
	    //move the moved piece
		Piece lastMovedPiece = movedPiece.movePiece(this);
	    builder.setPiece(lastMovedPiece);
	    builder.setMoveExecution(new MoveExecution(this,lastMovedPiece));
	    builder.setMoveMaker(piece.getPieceAlliance());
	    builder.makeTransient();
	    
		//System.out.println("=================================");	    
	    //System.out.println(this.board);

		Board transitionBoard = builder.build();

		//System.out.println(transitionBoard);
		//System.out.println("=================================");
		
        return transitionBoard;	 
	 }
  }
  
  public static class NullMove extends Move {
	  public NullMove() {
         super(null, null, -1);
	  }
	  
	  @Override
	  public Board execute() {
		  throw new RuntimeException("null move!!!");
	  }
  }
  
  public int getDestinationCoordinate() {
	return destinationCoordinate;
  }

  public Board getBoard() {
	return this.board;
}

public int getCurrentCoordinate() {
	return this.movedPiece.getPiecePosition();
  }
  
  public Piece getMovedPiece() {
		return movedPiece;
  }
  
  public boolean isAttack() {
	  return false;
  }
  
  public boolean isCatlingMove() {
	  return false;
  }
  
  public Piece getAttackedPiece() {
	  return null;
  }
 
  public Board execute() {
    final Builder builder = new Builder();
    
    for (Piece piece : board.getCurrentPlayer().getActivePieces()) {
		if (!this.movedPiece.equals(piece)) {
			builder.setPiece(piece);
		}
	}
    
    for (Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
	  builder.setPiece(piece);
	}
    
    //move the moved piece
    Piece lastMovedPiece = movedPiece.movePiece(this);
    builder.setPiece(lastMovedPiece);
    builder.setMoveExecution(new MoveExecution(this,lastMovedPiece));
    builder.setPiece(lastMovedPiece);
    builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
    
	return builder.build();
  }
  
	public static List<Move> eliminateNonAttackAndMaxCapturedMovesInSameLevel(List<Move> legalMoves) {
		final List<Move> newLegalMoves = new ArrayList<>();
		if (hasAttackMoveInSameLevel(legalMoves)) {
			final int maxCapturedPieceCount = calculateMaxCapturedPieceCount(legalMoves);
			for (Move move : legalMoves) {
			 if (move.isAttack() && ((AttackMove)move).getCountOfCapturedPiecesWithAttackMoves() >= maxCapturedPieceCount ) {
				 newLegalMoves.add(move);
			 }
			}
			return newLegalMoves;
		}
		
		return legalMoves;
	}
	
	private static int calculateMaxCapturedPieceCount(List<Move> legalMoves) {
		int maxCapturedPieceCount = 0;
		for (Move move : legalMoves) {
			 if (move.isAttack()) {
				 int countOfCapturedPiecesWithAttackMoves = ((AttackMove)move).getCountOfCapturedPiecesWithAttackMoves();
				 if (countOfCapturedPiecesWithAttackMoves > maxCapturedPieceCount) {
					 maxCapturedPieceCount = countOfCapturedPiecesWithAttackMoves;
				 }
			 }
		}		
		
		return maxCapturedPieceCount;
	}

	public static boolean hasAttackMoveInSameLevel(List<Move> legalMoves) {
		for (Move move : legalMoves) {
			if (move.isAttack()) return true;
		}
		
		return false;
	}
	
	public static AttackMove calculateNextAttackMoves(final Piece piece, AttackMove attackMove) {
	    final Board transitionBoard = attackMove.execute(piece);
	    
	    List<Move> lastMovedPieceLegalMoves = transitionBoard.getLastMovedPieceLegalMoves();
	    
	    if (hasAttackMoveInSameLevel(lastMovedPieceLegalMoves)) {
			for (Move move : lastMovedPieceLegalMoves) {
				if (move.isAttack()) 
				{
					final AttackMove nextAttackMove = ((AttackMove)move);
	                //next tas yonu bir onceki attack hareketinin ayni yonu olamaz
					if (!nextAttackMove.moveDirection.equals(attackMove.moveDirection.getOpposite())) {
						attackMove.setNextAttackMove(calculateNextAttackMoves(transitionBoard.getLastMovedPiece(), nextAttackMove));						
					}
				}
			}
		}
		
		return attackMove;
	}
  
  public static class MoveFactory {
	private  MoveFactory() {
		  throw new RuntimeException("Not instantible!");
	  }
	  
	  public static Move createMove(final Board board,
			                        final int currentCoordinate,
			                        final int destinationCoordinate) {
		  for(final Move move : board.getAllLegalMoves()) {
			  if (move.getCurrentCoordinate()==currentCoordinate && move.getDestinationCoordinate()==destinationCoordinate) {
				return move;
			}
		  }
		  
		  return NULL_MOVE;
	  }
  }
  
  @Override
  public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  
	  result = prime * result * this.destinationCoordinate;
	  result = prime * result * this.movedPiece.hashCode();
	  
	  return result;
  }
  
  @Override
  public boolean equals(final Object object) {
	  if(this==object) return true;
	  
	  if (!(object instanceof Move)) {return false;}
	  
	  Move otherMove = (Move) object;
	  
	  return otherMove.getDestinationCoordinate() == this.getDestinationCoordinate() &&
			 otherMove.getMovedPiece() == this.getMovedPiece();
  }
  
}
