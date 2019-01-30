package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client extends Thread {

	/**
	 * Main to start client application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Start by asking user for name
		String clientName = readString("Please enter a user name.");
		
		
		//In case of computer player, ask for the maximum computation time it is given
		int moveTime = 0;
		if (clientName.equals("ComputerPlayer")) {
			moveTime = Integer.parseInt(readString("You indicated that you want to play"
						+ " with a computer player. \r"
							+ "Please enter the maximum 'thinking time' "
								+ "for your computer player in milliseconds (3000ms = 30s)."));
		}
		
		InetAddress host = null;
		int port = 0;

		
		boolean correctHostname = false;
		String hostInput;
		while (!correctHostname) {
			try {
				//ask for a valid host name, repeat asking in case of invalid host name
				hostInput = readString("Please enter the server hostname or ip-address.");
				host = InetAddress.getByName(hostInput);
				correctHostname = true;
			} catch (UnknownHostException e) {
				System.out.println("The name you entered was no valid hostname!");
				hostInput = readString("Please enter the server hostname or ip-address.");
			}
		}
			
		
			
		boolean validPort = false;
		String portInput;
		while (!validPort) {
			try {
				//ask for a valid port, repeat asking in case of invalid port
				portInput = readString("Please enter the port number of the server.");
				port = Integer.parseInt(portInput);
				validPort = true;
			} catch (NumberFormatException e) {
				System.out.println("The port you entered was no valid portnummer!");
				portInput = readString("Please enter the port number of the server.");
			}
		}

		

		try {
			
			//making client object
			Client client = new Client(clientName, host, port);

			//set client computer move time
			if (clientName.equals("ComputerPlayer")) {
				client.setComputerMoveTime(moveTime);
			}
			
			
			//start with client communication according to protocol
			client.sendMessage("HANDSHAKE+" + clientName);
			System.out.println("HANDSHAKE+" + clientName);
			
			client.start();
			
		} catch (IOException e) {
			System.out.println("ERROR: couldn't construct a client object!");
			System.exit(0);
		}

	}
	
	// -------------- Fields --------------- //
	
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private ClientInputHandler cih;
	private int computerMoveTime;

	// --------------- Constructor ---------- //
	
	/**
	 * Constructs a Client-object and tries to make a socket connection.
	 * @param name - String name of client
	 * @param host - InetAddress of server
	 * @param port - port nr of server
	 * @throws IOException
	 */
	public Client(String name, InetAddress host, int port)
			throws IOException {
		this.clientName = name;
		sock = new Socket(host, port);
		System.out.println("Created Socket!");
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.cih = new ClientInputHandler(this);
	}

	// ---------- Commands & Queries ------------ //
	
	/**
	 * Set maximum of computational time for computer player.
	 * @param i - Integer representing computation time in milliseconds
	 */
	public void setComputerMoveTime(int i) {
		this.computerMoveTime = i;
	}
	
	/**
	 * Method to obtain maximum computation time for current. 
	 * computer player
	 * @return Integer representing computation time of computer player
	 */
	public int getComputerMoveTime() {
		return this.computerMoveTime;
	}
	
	/**
	 * Reads the messages in the socket connection. 
	 * Each message will be checked for action in the clientinputHandler
	 */
	public void run() {
		
		while (true) {
			String[] input = this.receiveAnswer();
		
			if (input != null) {
				String msg = this.getCIH().checkInput(input);
				if (msg != null) {
					this.sendMessage(msg);
				}
			}
		}	
	}
	
	/**
	 * Method to obtain ClientInputHandler of client object.
	 * @return ClientInputHandler
	 */
	public ClientInputHandler getCIH() {
		return this.cih;
	}

	/**
	 * Sends a message to a ClientHandler. 
	 * @throws IOException
	 * @param msg - String representing to be send message
	 */
	public void sendMessage(String msg) {
		System.out.println("Send Message: " + msg);
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
		}

	}

	public void setClientName(String s) {
		this.clientName = s;
	}

	
	/**
	 * Closes the socket connection. 
	 * @throws IOException
	 */
	public void shutdown() {
		System.out.println("It seems that the server has disconnected."
					+ " The socket connection will be closed now...");
		try {
			sock.close();
		} catch (IOException e) {
			System.out.println("Error when closing socket!");
			e.printStackTrace();
		}
	}

	/**
	 * Method to obtain client name.
	 * @return String representing client name
	 */
	public String getClientName() {
		return clientName;
	}
	
	/***
	 * This method reads a String from the input stream and splits this String in order to receive
	 * the server arguments. 
	 * @return String array containing the arguments sent by the server.
	 */
	public String[] receiveAnswer() {
		String[] args = new String[20];
		String a;
		
		try {
			
			a = in.readLine();
			if (a != null) {
				args = a.split("\\+");
				return args;
			}	

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * Method to parse a String with a semicolon.
	 * @param a - String that needs parsing
	 * @return String array containing parsed String
	 */
	public String[] parseGameState(String a) {
		String[] answer = new String[20];
		answer = a.split(";");
		return answer;
	}
	
	/**
	 * Method to read user input.
	 * @param prompt - Prompt or question that is presented to the user
	 * @return String containing user input from system.in
	 */
	public static String readString(String prompt) {
		System.out.println(prompt);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}
}
