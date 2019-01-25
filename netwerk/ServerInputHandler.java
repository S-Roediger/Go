package netwerk;

import go.Color;
import go.OnlineGame;

public class ServerInputHandler {

	
    private String clientName;
    private int dim;
    private Color c;
    private int preferredColor = 0;
 
    private ClientHandler ch;
    private boolean secondPlayerAckn = false;
	private int choice;
	
	
    
    public boolean readyToStartGame() {
    	return ch.getLobby().getConfig() && secondPlayerAckn;
    }
	
    public ServerInputHandler(ClientHandler ch) {
    	this.ch = ch;
    	
    }
    
    public int getDim() {
    	return dim;
    }
    

    public int getChoice() {
    	return choice;
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
			
		case "MOVE":
			if (input[0].equals("MOVE")) { //&& answer[2].equals(p.getName())
				this.choice = Integer.parseInt(input[3]);
			} else if (input[0].equals("EXIT")) {
				this.choice = -99;
			}
			
			if(!ch.getLobby().getGame().getBoard().gameOver() || ch.getLobby().getGame().getExit()) {
				//choice = game.getPlayers()[game.getCurrent()].determineMove(); //get player choices
				if (!ch.getLobby().getGame().getBoard().isValidMove(choice, ch.getLobby().getGame().getPlayers()[ch.getLobby().getGame().getCurrent()].getColor())) { //check whether field is empty, on board and != recreate prevBoardState
					ch.getLobby().broadcast("INVALID_MOVE+Invalid move");; //loop to ask again in case of faulty input
					//choice = players[current].determineMove();
				}		
				if (choice == -1) { 				// enforce pass rule
					ch.getLobby().getGame().getBoard().increasePass();
				} else if (choice == -99) {
					ch.getLobby().getGame().setExit(true);
				} else {
					ch.getLobby().getGame().getPlayers()[ch.getLobby().getGame().getCurrent()].makeMove(ch.getLobby().getGame().getBoard(), choice);
					ch.getLobby().getGame().handleCapture(Color.getOther(ch.getLobby().getGame().getPlayers()[ch.getLobby().getGame().getCurrent()].getColor()), choice); // je checkt eerst of jouw move een ander heeft gecaptured
					ch.getLobby().getGame().handleCapture(ch.getLobby().getGame().getPlayers()[ch.getLobby().getGame().getCurrent()].getColor(), choice);		// 	is dat uberhaupt logisch? Kan de huidige player gecaptured worden in eigen zet?	|	en dan kijk je naar suicide
					ch.getLobby().getGame().handleSuicide(ch.getLobby().getGame().getPlayers()[ch.getLobby().getGame().getCurrent()].getColor(), choice); //je kijkt of je eigen steen suicide gepleegt heeft
					ch.getLobby().getGame().getBoard().resetPass();
				}
				int playerWhoMadeLastMove = ch.getLobby().getGame().getCurrent() +1; //player who made most recent move, needed for protocol
				ch.getLobby().getGame().setCurrent((ch.getLobby().getGame().getCurrent()+3) %2);
				ch.getLobby().getGame().setCurrentPlayer(ch.getLobby().getGame().getCurrent() + 1);
				ch.getLobby().broadcast("ACKNOWLEDGE_MOVE+"+ch.getLobby().getGameID()+"+"+choice+";"+playerWhoMadeLastMove+"+"+ch.getLobby().getStatus());
				System.out.println("ACKNOWLEDGE_MOVE+"+ch.getLobby().getGameID()+"+"+choice+";"+playerWhoMadeLastMove+"+"+ch.getLobby().getStatus());
			} else {
				ch.getLobby().broadcast("GAME_FINISHED+"+ch.getLobby().getGameID()+"+"+ch.getLobby().getGame().getWinner()+"+"+ch.getLobby().getGame().getScore(ch.getLobby().getGame().getWinner()));
			}
			
		
			
			
		
			break;
			
		case "EXIT": 
			//deal with this
			break;

		}
	}
}
