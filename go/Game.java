package go;

import java.util.ArrayList;
import java.util.Scanner;

import view.GUI;
import view.TUI;

public class Game {
/***
 * Controller class - Offline Game.
 */
	
	// ------------ Fields ------------------- //
	
	private Board board;
	private Player[] players;
	private TUI tui = new TUI();
	private int current;
	private boolean again = true;
	private GUI gui;
	
	// ----------- Constructor ---------------- //
	
	
	/***
	 * Constructor of the game, creates a new game object.
	 * @param board
	 * @param s0
	 * @param s1
	 */
	public Game(int dim, Player s0, Player s1) {
		this.board = new Board(dim);
		gui = new GUI(dim);
		players = new Player[2];
		if (s0.getColor().equals(Color.BLACK)) { //enforces rule that black starts
			players[0] = s0;
			players[1] = s1;
		} else {
			players[0] = s1;
			players[1] = s0;
		}
		current = 0;
		
	}
	
	// ----------- Commands & Queries ----------- //
	
	
	/***
	 * Resets the board and starts a new game.
	 * While loop to be able to keep going after game ended
	 */
	public void start() {
		while (again) {
			reset();
			play();
			again = readBoolean("\r" + "Do you want to play again? (Yes/No)", "Yes", "No");
		}

	}
	
	/***
	 * Reset the board for new game.
	 */
	public void reset() {
		board.reset();
		current = 0;
		gui.clearBoard();
	}
	
	/***
	 * The game loop.
	 * updates view, enforces rules and makes move of players
	 * 
	 */
	public void play() {
		int choice = 0;
		while (!board.gameOver()) {
			tui.showGame(board);
			
			//get player choices
			choice = players[current].determineMove(); 
			//check whether field is empty, on board and != recreate prevBoardState
			while (!board.isValidMove(choice, players[current].getColor())) { 
				//loop to ask again in case of faulty input
				System.out.println("ERROR: field " + choice + " is no valid choice."); 
				choice = players[current].determineMove();
			}		
			if (choice == -1) { 				// enforce pass rule
				board.increasePass();
				System.out.println("\r " + players[current].getName() + " has passed." + "\r");
			} else {
				players[current].makeMove(board, choice);
				gui.addStone(choice, players[current].getColor());
				gui.addHintIndicator(choice);
				// je checkt eerst of jouw move een ander heeft gecaptured
				handleCapture(Color.getOther(players[current].getColor()), choice); 	
				handleCapture(players[current].getColor(), choice);
				//je kijkt of je eigen steen suicide gepleegt heeft
				handleSuicide(players[current].getColor(), choice); 
				board.resetPass();
			}
			current = (current + 3) % 2;
			
		}
		System.out.println("\r" + "The game is over." + "\r");
		printResult();
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
			for (int i:suicide) {
				gui.removeStone(i);	
			}
			
			System.out.println(c + " has commited suicide on field " + lastSet);
		}
		
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
				for (int i:a) {
					gui.removeStone(i);
				}
				
				System.out.println(c + " was captured! The following fields are removed " + a);
			}
		}
	}
	
	/**
	 * Method that prints the results at the end of the game.
	 */
	public void printResult() {
		double[] score = board.getScore();
		System.out.println("Black has the following amount of points: " + score[0] + "\r" +
				"White has the following amount of points: " + score[1]);
		if (score[0] > score[1]) {
			System.out.println("Black has won!");
		} else if (score[0] < score[1]) {
			System.out.println("White has won!");
		} else if (score[0] == score[1]) {
			System.out.println("There is a draw! Both players win!");
		}
	}
	
	/**
	 * Method to read a boolean.
	 * @param prompt
	 * @param yes
	 * @param no
	 * @return boolean corresponding to user input
	 */
	private boolean readBoolean(String prompt, String yes, String no) {
        String answer;
        Scanner in;
        do {
            System.out.print(prompt);
            in = new Scanner(System.in);
            answer = in.hasNextLine() ? in.nextLine() : null;
        } while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
        in.close();
        return answer.equals(yes);
    }
}
