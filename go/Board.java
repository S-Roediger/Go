package go;

import java.util.ArrayList;
import java.util.Random;


public class Board {
/***
 * Model class, keep log of game changes and rules.
 */
	
// ------------------ Fields --------------------- //	

	private Color[] fields;
	private int dim; 
	private int pass;
	private Color[][] pastBoardStates;
	private ArrayList<Color> currentNeighColor;
	private ArrayList<Integer> currentNeighIndex;
	private ArrayList<Integer> checkedStonesGetGroup;
	
	
// --------------------- Constructor ---------------- //
	
	/***
	 * Creates an empty board with given dimensions.
	 * Initializes the int pass to keep track of passes from players
	 * Initializes 2d array pastBoardStates and adds initial board state to keep track of history
	 * Initializes an arrayList of current neighbour colors
	 * Initializes an arrayList of current neighbour indices
	 * Initializes an arrayList of checked stones for the method 'getGroup'
	 * @param dim - Dimension of the board 
	 */
	public Board(int dim) {
		this.dim = dim;
		fields = new Color[dim * dim];
		for (int i = 0; i < dim * dim; i++) {
			fields[i] = Color.EMPTY;
		}
		pass = 0;
		pastBoardStates = new Color[dim * dim][dim * dim];
		pastBoardStates[0] = fields; 
		currentNeighColor = new ArrayList<Color>();
		currentNeighIndex = new ArrayList<Integer>();
		checkedStonesGetGroup = new ArrayList<Integer>();
	}
	
	
	/***
	 * Creates a new board from a String representation of a board.
	 * @param dim - Dimension of the board
	 * @param s - String representation of a board ('0' = Empty; '1' = Black; '2' = White)
	 */
	public Board(int dim, String s) {
		this.dim = dim;
		this.fields = new Color[dim * dim];
		char[] c = s.toCharArray();
		
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '0') {
				fields[i] = Color.EMPTY;
			} else if (c[i] == '1') {
				fields[i] = Color.BLACK;
			} else if (c[i] == '2') {
				fields[i] = Color.WHITE;
			}
		}
		pastBoardStates = new Color[dim * dim][dim * dim];
		pastBoardStates[0] = fields; 
	}
	
	
	
// --------------------- Commands & Queries ------------------- //
	
	/***
	 * Counter variable that keeps track of the amount of times that players passed.
	 * @return Integer representing the current amount of passes 
	 * from both players (during current round) 
	 */
	public int getPass() {
		return pass;
	}
	
	/***
	 * Method to increase the Integer pass that counts current amount of passes.
	 */
	public void increasePass() {
		pass++;
	}
	
	/***
	 * Method to reset the pass Integer.
	 */
	public void resetPass() {
		pass = 0;
	} 
	
	/***
	 * Method to convert the coordinates row and col to an index.
	 * @param row - representing x coordinate of board
	 * @param col - representing y coordinate of board 
	 */
	public int index(int row, int col) {
		return dim * row + col;
	}
	
    /**
     * Returns true if ix is a valid index of a field on the board.
     * @param index - Integer representing index on board
     * @return true if 0 <= index < dim * dim
     */
    public boolean isField(int index) {
    	return index >= 0 && index < dim * dim;
    }
    
    /**
     * Returns the content of the field i.
     * @param i - Integer representing index on board
     * @return the color on the field
     */
    public Color getField(int i) {
    	return fields[i];
    }
    
    /**
     * Returns the content of the field referred to by the (row,col) pair.
     * @param row - representing x coordinate of board
     * @param col - representing y coordinate of board
     * @return the color on the field
     */
    public Color getField(int row, int col) {
    	return getField(index(row, col));
    	
    }
    
    /**
     * Returns true if the field i is empty.
     * @param i - Integer representing index on board
     * @return true if the field is empty
     */
    public boolean isEmptyField(int i) {
    	return fields[i] == Color.EMPTY;
    }
    
    /**
     * Returns true if the game is over. The game is over when both players have passed.
     * @return true if the game is over, false if the game is not yet over.
     */
    public boolean gameOver() {
        if (pass > 1) {
        	return true;
        }
        return false;
    }
    
    /**
     * Empties all fields of this board (i.e., let them refer to the value Color.EMPTY).
     */
    public void reset() {
    	for (int i = 0; i < dim * dim; i++) {
			fields[i] = Color.EMPTY;
		}
    	pass = 0;
    }

    /***
     * Method to check whether a particular set with a particular color would recreate
     * a previous board state.
     * @param set - index of the potential set that player attempts to make
     * @param c - Color of player that attempts to make the particular set
     * @return true if this particular set causes the board to obtain the same state as previously
     */
    public boolean checkPreviousBoardState(int set, Color c) {
    	int diff = 0;
    	int diffAll = 0;
    	//create new array that simulates board after potential set is done
        Color[] newBoard = new Color[dim * dim];		
        
        for (int k = 0; k < fields.length; k++) {
        	newBoard[k] = fields[k];
        }
        //make the potential set in the newly created boardCopy
        newBoard[set] = c;
       
       //now you can check whether this particular board constellation was created previously
        
        for (Color[] col:pastBoardStates) {	//loop through all past board states
        	diff = 0;
        	for (int j = 0; j < dim * dim; j++) {
        		if (col[j] != newBoard[j]) { //compare all fields of the old board constellation 
        			diff++;					//with all fields of newBoard
        		}
        	}
        	if (diff > 0) { //if there were any difference found increase the difference count
        		diffAll++;
        	} else {
        		return true; //otherwise immediately return true, 
        	}				//since this set would recreate a previous board state
        }
        if (diffAll >= pastBoardStates.length) { //check whether all past board states 
        	return false;						// were different from newBoard
        } else {
        	return true;
        }
    }
    
    /**
     * Sets the content of field i to the color c.
     * Immediately adds the new board constellation to the list of past board states
     * @param i - Integer representing index on board
     * @param c - the color to be placed     
     */
    public void setField(int i, Color c) {
    	fields[i] = c;
    	for (int k = 0; k < dim * dim; k++) {
    		pastBoardStates[pastBoardStates.length - 1][k] = fields[k];
    	}
    }
    
    
    /***
     * checks whether a stone on field i is captured
     * A stone or solidly connected group of stones of 
     * one color is captured and removed from the board 
     * when all the intersections directly adjacent to it 
     * are occupied by the enemy. (Capture of the enemy takes precedence over self-capture.)
     * 
     * @param Color c - Color of the player that potentially captured a group (if single stone
     *  is tested and suicide is tested), otherwise c needs to be Color.EMPTY since with groups
     *  you cannot check for same color neighbour, but have to check whether any of the stones
     * in group have Color.Empty as neighbour
     * @param ArrayList<Integer> group - Indexes of potentially captured fields
     * @return true if group is captured, otherwise not
     */
    public boolean isCaptured(Color c, ArrayList<Integer> group) {
    	
    	//needs to be called after getGroups() is called, to make sure list is reset for next call
    	this.checkedStonesGetGroup.clear();
    	
    	//if there is no stone (empty list is checked), this stone cannot be captured
    	if (group.size() == 0) {
    		return false;
    	}
    	
    	int freeIntersections = 0;
    	
    	for (int i = 0; i < group.size(); i++) {
    		ArrayList<Color> neighColors = getCurrentNeighColor(group.get(i));
    		for (Color co:neighColors) {
    			//if one of the borders has the opponent color as neighbour (or Empty),
    			//the area is not captured/owned
    			if (co.equals(Color.getOther(c))) { 
    				return false;
    			}
    			if (co.equals(Color.EMPTY)) { 
    				freeIntersections++;
    			}
    		}
    	}
    	if (freeIntersections >= group.size() * group.size()) { 
    		return false;
    	}
    	return true;
    }
     
    
    /***
     * Finds a connected group of same colored stones and puts in
     * a list containing the indexes of all stones in the group.
     * @param i - Index for which we want to find all related neighbours
     * @param c - Color of index i
     * @param ArryList<Integer> group - Base group that will be filled with related
     * indices during function execution
     */
    public void getGroup(int i, Color c, ArrayList<Integer> group) {
    	
    	//Since this function is recursive we need a stop condition
    	//We stop when we check an index that we already checked before
    	if (this.checkedStonesGetGroup.contains(i)) {
    		return;
    	}
 
    	
    	if (!group.contains(i)) {
    		group.add(i);
    		this.checkedStonesGetGroup.add(i);
    	}
    	
    	for (int j = 0; j < this.getCurrentNeighColor(i).size(); j++) {
    		//if one of the current neighbours of i equals its color 
    		if (this.getCurrentNeighColor(i).get(j).equals(c)) {
    			// and our group does not contain this neighbour yet
    			if (!group.contains(this.getCurrentNeighIndex(i).get(j))) {		
    				//look again whether the neighbours of this (neighbouring) index
    				//have the same color
    				getGroup(this.getCurrentNeighIndex(i).get(j), c, group);
    			}
    		}
    	}
    	
    }
    
    /***
     * Returns an array with all neighbours. 
     * When the given index is at the board edge 
     * and therefore has no direct neighbour in one of the directions,
     * the Color OFF is returned in the array
     * @param i - index on the board
     * @return array with colors of all adjacent intersections
     * in the following order: left, above, right, down
     */
    public void updateCurrentNeighbours(int i) {
    	
    	currentNeighColor.clear();
    	currentNeighIndex.clear();
    	
	   	
	   	if (i % dim != 0) { //i is not at the left edge
	   		this.currentNeighColor.add(getField(i - 1));
	   		this.currentNeighIndex.add(i - 1);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i - 1);
	   	}
	   	
	   	if (i > dim - 1) { //i is not at the upper edge
	   		this.currentNeighColor.add(getField(i - dim));
	   		this.currentNeighIndex.add(i - dim);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i - dim);
	   	}
	   	
	   	if (i % dim != dim - 1) { //i is not at the right edge
	   		this.currentNeighColor.add(getField(i + 1));
	   		this.currentNeighIndex.add(i + 1);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i + 1);
	   	}
	   	
	   	if (i < (dim * dim) - dim - 1) { //i is not at the bottom edge
	   		this.currentNeighColor.add(getField(i + dim));
	   		this.currentNeighIndex.add(i + dim);
	   		
	   	} else {
	   		this.currentNeighColor.add(Color.OFF);
	   		this.currentNeighIndex.add(i + dim);
	   	}
    }
    
    
    /***
     * Updates and returns the field ArrayList<Color> currentNeighColor for index i.
     * @param i - index on board
     * @return ArrayList containing the colors of the neighbours of field i
     */
    public ArrayList<Color> getCurrentNeighColor(int i) {
    	this.updateCurrentNeighbours(i);
    	return this.currentNeighColor;
    }
    
    /***
     * Updates and returns the field ArrayList<Integer> currentNeighIndex for index i.
     * @param i - index on board
     * @return ArrayList containing the indices of the neighbours of field i
     */
    public ArrayList<Integer> getCurrentNeighIndex(int i) {
    	this.updateCurrentNeighbours(i);
    	return this.currentNeighIndex;
    }
    
    /***
     * Removes stones from given index.
     * After method execution this field will have the Color.EMPTY again
     * @param i - index on board
     */
    public void remove(ArrayList<Integer> i) {
    	for (int k = 0; k < i.size(); k++) {
    		fields[i.get(k)] = Color.EMPTY;
    	}
    	
    }
    
    /***
     * Checks whether all fields of the current board have the color EMPTY.
     * @return true if the board is empty, otherwise false
     */
    public boolean boardIsEmpty() {
    	int empty = 0;
    	for (int i = 0; i < fields.length; i++) {
    		if (fields[i] == Color.EMPTY) {
    			empty++;
    		}
    	}	
    	if (empty >= fields.length) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /***
     * Calculates the score at the end of the game.
     * White gets bonus points in the amount of 0.5 points for being second player.
     * @return an int array containing the final score of both players
     */
    public double[] getScore() { 
    	//needs to be called after (&before) getGroups() is called,
    	//to make sure list is reset for next call
    	this.checkedStonesGetGroup.clear();
    	double[] score = new double[2];
    	double scoreWhite = 0;
    	double scoreBlack = 0;
    	ArrayList<Integer> checkFields = new ArrayList<>();
    	
    	if (boardIsEmpty()) {
        	score[0] = scoreBlack;
        	score[1] = scoreWhite + 0.5;
    		return score;
    	}
    	
    	//get an array containing all indexes of the board
    	for (int i = 0; i < fields.length; i++) { 
    		checkFields.add(i);
    		//add a point to the black score for every black stone
    		if (fields[i].equals(Color.BLACK)) {
    			scoreBlack++;	
    		}
    		//add a point to the white score for every white stone
    		if (fields[i].equals(Color.WHITE)) {  
    			scoreWhite++;	
    		}
    	}
    	
    	for (int j = 0; j < fields.length; j++) {
    		ArrayList<Integer> group = new ArrayList<>();
    		//find empty groups
    		if (checkFields.contains(j) && fields[j].equals(Color.EMPTY)) {
    			getGroup(j, Color.EMPTY, group); 
    			
    			//if empty group was owned by white assign points to white
    			if (isCaptured(Color.WHITE, group)) { 
    				scoreWhite += group.size();	
    				
    			//if empty group was owned by black assign points to black
    			} else if (isCaptured(Color.BLACK, group)) {
    				scoreBlack += group.size();
    				
    			}
    			//remove groups that you already checked from checkFields
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
     * Update board fields with String.
     * @param s - String representation of a board
     */
    public void update(String s) {
		char[] c = s.toCharArray();
		
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '0') {
				fields[i] = Color.EMPTY;
			} else if (c[i] == '1') {
				fields[i] = Color.BLACK;
			} else if (c[i] == '2') {
				fields[i] = Color.WHITE;
			}
		}
		//Since in the online game the client does not set moves, but
		//only updates the board using this method, the past board 
		//states need to be updated here too, so that an AI can check the
		//validity of its own moves
    	for (int k = 0; k < dim * dim; k++) {
    		pastBoardStates[pastBoardStates.length - 1][k] = fields[k];
    	}
    }
    
    /***
     * Method to get the current fields.
     * @return Color array containing the fields of the current board
     */
    public Color[] getFields() {
    	return fields;
    }
    
    /***
     * Method to check whether a move is valid.
     * @param choice - index representing the potential set of a player
     * @return true when potential set is valid, otherwise false
     */
    public boolean isValidMove(int choice, Color c) {
    	//-1 represents a pass and -99 represents the command 'Exit'
    	if (choice == -1 || choice == -99) {
    		return true;
    	} else {
    		return isField(choice) && isEmptyField(choice) && !checkPreviousBoardState(choice, c);
    	}
    	
    }
    
    /***
     * Determines a random valid move. 
     * @param c - Color that you would like to determine a valid move for
     * @param t - Integer indicating how much time the bot has to find
     * a random valid move before a pass is returned
     * @return Integer representing a valid random move
     */
    public int determineRandomValidMove(Color c, int t) {
    	
    	int[] fieldsIndexCopyPlusPass = new int[dim * dim + 1];
    	//make a copy of the current board fields
    	for (int i = 0; i < fields.length; i++) {
    		fieldsIndexCopyPlusPass[i] = i;
    	}
    	//Add a 'pass' to the possible moves too
    	fieldsIndexCopyPlusPass[fieldsIndexCopyPlusPass.length - 1] = -1;
    	long startTime = System.currentTimeMillis();
    	long elapsedTime = 0;
    	//choose a random field to set stone
    	int random = new Random().nextInt(fieldsIndexCopyPlusPass.length);
    	//check whether given time already passed
    	while (elapsedTime < t) {
    		//check validity of move and if valid return this index
    		if (isValidMove(random, c)) { 
    			return random;
    		}
    		//otherwise choose a new random index
    		random = new Random().nextInt(fieldsIndexCopyPlusPass.length);
    		elapsedTime = System.currentTimeMillis() - startTime;
    	}
    	//if you exceed thinking time (which currently will never happen), pass
    	return -1;
    	
    }
   
    
// ------------------------ TUI ---------------------- //	
	
    private static final String DELIM = "     \r";
	
    /**
     * Returns a String representation of this board. 
     * @return the game situation as String
     */
	
    public String toString() { 
        String s = "";
        for (int k = 0; k < dim; k++) {
        	s = s + "----------";
        }
        s = s + DELIM;
        for (int i = 0; i < dim; i++) {	
            String row = "";
            for (int j = 0; j < dim; j++) {
            	if (index(i, j) < 10) {
            		row = row + " " + getField(i, j).toString() + "  " + index(i, j);
            	} else if (index(i, j) >= 10 && index(i, j) < 100) {
            		row = row + " " + getField(i, j).toString() + " " + index(i, j);
            	} else {
            		row = row + " " + getField(i, j).toString() + "" + index(i, j);
            	}
            		
                if (j < dim - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM; 
            for (int k = 0; k < dim; k++) {
            	s = s + "----------";
            }
            s = s + DELIM;
        }    
        return s;
    }	
}
