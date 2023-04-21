package com.dama.engine.player;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;
import com.dama.engine.pieces.Piece;

public enum MoveDirection {
 UP {
	@Override
	public MoveDirection getOpposite() {
		return DOWN;
	}
},
 DOWN {
	@Override
	public MoveDirection getOpposite() {
		return UP;
	}
},
 RIGHT {
	@Override
	public MoveDirection getOpposite() {
		return LEFT;
	}
},
 LEFT {
	@Override
	public MoveDirection getOpposite() {
		return RIGHT;
	}
};
	
    public abstract MoveDirection getOpposite();
    
    public static MoveDirection getMoveDirection(int offSet) {
    	switch (offSet) {
		case 1: {
			return RIGHT;
		}
		case -1: {
			
			return LEFT;
		}
		case 2: {
			return RIGHT;
		}
		case -2: {
			
			return LEFT;
		}
		case 8: {
			return UP;
		}
		case 16: {
			return UP;
		}
		case -8: {
			return DOWN;
		}
		case -16: {
			return DOWN;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + offSet);
		}
    	
    }

	public static boolean checkIsOppositeDirection(Board board, Piece piece, int offSet) {
        if (board.getLastMoveExecution()!=null) {
        	
        	Move.AttackMove attackMove = (Move.AttackMove)board.getLastMoveExecution().getMove();
    
        	//ayni renkteki tasin hamleleri icin bir onceki hamlenin hangi yon oldugu onemli degil
        	if (!piece.getPieceAlliance().equals(board.getLastMovedPiece().getPieceAlliance())) {
				return false;
			}
        	
			if (attackMove.getMoveDirection().equals(getMoveDirection(offSet).getOpposite())) {
				return true;
			}
		}
        
		return false;
	}
}
