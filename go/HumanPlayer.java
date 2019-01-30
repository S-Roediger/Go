package go;

import java.util.Scanner;


public class HumanPlayer extends Player {

	
// ------------ Constructor ------------------- //
	
	public HumanPlayer(String name, Color color) {
		super(name, color);
		
	}

	
// ------------- Commands --------------------- //
	
	 /**
	 * Asks the user to input the field where to place the next color. This is
	 * done using the standard input/output.
	 * @return the player's chosen field
	 */
	@Override
	public int determineMove() {
		String prompt = "> " + getName() + " (" + getColor().toString() + ")"
	               + ", what is your choice? (Enter the index of any free "
	               	+ "field or enter -1 to pass)";
	    int choice = readInt(prompt);
	    return choice;  
	}
	
	/**
     * Writes a prompt to standard out and tries to read an int value from
     * standard in. This is repeated until an int value is entered.
     * 
     * @param prompt - the question to prompt the user
     * @return the first int value which is entered by the user
     */
	
    private int readInt(String prompt) {
        int value = 0;
        boolean intRead = false;
        @SuppressWarnings("resource")
        Scanner line = new Scanner(System.in);
        do {
            System.out.print(prompt);
            try (Scanner scannerLine = new Scanner(line.nextLine());) {
                if (scannerLine.hasNextInt()) {
                    intRead = true;
                    value = scannerLine.nextInt();
                }
            }
        } while (!intRead);
        return value;
    }

}
