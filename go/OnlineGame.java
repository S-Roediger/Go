package go;

import java.util.ArrayList;



public class OnlineGame extends Thread {

// ----------- Fields ------------------ //
	
	private Board board;
	private int current;
	private Player[] players;
	private int currentPlayerAckn = 1;
	private boolean exit = false;

// ------------- Constructor -------------- //
	
	public OnlineGame(int dim, Player p0, Player p1) {
		board = new Board(dim);
		current = 0;
		players = new Player[2];
		if (p1.getColor().equals(Color.BLACK)) { //enforces rule that black starts
			players[0] = p1;
			players[1] = p0;
		} else {
			players[0] = p0;
			players[1] = p1;
		}	
	}
	

// -------------- Queries ---------------- //	
	
	/**
	 * Method to obtain value of exit.
	 * @return true if player requested to exit game
	 */
	public boolean getExit() {
		return this.exit;
	}
	
	/**
	 * Method to obtain current player.
	 * @return Integer representing current player
	 */
	public int getCurrent() {
		return this.current;
	}
	
	/**
	 * Method to get current board.
	 * @return current board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Method to obtain array of current players.
	 * @return array of current players
	 */
	public Player[] getPlayers() {
		return this.players;
	}

	/**
	 * Method to obtain current player for Acknowledge move message.
	 * @return current player
	 */
	public int getCurrentPlayer() {
		return this.currentPlayerAckn;
	}
	
	/**
	 * Method to obtain the player name of the winner of the current game.
	 * @return String representing the name of the current winner
	 */
	public String getWinner() {
		
		if (exit) {
			//if a player exited, the opponent has won the match automatically
			int tempCurrent = current;
			tempCurrent = (tempCurrent + 3) % 2;
			return players[tempCurrent].getName();
		}
		
		double[] score = board.getScore();
		
		if (score[0] > score[1]) { //Black has won
			return players[0].getName();
		} else if (score[0] < score[1]) {
			return players[1].getName(); //White has won
		} else if (score[0] == score[1]) {
			return "draw"; //since currently white gets .5 extra points at the end of game,
							//this is currently impossible
		}
		return "";
	}
	
	/**
	 * Method to obtain the score of the current game.
	 * @return a double array containing the score for black and white
	 */
	public String getScore() {
		double[] score = board.getScore();
		String result = score[0] + ";" + score[1];
		return result;
	}
	
	/**
	 * Method to obtain a String representation of the current board.
	 * @return a String representing the fields of the current board
	 */
	public synchronized String getBoardString() {
		Color[] fieldsCopy = board.getFields();
		String a = "";
		for (int i = 0; i < fieldsCopy.length; i++) {
			a += Color.getNr(fieldsCopy[i]);
		}
		return a;
	}
		
	/**
	 * Method to check whether the game is over.
	 * A game is over when two players passed consecutively.
	 * @return true when the game is over, otherwise false
	 */
	public boolean gameOver() {
		return board.gameOver();
	}
	
	
// ------------- Commands --------------- //	
		
	/**
	 * Method to set current player.
	 * @param i - Integer representing current player
	 */
	public void setCurrent(int i) {
		this.current = i;
	}
	
	/**
	 * Method to set boolean exit.
	 * @param b - new boolean value of exit 
	 */
	public void setExit(Boolean b) {
		this.exit = b;
	}
	
	
	/***
	 * Handles suicide.
	 * @param c - Color for to be checked stone
	 * @param lastSet - index of last set
	 */
	public void handleSuicide(Color c, int lastSet) {
		ArrayList<Integer> suicide = new ArrayList<Integer>();
		suicide.add(lastSet);
		if (board.isCaptured(Color.getOther(players[current].getColor()), suicide)) {
			board.remove(suicide);
		}
		
	}
	
	/**
	 * Method to set the current player for acknowledge move message.
	 * @param i - Integer representing new player
	 */
	public void setCurrentPlayer(int i) {
		this.currentPlayerAckn = i;
	}
	
	/**
	 * Handles captures.
	 * @param c - Color of potentially captured party
	 * @param lastSet - index representing last set
	 */
	public void handleCapture(Color c, int lastSet) {
		
		
		//Only look at direct neighbours of the last set
		ArrayList<ArrayList<Integer>> groepen = new ArrayList<>();
		ArrayList<Integer> fieldsToBeChecked = new ArrayList<Integer>();
		
		//get neighbours from last set
		for (int j = 0; j < board.getCurrentNeighColor(lastSet).size(); j++) {
			//see whether they have the color of your opponent
			if (board.getCurrentNeighColor(lastSet).get(j).equals(c)) {	
				//add these stones to the fieldsToBeChecked
				fieldsToBeChecked.add(board.getCurrentNeighIndex(lastSet).get(j));
			}
		}	
		
		for (int i = 0; i < fieldsToBeChecked.size(); i++) { 
			//find the groups for the fields to be checked
			ArrayList<Integer> r = new ArrayList<Integer>();
			board.getGroup(fieldsToBeChecked.get(i), c, r);
			groepen.add(r);
				
		}
		
		for (ArrayList<Integer> a:groepen) {
			if (board.isCaptured(Color.EMPTY, a)) {
				board.remove(a);
			}
		}
	}
}
