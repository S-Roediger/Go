package go;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import view.GUI;
import view.TUI;

public class Game {
/***
 * Hier komt alles bij elkaar - Controller class
 */
	
	private Board board;
	private Player[] players;
	private TUI tui = new TUI();
	private int current;
	private boolean again = true;
	private GUI gui;
	
	// ----------- Constructor ---------------- //
	
	
	/***
	 * Constructor of the game, creates a new game object
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
	 * Resets the board and starts a new game
	 * While loop to be able to keep going after game ended
	 */
	public void start() {
		while (again) {
			reset();
			play();
			again = readBoolean("\r"+"Do you want to play again? (Yes/No)", "Yes", "No");
		}

	}
	
	/***
	 * Reset the board for new game
	 */
	public void reset() {
		board.reset();
		current = 0;
		gui.clearBoard();
	}
	
	/***
	 * game loop:
	 * updates view, enforces rules and makes move of players
	 * 
	 */
	public void play() {
		int choice = 0;
		while(!board.gameOver()) {
			tui.showGame(board);
			//updateGui();
	
			//check rules!
			
			choice = players[current].determineMove(); //get player choices
			while (!board.isValidMove(choice, players[current].getColor())) { //check whether field is empty, on board and != recreate prevBoardState
				System.out.println("ERROR: field " + choice + " is no valid choice."); //loop to ask again in case of faulty input
				choice = players[current].determineMove();
			}		
			if (choice == -1) { 				// enforce pass rule
				board.increasePass();
				System.out.println("\r " + players[current].getName() + " has passed." + "\r");
			} else {
				players[current].makeMove(board, choice);
				gui.addStone(choice, players[current].getColor());
				gui.addHintIndicator(choice);
				handleCapture(Color.getOther(players[current].getColor()), choice); // je checkt eerst of jouw move een ander heeft gecaptured
				handleCapture(players[current].getColor(), choice);		// 	is dat uberhaupt logisch? Kan de huidige player gecaptured worden in eigen zet?	|	en dan kijk je naar suicide
				handleSuicide(players[current].getColor(), choice); //je kijkt of je eigen steen suicide gepleegt heeft
				board.resetPass();
			}
			current = (current + 3) % 2;
			
		}
		System.out.println("\r" + "The game is over." + "\r");
		printResult();
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
			gui.removeStone(suicide);
			System.out.println(c+" has commited suicide on field " + lastSet);
		}
		
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
				gui.removeStone(a);
				System.out.println(c+" was captured! The following fields are removed "+a);
			}
		}
	}
	
	public void printResult() {
		double[] score = board.getScore();
		System.out.println("Black has the following amount of points: "+score[0] +"\r" + "White has the following amount of points: "+ score[1]);
		if (score[0] > score[1]) {
			System.out.println("Black has won!");
		} else if (score[0] < score[1]) {
			System.out.println("White has won!");
		} else if (score[0] == score[1]) {
			System.out.println("There is a draw! Both players win!");
		}
	}
	
	private boolean readBoolean(String prompt, String yes, String no) {
        String answer;
        Scanner in;
        do {
            System.out.print(prompt);
            in = new Scanner(System.in);
            answer = in.hasNextLine() ? in.nextLine() : null;
        } while (answer == null || (!answer.equals(yes) && !answer.equals(no)));
        return answer.equals(yes);
    }
	
	
	/***
	 * Returns the board so that I can manipulate it in the BoardTest class
	 * @return board of current game
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/***
	 * update GUI
	 */
	public void updateGui() {
		gui.clearBoard();
		for (int i = 0; i < board.getFields().length; i++) {
			if (board.getFields()[i].equals(Color.WHITE)) {
				gui.addStone(i, Color.WHITE);
			} else if (board.getFields()[i].equals(Color.BLACK)) {
				gui.addStone(i, Color.BLACK);
			}
		}
	
	}
}
