package go;

public class Board {
/***
 * Model class, keep log of game changes, rules
 * 
 * Board maakt TUI
 */
	
	private Color[] prevGameStatus;
	private Color[] fields;
	private int dim; 
	private int pass;
	
	
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
    	if (isField(i)) {
    		return fields[i];
    	}
        return null;
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
    	if (isField(row, col)) {
    		return getField(index(row, col));
    	}
        return null;
    }
    
    /**
     * Returns true if the field i is empty.
     *
     * @param i
     *            the index of the field (see NUMBERING)
     * @return true if the field is empty
     */
    public boolean isEmptyField(int i) {
    	if (isField(i)) {
    		return fields[i] == Color.EMPTY;
    	}
        return false;
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
    	if (isField(row, col)) {
    		return fields[index(row, col)] == Color.EMPTY;
    	}
        return false;
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
    
    public boolean hasWinner() {
    	return false;
    	//TODO
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

    
    /**
     * Sets the content of field i to the color c.
     *
     * @param i
     *            the field number (see NUMBERING)
     * @param c
     *            the color to be placed
     */
    public void setField(int i, Color c) {
    	if (this.isField(i) && this.isEmptyField(i)) {
    		fields[i] = c;
    	}
   
    }

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
     * checks whether recent move ends in capture
     * @return
     */
    public boolean isCaptured() {
    	return false;
    }
    
    /***
     * Calculates the score at the end of the game
     * @return
     */
    public int[] score() {
    	return null;
    }
    
    /***
     * Check whether recent move recreated previous game situation (immediately before)
     * @return false immediate game state before was not recreated, otherwise return true
     */
    public boolean didRecreate(Board prevBoard) {
    	// compare with current board (gameStatus/fields), if the same return true
    	return false;
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
