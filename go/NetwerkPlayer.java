package go;

import netwerk.ClientHandler;

public class NetwerkPlayer extends Player {

	
// ------------- Fields ------------------ //
	
	Player p;
	ClientHandler ch;
	boolean read = false;
	
// ------------- Constructor ------------- //	
	
	public NetwerkPlayer(String name, Color color, ClientHandler ch) {
		super(name, color);
		this.ch = ch;
	}

// -------------- Command -------------- //	

	
	@Override
	public int determineMove() {	
		int choice = ch.getSih().getChoice();
		return choice;
	}
}
