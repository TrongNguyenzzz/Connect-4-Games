/**
 * FILE: Connect4Model.java
 * Purpose: This is the model of the game Connect4 which will store the information
 * about the board, players and turn.
 * 
 * @author Trong Nguyen
 *  
 * @author Kailai huang
 */
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Connect4Model extends Observable{

	private Character[][] board;

	private final int ROW = 6; // The amounts of rows of the board

	private final int COL = 7; // The amount of cols of the board
	
	private boolean turn = false;
	
	private boolean isHuman = true;
	
	private int row;
	
	private int col;

	public Connect4Model() {
		board = new Character [ROW][COL];
		for (int i = 0; i<ROW; i++) {
			for (int j = 0; j < COL; j ++) {
				board[i][j] = 'W';
			}
		}
	}
	
	/** 
	 * Purpose: check certain token on the board
	 * 
	 * @param row - the row we want to check
	 * 
	 * @param col - the column we want to check
	 * 
	 * @return board[row][col] - the token on the place we choose
	 */
	public Character getPiece(int row, int col) {
		return board[row][col];
	}
	
	/** 
	 * Purpose: check the current row
	 * 
	 * @return row - the current row
	 */
	public int row() {
		return row;
	}
	
	/** 
	 * Purpose: check the current column
	 * 
	 * @return col - the current column
	 */
	public int col() {
		return col;
	}
	
	/** 
	 * Purpose: place a new token into certain column
	 * 
	 * @param column - the column we want to put the new token
	 * 
	 * @param turn - the color for current player
	 * 
	 * @return boolean - if the placement is legal or not
	 */
	public boolean putPieces(int column, int turn) {
		this.col = column - 1; //update the last col
		int currRow = ROW - 1;
		while(currRow >= 0 && board[currRow][this.col] != 'W' ) {
			currRow = currRow - 1;
		}
		if(currRow < 0) {
			return false;
		} else {
			Character currentTurn = 'W';
			if (turn == 2) {
				currentTurn = 'R';
			} else {
				currentTurn = 'Y';
			}
			board[currRow][this.col] = currentTurn;
			row = currRow;
			return true;
		}
	}
	
	/** 
	 * Purpose: check whether a column  is legal to place a new token
	 * 
	 * @param column - the column we want to put the new token
	 * 
	 * @return boolean - if the placement is legal or not
	 */
	public int isLegal(int column) {
		int currRow = ROW - 1;
		while(currRow >= 0 && board[currRow][column - 1] != 'W' ) {
			currRow = currRow - 1;
		}
		if(currRow < 0) {
			return -1;
		}
		return currRow;
	}
	
	/** 
	 * Purpose: print out the current board
	 */
	public void printBoard() {
		String bottom = "0 1 2 3 4 5 6";	
		for (int index = 0; index < ROW; index ++) {
			String currRow = "";
			for(int i = 0; i < COL; i ++) {
				currRow += Character.toString(board[index][i]) + " ";
			}
			System.out.println(currRow);
		}
		System.out.println(bottom);
	}

	/** 
	 * Purpose: return the current board
	 * 
	 * @return board - the current board
	 */
	public Character[][] board(){
		return board;
	}
	
	/** 
	 * Purpose:  change the turn for players
	 *
	 */
	public void changeTurn() {
		if (turn == true) {
			turn = false;
		} else {
			turn = true;
		}
	}
	
	/** 
	 * Purpose: return the current turn
	 * 
	 * @return turn - the current turn
	 */
	public boolean getTurn() {
		return turn;
	}
	
	/** 
	 * Purpose: set the current turn to be certain player
	 * 
	 * @param i - the current turn
	 */
	public void setMyTurn(boolean i) {
		turn = i;
	}
	
	/**
	 * Purpose: set the boolean value to see if the model
	 * is human or AI 
	 * 
	 * @param human - if it is human then true otherwise false
	 */
	public void setHuman(boolean human) {
		isHuman = human;
	}
	
	/**
	 * Purpose: Get the current status human/comp of the model
	 * 
	 * @return true if it is human and false otherwise
	 */
	public boolean getHuman() {
		return isHuman;
	}
	
	/**
	 * Purpose: Update the board based on the passed in parameters from the 
	 * controller
	 * 
	 * @param row - current row of the updated element
	 * 
	 * @param column - current column of the updated element
	 * 
	 * @param color - current color of the updated element
	 */
	public void updateTurn(int row, int column, int color) {
		int nextRow = isLegal(column);
		Connect4MoveMessage message = new Connect4MoveMessage(nextRow, column, color);
		setChanged();
		notifyObservers(message);
	}
}

