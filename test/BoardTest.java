package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Before;

import org.junit.jupiter.api.Test;

import go.Board;
import go.Color;

class BoardTest {

	private Board bSimple;
	private Board bComplex;
	
	
	
	//in this class you also need to test (self defined) thrown exceptions from board
	
	@Before
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
		try {
			this.setUp(); //Waarom werkt setup niet gewoon?
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		ArrayList<Integer> captured = new ArrayList<Integer>();
		captured.add(10);
		assertTrue(bSimple.isCaptured(Color.WHITE, captured));
	}
	
	
	
	@Test
	public void testComplexCapture() {
		try {
			this.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Integer> captured = new ArrayList<Integer>();
		bComplex.getGroup(9, Color.BLACK, captured);
		System.out.println(bComplex.toString());
		assertTrue(bComplex.isCaptured(Color.WHITE, captured)); //this should be working ?!
	}	

}
