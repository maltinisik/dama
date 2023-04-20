package com.dama.engine.player;

import com.dama.engine.board.Move;
import com.dama.engine.pieces.Piece;

public class MoveExecution {
	 private final Move move;
	 private final Piece lastMovedPiece;
	 
	 public MoveExecution(Move move, Piece lastMovedPiece) {
		super();
		this.move = move;
		this.lastMovedPiece = lastMovedPiece;
	 }

	public Move getMove() {
		return move;
	}

	public Piece getLastMovedPiece() {
		return lastMovedPiece;
	}
	 
}
