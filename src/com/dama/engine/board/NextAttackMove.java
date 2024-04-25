package com.dama.engine.board;

import java.util.List;

import com.dama.engine.board.Move.AttackMove;
import com.dama.engine.pieces.Piece;

public class NextAttackMove extends Thread {
	private Piece piece; 
	public AttackMove attackMove;
	
	public NextAttackMove( Piece piece, AttackMove attackMove) {
		this.piece=piece;
		this.attackMove=attackMove;
	}
	
    public void run() {
	    final Board transitionBoard = attackMove.execute(piece);
	    
	    List<Move> lastMovedPieceLegalMoves = transitionBoard.getLastMovedPieceLegalMoves();
	    
	    //if (Move.hasAttackMoveInSameLevel(lastMovedPieceLegalMoves)) {
			for (Move move : lastMovedPieceLegalMoves) {
				if (move.isAttack()) 
				{
					final AttackMove nextAttackMove = ((AttackMove)move);
	                //next tas yonu bir onceki attack hareketinin ayni yonu olamaz
					if (!nextAttackMove.moveDirection.equals(attackMove.moveDirection.getOpposite())) {
						try {
							NextAttackMove nextAttackMoveFuture = new NextAttackMove(transitionBoard.getLastMovedPiece(), nextAttackMove);
							nextAttackMoveFuture.start();
							nextAttackMoveFuture.join();
							this.attackMove.setNextAttackMove(nextAttackMoveFuture.attackMove);
						}
						catch(InterruptedException ex) 
						{ System.err.println(ex);}
					}
				}
			}
		//}
    }	
}
