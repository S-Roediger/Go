package go;

public class Go {
	
	
	public static void main(String[] args) {
		Board board = new Board(9);
		Player p1 = new HumanPlayer("Hannah", Color.WHITE);
		Player p2 = new HumanPlayer("Anna", Color.BLACK);
		
		Game game = new Game(board, p1, p2);
		game.start();
	}
 
}
