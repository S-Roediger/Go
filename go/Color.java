package go;

public enum Color {
	
	BLACK, WHITE, EMPTY, OFF; //OFF is 'off the playing field', needed for getNeighbours() in board
	
	
	public static int getNr(Color c) {
		switch(c) {
		
		case BLACK:
			return 1;
		case WHITE:
			return 2;
		case EMPTY:
			return 0; //3-9 can be implemented later if you want to extend
		case OFF:
			return -99;
	}
	
	return 0;
	}
	
	public static Color getColor(int i) {
		
		switch(i) {
			case 1:
				return Color.BLACK;
			case 2:
				return Color.WHITE;
			case 0:
				return Color.EMPTY; //3-9 can be implemented later if you want to extend
		}
		
		return null;
			
	}
	
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
	
	@Override
	public String toString() {
		if (this.equals(EMPTY)) {
			return "     ";
		} else if (this.equals(BLACK)) {
			return "BLACK";
		} else if (this.equals(WHITE)) {
			return "WHITE";
		}
		return null;
	}
}
