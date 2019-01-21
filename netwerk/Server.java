package netwerk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import go.Game;
import go.Player;
import javafx.util.Pair;

// black makes first move, so player one always has to has black as color 


public class Server {
    private static final String USAGE
            = "usage: " + Server.class.getName() + " <port>";

    /** Start een Server-applicatie op. */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(USAGE);
            System.exit(0);
        }
        
        Server server = new Server(Integer.parseInt(args[0]));
        server.run();
        
    }


    private int port;
    private List<ClientHandler> threads;
    private HashMap<Integer, HashMap<Integer, Game>> games;
    private int lastGameID = 0;
    private ArrayList<Integer> gameID;
    /** Constructs a new Server object */
    public Server(int portArg) {
    	this.port = portArg;
        this.threads = new ArrayList<ClientHandler>();
        this.games = new HashMap<Integer, HashMap<Integer, Game>>();
        gameID = new ArrayList<>();
    }
    
    /**
     * Listens to a port of this Server if there are any Clients that 
     * would like to connect. For every new socket connection a new
     * ClientHandler thread is started that takes care of the further
     * communication with the Client.
     */
    public void run() {
    	try {
			ServerSocket ssocket = new ServerSocket(port);
			while (true) {
				Socket s = ssocket.accept();
				
				ClientHandler ch = null;
				this.connect(ch, s);
				ch.start();
				addHandler(ch);
				
				for (Integer i:gameID) {
					while (!games.get(i).containsKey(2)) {
						wait();
					}
					
					Game g = new Game(threads.get(i*2).getDim(), threads.get(i*2).getPlayer(), threads.get(i*2+1).getPlayer()); //input for new game, must come from clientServer
					games.get(i).put(2, g);
					g.start();
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    
    /***
     * handles new connections with clientHandler
     * @throws IOException 
     */
    public void connect(ClientHandler ch, Socket s) throws IOException {
    	if (games.get(lastGameID) != null && games.get(lastGameID).containsKey(2)) { //als er all een gameID is,maar nog geen twee spelers
			
			if (games.get(lastGameID).containsKey(0)) { //als er nog geen game gestart is met deze gameID en nog geen speler is toegevoegd
				ch = new ClientHandler(this, s, lastGameID, true); //is 0 checken dan nog necessary?
				games.get(lastGameID).put(1, null);
			} else if (games.get(lastGameID).containsKey(1)) {
				ch = new ClientHandler(this, s, lastGameID, false);
				games.get(lastGameID).put(2, null);
				gameID.add(lastGameID);
				lastGameID++;
				notifyAll();
			}
		} else {
			games.put(lastGameID, new HashMap<Integer, Game>());
			games.get(lastGameID).put(1, null);
			ch = new ClientHandler(this, s, lastGameID, true);	
		}
    }
    
    public void print(String message){
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
    
    public String getBoardStatus(int gameID) {//TODO
    
    	return "";
    }
    
    public String getOpponent(int gameID) {//TODO
        
    	return "";
    }
}
