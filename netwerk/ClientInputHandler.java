package netwerk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import go.Board;
import go.Color;
import view.GUI;
import view.TUI;

public class ClientInputHandler {

	// --------------- Fields ---------------- //
	
	private int gameID;
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
	private int invalidCounter = 0;
	
	// --------------- Constructor ------------ //
	
	/**
	 * Constructs a clientInputHandler object.
	 * @param client - client for whom this object handles the input
	 */
	public ClientInputHandler(Client client) {
		this.client = client;
	}
	
	// -------------- Command ---------------- // 
	
	/**
	 * Method that checks the server input for possible commands.
	 * @param args - String array containing server input
	 * @return String - representing message that client can send to server
	 */
	public String checkInput(String[] args) {
	
		switch (args[0]) {

			case "ACKNOWLEDGE_HANDSHAKE":
				gameID = Integer.parseInt(args[1]);
				break;

			case "REQUEST_CONFIG":
	
				String userInput = readString(args[1]);
				String[] userInputSplit = userInput.split(" ");
				int tempColor = 0;
	
				if (userInputSplit[0].equals("white")) {
					tempColor = 2;
				} else {
					tempColor = 1;
				}
	
				return "SET_CONFIG+" + gameID + "+" + tempColor + "+" + userInputSplit[1];

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
				currentGameState = client.parseGameState(gameState);
				opponent = args[5];
	
				System.out.println(opponent + " has joined to play with you. \r" 
						+ "Your name is " + client.getClientName()
							+ "\r" + "You will be playing on a " 
								+ boardSize + " by " + boardSize + " board. \r"
									+ "Your color will be " + color + "." + "\r"
										+ "Now GET READY, because the game is about to start!");
	
				if (!rematch) {
					tui = new TUI();
					board = new Board(boardSize, currentGameState[2]);
					gui = new GUI(boardSize);
				}
	
				board.update(currentGameState[2]);
				tui.showGame(board);
				gui.update(currentGameState[2]);
	
				if (currentGameState[0].equals("PLAYING")
						&& Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
	
					if (this.client.getClientName().contains("ComputerPlayer")) {
						return "MOVE+" + gameID + "+" + client.getClientName() + "+"
								+ board.determineRandomValidMove(color, 3000);
					} else {
						System.out.println("Please enter a move (index)"
								+ " or pass with '-1' \r Enter 'EXIT' to exit the game");
						System.out.println("(HINT: The following would be a valid move: "
								+ board.determineRandomValidMove(color, 3000) + ")");
						userInput = readString("");
						if (!userInput.equals("EXIT")) {
							lastMove = Integer.parseInt(userInput);
							return "MOVE+" + gameID + "+" + client.getClientName() + "+" + lastMove;
						} else {
							return "EXIT+" + gameID + "+" + client.getClientName();
						}
					}
	
				}
	
				break;

			case "ACKNOWLEDGE_MOVE":
	
				gameState = args[3];
				currentGameState = client.parseGameState(gameState); 
	
				board.update(currentGameState[2]);
				tui.showGame(board);
				gui.update(currentGameState[2]);
	
				if (currentGameState[0].equals("PLAYING")
						&& Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
	
					if (this.client.getClientName().contains("ComputerPlayer")) {
						return "MOVE+" + gameID + "+" + client.getClientName() + "+"
								+ board.determineRandomValidMove(color, 3000);
					} else {
						System.out.println("Please enter a move (index) or"
								+ " pass with '-1' \r Enter 'EXIT' to exit the game");
						hint = board.determineRandomValidMove(color, 3000);
						System.out.println("(HINT: The following would be a "
								+ "valid move: " + hint + ")");
						gui.addHintIndicator(hint);
						userInput = readString("");
						if (!userInput.equals("EXIT")) {
							lastMove = Integer.parseInt(userInput);
							return "MOVE+" + gameID + "+" + client.getClientName() + "+" + lastMove;
						} else {
							return "EXIT+" + gameID + "+" + client.getClientName();
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
	
				if (currentGameState[0].equals("PLAYING")
						&& Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
	
					if (this.client.getClientName().contains("ComputerPlayer")) {
						if (invalidCounter > 10) {
							return "MOVE+" + gameID + "+" + client.getClientName() + "+" + "-1";
						} else {
							invalidCounter++;
							return "MOVE+" + gameID + "+" + client.getClientName() + "+"
									+ board.determineRandomValidMove(color, 3000);
						}
	
					} else {
						System.out.println(args[1] + "\r");
						System.out.println("Please enter another move (index) or "
								+ "pass with '-1' \r Enter 'EXIT' to exit the game");
						gui.removeHintIndicator();
						hint = board.determineRandomValidMove(color, 3000);
						System.out.println("(HINT: The following would be"
								+ " a valid move: " + hint + ")");
						gui.addHintIndicator(hint);
						userInput = readString("");
						if (!userInput.equals("EXIT")) {
							lastMove = Integer.parseInt(userInput);
							return "MOVE+" + gameID + "+" + client.getClientName() + "+" + lastMove;
						} else {
							return "EXIT+" + gameID + "+" + client.getClientName();
						}
					}
				}
				break;

			case "UNKNOWN_COMMAND":
				if (currentGameState[0].equals("PLAYING")
						&& Integer.parseInt(currentGameState[1]) == (Color.getNr(color))) {
	
					if (this.client.getClientName().contains("ComputerPlayer")) {
						return "MOVE+" + gameID + "+" + client.getClientName() + "+"
								+ board.determineRandomValidMove(color, 3000);
					} else {
						System.out.println("You just entered an unkown command. \r");
						System.out.println("Please enter another move (index) or"
								+ " pass with '-1' \r Enter 'EXIT' to exit the game");
						gui.removeHintIndicator();
						hint = board.determineRandomValidMove(color, 3000);
						System.out.println("(HINT: The following would be "
								+ "a valid move: " + hint + ")");
						gui.addHintIndicator(hint);
						userInput = readString("");
						if (!userInput.equals("EXIT")) {
							lastMove = Integer.parseInt(userInput);
							return "MOVE+" + gameID + "+" + client.getClientName() + "+" + lastMove;
						} else {
							return "EXIT+" + gameID + "+" + client.getClientName();
						}
					}
				}
				break;

			case "GAME_FINISHED":
				winner = args[2];
				score = client.parseGameState(args[3]);
				System.out.println(winner + " has won. \r" + "Black has " 
						+ score[0] + " points. \r" + "White has "
							+ score[1] + " points. \r" + args[4]);
	
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
