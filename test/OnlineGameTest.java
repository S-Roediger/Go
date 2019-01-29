package test;

import static org.junit.jupiter.api.Assertions.*;

import java.net.BindException;
import java.net.InetAddress;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import netwerk.Client;
import netwerk.Server;

class OnlineGameTest {

	Client c1;
	Client c2;
	
	@BeforeEach
	void setUp() throws Exception {
		Server s = new Server(2020);
		s.run();
		
		c1 = new Client("Hannah", InetAddress.getByName("localhost"), 2020);
		c2 = new Client("Anna", InetAddress.getByName("localhost"), 2020);
	}

	@Test
	void test() {
		assertEquals("Hannah", c1.getClientName());
		assertEquals("Anna", c2.getClientName());
		
		
		
	}
	
	
	

}
