package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import go.Board;
import go.Color;

class BoardTest {

	private Board bSimple;
	private Board bComplex;
	
	@BeforeEach
	public void setUp() throws Exception {
		bSimple = new Board(9);
		bComplex = new Board(9);
		
		bSimple.setField(10, Color.BLACK);
		bSimple.setField(1, Color.WHITE);
		bSimple.setField(11, Color.WHITE);
		bSimple.setField(9, Color.WHITE);
		bSimple.setField(19, Color.WHITE);
		
		
		
		bComplex.setField(9, Color.BLACK);
		bComplex.setField(10, Color.BLACK);
		bComplex.setField(0, Color.WHITE);
		bComplex.setField(1, Color.WHITE);
		bComplex.setField(11, Color.WHITE);
		bComplex.setField(19, Color.WHITE);
		bComplex.setField(18, Color.WHITE);
	}
	

	@Test
	public void testSimpleCapture() {
		ArrayList<Integer> captured = new ArrayList<Integer>();
		captured.add(10);
		assertTrue(bSimple.isCaptured(Color.WHITE, captured));
	}
	
	
	
	@Test
	public void testGetGroup() {
		ArrayList<Integer> captured = new ArrayList<Integer>();
		bComplex.getGroup(9, Color.BLACK, captured);
		
		ArrayList<Integer> expected = new ArrayList<>();
		expected.add(9);
		expected.add(10);
		assertEquals(captured, expected);
	}
	
	@Test
	public void testComplexCapture() {
		ArrayList<Integer> captured = new ArrayList<Integer>();
		bComplex.getGroup(9, Color.BLACK, captured);
		
		assertTrue(bComplex.isCaptured(Color.EMPTY, captured));
	}	
	
	@Test
	public void testStringConstructor() {
		String boardString = "000100000";
		Board b = new Board(3);
		b.setField(3, Color.BLACK);
		Board b1 = new Board(3, boardString);

		
		assertEquals(b.getFields()[3], b1.getFields()[3]);
		assertEquals(b.getFields()[0], b1.getFields()[0]);
		assertEquals(b.getFields()[8], b1.getFields()[8]);
	}
	
	@Test
	public void updateBoardWithString() {
		String boardString = "000100000";
		
		Board b = new Board(3);
		b.setField(3, Color.BLACK);
		Board b1 = new Board(3);
		b1.update(boardString);

		
		assertEquals(b.getFields()[3], b1.getFields()[3]);
		assertEquals(b.getFields()[0], b1.getFields()[0]);
		assertEquals(b.getFields()[8], b1.getFields()[8]);
	}
	
	@Test
	public void testCheckPrevBoardState() {
		Board b = new Board(3);
		b.setField(1, Color.BLACK);
		ArrayList<Integer> rem = new ArrayList<>();
		rem.add(1);
		b.remove(rem);
		assertTrue(b.checkPreviousBoardState(1, Color.BLACK));
	}
	
	@Test
	public void testGetCurrentNeigh() {
		ArrayList<Color> expected = new ArrayList<>();
		expected.add(Color.WHITE);
		expected.add(Color.WHITE);
		expected.add(Color.WHITE);
		expected.add(Color.WHITE);
		
		ArrayList<Color> result;
		result = bSimple.getCurrentNeighColor(10);
		
		assertEquals(expected, result);
	}
	
	@Test
	public void testRemove() {
		ArrayList<Integer> rem = new ArrayList<>();
		rem.add(1);
		bSimple.remove(rem);
		
		assertEquals(bSimple.getField(1), Color.EMPTY);
	}
	
	@Test
	public void testGetScore() {
		bSimple.setField(3, Color.BLACK);
		ArrayList<Integer> rem = new ArrayList<>();
		rem.add(10);
		bSimple.remove(rem);
		
		double[] result = this.bSimple.getScore();
		double[] expected = new double[2];
		expected[0] = 1;
		expected[1] = 6.5;
		
		assertEquals(expected[0], result[0]);
		assertEquals(expected[1], result[1]);
		
	}
	
	@Test
	public void testIsValidMove() {
		assertTrue(bSimple.isValidMove(15, Color.WHITE));
		bSimple.setField(15, Color.WHITE);
		assertFalse(bSimple.isValidMove(15, Color.BLACK));
	}


}
