package view;

import go.Board;

public class TUI {
	
	public void showGame(Board board) {
		System.out.println("\r" + board.toString() + "\r");
	}
	
	
}
