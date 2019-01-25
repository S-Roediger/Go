package netwerk;

import go.Color;

public class ServerInputHandler {

	
    private String clientName;
    private int dim;
    private Color c;
    private int preferredColor = 0;
 
    private ClientHandler ch;
    private boolean secondPlayerAckn = false;
	
    
    
    public boolean readyToStartGame() {
    	return ch.getLobby().getConfig() && secondPlayerAckn;
    }
	
    public ServerInputHandler(ClientHandler ch) {
    	this.ch = ch;
    }
    
    public int getDim() {
    	return dim;
    }
    

    
    public String getClientName() {
    	return clientName;
    }
	
	public void checkInput(String[] input) {
		
		//String[] args = input.split("\\+");
		
		switch (input[0]) {
		
		case "HANDSHAKE":
			clientName = input[1];
			
			if(ch.getLobby().isLeader(ch)) {
				ch.sendMessage("ACKNOWLEDGE_HANDSHAKE+"+ch.getLobby().getGameID()+"+"+1); 
		    	ch.sendMessage("REQUEST_CONFIG+Please provide a preferred configuration by entering board size and preferred color (e.g. white/black 9)+$PREFERRED_COLOR+$BOARD_SIZE");

				//ch.getLobby().getGameState().changeState("CONNECTION+FIRST");

			} else if (!ch.getLobby().isLeader(ch) && ch.getLobby().getConfig()){
				ch.sendMessage("ACKNOWLEDGE_HANDSHAKE+"+ch.getLobby().getGameID()+"+"+0);
				ch.getLobby().setColor(clientName, ch.getLobby().getColors()[1]);
				c = ch.getLobby().getColors()[1];
				this.secondPlayerAckn = true;
				//ch.getLobby().getGameState().changeState("CONNECTION+SECOND");
			}
			break;
			
		case "SET_CONFIG":
			dim = Integer.parseInt(input[3]);
			preferredColor = Integer.parseInt(input[2]);
			ch.getLobby().setDim(dim);
			
			ch.getLobby().setColor(clientName, Color.getColor(preferredColor));
			c = ch.getLobby().getColors()[0];
			
			if (ch.getLobby().isFull() && ch.getLobby().getConfig()) {
				ch.getLobby().startGame();
			}
			break;
			
		case "SET_REMATCH":
			//deal with this
			break;
			
		//case "MOVE":
			//deal with this
		//	break;
			
		case "EXIT": 
			//deal with this
			break;

		}
	}
}
