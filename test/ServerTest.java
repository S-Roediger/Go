package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import netwerk.Server;

class ServerTest {
	
	/**
	 * In order to use this test a server has to be started externally. 
	 * For each individual test the correct portnumber needs to be edited 
	 * (third arg. of testClient).
	 * Each individual test needs to be tested on a different server or in a consecutive order.
	 */

	
	TestClient c1;
	TestClient c2;
	Server server;
	ServerSocket s;
	
	@BeforeEach
	void setUpServer() throws Exception {
		
		//TODO Normally, the server should be started here in a different thread
		// and @teardown should be used to stop server after test.
		// However, due to time constraints this wasn't feasible in the current 
		// implementation of this servertest
	}

	

	@Test
	void testConnectingOneClientCorrectly() {
		try {
			c1 = new TestClient("Hannah", InetAddress.getByName("localhost"), 2030);
		} catch (IOException e) {
			e.printStackTrace();
		}
		c1.sendMessage("HANDSHAKE+Hannah");
		String answer = c1.returnServerAnswer(); 

		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+1", answer);
		
	}
	
	@Test
	void testConnectingOneClientIncorrectly() {
		try {
			c1 = new TestClient("Hannah", InetAddress.getByName("localhost"), 2030);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		c1.sendMessage("SOMETHING_WRONG+Hannah");
		String answer = c1.returnServerAnswer(); 
		assertEquals("UNKNOWN_COMMAND+Unknown command", answer);
	}
	
	@Test
	void testConnectingTwoClientsCorrectly() {
		
		try {
			c1 = new TestClient("Hannah", InetAddress.getByName("localhost"), 2030);
			c2 = new TestClient("Anna", InetAddress.getByName("localhost"), 2030);
		} catch (UnknownHostException e) {		
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
	
		c1.sendMessage("HANDSHAKE+Hannah");
		String answer = c1.returnServerAnswer(); 
		c2.sendMessage("HANDSHAKE+Anna");
		String answer2 = c2.returnServerAnswer();
		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+1", answer);
		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+0", answer2);
		
	}
	
	@Test
	void testStartGameCorrectly() {
		
		try {
			c1 = new TestClient("Hannah", InetAddress.getByName("localhost"), 2030);
			c2 = new TestClient("Anna", InetAddress.getByName("localhost"), 2030);
		} catch (UnknownHostException e) {		
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
	
		c1.sendMessage("HANDSHAKE+Hannah");
		String answer = c1.returnServerAnswer(); 
		c2.sendMessage("HANDSHAKE+Anna");
		String answer2 = c2.returnServerAnswer();
		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+1", answer);
		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+0", answer2);
		
		answer = c1.returnServerAnswer();
		assertEquals("REQUEST_CONFIG+Please provide a preferred configuration by entering board size and preferred color (e.g. white/black 9)+$PREFERRED_COLOR+$BOARD_SIZE",answer);
		c1.sendMessage("SET_CONFIG+0+1+3");
		
		answer = c1.returnServerAnswer();
		answer2 = c2.returnServerAnswer();
		
		assertEquals("ACKNOWLEDGE_CONFIG+Hannah1+1+3+PLAYING;1;000000000+Anna", answer);
		assertEquals("ACKNOWLEDGE_CONFIG+Anna+2+3+PLAYING;1;000000000+Hannah1", answer2);
	}

	@Test
	void testStartGameIncorrectly() {
		
		try {
			c1 = new TestClient("Hannah", InetAddress.getByName("localhost"), 2020);
			c2 = new TestClient("Anna", InetAddress.getByName("localhost"), 2020);
		} catch (UnknownHostException e) {		
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
	
		c1.sendMessage("HANDSHAKE+Hannah");
		String answer = c1.returnServerAnswer(); 
		c2.sendMessage("HANDSHAKE+Anna");
		String answer2 = c2.returnServerAnswer();
		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+1", answer);
		assertEquals("ACKNOWLEDGE_HANDSHAKE+0+0", answer2);
		
		answer = c1.returnServerAnswer();
		assertEquals("REQUEST_CONFIG+Please provide a preferred configuration by entering board size and preferred color (e.g. white/black 9)+$PREFERRED_COLOR+$BOARD_SIZE",answer);
		c1.sendMessage("SET_SOMETHINGWRONG+0+1+9");
		
		answer = c1.returnServerAnswer();
		
		
		assertEquals("UNKNOWN_COMMAND+Unknown command", answer);
	
	}
	
}
