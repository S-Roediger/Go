package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class Client extends Thread{
	
	private static final String USAGE
        = "usage: <name> <address> <port>";


	/** Start een Client-applicatie op. */
	public static void main(String[] args) {
		
		
	String clientName = readString("Please enter a user name.");
		
	int moveTime = 0;
	if (clientName.equals("ComputerPlayer")) {
		moveTime = Integer.parseInt(readString("You indicated that you want to play with a computer player. \r Please enter the maximum 'thinking time' for your computer player."));
	}
		
		InetAddress host=null;
		int port =0;

		try {
			String hostInput = readString("Please enter the server hostname or ip-address.");
			host = InetAddress.getByName(hostInput);
		} catch (UnknownHostException e) {
			print("The name you entered was no valid hostname!");
			String hostInput = readString("Please enter a valid server hostname or ip-address.");
			try {
				host = InetAddress.getByName(hostInput);
			} catch (UnknownHostException e1) {
				print("You entered an invalid hostname twice. The system will shutdown now.");
				System.exit(0);
			}
			
		}

		try {
			String portInput = readString("Please enter the port number of the server.");
			port = Integer.parseInt(portInput);
		} catch (NumberFormatException e) {
			print("The port you entered was no valid portnummer!");
			String portInput = readString("Please enter a valid port number of the server.");
			try {
				port = Integer.parseInt(portInput);
			} catch (NumberFormatException e1) {
				print("You entered an invalid port number twice. The system will shutdown now");
				System.exit(0);
			}
		}

		try {
			
			//making client object


			
			//String clientName = args[0];
			Client client = new Client(clientName, host, port);

			if (clientName.equals("ComputerPlayer")) {
				client.setComputerMoveTime(moveTime);
			}
			
			
			//communicatie volgens protocol
			client.sendMessage("HANDSHAKE+"+clientName);
			
			client.start();
			
			//do{ // wat doet dit precies?
			//	String input = readString("");
			//	client.sendMessage(input);
			//}while(true);
			
		} catch (IOException e) {
			print("ERROR: couldn't construct a client object!");
			System.exit(0);
		}

	}
	
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private ClientInputHandler cih;
	private int computerMoveTime;

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
		this.cih = new ClientInputHandler(this);
	}

	public void setComputerMoveTime(int i) {
		this.computerMoveTime = i;
	}
	
	public int getComputerMoveTime() {
		return this.computerMoveTime;
	}
	
	/**
	 * Reads the messages in the socket connection. Each message will
	 * be forwarded to the MessageUI
	 */
	public void run() {
		
		
		
		while (true) {
			String[] input = this.receiveAnswer();
			
			if (input != null) {
				String msg = this.getCIH().checkInput(input);
				if ( msg!= null) {
					this.sendMessage(msg);
				}
			}
			
		}
		
		
		/*try {
			String line = in.readLine();
			while (line != null) {
				print(line);
				line = in.readLine();
			}
			shutdown();
		} catch (IOException e) {
			shutdown();
		} */
	}
	
	public ClientInputHandler getCIH() {
		return this.cih;
	}

	/** send a message to a ClientHandler. 
	 * @throws IOException */
	public void sendMessage(String msg) {
		System.out.println("Send Message: "+msg);
		try {
			//System.out.println("Im in the sendMessage() and send this: " +msg);
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
	 * This receives arguments and reads from inputstream
	 * @return
	 */
	public String[] receiveAnswer() {
		String[] args = new String[20];
		String a;
		
		try {
			
			a = in.readLine();
			if ( a != null ) {
				args = a.split("\\+");
				return args;
			}	

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * parse String using semicolon
	 * @param a
	 * @return
	 */
	public String[] parseGameState(String a) {
		String[] answer = new String[20];
		answer = a.split(";");
		return answer;
	}
	
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
}
