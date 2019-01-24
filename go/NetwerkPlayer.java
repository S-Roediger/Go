package go;

import java.util.Scanner;

import netwerk.ClientHandler;

public class NetwerkPlayer extends Player {

	Player p;
	ClientHandler ch;
	
	public NetwerkPlayer(String name, Color color, ClientHandler ch) {
		super(name, color);
		this.ch = ch;
	}

	@Override
	public int determineMove() {
		int choice = 0;
		String a = ch.readFromIn();
		String[] answer = ch.readAnswer(a);
		if (answer[0].equals("MOVE")) { //&& answer[2].equals(p.getName())
			choice = Integer.parseInt(answer[3]);
		}
		return choice;
	}
}
