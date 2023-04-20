package com.test.dama.engine.board;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.dama.engine.board.Board;
import com.dama.engine.board.Board.Builder;
import com.dama.engine.pieces.Alliance;
import com.dama.engine.pieces.Pawn;
import com.dama.engine.pieces.Queen;

public class TestBoard {

	@Test
	public void intialBoard() {
		Board board = Board.createStandardBoard();
		assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 8);
		assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 8);
		assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
		assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
	}
	
	@Test
	public void checkPlayer() {
		Board board = Board.createStandardBoard();
		assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
		assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
	}

	@Test
	public void checkCustomBoard1() {
		Builder builder = new Builder();
		
		builder.setPiece(new Pawn(29,Alliance.WHITE));
		builder.setPiece(new Pawn(15,Alliance.WHITE));
		builder.setPiece(new Queen(28,Alliance.BLACK));
		builder.setMoveMaker(Alliance.BLACK); 
		
		Board board = builder.build();
		
		assertEquals(board.getAllLegalMoves(), null); ;
	}
}
