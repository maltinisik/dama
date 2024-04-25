package com.dama.engine.player;

import java.util.Collection;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;
import com.dama.engine.pieces.Alliance;
import com.dama.engine.pieces.Piece;

public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, 
			           final Collection<Move> whiteStandartLegalMoves,
			           final Collection<Move> blackStandartLegalMoves) {
		super(board, blackStandartLegalMoves, whiteStandartLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		return board.getWhitePlayer();
	}

	@Override
	public int getPieceCount() {
		return this.getActivePieces().size();
	}
}
