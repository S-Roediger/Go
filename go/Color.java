package go;

public enum Color {
	
	BLACK, WHITE, EMPTY;
	
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
	
	//Do I need a getOther() method here?
}
