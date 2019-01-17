package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import go.Board;
import go.Color;
import go.Game;
import go.HumanPlayer;
import go.Player;

class BoardTest {

	Board bSimple;
	Board bComplex;
	Game game;
	
	
	//in this class you also need to test (self defined) thrown exceptions from board
	
	@Before
	void setUpGame() throws Exception {
		Player p1 = new HumanPlayer("Hannah", Color.WHITE);
		Player p2 = new HumanPlayer("Anna", Color.BLACK);
		
		game = new Game(9, p1, p2);
		//game.start();
		
	}
	
	
	@Before
	void setUpSimpleBoard() throws Exception {
		bSimple = game.getBoard();
		bSimple.setField(10, Color.BLACK);
		bSimple.setField(1, Color.WHITE);
		bSimple.setField(11, Color.WHITE);
		bSimple.setField(9, Color.WHITE);
		bSimple.setField(19, Color.WHITE);
	}
	

	@Before
	void setUpComplexBoard() throws Exception {
		Board bComplex = new Board(9);
		bComplex.setField(9, Color.BLACK);
		bComplex.setField(10, Color.BLACK);
		bComplex.setField(0, Color.WHITE);
		bComplex.setField(1, Color.WHITE);
		bComplex.setField(11, Color.WHITE);
		bComplex.setField(19, Color.WHITE);
		bComplex.setField(18, Color.WHITE);
	}

	@Test
	void testSimpleCapture() {
		assertTrue(bSimple.isCaptured(10, Color.BLACK, bSimple.getNrGroupMembers(10, Color.BLACK))); //Waarom nullpointer?
	}
	
	
	
	@Test
	void testComplexCapture() {
		assertTrue(bComplex.isCaptured(9, Color.BLACK, bSimple.getNrGroupMembers(9, Color.BLACK)));
		assertTrue(bComplex.isCaptured(10, Color.BLACK, bSimple.getNrGroupMembers(10, Color.BLACK)));
	}

}
