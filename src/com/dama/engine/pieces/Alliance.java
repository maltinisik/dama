package com.dama.engine.pieces;

import com.dama.engine.board.BoardUtils;

public enum Alliance {
    WHITE {
		@Override
		public int getDirection() {
			return -1;
		}

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public boolean isWhite() {
			return true;
		}
		
		@Override
		public boolean isPawnPromotionSquare(int position) {
			return BoardUtils.FIRST_ROW[position];
		}
		
	}, BLACK {
		@Override
		public
		int getDirection() {
			return 1;
		}

		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public boolean isWhite() {
			return false;
		}

		@Override
		public boolean isPawnPromotionSquare(int position) {
			return BoardUtils.EIGTH_ROW[position];
		}
	};
    
    public abstract int getDirection();
    
    public abstract boolean isBlack();
    
    public abstract boolean isWhite();
    
    public abstract boolean isPawnPromotionSquare(int position);
}
