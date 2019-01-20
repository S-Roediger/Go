package go;

public class Go {
	
	
	public static void main(String[] args) {
		Player p1 = new HumanPlayer("Hannah", Color.WHITE);
		Player p2 = new HumanPlayer("Anna", Color.BLACK);
		
		Game game = new Game(9, p1, p2);
		game.start();
	}
 
}
