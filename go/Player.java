package go;

public abstract class Player {

/***
 * Class needs to be inherited by other classes, since you have different kinds of players: humans and AI
 */
	
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
	
	
	public abstract int determineMove(Board board);
	
	public void makeMove(Board board) {
        int keuze = determineMove(board);
        board.setField(keuze, getColor());
        //TODO
	}
	
}
