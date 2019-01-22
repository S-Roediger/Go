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
	Color first;
	private Color[] colors = new Color[2];
	
	//hier moet een spel gestart
	public Lobby(int gameID) {
		this.gameID = gameID;
		handlers = new ArrayList<>();
	}
	
	/***
	 * starts the game
	 */
	public void startGame() {
		
		players.put(handlers.get(1).getClientName(), Color.getNr(colors[1]));
		game = new OnlineGame(dim, this);
		game.start();
	}
	
	public void updateLobby() {
		String status = game.getStatus();
		// the lobby can broadcast all changes to both clients
	}
	
	public boolean getConfig() {
		return config;
	}
	
	public void setDim(int i) {
		dim = i;
	}
	
	/***
	 * set color for first player (who could utter preference) and also set color for second player
	 * @param name
	 * @param c
	 */
	public void setColor(String name, Color c) {	
		players.put(name, Color.getNr(c));
		config = true;
		colors[0] = c;
		colors[1] = Color.getOther(c);
	}
	
	public int getGameID() {
		return gameID;
	}
	
	
	public boolean isLeader(ClientHandler ch) {
		if (handlers.get(0) == ch) {
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
	public boolean addClient(ClientHandler ch) { 
		if (handlers.size() < 2) {
			handlers.add(ch);
			return true;
		}
		return false;
	}
	
	
    public String getStatus() { //$STATUS(PLAYING, WAITING, FINISHED;$CURRENT_PLAYER int color of player that should make move;$BOARD String of fields
    	//return game.getStatus();
    	return "";
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
