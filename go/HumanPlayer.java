package go;

import java.util.Scanner;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	public int determineMove(Board board) {
		return 0;
	}
	
	// --------- Write prompt to ask for player input -------- // 
	// Niet nodig sinds wij de input door de client server connectie willen laten lopen
	
	
	
	/**
     * Writes a prompt to standard out and tries to read an int value from
     * standard in. This is repeated until an int value is entered.
     * 
     * @param prompt
     *            the question to prompt the user
     * @return the first int value which is entered by the user
     */
	/*
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
    } */

}
