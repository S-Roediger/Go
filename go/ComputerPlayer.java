package go;

public class ComputerPlayer extends Player {

	public ComputerPlayer(Color color) {
		super("Computer Player", color);
	}

	@Override
	public int determineMove() {
		return 0;
	}

}
