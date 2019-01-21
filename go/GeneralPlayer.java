package go;

public class GeneralPlayer extends Player {

	public GeneralPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	public int determineMove() {
		return 0;
	}

}
