package go;

import java.util.ArrayList;
import java.util.Scanner;

import netwerk.Lobby;
import view.TUI;

public class OnlineGame extends Thread {

	
	private Board board;
	int current;
	Player[] players;
	Lobby lobby;
	String status;
	int dim;
	int currentPlayerAckn = 1;
	int playerWhoMadeLastMove;
	
	public OnlineGame(int dim, Lobby lobby, Player p0, Player p1) {
		board = new Board(dim);
		this.dim = dim;
		current = 0;
		players = new Player[2];
		if (p1.getColor().equals(Color.BLACK)) { //enforces rule that black starts
			players[0] = p1;
			players[1] = p0;
		} else {
			players[0] = p0;
			players[1] = p1;
		}
		
		this.lobby = lobby;
		
		
	}
	
	
	/***
	 * Starts a new game, can potentially be used with while loop to start new game when recent game ended
	 * TODO
	 */
	public void start() {
		play();
	}
	
	/***
	 * keeps track of game
	 */
	public void play() {
		int choice = 0;
		while(!board.gameOver()) {
			
			choice = players[current].determineMove(); //get player choices
			while (!board.isValidMove(choice, players[current].getColor())) { //check whether field is empty, on board and != recreate prevBoardState
				lobby.broadcast("INVALID_MOVE+Invalid move");; //loop to ask again in case of faulty input
				choice = players[current].determineMove();
			}		
			if (choice == -1) { 				// enforce pass rule
				board.increasePass();
			} else {
				players[current].makeMove(board, choice);
				handleCapture(Color.getOther(players[current].getColor()), choice); // je checkt eerst of jouw move een ander heeft gecaptured
				handleCapture(players[current].getColor(), choice);		// 	is dat uberhaupt logisch? Kan de huidige player gecaptured worden in eigen zet?	|	en dan kijk je naar suicide
				handleSuicide(players[current].getColor(), choice); //je kijkt of je eigen steen suicide gepleegt heeft
				board.resetPass();
			}
			playerWhoMadeLastMove = current +1; //player who made most recent move, needed for protocol
			current = (current + 3) % 2;
			this.currentPlayerAckn = current + 1;
			lobby.broadcast("ACKNOWLEDGE_MOVE+"+lobby.getGameID()+"+"+choice+";"+playerWhoMadeLastMove+"+"+lobby.getStatus());
			
		}
		//lobby.broadcast("ACKNOWLEDGE_MOVE+"+lobby.getGameID()+"+"+choice+";"+playerWhoMadeLastMove+"+"+lobby.getStatus()); //dit is nodig om laatste move nog te ackn met gameStatus: FINISHED
		lobby.broadcast("GAME_FINISHED+"+lobby.getGameID()+"+"+getWinner()+"+"+getScore()+"+The game has ended since both players passed");
	}
		
	/***
	 * Handles suicide
	 * @param c
	 * @param lastSet
	 */
	public void handleSuicide(Color c, int lastSet) {
		ArrayList<Integer> suicide = new ArrayList<Integer>();
		suicide.add(lastSet);
		if (board.isCaptured(Color.getOther(players[current].getColor()), suicide)) {
			board.remove(suicide);
		}
		
	}
	
	public int getCurrentPlayer() {
		return this.currentPlayerAckn;
	}
	
	
	public void handleCapture(Color c, int lastSet) {
		
		
		// bekijk alleen alle neigbours van de laatste zet, anders wordt het computationally misschien te zwaar
		
		ArrayList<ArrayList<Integer>> groepen = new ArrayList<>();
		ArrayList<Integer> fieldsToBeChecked = new ArrayList<Integer>();
		
		// ----- nieuwe opzet op basis van nieuwe manier van neigh fixen ---
		
		//board.updateCurrentNeighbours(lastSet);
		
		for (int j = 0; j < board.getCurrentNeighColor(lastSet).size(); j++) {	// krijg de buren van je laatste zet
			if (board.getCurrentNeighColor(lastSet).get(j).equals(c)) {	//kijk of die de kleur van je opponent hebben
				fieldsToBeChecked.add(board.getCurrentNeighIndex(lastSet).get(j));	//voeg de index van de stenen van je opponent toe
			}
		}	
		
		for (int i = 0; i < fieldsToBeChecked.size(); i++) { //vind de groepen voor de fieldsToBeChecked
			
			ArrayList<Integer> r = new ArrayList<Integer>();
			board.getGroup(fieldsToBeChecked.get(i), c, r);
			groepen.add(r);
				
		}
		
		for (ArrayList<Integer> a:groepen) {
			if (board.isCaptured(Color.EMPTY, a)) {
				board.remove(a);
				//gui.removeStone(a);
				//System.out.println(c+" was captured! The following fields are removed "+a);
			}
		}
	}
	
	public String getWinner() {
		double[] score = board.getScore();
		//System.out.println("Black has the following amount of points: "+score[0] +"\r" + "White has the following amount of points: "+ score[1]);
		if (score[0] > score[1]) {
			//System.out.println("Black has won!");
			return players[0].getName();
		} else if (score[0] < score[1]) {
			//System.out.println("White has won!");
			return players[1].getName();
		} else if (score[0] == score[1]) {
			return "draw"; //TODO what to do in case of draw? WE DO NOT HAVE A DRAW EVER SINCE WHITE GETS .5 POINTS AT THE END AND IN GAME YOU CANNOT EARN HALF POINTS
			//System.out.println("There is a draw! Both players win!");
		}
		return "";
	}
	
	
	public String getScore() {
		double[] score = board.getScore();
		String result = score[0]+";"+score[1];
		return result;
	}
	
	public synchronized String getBoardString() {
		Color[] fieldsCopy = board.getFields();
		String a = "";
		for (int i = 0; i < fieldsCopy.length; i++) {
			a += Color.getNr(fieldsCopy[i]);
		}
		return a;
	}
		
	public boolean gameOver() {
		return board.gameOver();
	}
	
	
	
}