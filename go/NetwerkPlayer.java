package go;

import java.util.Scanner;

import netwerk.ClientHandler;

public class NetwerkPlayer extends Player {

	Player p;
	ClientHandler ch;
	boolean read = false;
	public NetwerkPlayer(String name, Color color, ClientHandler ch) {
		super(name, color);
		this.ch = ch;
	}

	

	
	@Override
	public int determineMove() {
		
		int choice = ch.getSih().getChoice();
	
	
		/*String a = ch.readFromIn();
		if (a != null) {
			String[] answer = ch.readAnswer(a);
			if (answer[0].equals("MOVE")) { //&& answer[2].equals(p.getName())
				choice = Integer.parseInt(answer[3]);
			} else if (answer[0].equals("EXIT")) {
				choice = -99;
			}
		} */
		
		return choice;
	}
}
