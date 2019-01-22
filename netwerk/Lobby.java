package netwerk;

import java.util.ArrayList;

import go.Color;
import go.Player;

public class Lobby {

	private int gameID;
	private int dim;
	private ArrayList<ClientHandler> handlers;
	private ArrayList<Player> players;
	private Color colorFirst;
	
	//hier moet een spel gestart
	public Lobby(int gameID) {
		this.gameID = gameID;
		players = new ArrayList<>();
		handlers = new ArrayList<>();
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
	
	
    public String getBoardStatus(int gameID) {//TODO
        
    	return "";
    }
    
    public String getOpponentName(int gameID) {//TODO
        
    	return "";
    }
    
    
    
	
}
