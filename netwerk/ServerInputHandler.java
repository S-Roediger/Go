package netwerk;

import java.util.Random;

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
	private OnlineGame game;
	
	
    
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
		this.game = ch.getLobby().getGame();
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
			this.clientName = clientName+1; //add int 1 to name to make sure both cannot have the same name
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
			
			if(!game.getBoard().gameOver() || game.getExit()) {
				//choice = game.getPlayers()[game.getCurrent()].determineMove(); //get player choices
				if (!game.getBoard().isValidMove(choice, game.getPlayers()[game.getCurrent()].getColor())) { //check whether field is empty, on board and != recreate prevBoardState
					ch.getLobby().broadcast("INVALID_MOVE+Invalid move");; //loop to ask again in case of faulty input
					//choice = players[current].determineMove();
				}		
				if (choice == -1) { 				// enforce pass rule
					game.getBoard().increasePass();
				} else if (choice == -99) {
					game.setExit(true);
				} else {
					game.getPlayers()[game.getCurrent()].makeMove(game.getBoard(), choice);
					game.handleCapture(Color.getOther(game.getPlayers()[game.getCurrent()].getColor()), choice); // je checkt eerst of jouw move een ander heeft gecaptured
					game.handleCapture(game.getPlayers()[game.getCurrent()].getColor(), choice);		// 	is dat uberhaupt logisch? Kan de huidige player gecaptured worden in eigen zet?	|	en dan kijk je naar suicide
					game.handleSuicide(game.getPlayers()[game.getCurrent()].getColor(), choice); //je kijkt of je eigen steen suicide gepleegt heeft
					game.getBoard().resetPass();
				}
				int playerWhoMadeLastMove = game.getCurrent() +1; //player who made most recent move, needed for protocol
				game.setCurrent((game.getCurrent()+3) %2);
				game.setCurrentPlayer(game.getCurrent() + 1);
				ch.getLobby().broadcast("ACKNOWLEDGE_MOVE+"+ch.getLobby().getGameID()+"+"+choice+";"+playerWhoMadeLastMove+"+"+ch.getLobby().getStatus());
				System.out.println("ACKNOWLEDGE_MOVE+"+ch.getLobby().getGameID()+"+"+choice+";"+playerWhoMadeLastMove+"+"+ch.getLobby().getStatus());
			} else {
				ch.getLobby().broadcast("GAME_FINISHED+"+ch.getLobby().getGameID()+"+"+game.getWinner()+"+"+game.getScore(game.getWinner()));
			}
			
		
			
			
		
			break;
			
		case "EXIT": 
			//deal with this
			break;

		}
	}
}
