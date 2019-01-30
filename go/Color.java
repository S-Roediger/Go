package go;

public enum Color {
	
	BLACK, WHITE, EMPTY, OFF; //OFF is 'off the playing field', needed for getNeighbours() in board
	
	/***
	 * Method to convert the Color representation of a field to an Integer representation.
	 * @param c - Color that needs to be converted
	 * @return Integer representation of color c
	 */ 
	public static int getNr(Color c) {
		switch (c) {
		
			case BLACK:
				return 1;
			case WHITE:
				return 2;
			case EMPTY:
				return 0; 
			case OFF:
				return -99;
		}
		return 0;
	}
	
	/***
	 * Method to convert the Integer representation of a field to a Color representation.
	 * @param i - Integer that needs to be converted
	 * @return Color representation of Integer i
	 */
	public static Color getColor(int i) {
		
		switch (i) {
			case 1:
				return Color.BLACK;
			case 2:
				return Color.WHITE;
			case 0:
				return Color.EMPTY; 
		}
		
		return null;
			
	}
	
	/***
	 * Method to obtain the opponent Color.
	 * @param c - Color of which the opponent color should be obtained
	 * @return Color of the opponent
	 */
	public static Color getOther(Color c) {
		if (c.equals(Color.BLACK)) {
			return Color.WHITE;
		}
		if (c.equals(Color.WHITE)) {
			return Color.BLACK;
		}
		if (c.equals(Color.EMPTY)) {
			return Color.EMPTY;
		}
		return null;
	}
}
