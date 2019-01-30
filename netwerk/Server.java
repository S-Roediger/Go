package netwerk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class Server {
	
	// -------------- fields ------------------- //
    
    private int port;
    private List<ClientHandler> threads;
    private HashMap<Integer, Lobby> gameLobbies; 
    private int lastGameID;
    private ServerSocket ssocket;

    
    // ------------- Constructor ---------------- //
    
    /**
     * Constructs a new Server object.
     * @param portArg - Integer representing port number on which server will listen
     */
    public Server(int portArg) {
    	this.port = portArg;
        this.threads = new ArrayList<ClientHandler>();
        this.gameLobbies = new HashMap<Integer, Lobby>();
        this.lastGameID = 0;
    }
    
    /**
     * Main to start a server-application.
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("Please enter a port number.");
        String antw = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			antw = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Server server = new Server(Integer.parseInt(antw));
        server.run();     
    }
    
    // ----------- Commands & Queries ------------ //
    
    /**
     * Listens to a port of this Server if there are any Clients that 
     * would like to connect. For every new socket connection a new
     * ClientHandler thread is started that takes care of the further
     * communication with the Client.
     */
    public void run() {
    	try {
    		
    		boolean correctPort = false;
    		while (!correctPort) {
    			try {
    				ssocket = new ServerSocket(port); 
    				correctPort = true;
    			} catch (BindException e) {
    				System.out.println("You entered a portnumber that "
    						+ "is already in use. Please enter another port number");
    		        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    				try {
    					this.port = Integer.parseInt(in.readLine());
    				} catch (IOException e1) {
    					e1.printStackTrace();
    				}
    			}
    		}
			while (true) {
				
				Socket s = ssocket.accept();
				
				ClientHandler ch = new ClientHandler(this, s);
				addHandler(ch);
				System.out.println("Added handler to threads");
				ch.addLobbie(joinLobby(ch));
				System.out.println("Added handler to lobby");
				ch.start(); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Method that checks all lobbies and starts a game if the lobby is
     * full and config is set.
     */
    public void startGameInFullLobbies() {
    	Collection<Integer> gameIds = gameLobbies.keySet();
    	for (int k:gameIds) {
    		if (this.gameLobbies.get(k).isFull() && this.gameLobbies.get(k).getConfig()) {
				this.gameLobbies.get(k).startGame();
    		}
    	}
    }
    
    /***
     * Lets a clientHandler join a lobby, when there is no lobby with a free spot
     * it creates a new one.
     * @param ch - client handler that wants to join a lobby
     * @return lobby that was joined by ch in order to let ch know which lobby he/she joined
     */
    public synchronized Lobby joinLobby(ClientHandler ch) {
    	boolean joined = false;
    	Lobby joinedLobby = null;
    	Collection<Integer> gameIds = gameLobbies.keySet();
    	for (int k:gameIds) {
    		if (!this.gameLobbies.get(k).isFull()) {
    			joined = this.gameLobbies.get(k).addClient(ch);
    			joinedLobby = this.gameLobbies.get(k);
    		}
    	}
    	if (!joined) {
    		Lobby lob = new Lobby(this.generateGameID());
    		lob.addClient(ch);
    		this.gameLobbies.put(lob.getGameID(), lob);
    		joinedLobby = lob;
    		
    	}
    	
    	return joinedLobby;
    }
   
    /**
     * Generates next gameID.
     * @return Integer representing new game id
     */
    public int generateGameID() {
    	return this.lastGameID++;
    }
    
    public void print(String message) {
        System.out.println(message);
    } 
    
    /**
     * Sends a message using the collection of connected ClientHandlers
     * to all connected Clients.
     * @param msg message that is send
     */
    public void broadcast(String msg) { 
    	print(msg);
    	for (ClientHandler c:threads) {
        	c.sendMessage(msg);
        }
    }
    
    /**
     * Add a ClientHandler to the collection of ClientHandlers.
     * @param handler ClientHandler that will be added
     */
    public void addHandler(ClientHandler handler) {
        threads.add(handler);
    }
    
    /**
     * Remove a ClientHandler from the collection of ClientHanlders. 
     * @param handler ClientHandler that will be removed
     */
    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
    }
    
    /**
     * Shuts down the server socket connection.
     * @param s - ServerSocket
     * @throws IOException
     */
    public void shutdown(ServerSocket s) throws IOException {
    	this.broadcast("The server is shutting down");
    	s.close();
    }
    
}
