package com.dama.engine.player;

import com.dama.engine.board.Board;
import com.dama.engine.board.Move;

public class MoveTransition {
 private final Board transitionBoard;
 private final Move move;
 private final MoveStatus moveStatus;

 public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus) {
	super();
	this.transitionBoard = transitionBoard;
	this.move = move;
	this.moveStatus = moveStatus;
 }
 
 public MoveStatus getMoveStatus() {
	 return this.moveStatus;
 }

 public Board getTransitionBoard() {
	return transitionBoard;
 }
 
}
