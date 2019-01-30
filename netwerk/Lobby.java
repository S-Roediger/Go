package netwerk;

import java.util.ArrayList;
import java.util.HashMap;

import go.Color;

import go.OnlineGame;


public class Lobby {
	
	
	// ------------------ Fields ----------------- //

	private int gameID;
	private int dim;
	private ArrayList<ClientHandler> handlers;
	private OnlineGame game;
	private HashMap<String, Integer> players = new HashMap<>();
	private int[] currentMove;
	private boolean config = false;
	private int rematch;
	private Color[] colors = new Color[2];
	private boolean gameStarted = false;
	private boolean rematchSet = false;
	private int rematchCounter = 0;
	private int handshake = 0;
	
	
	// -------------- Constructor ----------------- //
	
	public Lobby(int gameID) {
		this.gameID = gameID;
		handlers = new ArrayList<>();	
	}
	
	
	// ------------- Commands & Queries ----------- //
	
	
	/**
	 * Method to increase handshake counter.
	 */
	public void increaseHandshake() {
		this.handshake++;
	}
	
	
	/***
	 * Starts the game.
	 */
	public void startGame() {
		gameStarted = true;
		
		players.put(handlers.get(1).getClientName(), Color.getNr(colors[1])); 
		game = new OnlineGame(dim, handlers.get(0).getPlayer(), handlers.get(1).getPlayer());
		
		for (ClientHandler ch:handlers) { 
			ch.acknConfig();
		}
		
		game.start();
		
	}
	
	/**
	 * Starts rematch.
	 */
	public void startRematch() {
		this.game = new OnlineGame(dim, handlers.get(0).getPlayer(), handlers.get(1).getPlayer());
		for (ClientHandler ch:handlers) { 
			ch.acknConfig();
		}
		game.start();
	}
 
	/**
	 * Sets rematch value.
	 * @param b - boolean, true if party agrees to rematch,
	 * otherwise false
	 */
	public synchronized void setRematch(boolean b) {
		if (b) {
			this.rematch++;
		}
		rematchCounter++;
		System.out.println("Rematch[] " + rematch);
		if (rematchCounter > 1) { //check whether both players have answered rematch_request
			rematchSet = true;
			
		}
	}
	
	public boolean getRematchSet() {
		return this.rematchSet;
	}
	
	public void setRematchSetFalse() {
		this.rematchSet = false;
	}
	
	/**
	 * Returns true if both parties agreed to rematch,
	 * otherwise false.
	 * @return true if rematch is wanted, otherwise false
	 */
	public boolean rematch() {
		if (rematch > 1) {
			this.rematch = 0;
			return true;
			
		}
		return false;
	}
	
	public OnlineGame getGame() {
		return this.game;
	}
	
	public synchronized boolean getGameStarted() {
		return this.gameStarted;
	}
	
	public synchronized boolean getConfig() {
		return config;
	}
	
	public synchronized void setDim(int i) {
		dim = i;
	}
	
	public synchronized int getDim() {
		return this.dim;
	}
	/***
	 * Set color for first player (who could utter preference) and also set color for second player.
	 * @param name
	 * @param c
	 */
	public synchronized void setColor(String name, Color c) {	
		players.put(name, Color.getNr(c));
		
		if (!config) { 
			colors[0] = c;
			colors[1] = Color.getOther(c);
		}
		
		config = true;

	}
	
	public synchronized int getGameID() {
		return gameID;
	}
	
	
	public synchronized boolean isLeader(ClientHandler ch) {
		if (handlers.get(0) == ch) {
			return true;
		}
		return false;
	}
	
	/***
	 * If the player with name 'name' has color black, then he/she starts otherwise, return false.
	 * @param name
	 * @return
	 */
	public boolean isFirstPlayer(String name) {
		if (players.get(name) == 1) {
			return true;
		}
		return false;
		
	}
	
	public boolean isFull() { 
		if (handlers.size() == 2 && handshake == 2) { 
			return true;
		}
		return false;
	}
	
	
	/***
	 * Add client and return true if successful.
	 * @return
	 */
	public synchronized boolean addClient(ClientHandler ch) { 
		if (handlers.size() < 2) {
			handlers.add(ch);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Returns current game status.
	 * @return String with delimiter ';', representing game status
	 */
    public String getStatus() {
    	String board = game.getBoardString();
    	int currentPlayer = game.getCurrentPlayer();
    	
    	if (!game.gameOver()) { 
    		return "PLAYING;" + currentPlayer + ";" + board;
    	}
    	return "FINISHED;" + currentPlayer + ";" + board;
    	
    }
    
    public String getOpponentName(String playerName) {
        if (handlers.get(0).getClientName().equals(playerName)) {
        	return handlers.get(1).getClientName();	
        }		
    	return handlers.get(0).getClientName();
    }
    
    /***
     * Sends a message to all participating players.
     */
    public void broadcast(String msg) {
    	for (ClientHandler c:handlers) {
    		c.sendMessage(msg);
    	}
    }
    
    /***
     * Method to obtain color of specific player.
     * @param playerName
     * @return color of player
     */
    public Color getColor(String playerName) {
    	return Color.getColor(players.get(playerName));
    }
    
    /***
     * Method used by clientHandler to communicate current move.
     * @param move that current player wants to make
     */
    public void makeMove(String move) {
    	String[] temp = move.split(";");
    	currentMove[0] = Integer.parseInt(temp[0]);
    	currentMove[1] = Integer.parseInt(temp[1]);
    }
    
    /***
     * Method used by game to get current move.
     * @return Integer representing current move
     */
    public int[] getMove(int i) {
    	return currentMove;
    }
	
    public Color[] getColors() {
    	return colors;
    }
    
    
}
