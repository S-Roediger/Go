package go;

import netwerk.Lobby;
import view.TUI;

public class OnlineGame {

	
	private Board board;
	int current;
	int[] players;
	Lobby lobby;
	String status;
	
	public OnlineGame(int dim, Lobby lobby) {
		board = new Board(dim);
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
		updateStatus();
		play();
	}
	
	/***
	 * keeps track of game
	 */
	public void play() {
		while (!board.gameOver()) {
			
			//getInput from current player
			//check input
			//set input on board
			//update status
			
		}
		updateStatus();
	}
	
	public String getStatus() {
		return status;
	}
	
	public void updateStatus() {//$STATUS(PLAYING, WAITING, FINISHED;$CURRENT_PLAYER int color of player that should make move;$BOARD String of fields
		
	}
	
}
