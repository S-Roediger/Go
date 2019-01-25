package netwerk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import go.Board;
import go.Color;
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
	private Board board;
	private String winner;
	private String[] score;
	private boolean rematch = false;
	
	public ClientInputHandler(Client client) {
		this.client = client;
	}
	
	public String checkInput(String[] args) {
		
		//String[] args = input.split("\\+");
		
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
			if (args[1].equals(client.getClientName())) {
				color = Color.getColor(Integer.parseInt(args[2]));
				boardSize = Integer.parseInt(args[3]);
				String gameState = args[4];
				currentGameState = client.parseGameState(gameState); //$STATUS;$CURRENT_PLAYER;$SCORE;$BOARD
				opponent = args[5];
			}
			
			System.out.println(opponent + " has joined to play with you. \r" +
					"Your name is " + client.getClientName() + "\r" +
					"You will be playing on a "+ boardSize+" by "+boardSize+" board. \r"+
					"Your color will be " + color +"."+ "\r" +
					"Now GET READY, because the game is about to start!");
						
			tui = new TUI();
			board = new Board(boardSize, currentGameState[2]);
			tui.showGame(board);
			
			
			if (currentGameState[0].equals("PLAYING") && Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
				userInput = readString("Please enter a move (index) or pass with '-1' \r Enter 'EXIT' to exit the game");
				if (!userInput.equals("EXIT")) {
					lastMove = Integer.parseInt(userInput);
					return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+lastMove;
				} else {
					return "EXIT+"+GAME_ID+"+"+client.getClientName();
				}

			}
			
			break;
			
		case "ACKNOWLEDGE_MOVE":
			
			String gameState = args[3];
			currentGameState = client.parseGameState(gameState); //$STATUS;$CURRENT_PLAYER;$BOARD
				
			board.update(currentGameState[2]); //elke keer bij ackn move moet je board updaten
			tui.showGame(board);
			
			if (currentGameState[0].equals("PLAYING") && Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
				userInput = readString("Please enter a move (index) or pass with '-1' \r Enter 'EXIT' to exit the game");
				if (!userInput.equals("EXIT")) {
					lastMove = Integer.parseInt(userInput);
					return "MOVE+" +GAME_ID+"+"+client.getClientName()+"+"+lastMove;
				} else {
					return "EXIT+"+GAME_ID+"+"+client.getClientName();
				}

			} 
			break;
			
		case "ACKNOWLEDGE_REMATCH":
			if (Integer.parseInt(args[1]) == 1) {
				rematch = true;
			} else {
				rematch = false;
			}
			return null;
			
		case "INVALID_MOVE":
			// handle this
			break;
			
		case "UNKNOWN_COMMAND":
			// handle this
			break;
			
		case "GAME_FINISHED":
			winner = args[2];
			score = client.parseGameState(args[3]);
			System.out.println(winner + " has won. \r Black has " + score[0] + " points. \r White has " + score[1] + " points.");
			
			
			return null;
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
