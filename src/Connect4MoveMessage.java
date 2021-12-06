/**
 * FILE: Connect4MoveMessage.java
 * Purpose: This is the instance of the object on the board which
 * represent for the moves of the users
 * 
 * @author Trong Nguyen
 * 
 * @author Kailai huang
 */
import java.io.Serializable;

public class Connect4MoveMessage implements Serializable {
	public static int YELLOW = 1;
	public static int RED = 2;

	private static final long serialVersionUID = 1L;

	private int row;
	private int col;
	private int color;

	// The constructor of the class
	public Connect4MoveMessage(int row, int col, int color) {
		this.row = row;
		this.col = col;
		this.color = color;
	}

	/** 
	 * Purpose: check the current row
	 * 
	 * @return row - the current row
	 */
	public int getRow() { 
		return row; 
	} 

	/** 
	 * Purpose: check the current column
	 * 
	 * @return col - the current column
	 */
	public int getColumn() { 
		return col;
	}
	
	/** 
	 * Purpose: check the current color
	 * 
	 * @return color - the current color
	 */
	public int getColor() { 
		return color; 
	}
}

