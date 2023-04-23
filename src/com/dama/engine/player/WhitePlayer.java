package com.dama.engine.player;

import java.util.Collection;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;
import com.dama.engine.pieces.Alliance;
import com.dama.engine.pieces.Piece;

public class WhitePlayer extends Player {

	public WhitePlayer(final Board board, 
			           final Collection<Move> whiteStandartLegalMoves,
			           final Collection<Move> blackStandartLegalMoves) {
		super(board, whiteStandartLegalMoves, blackStandartLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return board.getBlackPlayer();
	}

	@Override
	public int getPieceCount() {
		return this.getActivePieces().size();
	}

}
