package go;

public class ComputerPlayer extends Player {

// ------------ Constructor ---------------- //	
	
	
	public ComputerPlayer(String name, Color color) {
		super(name, color);
	}
	
// ------------ Command ------------------- //	

	@Override
	public int determineMove() {
		return 0;
	}
}
 