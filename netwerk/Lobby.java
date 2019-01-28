package netwerk;

import java.util.ArrayList;
import java.util.HashMap;

import go.Color;
import go.Game;
import go.HumanPlayer;
import go.OnlineGame;
import go.Player;

public class Lobby {

	private int gameID;
	private int dim;
	private ArrayList<ClientHandler> handlers;
	private OnlineGame game;
	private HashMap<String, Integer> players = new HashMap<>();
	private int[] currentMove;
	private boolean config = false;
	private int rematch;
	private Color[] colors = new Color[2];
	private GameState gameState;
	private boolean gameStarted = false;
	private boolean rematchSet = false;
	private int rematchCounter = 0;
	//hier moet een spel gestart
	public Lobby(int gameID) {
		this.gameID = gameID;
		handlers = new ArrayList<>();
		gameState = new GameState();
	}
	
	public synchronized GameState getGameState() {
		return this.gameState;
	}
	
	
	
	/***
	 * starts the game
	 */
	public void startGame() {
		gameStarted = true;
		//gameState.setState("MOVE+FIRST");
		players.put(handlers.get(1).getClientName(), Color.getNr(colors[1])); //niet meer nodig want je zet de color bij beiden
		game = new OnlineGame(dim, this, handlers.get(0).getPlayer(), handlers.get(1).getPlayer());
		
		for (ClientHandler ch:handlers) { // CH moeten nog ackn config krijgen
			ch.ackn_config();
		}
		
		game.start();
		
	}
	
	public void startRematch() {
		this.game = new OnlineGame(dim, this, handlers.get(0).getPlayer(), handlers.get(1).getPlayer());
		for (ClientHandler ch:handlers) { // CH moeten nog ackn config krijgen
			ch.ackn_config();
		}
		
		game.start();
	}
 
	public synchronized void setRematch(boolean b) {
		if (b) {
			this.rematch++;
		}
		rematchCounter++;
		System.out.println("Rematch[] "+ rematch);
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
	 * set color for first player (who could utter preference) and also set color for second player
	 * @param name
	 * @param c
	 */
	public synchronized void setColor(String name, Color c) {	
		players.put(name, Color.getNr(c));
		
		
		if (!config) { //als de eerste player zijn kleuren nog niet heeft doorgegeven, anders willen we dit niet nog een keer uitvoeren
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
	 * if the player with name 'name' has color black, then he/she starts otherwise, return false
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
		if (handlers.size() == 2) {
			return true;
		}
		return false;
	}
	
	
	/***
	 * add client and return true if successful
	 * @return
	 */
	public synchronized boolean addClient(ClientHandler ch) { 
		if (handlers.size() < 2) {
			handlers.add(ch);
			return true;
		}
		return false;
	}
	
	
    public String getStatus() { //$STATUS(PLAYING, WAITING, FINISHED;$CURRENT_PLAYER int color of player that should make move;$BOARD String of fields
    	String board = game.getBoardString();
    	int currentPlayer = game.getCurrentPlayer();
    	
    	
    	
    	//if (this.gameState.getState().equals("MOVE+FIRST")) {
    	//	currentPlayer = 1;
    	//} else {
    	//	currentPlayer = 2;
    	//}
    	
    	if (!game.gameOver()) { //dit klopt
    		return "PLAYING;"+currentPlayer+";"+board;
    	}
    	return "FINISHED;"+currentPlayer+";"+board;
    	
    }
    
    public String getOpponentName(String playerName) {
        if (handlers.get(0).getClientName().equals(playerName)) {
        	return handlers.get(1).getClientName();	
        }		
    	return handlers.get(0).getClientName();
    }
    
    /***
     * sends a message to all participating players
     */
    public void broadcast(String msg) {
    	for (ClientHandler c:handlers) {
    		c.sendMessage(msg);
    	}
    }
    
    /***
     * 
     * @param playerName
     * @return color of player
     */
    public Color getColor(String playerName) {
    	return Color.getColor(players.get(playerName));
    }
    
    /***
     * Can be used by clientHandler to communicate current move
     * @param move that current player wants to make
     */
    public void makeMove(String move) {
    	String temp[] = move.split(";");
    	currentMove[0] = Integer.parseInt(temp[0]);
    	currentMove[1] = Integer.parseInt(temp[1]);
    }
    
    /***
     * can be used by game to get current move
     * @return
     */
    public int[] getMove(int i) {
    	return currentMove;
    }
	
    public Color[] getColors() {
    	return colors;
    }
    
    
}
