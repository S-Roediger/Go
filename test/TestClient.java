package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TestClient {

	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

/**
 * Constructs a Client-object and tries to make a socket connection.
 */
	public TestClient(String name, InetAddress host, int port)
			throws IOException {
		this.clientName = name;
		sock = new Socket(host, port);
		System.out.println("Created Socket!");
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		
	}
	
	
	
	/**
	 * Reads the messages in the socket connection. 
	 * Each message will be checked for action in the clientinputHandler.
	 * @return 
	 * 
	 */
	public String returnServerAnswer() {
		
		String a = "";
		try {
			a = in.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return a;

	}

	/**
	 * send a message to a ClientHandler.
	 * 
	 * @throws IOException
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
	 * close the socket connection.
	 * 
	 * @throws IOException
	 */
	public void shutdown() {
		print("It seems that the server has disconnected."
				+ " The socket connection will be closed now...");
		try {
			sock.close();
		} catch (IOException e) {
			System.out.println("Error when closing socket!");
			e.printStackTrace();
		}
	}

	/** Returns the client name. */
	public String getClientName() {
		return clientName;
	}

	private static void print(String message) {
		System.out.println(message);
	}

	/***
	 * This method reads from input stream and splits the read String.
	 * 
	 * @return
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
	 * Parses a String using semicolon.
	 * 
	 * @param a
	 * @return
	 */
	public String[] parseGameState(String a) {
		String[] answer = new String[20];
		answer = a.split(";");
		return answer;
	}

	public static String readString(String tekst) {
		System.out.println(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}

}
