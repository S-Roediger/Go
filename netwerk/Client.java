package netwerk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


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
			
			//making client object
			String clientName = args[0];
			Client client = new Client(args[0], host, port);
			
			//communicatie volgens protocol
			client.sendMessage("HANDSHAKE+"+args[0]);
			
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
		try {
			//System.out.println("Im in the sendMessage() and send this: " +msg);
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
	
}
