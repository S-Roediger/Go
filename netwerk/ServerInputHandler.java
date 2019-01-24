package netwerk;

public class ServerInputHandler {

	
	
	public void checkInput(String input) {
		
		String[] args = input.split("\\+");
		
		switch (args[0]) {
		
		case "HANDSHAKE":
			//deal with this
			break;
			
		case "SET_CONFIG":
			//deal with this
			break;
			
		case "SET_REMATCH":
			//deal with this
			break;
			
		case "MOVE":
			//deal with this
			break;
			
		case "EXIT": 
			//deal with this
			break;

		}
	}
}
