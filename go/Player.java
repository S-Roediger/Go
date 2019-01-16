package go;

public abstract class Player {

/***
 * Class needs to be inherited by other classes, since you have different kinds of players: humans and AI
 */
	
	private String name;
	private Color color;

	
	//wat moet je met de stenen aan
	
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
       // Color[] c = board.getNeighbours(choice);
       // System.out.println("List of Neighbours for last set (left, up, right, down): " + c[0] + " " + c[1] + " " + c[2] + " " + c[3]);
        
	}
	
}
