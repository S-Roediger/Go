package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import go.Color;
import go.GeneralPlayer;
import go.HumanPlayer;
import go.Player;


//hier ook game aanmaken 
//computerplayer??

public class ClientHandler extends Thread {
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private int dim;
    private Color c;
    private int preferredColor = 0;
    private Lobby lobby;
    private boolean isFirstPlayer;
    

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     */
    //@ requires serverArg != null && sockArg != null;
    public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
        this.server = serverArg;
        in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
    }

    
    public void addLobbie(Lobby lobbie) {
    	this.lobby = lobbie;
    }
    
    /***
     * To communicate dim to server
     * @return
     */
    public int getDim() {
    	return dim;
    }
    
    public Player getPlayer() {
    	Player p = new GeneralPlayer(clientName, c);
		return p;
    	
    }
    
    public String getClientName() {
    	return clientName;
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
			this.startProtocol();
			
			//if (lobby.isLeader(this) && lobby.getGameState().getState().equals("GAMESTART")) {
			//	lobby.startGame();
			//}
			
			isFirstPlayer = lobby.isFirstPlayer(clientName);
			String line = "";
			String[] answer = readAnswer(line);
			answer[0] = line; //test waardes zodat while niet stuk gaat
			answer[1] = "99";
			
			while (!answer[0].equals("GAME_FINISHED") && Integer.parseInt(answer[1]) == this.lobby.getGameID()) {
				
				
				
				if (isFirstPlayer && this.lobby.getGameState().getState().equals("MOVE+FIRST")) {
					line = in.readLine();
					answer = readAnswer(line);
					System.out.println(answer);
					//if (answer[0].equals("")) {
						
					//}
				}
				
				if (!isFirstPlayer && this.lobby.getGameState().getState().equals("MOVE+SECOND")) {
					line = in.readLine();
					answer = readAnswer(line);
					//setUserInput on board
					System.out.println(answer);
				}
				
				
				
			}
			
			//line = in.readLine();
			//answer = readAnswer(line);
			
			
			
			System.out.println(this.clientName+": The protocol is done now!");
			
			

		
			
			
    		//String line = in.readLine();
    		//String[] answer = readAnswer(line);
    		//hier moet de loop die steeds checkt of player aan de beurt is of niet

  		/*while (line != null) {
				server.broadcast(lobby.getGameID(), clientName + ": " + line);
				line = in.readLine();
			} */
			
		} catch (IOException e) {
			shutdown();
		}
      
    }
    
    /***
     * makes initial connection with client according to protocol
     * @throws IOException 
     */
    public void startProtocol() throws IOException {
    	String line = in.readLine();
		String[] answer = readAnswer(line);
		if (answer.length == 2 && answer[0].equals("HANDSHAKE")) {
			clientName = answer[1];
			
			while (!lobby.getGameState().getState().equals("GAMESTART")) {
				
			
			
				if(lobby.isLeader(this) && lobby.getGameState().getState().equals("CONNECTION+FIRST")) {
					this.sendMessage("ACKNOWLEDGE_HANDSHAKE+"+lobby.getGameID()+"+"+1); 
					handleConfig();
					lobby.getGameState().changeState("CONNECTION+FIRST");

				} else if (!lobby.isLeader(this) && lobby.getGameState().getState().equals("CONNECTION+SECOND")){
					this.sendMessage("ACKNOWLEDGE_HANDSHAKE+"+lobby.getGameID()+"+"+0);
					lobby.setColor(clientName, lobby.getColors()[1]);
					c = lobby.getColors()[1];
					lobby.getGameState().changeState("CONNECTION+SECOND");
				}
			}
			
			if (!lobby.getGameStarted()) {
				lobby.startGame();	
			}
		}
    }
    
    public void ackn_config() {
		String status = lobby.getStatus();
		String opponent = lobby.getOpponentName(clientName);
    	this.sendMessage("ACKNOWLEDGE_CONFIG+"+clientName+"+"+Color.getNr(lobby.getColor(clientName))+"+"+lobby.getDim()+"+"+status+"+"+opponent);
		
    }
    
    /***
     * handles the config of game with client
     */
    public void handleConfig() {
    	this.sendMessage("REQUEST_CONFIG+Please provide a preferred configuration by entering board size and preferred color (e.g. white/black 9)+$PREFERRED_COLOR+$BOARD_SIZE");
    	try {
			String[] answer = readAnswer(in.readLine());
			if (answer.length == 4 && answer[0].equals("SET_CONFIG") && Integer.parseInt(answer[1]) == lobby.getGameID()) {
				dim = Integer.parseInt(answer[3]);
				preferredColor = Integer.parseInt(answer[2]);
				lobby.setDim(dim);
				
				lobby.setColor(clientName, Color.getColor(preferredColor));
				c = lobby.getColors()[0];
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
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
    	String[] answer = s.split("\\+");
    	return answer;
    }
    

    /**
     * This ClientHandler signs off from the Server and subsequently
     * sends a last broadcast to the Server to inform that the Client
     * is no longer participating in the chat. 
     */
    private void shutdown() {
        server.removeHandler(this);
        server.broadcast(lobby.getGameID(),"[" + clientName + " has left]");
    }
    
    
    
}
