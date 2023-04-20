package com.dama.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.dama.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

public abstract class Tile {
	  protected final int tileCoordinate;
	 
	  private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHES = createAllEmptryTiles();

	  private static final int TILE_SIZE = 64;
	 
	  private Tile (int tileCoordinate) {
		 this.tileCoordinate=tileCoordinate;
	  }
	 
	  public static Tile createTile(final int tileCoordinate, final Piece piece) {
		 return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHES.get(tileCoordinate);
	  }
	 
	  public int getTileCoordinate() {
		  return tileCoordinate;
	  }
	  
	  private static Map<Integer, EmptyTile> createAllEmptryTiles() {
		 
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		 
		for (int i = 0; i < TILE_SIZE; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		 
		return ImmutableMap.copyOf(emptyTileMap);
	 }

	 public abstract boolean isTileOccupied();
	 
	 public abstract Piece getPiece();

     public static final class EmptyTile extends Tile {
    	 private EmptyTile(final int coordinate) {
    		 super(coordinate);
    	}

		@Override
		public boolean isTileOccupied() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}
		
		@Override
		public String toString() {
			return "-";
		}
     }

     public static final class OccupiedTile extends Tile {
		 private final Piece pieceOnTile;
		 
		 private OccupiedTile(int coordinate,Piece pieceOnTile) {
			 super(coordinate);
			 this.pieceOnTile=pieceOnTile;
		 }

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return pieceOnTile;
		}
		
		@Override
		public String toString() {
			return this.pieceOnTile.getPieceAlliance().isBlack() ? this.pieceOnTile.toString().toLowerCase() : this.pieceOnTile.toString();
		}
     }

}
