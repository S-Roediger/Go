package go;

import java.util.ArrayList;


public class Board {
/***
 * Model class, keep log of game changes, rules
 * 
 * 
 */
	
	
	private Color[] fields;
	private int dim; 
	private int pass;
	private Color[][] pastBoardStates;
	private ArrayList<Color> currentNeighColor;
	private ArrayList<Integer> currentNeighIndex;
	private ArrayList<Integer> checkedStonesGetGroup;
	
	
// --------------------- Constructor ---------------- //
	
	/***
	 * Creates empty board with given dimensions
	 * Initializes the int pass to keep track of passes from players
	 * Initializes 2d array pastBoardStates and adds initial board state to keep track of history
	 * 
	 * 
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
		currentNeighColor = new ArrayList<Color>();
		currentNeighIndex = new ArrayList<Integer>();
		checkedStonesGetGroup = new ArrayList<Integer>();
	}
	
	
	
// --------------------- Commands & Queries ------------------- //
	/***
	 * 
	 * @return int counter variable that keeps track of the amount of times that players passed
	 */
	public int getPass() {
		return pass;
	}
	
	/***
	 * Method to increase int pass
	 */
	public void increasePass() {
		pass++;
	}
	
	/***
	 * Method to reset int pass
	 */
	public void resetPass() {
		pass = 0;
	}
	
	/***
	 * Creates a deepCopy from the current board
	 * @return deepCopy of board
	 */
	public Board deepCopy() { //TODO Do I really need this?
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
        Color[] nieuw = new Color[dim*dim];		//create new array that simulates board after potential set is done
        
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
     * @param Color c - Color of the player that potentially captured a group
     * @param ArrayList<Integer> group - Indexes of potentially captured fields
     * @return
     */
    public boolean isCaptured(Color c, ArrayList<Integer> group) { //TODO translate
    	
    	//needs to be called after getGroups() is called, to make sure list is reset for next call
    	this.checkedStonesGetGroup.clear(); //als je gaat kijken of je gevonden groep gecaptured is of niet, dan kan je de hulplijst voor getGroup resetten
    	
    	if (group.size() == 0) { //als er geen steen is, heeft deze ook geen freeIntersections en kan ook niet gecaptured worden
    		return false;
    	}
    	
    	int freeIntersections = 0;
    	
    	for (int i = 0; i < group.size(); i++) {
    		ArrayList<Color> neighColors = getCurrentNeighColor(group.get(i));
    		for (Color co:neighColors) {
    			if (co.equals(Color.getOther(c))) { //if one of the borders has the opponent color as neighbour, the area is not captured/owned
    				return false;
    			}
    			if (co.equals(Color.EMPTY)) { //GAAT DIT GOED?? TODO
    				freeIntersections++;
    			}
    		}
    	}
    	if (freeIntersections >= group.size()*group.size()) { //you need to take this to power of 2 because
    		return false;									//it needs to be >= otherwise it will go wrong when successful suicide move is done
    	}
    	return true;
    }
     
    
    /***
     * Finds a connected group of same colored stones and puts in a list containing the indexes of all stones in the group
     * @param i
     * @param c
     * @return
     */
    public void getGroup(int i, Color c, ArrayList<Integer> group) { //TODO comment this function
    	
    	
    	if (this.checkedStonesGetGroup.contains(i)) {
    		return;
    	}
 
    	
    	if (!group.contains(i)) {
    		group.add(i);
    		this.checkedStonesGetGroup.add(i);
    	}
    	
    	for (int j = 0; j < this.getCurrentNeighColor(i).size(); j++) {
    		if (this.getCurrentNeighColor(i).get(j).equals(c)) {
    			if (!group.contains(this.getCurrentNeighIndex(i).get(j))) {		
    				getGroup(this.getCurrentNeighIndex(i).get(j), c, group);
    			}
    		}
    	}
    	
    }
    
    /***
     * Returns an array with all neighbours, 
     * when the given index is at the board edge and therefore has no direct neighbour in one of the directions,
     * the Color OFF is returned in the array
     * @param i
     * @return array with colors of all adjacent intersections in the following order: left, above, right, down
     */
    public void updateCurrentNeighbours(int i) {
    	
    	currentNeighColor.clear();
    	currentNeighIndex.clear();
    	
	   	
	   	if (i % dim != 0) { //i is not at the left edge
	   		this.currentNeighColor.add(getField(i-1));
	   		this.currentNeighIndex.add(i-1);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i-1);
	   	}
	   	
	   	if (i > dim-1) { //i is not at the upper edge
	   		this.currentNeighColor.add(getField(i-dim));
	   		this.currentNeighIndex.add(i-dim);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i-dim);
	   	}
	   	
	   	if (i % dim != dim-1) { //i is not at the right edge
	   		this.currentNeighColor.add(getField(i+1));
	   		this.currentNeighIndex.add(i+1);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i+1);
	   	}
	   	
	   	if (i < (dim*dim)-dim-1) { //i is not at the bottom edge
	   		this.currentNeighColor.add(getField(i+dim));
	   		this.currentNeighIndex.add(i+dim);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i+dim);
	   	}
    }
    
    
    /***
     * updates and returns currentNeighColors
     * @param i
     * @return
     */
    public ArrayList<Color> getCurrentNeighColor(int i) {
    	this.updateCurrentNeighbours(i);
    	return this.currentNeighColor;
    }
    
    /***
     * updates and returns currentNeighIndex
     * @param i
     * @return
     */
    public ArrayList<Integer> getCurrentNeighIndex(int i) {
    	this.updateCurrentNeighbours(i);
    	return this.currentNeighIndex;
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
     * White gets bonus points in the amount of 0.5 points for being second player
     * @return an int array containing the final score of both players
     */
    public double[] getScore() { //TODO
    	//needs to be called after (&before) getGroups() is called, to make sure list is reset for next call
    	this.checkedStonesGetGroup.clear();
    	double[] score = new double[2];
    	double scoreWhite = 0;
    	double scoreBlack = 0;
    	ArrayList<Integer> checkFields = new ArrayList<>();
    	
    	for (int i = 0; i < fields.length; i++) { //get an array containing all indexes of the board
    		checkFields.add(i);
    		if (fields[i].equals(Color.BLACK)) { //add a point to black score for every black stone
    			scoreBlack++;
    			System.out.println("I just assigned a point to black for a stone on field : "+i);
    		}
    		if (fields[i].equals(Color.WHITE)) { // add a point to white 
    			scoreWhite++;
    			System.out.println("I just assigned a point to white for a stone on field : "+i);
    		}
    	}
    	
    	for (int j = 0; j < fields.length; j++) {
    		ArrayList<Integer> group = new ArrayList<>();
    		if (checkFields.contains(j) && fields[j].equals(Color.EMPTY)) {
    			getGroup(j, Color.EMPTY, group); //find empty groups
    			
    			System.out.println("This group is currently checked "+group);
    			System.out.println("This group is captured by white: "+isCaptured(Color.WHITE, group));
    			System.out.println("This group is captured by black: "+isCaptured(Color.BLACK, group));
    			if (isCaptured(Color.WHITE, group)) { 	//if white "captured"/owned this area
    				scoreWhite+= group.size();	//assign one point per owned field to white
    				System.out.println("I just assigned this amount of points to white: "+group.size());
    			} else if (isCaptured(Color.BLACK, group)) {	//if it was owned by black
    				scoreBlack+= group.size();	//assign points to black
    				System.out.println("I just assigned this amount of points to black: "+group.size());
    			}
    			
    			for (int k = 0; k < group.size(); k++) {
    				checkFields.remove(group.get(k));
    			}
    		}

    		
    	}
    	
    	
    	score[0] = scoreBlack;
    	score[1] = scoreWhite + 0.5;
    	return score;
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
	
    public String toString() { //TODO
        String s = "";
        for (int k = 0; k < dim + 1; k++) {
        	s = s + "--------";
        }
        s = s + "-" + DELIM;
        for (int i = 0; i < dim; i++) {	
            String row = "";
            for (int j = 0; j < dim; j++) {
            	if (index(i,j) < 10) {
            		row = row + " " + getField(i, j).toString() + "  " + index(i,j);
            	} else {
            		row = row + " " + getField(i, j).toString() + " " + index(i,j);
            	}
            		
                if (j < dim - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM; //+ NUMBERING[i * 2];
            for (int k = 0; k < dim + 1; k++) {
            	s = s + "--------";
            }
            s = s + "-" + DELIM;
        }
        
            
            
           // if (i < dim - 1) {
             //   s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
            //}
        
        
        
        return s;
    }	
	
	
	
	
}
