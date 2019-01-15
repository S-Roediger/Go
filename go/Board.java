package go;

public class Board {
/***
 * Model class, keep log of game changes, rules
 * 
 * Board maakt TUI
 */
	
	private Color fields[];
	private int dim; // how do we get dimensions? input from server client
	
// --------------------- Constructor ---------------- //
	
	/***
	 * Creates empty board
	 */
	public Board(int dim) {
		this.dim = dim;
		// initializeer het board met fields[] TODO
	}
	
	
	
// --------------------- Commands & Queries ------------------- //
	
	/***
	 * Creates a deepCopy from the current board
	 * @return deepCopy of board
	 */
	public Board deepCopy() {
		//TODO
		return null;
	}
	
	
	public int index(int row, int col) {
		//TODO
		return 0;
	}
	
    /**
     * Returns true if ix is a valid index of a field on the board.
     * @return true if 0 <= index < DIM*DIM
     */
    public boolean isField(int index) {
    	return index >= 0 && index < DIM * DIM;
    	//TODO
    }
    
    /**
     * Returns true of the (row,col) pair refers to a valid field on the board.
     *
     * @return true if 0 <= row < DIM && 0 <= col < DIM
     */
    public boolean isField(int row, int col) {
        return row >= 0 && row < DIM && col >= 0 && col < DIM;
      //TODO
    }
    
    /**
     * Returns the content of the field i.
     *
     * @param i
     *            the number of the field (see NUMBERING)
     * @return the mark on the field
     */

    public Color getField(int i) {
    	if (isField(i)) {
    		return fields[i];
    	}
        return null;
      //TODO
    }
    
    /**
     * Returns the content of the field referred to by the (row,col) pair.
     *
     * @param row
     *            the row of the field
     * @param col
     *            the column of the field
     * @return the mark on the field
     */
    public Color getField(int row, int col) {
    	if (isField(row, col)) {
    		return getField(index(row, col));
    	}
        return null;
      //TODO
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
      //TODO
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
      //TODO
    }
    
    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or both players have passed.
     *
     * @return true if the game is over
     */
    public boolean gameOver() {
        return false;
      //TODO
    }
    
    public boolean hasWinner() {
    	return false;
    	//TODO
    }
    
    

    /**
     * Empties all fields of this board (i.e., let them refer to the value
     * Mark.EMPTY).
     */
    public void reset() {
    	for (int i = 0; i < DIM * DIM; i++) {
    		fields[i] = Color.EMPTY;
    	}
    	//TODO
    }

    
    /**
     * Sets the content of field i to the mark m.
     *
     * @param i
     *            the field number (see NUMBERING)
     * @param m
     *            the mark to be placed
     */
    public void setField(int i, Color c) {
    	if (this.isField(i) && this.isEmptyField(i)) {
    		fields[i] = c;
    	}
    	//TODO
    }

    /**
     * Sets the content of the field represented by the (row,col) pair to the
     * mark m.
     *
     * @param row
     *            the field's row
     * @param col
     *            the field's column
     * @param m
     *            the mark to be placed
     */
    public void setField(int row, int col, Color c) {
    	this.setField(index(row, col), c);
    }
// ------------------------ TUI ---------------------- //	
	
    public static final int DIM = 3;
    private static final String[] NUMBERING = {" 0 | 1 | 2 ", "---+---+---",
        " 3 | 4 | 5 ", "---+---+---", " 6 | 7 | 8 "};
    private static final String LINE = NUMBERING[1];
    private static final String DELIM = "     ";
	
    /**
     * Returns a String representation of this board. In addition to the current
     * situation, the String also shows the numbering of the fields.
     *
     * @return the game situation as String
     */
	
    public String toString() { //TODO
        String s = "";
        for (int i = 0; i < DIM; i++) {
            String row = "";
            for (int j = 0; j < DIM; j++) {
                row = row + " " + getField(i, j).toString() + " ";
                if (j < DIM - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM + NUMBERING[i * 2];
            if (i < DIM - 1) {
                s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
            }
        }
        return s;
    }	
	
	
	
	
}
