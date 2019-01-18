package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import go.Color;
import go.Game;
import go.HumanPlayer;
import go.Player;

class GameTest {

	@BeforeEach
	void setUp() throws Exception {
		Player p0 = new HumanPlayer("Anna", Color.BLACK);
		Player p1 = new HumanPlayer("Hannah", Color.WHITE);
		Game game = new Game(3, p0, p1);
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	
	

}
