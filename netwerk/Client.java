package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import go.Board;
import go.Color;
import view.TUI;

public class Client extends Thread{
	
	private static final String USAGE
        = "usage: <name> <address> <port>";

	/** Start een Client-applicatie op. */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println(USAGE);
			System.exit(0);
		}
		
		InetAddress host=null;
		int port =0;

		try {
			host = InetAddress.getByName(args[1]);
		} catch (UnknownHostException e) {
			print("ERROR: no valid hostname!");
			System.exit(0);
		}

		try {
			port = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			print("ERROR: no valid portnummer!");
			System.exit(0);
		}

		try {
			
			//making client and TUI object
			Client client = new Client(args[0], host, port);
			TUI tui = new TUI();
			
			//all needed variables
			String userInput;
			String[] userInputSplit;
			int GAME_ID = 99;
			boolean isLeader = false;
			Color color;
			int boardSize;
			String currentGameState;
			
			//communicatie volgens protocol
			client.sendMessage("HANDSHAKE+"+args[0]);
			String[] serverAntwoord = receiveAnswer(client);
			if (serverAntwoord[0].equals("ACKNOWLEDGE_HANDSHAKE")) {
				GAME_ID = Integer.parseInt(serverAntwoord[1]);
				isLeader = Boolean.parseBoolean(serverAntwoord[2]);
			} else {
				//TODO wat doen we als we geen bevestiging van de server krijgen? TIMEOUT?
			}
			
			//next round of communication between client and clienthandler (server)
			
			serverAntwoord = receiveAnswer(client);
			if (isLeader) {
				if (serverAntwoord[0].equals("REQUEST_CONFIG")) {
					userInput = readString(serverAntwoord[1]); //vraag naar user input
					tui.showMenu();
					userInputSplit = userInput.split("\\s"); //split op whitespace, gaat dit goed?
					client.sendMessage("SET_CONFIG+"+GAME_ID+"+"+userInputSplit[0]+"+"+userInputSplit[1]);	
					serverAntwoord = receiveAnswer(client);
				}
				
				
				if (serverAntwoord[0].equals("ACKNOWLEDGE_CONFIG")) {
					if (serverAntwoord[1].equals(client.getClientName())) {
						color = Color.getColor(Integer.parseInt(serverAntwoord[2]));
						boardSize = Integer.parseInt(serverAntwoord[3]);
						currentGameState = serverAntwoord[4]; //$STATUS;$CURRENT_PLAYER;$SCORE;$BOARD
					}
					
					//Maak hier een nieuw game aan met currentGameState dingen
					
					//houd currentBoard and previous board
					
					//heb je nog een game of maak je dat hier volledig?
					
					Board prevBoard;
				}
				
				
				
			}
			
			
			
			
			
			client.start();
			
			do{ // wat doet dit precies?
				String input = readString("");
				client.sendMessage(input);
			}while(true);
			
		} catch (IOException e) {
			print("ERROR: couldn't construct a client object!");
			System.exit(0);
		}

	}
	
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port)
			throws IOException {
		this.clientName = name;
		sock = new Socket(host, port);
		System.out.println("Created Socket!");
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	/**
	 * Reads the messages in the socket connection. Each message will
	 * be forwarded to the MessageUI
	 */
	public void run() {
		try {
			String line = in.readLine();
			while (line != null) {
				print(line);
				line = in.readLine();
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
		}
	}

	/** send a message to a ClientHandler. 
	 * @throws IOException */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}

	}

	/** close the socket connection. 
	 * @throws IOException */
	public void shutdown() {
		print("Closing socket connection...");
		try {
			sock.close();
		} catch (IOException e) {
			System.out.println("Error when closing socket!");
			e.printStackTrace();
		}
	}

	/** returns the client name */
	public String getClientName() {
		return clientName;
	}
	
	private static void print(String message){
		System.out.println(message);
	}
	
	
	/***
	 * This reads from system.in
	 * @param tekst
	 * @return
	 */
	public static String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}
	
	/***
	 * This receives arguments and reads from inputstream
	 * @return
	 */
	public static String[] receiveAnswer(Client c) {
		String[] args = new String[20];
		String a;
		try {
			a = c.in.readLine();
			args = a.split("+");
			return args;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
