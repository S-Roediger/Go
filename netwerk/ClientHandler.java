package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import go.Color;
import go.NetwerkPlayer;
import go.Player;



public class ClientHandler extends Thread {
	
	
// ----------- Fields ----------- //	
	
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private Lobby lobby;  
    private ServerInputHandler sih;
    
    
// -------- Constructor ------------ //    
    

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     */
    public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
        this.server = serverArg;
        in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
        sih = new ServerInputHandler(this);
    }

 // --------- Commands & Queries ----------- //     
    
    
    /***
     * Method to read from input stream, used by player to read client response.
     * @return String containing user response
     */
    public String readFromIn() {
    	String a = "";
		try {
			a = in.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	return a;
    }
    
    /**
     * Method to obtain ServerInputHandler.
     * @return ServerInputHandler
     */
    public ServerInputHandler getSih() {
    	return this.sih;
    }
    
    /**
     * Method to add a lobby to the current clientHandler.
     * @param lobbie - Lobby that is about to be added
     */
    public void addLobbie(Lobby lobbie) {
    	this.lobby = lobbie;
    }
    
    /**
     * Method to obtain the lobby that is currently connected to this client handler.
     * @return Lobby
     */
    public Lobby getLobby() {
    	return this.lobby;
    }
    
    /***
     * Method to obtain game dimensions communicated by user.
     * @return Integer representing board dimensions
     */
    public int getDim() {
    	return sih.getDim();
    }
    
    /**
     * Method to make and obtain a player from the current Client Handler.
     * @return Player
     */
    public Player getPlayer() {
    	Player p = new NetwerkPlayer(sih.getClientName(), 
    			lobby.getColor(sih.getClientName()), this);
		return p;
    	
    }
    
    /**
     * Method to obtain the client name.
     * @return String representing the client name
     */
    public String getClientName() {
    	return sih.getClientName();
    }
    

    /**
     * This method takes care of sending messages from the Client.
     * The ServerInputHandler will check these messages for user commands.
     * If an NullPointerException or IOException is thrown while reading the message, the method 
     * concludes that the socket connection is broken and will announce the game finished
     * state. 
     */
    public void run() {
    	try {
    		boolean disconnected = false;
    		while (!disconnected) {
    			String line = in.readLine();
    			System.out.println(this.getClientName() + ": " + line);
    			String[] answer;
    			
    			answer = readAnswer(line);
    			try {
    				sih.checkInput(answer);
    				
    			} catch (NullPointerException e) {
    				lobby.broadcast("GAME_FINISHED+" + lobby.getGameID() + "+" 
    						+ lobby.getOpponentName(this.getClientName()) + 
    							"+" + lobby.getGame().getScore() + "+Game ended, because " 
    								+ this.getClientName() + " has disconnected.");
    				disconnected = true;
    			}
    				
    		}
		} catch (IOException e) {
			lobby.broadcast("GAME_FINISHED+" + lobby.getGameID() + "+" 
					+ lobby.getOpponentName(this.getClientName()) + "+" 
						+ lobby.getGame().getScore() + "+Game ended, because " 
							+ this.getClientName() + " has disconnected.");
		}
      
    }
    
    /**
     * This method sends the initial acknowledge config message to the player.
     */
    public void acknConfig() {
		String status = lobby.getStatus();
		String opponent = lobby.getOpponentName(sih.getClientName());
    	this.sendMessage("ACKNOWLEDGE_CONFIG+" + sih.getClientName() + 
    			"+" + Color.getNr(lobby.getColor(sih.getClientName())) + 
    				"+" + lobby.getDim() + "+" + status + "+" + opponent);
		System.out.println("ACKNOWLEDGE_CONFIG+" + sih.getClientName() 
			+ "+" + Color.getNr(lobby.getColor(sih.getClientName())) 
				+ "+" + lobby.getDim() + "+" + status + "+" + opponent);
		
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     * @param msg - String that will be send to the client
     */
    public void sendMessage(String msg) {
    	try {
			out.write(msg);
	    	out.newLine();
	    	out.flush();
		} catch (IOException e) {
			shutdown();
		}

    }
    
    /**
     * This method takes a String and splits it on every plus sign.
     * @param s - String that needs splitting
     * @return String array containing the components of s
     */
    public String[] readAnswer(String s) {
    	if (s != null) {
    		String[] answer = s.split("\\+");
    		return answer;	
    	}
    	return null;
    }
    

    /**
     * This ClientHandler signs off from the Server and subsequently
     * sends a last broadcast to the Server to inform that the Client
     * is no longer participating in the chat. 
     */
    private void shutdown() {
        server.removeHandler(this);
        server.broadcast("" + sih.getClientName() + " disconnected");
    }
    
    
    
}
