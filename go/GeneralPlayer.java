package go;

public class GeneralPlayer extends Player {

	Player p;
	
	public GeneralPlayer(String name, Color color) {
		super(name, color);
		if (name.equals("ComputerPlayer")) {
			 this.p = new ComputerPlayer(name, color);
		} else {
			this.p = new HumanPlayer(name, color);
		}
	}

	@Override
	public int determineMove() {
		return p.determineMove();
	}

}
