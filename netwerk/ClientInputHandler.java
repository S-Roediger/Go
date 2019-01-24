package netwerk;

public class ClientInputHandler {

	
	
	public void checkInput(String input) {
		
		String[] args = input.split("\\+");
		
		switch (args[0]) {
		
		case "ACKNOWLEDGE_HANDSHAKE":
			//doe iets
			break;
			
		case "REQUEST_CONFIG":
			//handle this
			break;
			
		case "REQUEST_REMATCH":
			// handle this
			break;
			
		case "ACKNOWLEDGE_CONFIG":
			// handle this
			break;
			
		case "ACKNOWLEDGE_MOVE":
			// handle this
			break;
			
		case "ACKNOWLEDGE_REMATCH":
			// handle this
			break;
			
		case "INVALID_MOVE":
			// handle this
			break;
			
		case "UNKNOWN_COMMAND":
			// handle this
			break;
			
		case "GAME_FINISHED":
			// handle this
			break;
		}
	}
	
}
