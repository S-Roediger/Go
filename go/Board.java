package go;

import java.util.ArrayList;

public class Board {
/***
 * Model class, keep log of game changes, rules
 * 
 * Board maakt TUI
 */
	
	//private Color[] prevGameStatus;
	private Color[] fields;
	private int dim; 
	private int pass;
	private Color[][] pastBoardStates;
	
	
// --------------------- Constructor ---------------- //
	
	/***
	 * Creates empty board with given dimensions
	 */
	public Board(int dim) {
		this.dim = dim;
		fields = new Color[dim*dim];
		for (int i = 0; i < dim*dim; i++) {
			fields[i] = Color.EMPTY;
		}
		pass = 0;
		pastBoardStates = new Color[dim*dim][dim*dim];
		pastBoardStates[0] = fields; 
	}
	
	
	
// --------------------- Commands & Queries ------------------- //
	/***
	 * 
	 * @return counter for amounts of times player passed
	 */
	public int getPass() {
		return pass;
	}
	
	/***
	 * Method to increase pass
	 */
	public void increasePass() {
		pass++;
	}
	
	
	/***
	 * Creates a deepCopy from the current board
	 * @return deepCopy of board
	 */
	public Board deepCopy() {
		Board b = new Board(dim);
		for (int i = 0; i < dim*dim; i++) {
			b.setField(i, fields[i]);
		}
		return b;
	} 
	
	
	public int index(int row, int col) {
		return dim*row+col;
	}
	
    /**
     * Returns true if ix is a valid index of a field on the board.
     * @return true if 0 <= index < dim * dim
     */
    public boolean isField(int index) {
    	return index >= 0 && index < dim * dim;
    }
    
    /**
     * Returns true of the (row,col) pair refers to a valid field on the board.
     *
     * @return true if 0 <= row < dim && 0 <= col < dim
     */
    public boolean isField(int row, int col) {
        return row >= 0 && row < dim && col >= 0 && col < dim;
    }
    
    /**
     * Returns the content of the field i.
     *
     * @param i
     *            the number of the field (see NUMBERING) //maybe I am not using this..
     * @return the color on the field
     */

    public Color getField(int i) {
    	return fields[i];
    }
    
    /**
     * Returns the content of the field referred to by the (row,col) pair.
     *
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return the color on the field
     */
    public Color getField(int row, int col) {
    	return getField(index(row, col));
    	
    }
    
    /**
     * Returns true if the field i is empty.
     *
     * @param i
     *            the index of the field (see NUMBERING)
     * @return true if the field is empty
     */
    public boolean isEmptyField(int i) {
    	return fields[i] == Color.EMPTY;
    }
	
    /**
     * Returns true if the field referred to by the (row,col) pair it empty.
     *
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return true if the field is empty
     */
    public boolean isEmptyField(int row, int col) {
    	return fields[index(row, col)] == Color.EMPTY;
    
    }
    
    
    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or both players have passed.
     *
     * @return true if the game is over
     */
    public boolean gameOver() {
        if (pass > 1) {
        	return true;
        }
        return false;
    }
    
    /**
     * Empties all fields of this board (i.e., let them refer to the value
     * Color.EMPTY).
     */
    public void reset() {
    	for (int i = 0; i < dim*dim; i++) {
			fields[i] = Color.EMPTY;
		}
    	pass = 0;
    	
    }

    /***
     * 
     * @return true if this set causes the board to obtain the same state as previously
     */
    public boolean checkPreviousBoardState(int set, Color c) {
    	int diff = 0;
    	int diffAll = 0;
        Color[] nieuw = new Color[dim*dim];		// maak een array die het board na de zet simuleert
        
        for (int k = 0; k < fields.length; k++) {
        	nieuw[k] = fields[k];
        }
        
       // nieuw = fields; this results in a deep copy EVIL!
        nieuw[set] = c;
       
       // en check of het board dan hetzelfde zou zijn als //
        
        for (Color[] col:pastBoardStates) {	//loop door alle past boardstates
        	diff = 0;
        	for (int j = 0; j < dim*dim; j++) {
        		if (col[j] != nieuw[j]) {			//vergelijk alle elementen in oudeBoardStatex met alle elementen in nieuw
        			diff++;
        		}
        	}
        	if (diff > 0) { //als er differences zijn gevonden
        		diffAll++;
        	} else {
        		return true; //als er geen gevonden zijn, geef meteen true terug
        	}
        }
        	if (diffAll >= pastBoardStates.length) { //check of alle pastBoardStates were different from nieuw
        		return false;
        	} else {
        		return true;
        	}
    }
    
    /**
     * Sets the content of field i to the color c.
     * Checks whether this is a valid move and therefore sets the move only if valid //should not validate here MODEL CLASS!!
     * Otherwise, it lets the user know that this is an invalid set
     * 
     * Possible to take stones away here when set was a capture
     * 
     * @param i
     *            the field number
     * @param c
     *            the color to be placed
     */
    public void setField(int i, Color c) {
    	fields[i] = c;
    	for (int k = 0; k < dim*dim; k++) {
    		pastBoardStates[pastBoardStates.length-1][k] = fields[k];
    	}
    	
    	
    }
    		//if (isCaptured) {
    		// haal de stenen weg die weg moeten
    		//}
    		
    		
    	
   
    

    /**
     * Sets the content of the field represented by the (row,col) pair to the
     * mark m.
     *
     * @param row
     *            the field's row
     * @param col
     *            the field's column
     * @param c
     *            the color to be placed
     */
    public void setField(int row, int col, Color c) {
    	this.setField(index(row, col), c);
    }
    
    
    /***
     * checks whether a stone on field i is captured
     * A stone or solidly connected group of stones of 
     * one color is captured and removed from the board 
     * when all the intersections directly adjacent to it 
     * are occupied by the enemy. (Capture of the enemy takes precedence over self-capture.)
     * @return
     */
    public boolean isCaptured(int i, Color c) { //hoe doe je dat met connected group of stones?
    	
    	Color[] checkNeighbours = getNeighbours(i);
    	int occupied = 0;
    	int edges = 0;
    	//getOther() .. capture only when all adjacent intersections are occupied by opponent
    	
    	for (int k = 0; k < checkNeighbours.length; k++) { //simpel capture: one stone is captured
    		if (checkNeighbours[k].equals(Color.getOther(c))) {
    			occupied++;
    		} else if (checkNeighbours[k].equals(Color.OFF)) { //check for edges with Color.OFF
    			edges++;
    		}
    	}
    	if (occupied == 4-edges) {
    		return true;
    	}
    	return false;
    }
    
    
    /***
     * Returns an array with all neighbours, 
     * when the given index is at the board edge and therefore has no direct neighbour in one of the directions,
     * the Color OFF is returned in the array
     * @param i
     * @return array with colors of all adjacent intersections in the following order: left, above, right, down
     */
    public Color[] getNeighbours(int i) {
    	Color[] n = new Color[4]; //making new array for neighbours
	   	
	   	if (i % dim != 0) { //i is not at the left edge
	   		n[0] = getField(i-1);
	   		
	   	} else {
	   		n[0] = Color.OFF;
	   	}
	   	
	   	if (i > dim-1) { //i is not at the upper edge
	   		n[1] = getField(i-dim);
	   		
	   	} else {
	   		n[1] = Color.OFF;
	   	}
	   	
	   	if (i % dim != dim-1) { //i is not at the right edge
	   		n[2] = getField(i+1);
	   		
	   	} else {
	   		n[2] = Color.OFF;
	   	}
	   	
	   	if (i < (dim*dim)-dim-1) { //i is not at the bottom edge
	   		n[3] = getField(i+dim);
	   		
	   	} else {
	   		n[3] = Color.OFF;
	   	}
	   	return n;
    }
    
    /***
     * Removes stones from given index
     * After method execution this field will be Color.EMPTY again
     * @param i
     */
    public void remove(ArrayList<Integer> i) {
    	for (int k = 0; k < i.size(); k++) {
    		fields[i.get(k)] = Color.EMPTY;
    	}
    	
    }
    
    /***
     * Calculates the score at the end of the game
     * Wit krijgt + 0.5 punten voor nadeel
     * @return
     */
    public int[] score() { //TODO
    	return null;
    }
    
    /***
     * 
     * @return fields
     */
    public Color[] getFields() {
    	return fields;
    }
    
    /***
     * 
     * @param choice
     * @return true when valid, otherwise false
     */
    public boolean isValidMove(int choice, Color c) {
    	if (choice == -1) {
    		return true;
    	} else {
    		return isField(choice) && isEmptyField(choice) && !checkPreviousBoardState(choice, c);
    	}
    	
    }
   
    
// ------------------------ TUI ---------------------- //	
	
    //private static final String[] NUMBERING = {" 0 | 1 | 2 ", "---+---+---",
   //     " 3 | 4 | 5 ", "---+---+---", " 6 | 7 | 8 "};
    //private static final String LINE = NUMBERING[82];
    private static final String DELIM = "     \r";
	
    /**
     * Returns a String representation of this board. In addition to the current
     * situation, the String also shows the numbering of the fields.
     *
     * @return the game situation as String
     */
	
    public String toString() { 
        String s = "";
        for (int i = 0; i < dim; i++) {
            String row = "";
            for (int j = 0; j < dim; j++) {
                row = row + " " + getField(i, j).toString() + " " + index(i,j);
                if (j < dim - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM; //+ NUMBERING[i * 2];
           // if (i < dim - 1) {
             //   s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
            //}
        }
        
        
        return s;
    }	
	
	
	
	
}
