package go;

import netwerk.Lobby;
import view.TUI;

public class OnlineGame {

	
	private Board board;
	int current;
	int[] players;
	Lobby lobby;
	String status;
	int dim;
	
	public OnlineGame(int dim, Lobby lobby) {
		board = new Board(dim);
		this.dim = dim;
		current = 0;
		players = new int[2];
		players[0] = 1; //black starts
		players[1] = 2; //white is second
		this.lobby = lobby;
		
		
	}
	
	
	/***
	 * Starts a new game, can potentially be used with while loop to start new game when recent game ended
	 * TODO
	 */
	public void start() {
		lobby.broadcast("The game has started");
		
		play();
	}
	
	/***
	 * keeps track of game
	 */
	public void play() {
		while (!board.gameOver()) {
			
			int[] move = lobby.getMove(current);
			
			//getInput from current player
			//check input
			//set input on board
			//update status
			
		}
		
	}
	
	public String getBoardString() {
		Color[] fieldsCopy = board.getFields();
		String a = "";
		for (int i = 0; i < fieldsCopy.length; i++) {
			a += Color.getNr(fieldsCopy[i]);
		}
		return a;
	}
		
	public boolean gameOver() {
		return false;
	}
	
	
	
}
