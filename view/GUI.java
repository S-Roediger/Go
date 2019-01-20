package view;

import java.util.ArrayList;

import com.nedap.go.gui.GoGuiIntegrator;

import go.Color;

public class GUI {

	
	private GoGuiIntegrator gogui;
	private int boardSize;

	public GUI(int boardSize) {
		this.boardSize = boardSize;
		gogui = new GoGuiIntegrator(true, true, boardSize);
		gogui.startGUI();
		gogui.setBoardSize(boardSize);
	}
	
	
	
	/***
	 * Method to turn index into coordinates x, y
	 * @param i - index of intersection
	 * @return int[] containing coordinates x and y
	 */
	public int[] getCoordinates(int i) {
		int x = i % boardSize;
		int y = i/boardSize;
		int[] result = new int[2];
		result[0] = x;
		result[1] = y;
		return result;
	}
	
	
	/***
	 * Method to add a stone on intersection i with given color c
	 * @param i - index of intersection
	 * @param c - color of stone that will be placed
	 */
	public void addStone(int i, Color c) {
		boolean white = false;
		if (c.equals(Color.WHITE)) {
			white = true;
		}
		int[] coordinates = getCoordinates(i);
		gogui.addStone(coordinates[0], coordinates[1], white);
	}
	
	/***
	 * Method to remove stone at intersection i
	 * @param i - index of to be cleared intersection
	 */
	public void removeStone(ArrayList<Integer> i) {
		for (int index:i) {
			int[] coordinates = getCoordinates(index);
			gogui.removeStone(coordinates[0], coordinates[1]);
		}

	}
	
	/***
	 * Adds .. TODO
	 * @param i
	 * @param c
	 */
	public void addAreaIndicator(int i, Color c) { //TODO probably not necessary!!
		boolean white = false;
		if (c.equals(Color.WHITE)) {
			white = true;
		}
		int[] coordinates = getCoordinates(i);
		gogui.addAreaIndicator(coordinates[0], coordinates[1], white);
	}
	
	/***
	 * Adds hint indicator
	 * @param i - field that is suggested to user
	 */
	public void addHintIndicator(int i) {
		int[] coordinates = getCoordinates(i);
		gogui.addHintIndicator(coordinates[0], coordinates[1]);
	}
	
	/***
	 * TODO
	 */
	public void removeHintIndicator() {
		gogui.removeHintIdicator();
	}
	
	/***
	 * TODO
	 */
	public void clearBoard() {
		gogui.clearBoard();
	}
	
}
