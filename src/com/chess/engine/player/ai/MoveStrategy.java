package com.chess.engine.player.ai;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;

public interface MoveStrategy {

	Move execute(Board board);
}
