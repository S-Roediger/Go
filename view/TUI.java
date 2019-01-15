package view;

import go.Board;

public class TUI {

	public String showMenu() {
		return "SET_CONFIG /r" + "ANDERE COMMANDS /r" + "Start Game /r";
	}
	
	
	public void showGame(Board board) {
		System.out.println("\r" + board.toString() + "\r");
	}
	
	
}
