package netwerk;

import java.util.ArrayList;

import go.Color;
import go.Game;
import go.HumanPlayer;
import go.Player;

public class Lobby {

	private int gameID;
	private int dim;
	private ArrayList<ClientHandler> handlers;
	private Color colorFirst;
	private Game game;
	
	//hier moet een spel gestart
	public Lobby(int gameID) {
		this.gameID = gameID;
		handlers = new ArrayList<>();
	}
	
	
	public void startGame() {
		Player p1 = new HumanPlayer(handlers.get(0).getName(), colorFirst);
		Player p2 = new HumanPlayer(handlers.get(1).getName(), Color.getOther(colorFirst));
		game = new Game(dim, p1, p2);
		game.start();
	}
	
	
	public void setDim(int i) {
		dim = i;
	}
	
	public void setColorFirst(Color c) {
		colorFirst = c;
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
	
	
    public String getBoardStatus() {
        
    	return "";
    }
    
    public String getOpponentName(String playerName) {
        if (handlers.get(0).getName().equals(playerName)) {
        	return handlers.get(1).getName();	
        }		
    	return handlers.get(0).getName();
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
     * @return
     */
    public Color getColor(String playerName) {
    	return null;
    }
    
	
}
