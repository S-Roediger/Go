package go;

public abstract class Player {

	// ------- Fields ------------ //
	
	private String name;
	private Color color;

	
	// ------ Constructor ----------- //
	
	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	// ------- Commands & Queries ------ //
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}

	
	public abstract int determineMove(); 
	
	public void makeMove(Board board, int choice) {
        board.setField(choice, getColor());
	}
	
}
