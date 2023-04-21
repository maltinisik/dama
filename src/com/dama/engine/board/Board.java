package com.dama.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dama.engine.pieces.Alliance;
import com.dama.engine.pieces.Pawn;
import com.dama.engine.pieces.Piece;
import com.dama.engine.pieces.Queen;
import com.dama.engine.player.BlackPlayer;
import com.dama.engine.player.MoveDirection;
import com.dama.engine.player.MoveExecution;
import com.dama.engine.player.Player;
import com.dama.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {

	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final MoveExecution moveExecution;
	private final boolean transientBoard;
	
	private Board(final Builder builder) {
		this.gameBoard=createGameBoard(builder);

		this.whitePieces=calculateActivePieces(Alliance.WHITE);
		this.blackPieces=calculateActivePieces(Alliance.BLACK);
		
		this.moveExecution = builder.moveExecution;
		this.transientBoard = builder.transientBuilder;
		
		final Collection<Move> whiteStandartLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandartLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this,whiteStandartLegalMoves,blackStandartLegalMoves);
		this.blackPlayer = new BlackPlayer(this,whiteStandartLegalMoves,blackStandartLegalMoves);
			
		this.currentPlayer = calculateCurrentPlayer(builder);
	}
	
	private Player calculateCurrentPlayer(Builder builder) {
		return builder.nextMoveMaker == Alliance.WHITE ? whitePlayer : blackPlayer;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = prettyPrint(this.gameBoard.get(i));
			sb.append(String.format("%3s", tileText));
			if ((i+1)%BoardUtils.NUM_TILES_PER_ROW == 0) {
				sb.append("\n");				
			}
		}
		
		/*
		sb.append("WHITE MOVES\n");
		for(Move move : this.whitePlayer.getLegalMoves()) {
            sb.append(move.getMovedPiece().toString() + "-" + move.getCurrentCoordinate() + "-" + move.getDestinationCoordinate()+"\n");
		}
		sb.append("BLACK MOVES\n");
		for(Move move : this.blackPlayer.getLegalMoves()) {
            sb.append(move.getMovedPiece().toString() + "-" + move.getCurrentCoordinate() + "-" + move.getDestinationCoordinate()+"\n");
		}
		*/
		
		return sb.toString();
	}

	public boolean isTransientBoard() {
		return this.transientBoard;
	}
	
	private static String prettyPrint(Tile tile) {
		return tile.toString();
	}

	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves= new ArrayList<Move>();
		boolean lastMovedPieceHasAttackMove = false;
		
		
		//eger en son oynanan tas hala alacak tasi var ise bu durumda bu renkteki diger taslarin hareketlerini gereksiz hesaplama
		if (this.getLastMovedPiece()!=null && this.getLastMovedPiece().hasAttackMove(this)) {
			lastMovedPieceHasAttackMove = true;
		}
		
		for (final Piece piece : pieces) 
		{ 
			//tasin attack durumda oldugu next attack hareketlerin hesaplamasina girmesin
			if (this.isTransientBoard() && this.getLastMovedPiece()!=null && this.getLastMovedPiece().getPieceAlliance()==piece.getPieceAlliance() && this.getLastMovedPiece()!=piece) {
				continue;
			}
			
			if (lastMovedPieceHasAttackMove && this.getLastMovedPiece().getPieceAlliance()==piece.getPieceAlliance() && this.getLastMovedPiece()!=piece ) {
				continue;
			}
			
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return ImmutableList.copyOf(Move.eliminateNonAttackAndMaxCapturedMovesInSameLevel(legalMoves));
	}

	public List<Move> getLastMovedPieceLegalMoves() {
		final List<Move> legalMoves= new ArrayList<Move>();
    	for (Move move : this.getCurrentPlayer().getLegalMoves()) {
			if (move.getMovedPiece().equals(this.getLastMovedPiece())) {
			  legalMoves.add(move);	
			}
		}
    	
    	return ImmutableList.copyOf(legalMoves);
	}
	
	private Collection<Piece> calculateActivePieces(final Alliance allience) {
		final List<Piece> activePieces= new ArrayList<Piece>();
		for (Tile tile : gameBoard) 
		{ 
			if (tile.getPiece()!=null && tile.getPiece().getPieceAlliance()==allience) {
				activePieces.add(tile.getPiece());				
			}

		}
		return ImmutableList.copyOf(activePieces);
	}

	private static List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		
		return ImmutableList.copyOf(tiles);
	}
	
	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
	}
	
	public static Board createStandardBoard() {
		
		Builder builder = new Builder();
		//Black
		
//		builder.setPiece(new Pawn(8,Alliance.BLACK));
//		builder.setPiece(new Pawn(9,Alliance.BLACK));
//		builder.setPiece(new Pawn(10,Alliance.BLACK));
//		builder.setPiece(new Pawn(11,Alliance.BLACK));
//		builder.setPiece(new Pawn(12,Alliance.BLACK));
//		builder.setPiece(new Pawn(13,Alliance.BLACK));
//		builder.setPiece(new Pawn(14,Alliance.BLACK));
//		builder.setPiece(new Pawn(15,Alliance.BLACK));
//		builder.setPiece(new Pawn(16,Alliance.BLACK));
//		builder.setPiece(new Pawn(17,Alliance.BLACK));
//		builder.setPiece(new Pawn(18,Alliance.BLACK));
//		builder.setPiece(new Pawn(19,Alliance.BLACK));
//		builder.setPiece(new Pawn(20,Alliance.BLACK));
//		builder.setPiece(new Pawn(21,Alliance.BLACK));
//		builder.setPiece(new Pawn(22,Alliance.BLACK));
//		builder.setPiece(new Pawn(23,Alliance.BLACK));
		
		//White
//		builder.setPiece(new Pawn(40,Alliance.WHITE));
//		builder.setPiece(new Pawn(41,Alliance.WHITE));
//		builder.setPiece(new Pawn(42,Alliance.WHITE));
//		builder.setPiece(new Pawn(43,Alliance.WHITE));
//		builder.setPiece(new Pawn(44,Alliance.WHITE));
//		builder.setPiece(new Pawn(45,Alliance.WHITE));
//		builder.setPiece(new Pawn(46,Alliance.WHITE));
//		builder.setPiece(new Pawn(47,Alliance.WHITE));		
//		builder.setPiece(new Pawn(48,Alliance.WHITE));
//		builder.setPiece(new Pawn(49,Alliance.WHITE));
//		builder.setPiece(new Pawn(50,Alliance.WHITE));
//		builder.setPiece(new Pawn(51,Alliance.WHITE));
//		builder.setPiece(new Pawn(52,Alliance.WHITE));
//		builder.setPiece(new Pawn(53,Alliance.WHITE));
//		builder.setPiece(new Pawn(54,Alliance.WHITE));
//		builder.setPiece(new Pawn(55,Alliance.WHITE));
		
		builder.setPiece(new Pawn(16,Alliance.BLACK));
		builder.setPiece(new Pawn(17,Alliance.BLACK));
		builder.setPiece(new Pawn(25,Alliance.BLACK));
		builder.setPiece(new Pawn(10,Alliance.BLACK));

		builder.setPiece(new Pawn(37,Alliance.BLACK));
	    builder.setPiece(new Pawn(21,Alliance.BLACK));
		builder.setPiece(new Pawn(23,Alliance.BLACK));

		builder.setPiece(new Pawn(19,Alliance.WHITE));
		builder.setPiece(new Pawn(28,Alliance.WHITE));
		builder.setPiece(new Pawn(32,Alliance.WHITE));
		builder.setPiece(new Pawn(33,Alliance.WHITE));
		builder.setPiece(new Pawn(40,Alliance.WHITE));
		builder.setPiece(new Pawn(41,Alliance.WHITE));
		builder.setPiece(new Pawn(46,Alliance.WHITE));
		builder.setPiece(new Pawn(39,Alliance.WHITE));
		
		
		builder.setMoveMaker(Alliance.WHITE);
		
		//		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	}

	public Tile getTile(int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}

	public static class Builder {
		
		Map<Integer,Piece> boardConfig;
		Alliance nextMoveMaker;
		MoveExecution moveExecution;
		boolean transientBuilder;
		
		public Builder() {
			this.transientBuilder = false;
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(),piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker=nextMoveMaker;
			return this;
		}
		
		public Board build() {
			return new Board(this);
		}
	
		public void setMoveExecution(MoveExecution moveExecution) {
			this.moveExecution = moveExecution;
		}
		
		public void makeTransientBuilder() {
			this.transientBuilder=true;
		}
	}

	public Collection<Piece> getBlackPieces() {
		return blackPieces;
	}

	public Collection<Piece> getWhitePieces() {
		return whitePieces;
	}

	public WhitePlayer getWhitePlayer() {
		return whitePlayer;
	}

	public BlackPlayer getBlackPlayer() {
		return blackPlayer;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public Piece getLastMovedPiece() {
		return this.moveExecution != null ? this.moveExecution.getLastMovedPiece() : null;
	}
	
	public MoveExecution getLastMoveExecution() {
		return this.moveExecution;
	}
}
