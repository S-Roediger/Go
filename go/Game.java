package go;

import java.util.ArrayList;
import java.util.Scanner;

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
	
	
	// ----------- Constructor ---------------- //
	
	
	/***
	 * Constructor of the game, creates a new game object
	 * @param board
	 * @param s0
	 * @param s1
	 */
	public Game(int dim, Player s0, Player s1) {
		this.board = new Board(dim);
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
			// do I need to make this with threads? You want to be able to host multiple games? TODO
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
				handleCapture(Color.getOther(players[current].getColor())); // je checkt eerst of jouw move een ander heeft gecaptured
				//handleCapture(players[current].getColor());		// 	is dat uberhaupt logisch? Kan de huidige player gecaptured worden in eigen zet?	|	en dan kijk je naar suicide
				
				
			}
			current = (current + 3) % 2;
			
		}
		System.out.println("\r" + "The game is over." + "\r");
		//printResult()?
	}
	
	public void handleCapture(Color c) {
		ArrayList<Integer> r = new ArrayList<Integer>();
		for (int i = 0; i < board.getFields().length; i++) { //check of er een capture is: loop alle fields van het board af
			if (board.getField(i).equals(c)) { //check of het field wat je bekijkt de kleur heeft voor die je wilt checken
				board.setAmountGroupMembers(board.getNrGroupMembers(i, c));
				if (board.isCaptured(i, c, board.getNrGroupMembers(i, c))) {
					r.add(i);
					System.out.println(c + " is captured at intersection " + i);
				}
			}
		
		}
		board.remove(r); //remove de captured stenen
		
	}
	
	public void printResult() {
		//print something
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
	
}
