package com.chess.engine.player.ai;

import com.dama.engine.board.Board;

public interface BoardEvaluator {

	int evaluate(Board board, int depth);
}
