package netwerk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import go.Board;
import go.Color;
import view.GUI;
import view.TUI;

public class ClientInputHandler {

	private int GAME_ID;
	private boolean isLeader;
	private Client client;
	private Color color;
	private int boardSize;
	private String opponent;
	private String[] currentGameState;
	private int lastMove;
	private TUI tui;
	private GUI gui;
	private Board board;
	private String winner;
	private String[] score;
	private boolean rematch = false;
	private int hint;
	private int invalidCounter;
	
	public ClientInputHandler(Client client) {
		this.client = client;
	}
	
	public String checkInput(String[] args) {
		
		
		
		switch (args[0]) {
		
		case "ACKNOWLEDGE_HANDSHAKE":
			GAME_ID = Integer.parseInt(args[1]);
			if (args[2].equals("0")) {
				isLeader = false;
			} else {
				isLeader = true;
			}
			return null;
			
			
		case "REQUEST_CONFIG":
			
			String userInput = readString(args[1]); 
			String[] userInputSplit = userInput.split(" "); 
			int tempColor = 0;
			
			
			//while (!userInputSplit[0].equals("white") || !userInputSplit[0].equals("black")) { //&& userInputSplit[1].matches("\\d+")
			//	userInput = readString("Your previous command was unknown, please retry. \r"+args[1]); 
			//	userInputSplit = userInput.split(" "); 
			//}
			if (userInputSplit[0].equals("white")) {
				tempColor = 2;
			} else {
				tempColor = 1;
			}
		
			

			
			return "SET_CONFIG+"+GAME_ID+"+"+tempColor+"+"+userInputSplit[1];	
			
			
			
		case "REQUEST_REMATCH":
			userInput = readString("Do you wish a rematch (Yes/No)?");
			if (userInput.equals("Yes")) {
				return "SET_REMATCH+1";
			} else {
				return "SET_REMATCH+0";
			}
			
			
		case "ACKNOWLEDGE_CONFIG":
				client.setClientName(args[1]);
				color = Color.getColor(Integer.parseInt(args[2]));
				boardSize = Integer.parseInt(args[3]);
				String gameState = args[4];
				currentGameState = client.parseGameState(gameState); //$STATUS;$CURRENT_PLAYER;$SCORE;$BOARD
				opponent = args[5];
			
			
			System.out.println(opponent + " has joined to play with you. \r" +
					"Your name is " + client.getClientName() + "\r" +
					"You will be playing on a "+ boardSize+" by "+boardSize+" board. \r"+
					"Your color will be " + color +"."+ "\r" +
					"Now GET READY, because the game is about to start!");
				
			if (!rematch) {
				tui = new TUI();
				board = new Board(boardSize, currentGameState[2]);
				gui = new GUI(boardSize);
			}

			
			board.update(currentGameState[2]);
			tui.showGame(board);
			gui.update(currentGameState[2]);
			
			
			
			if (currentGameState[0].equals("PLAYING") && Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
				
				
				if (this.client.getClientName().contains("ComputerPlayer")) { 
					return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+board.determineRandomValidMove(color, 3000);
				} else {
					System.out.println("Please enter a move (index) or pass with '-1' \r Enter 'EXIT' to exit the game");
					System.out.println("(HINT: The following would be a valid move: " + board.determineRandomValidMove(color, 3000) + ")");
					userInput = readString("");
					if (!userInput.equals("EXIT")) {
						lastMove = Integer.parseInt(userInput);
						return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+lastMove;
					} else {
						return "EXIT+"+GAME_ID+"+"+client.getClientName();
					}
				}

			}
			
			break;
			
		case "ACKNOWLEDGE_MOVE":
			
			gameState = args[3];
			currentGameState = client.parseGameState(gameState); //$STATUS;$CURRENT_PLAYER;$BOARD
				
			board.update(currentGameState[2]); //elke keer bij ackn move moet je board updaten
			tui.showGame(board);
			gui.update(currentGameState[2]);
			
			if (currentGameState[0].equals("PLAYING") && Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
				
				if (this.client.getClientName().contains("ComputerPlayer")) { //if Computer Player do random move, does it take previous board states into consideration though?
					return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+board.determineRandomValidMove(color, 3000);
				} else {
					System.out.println("Please enter a move (index) or pass with '-1' \r Enter 'EXIT' to exit the game");
					hint = board.determineRandomValidMove(color, 3000);
					System.out.println("(HINT: The following would be a valid move: " + hint + ")");
					gui.addHintIndicator(hint);
					userInput = readString("");
					if (!userInput.equals("EXIT")) {
						lastMove = Integer.parseInt(userInput);
						return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+lastMove;
					} else {
						return "EXIT+"+GAME_ID+"+"+client.getClientName();
					}
				}
			} 
			break;
			
		case "ACKNOWLEDGE_REMATCH":
			if (Integer.parseInt(args[1]) == 1) {
				rematch = true;
				gui.clearBoard();
				System.out.println("You both agreed to have a rematch! \r");
				
			} else {
				rematch = false;
			}
			return null;
			
		case "INVALID_MOVE":
			
			if (currentGameState[0].equals("PLAYING") && Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
				
				if (this.client.getClientName().contains("ComputerPlayer")) { 
					if (invalidCounter > 10) {
						return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+"-1";
					} else {
						invalidCounter++;
						return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+board.determineRandomValidMove(color, 3000);
					}
					
					
				} else {
					System.out.println(args[1] + "\r");
					System.out.println("Please enter another move (index) or pass with '-1' \r Enter 'EXIT' to exit the game");
					gui.removeHintIndicator();
					hint = board.determineRandomValidMove(color, 3000);
					System.out.println("(HINT: The following would be a valid move: " + hint + ")");
					gui.addHintIndicator(hint);
					userInput = readString("");
					if (!userInput.equals("EXIT")) {
						lastMove = Integer.parseInt(userInput);
						return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+lastMove;
					} else {
						return "EXIT+"+GAME_ID+"+"+client.getClientName();
					}
				}
			}
			break;
			
		case "UNKNOWN_COMMAND":
			if (currentGameState[0].equals("PLAYING") && Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
				
				if (this.client.getClientName().contains("ComputerPlayer")) { //if Computer Player do random move, does it take previous board states into consideration though?
					return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+board.determineRandomValidMove(color, 3000);
				} else {
					System.out.println("You just entered an unkown command. \r");
					System.out.println("Please enter another move (index) or pass with '-1' \r Enter 'EXIT' to exit the game");
					gui.removeHintIndicator();
					hint = board.determineRandomValidMove(color, 3000);
					System.out.println("(HINT: The following would be a valid move: " + hint + ")");
					gui.addHintIndicator(hint);
					userInput = readString("");
					if (!userInput.equals("EXIT")) {
						lastMove = Integer.parseInt(userInput);
						return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+lastMove;
					} else {
						return "EXIT+"+GAME_ID+"+"+client.getClientName();
					}
				}
			}
			break;
			
		case "GAME_FINISHED":
			winner = args[2];
			score = client.parseGameState(args[3]);
			System.out.println(winner + " has won. \r"+"Black has " + score[0] + " points. \r"+"White has " + score[1] + " points. \r" + args[4]);
			
			
			break;
		}
		return null;
	}
	
	public static String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}
	
}
