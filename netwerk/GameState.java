package netwerk;

public class GameState {

	
	private String currentState;
	
	public GameState() {
		this.currentState = "CONNECTION+FIRST";
	}
	
	
	public synchronized String getState() {
		return this.currentState;
	}
	
	
	public synchronized void setState(String status) {
		this.currentState = status;
	}
	
	public synchronized void changeState(String currentState) {
		
		switch (currentState) {
			case "CONNECTION+FIRST":
				this.currentState = "CONNECTION+SECOND";
				break;
				
			case"CONNECTION+SECOND":
				this.currentState = "GAMESTART";
				break;
				
			case"GAMESTART":
				this.currentState = "MOVE+FIRST";
				
			case "MOVE+FIRST":
				this.currentState = "MOVE+SECOND";
				break;
			
			case "MOVE+SECOND":
				this.currentState = "MOVE+FIRST";
				break;
		}
			
				
	}
	
}
