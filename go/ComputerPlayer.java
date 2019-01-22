package go;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	public int determineMove() {
		return 0;
	}


}
