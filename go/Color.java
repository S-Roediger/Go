package go;

public enum Color {
	
	BLACK, WHITE, EMPTY, OFF; //OFF is 'off the playing field', needed for getNeighbours() in board
	
	public static Color getColor(int i) {
		
		switch(i) {
			case 1:
				return BLACK;
			case 2:
				return WHITE;
			case 0:
				return EMPTY; //3-9 can be implemented later if you want to extend
		}
		
		return null;
			
	}
	
	public static Color getOther(Color c) {
		if (c.equals(Color.BLACK)) {
			return Color.WHITE;
		}
		return Color.BLACK;
	}
	
	//Do I need a getOther() method here?
}
