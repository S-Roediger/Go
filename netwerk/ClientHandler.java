package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import go.Color;
import go.NetwerkPlayer;
import go.HumanPlayer;
import go.Player;



public class ClientHandler extends Thread {
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
 


    private Lobby lobby;
  
    private ServerInputHandler sih;
    private boolean gameStarted = false;
    
    
    
    

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     */
    //@ requires serverArg != null && sockArg != null;
    public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
        this.server = serverArg;
        in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
        sih = new ServerInputHandler(this);
    }

    
    /***
     * Method to read from input stream, used by player to read client response
     * @return
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
    
    public ServerInputHandler getSih() {
    	return this.sih;
    }
    
    public void addLobbie(Lobby lobbie) {
    	this.lobby = lobbie;
    }
    
    public Lobby getLobby() {
    	return this.lobby;
    }
    
    /***
     * To communicate dim to server
     * @return
     */
    public int getDim() {
    	return sih.getDim();
    }
    
    public Player getPlayer() {
    	Player p = new NetwerkPlayer(sih.getClientName(), lobby.getColor(sih.getClientName()), this);
		return p;
    	
    }
    
    public String getClientName() {
    	return sih.getClientName();
    }
    

    /**
     * This method takes care of sending messages from the Client.
     * Every message that is received, is preprended with the name
     * of the Client, and the new message is offered to the Server
     * for broadcasting. If an IOException is thrown while reading
     * the message, the method concludes that the socket connection is
     * broken and shutdown() will be called. 
     */
    public void run() { //reads from client
    	try {
    		
    		while (true) {
    			String line = in.readLine();
    			String[] answer;
    			
    			answer = readAnswer(line);
    			sih.checkInput(answer);
    			
    			
    			if (sih.readyToStartGame() && !this.gameStarted) {
    				lobby.startGame();
    				gameStarted = true;
    			}
    				
    		}
		} catch (IOException e) {
			shutdown();
		}
      
    }
    
    public void ackn_config() {
		String status = lobby.getStatus();
		String opponent = lobby.getOpponentName(sih.getClientName());
    	this.sendMessage("ACKNOWLEDGE_CONFIG+"+sih.getClientName()+"+"+Color.getNr(lobby.getColor(sih.getClientName()))+"+"+lobby.getDim()+"+"+status+"+"+opponent);
		System.out.println("ACKNOWLEDGE_CONFIG+"+sih.getClientName()+"+"+Color.getNr(lobby.getColor(sih.getClientName()))+"+"+lobby.getDim()+"+"+status+"+"+opponent);
		
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     */
    public void sendMessage(String msg) {
    	try {
			out.write(msg);
	    	out.newLine();
	    	out.flush();
		} catch (IOException e) {
			shutdown();
			e.printStackTrace();
		}

    }
    
    public String[] readAnswer(String s) {
    	if ( s != null ) {
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
        server.broadcast(lobby.getGameID(),"[" + sih.getClientName() + " has left]");
    }
    
    
    
}
