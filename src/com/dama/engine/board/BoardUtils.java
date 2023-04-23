package com.dama.engine.board;

import com.dama.engine.pieces.Alliance;

public class BoardUtils {
	
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);
	
	public static final boolean[] FIRST_ROW = initRow(0);
	public static final boolean[] SECOND_ROW = initRow(8);
	public static final boolean[] THIRD_ROW = initRow(16);
	public static final boolean[] FOURTH_ROW = initRow(24);
	public static final boolean[] FIFTH_ROW = initRow(32);
	public static final boolean[] SIXTH_ROW = initRow(40);
	public static final boolean[] SEVENTH_ROW = initRow(48);
	public static final boolean[] EIGTH_ROW = initRow(56);
	
	public static final int NUM_TILES = 64;
	public static final int NUM_TILES_PER_ROW = 8;
	
	public static final int TILE_0 = 0;
	public static final int TILE_1 = 1;
	public static final int TILE_2 = 2;
	public static final int TILE_3 = 3;
	public static final int TILE_5 = 5;
	public static final int TILE_6 = 6;
	public static final int TILE_7 = 7;
	public static final int TILE_56 = 56;
	public static final int TILE_57 = 57;
	public static final int TILE_58 = 58;
	public static final int TILE_59 = 59;
	public static final int TILE_61 = 61;	
	public static final int TILE_62 = 62;
	public static final int TILE_63 = 63;
	public static final int TILE_60 = 60;
	
    public static final int START_TILE_INDEX = 0;
	
	public static boolean isValidTileCoordinate(int coordinate) {
		return coordinate >=0 && coordinate < NUM_TILES;
    }
	
	private static boolean[] initRow(int rowNumber) {
	   final boolean[] row = new boolean[NUM_TILES];

	   for (int i = 0; i < NUM_TILES_PER_ROW; i++) {
		   row[rowNumber+i] = true;
	   }
	   			
	   return row;
	}

	private static boolean[] initColumn(int columnNumber) {
		final boolean[] column = new boolean[NUM_TILES];
		
		do {
		  column[columnNumber] = true;
		  columnNumber += NUM_TILES_PER_ROW;
		} while (columnNumber<NUM_TILES);
		
		return column;
	}

	public static int getPieceRowNum(Alliance pieceAlliance, int piecePosition) {
		if (pieceAlliance == Alliance.BLACK) 
			return (int) (Math.floor(piecePosition/BoardUtils.NUM_TILES_PER_ROW)+1);

		return BoardUtils.NUM_TILES_PER_ROW - (int) Math.floor(piecePosition/BoardUtils.NUM_TILES_PER_ROW);
	}

}
