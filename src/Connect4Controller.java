
/**
 * FILE: Connect4Controller.java
 * Purpose: This is the controller which will connect the model and
 * the View of the game.
 *
 * @author Trong Nguyen
 * 
 * @author Kailai huang
 */
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import javafx.application.Platform;

public class Connect4Controller {
	private Connect4Model model;
	private final int ROW = 6;
	private final int COL = 7;
	private Socket connection;
	private boolean isServer = false;
	private boolean isConnected = false;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public Connect4Controller(Connect4Model model) {
		this.model = model;
	}

	/**
	 * Purpose: set up the server for the game
	 */
	public void startServer() {
		try {
			ServerSocket server = new ServerSocket(4000);
			//This blocks, and that's bad. But it's only a short block
			//so we won't solve it. Once the client connects, it will
			//unblock.
			connection = server.accept();

			//When we reach here, we have a client, so grab the
			//streams to do our message sending/receiving
			oos = new ObjectOutputStream(connection.getOutputStream());
			ois = new ObjectInputStream(connection.getInputStream());

			isServer = true;
			isConnected = true;
			model.setMyTurn(true);
			if(model.getHuman() == false) {
				int computer = compTurn();
				makeCircle(computer);
			}
		}
		catch(IOException e) {
			System.err.println("Something went wrong with the network! " + e.getMessage());
		}
	}


	/**
	 * Purpose: set up the client for the game
	 */
	public void startClient() {
		try {
			connection = new Socket("localhost", 4000);

			isServer = false;
			isConnected = true;
			model.setMyTurn(false);
			oos = new ObjectOutputStream(connection.getOutputStream());
			ois = new ObjectInputStream(connection.getInputStream());

			//A thread represents code that can be done concurrently
			//to other code. We have the "main" thread where our program
			//is happily running its event loop, and now we
			//create a second thread whose entire job it is to send
			//our message to the other program and to block (wait) for 
			//their response.
			Thread t = new Thread(() -> {
				try {
					Connect4MoveMessage otherMsg = (Connect4MoveMessage)ois.readObject();

					//The runLater method places an event on the main
					//thread's event queue. All things that change UI
					//elements must be done on the main thread. 
					Platform.runLater(() -> {
						model.setMyTurn(true);
						model.updateTurn(otherMsg.getRow(), otherMsg.getColumn(), otherMsg.getColor());		
						if(model.getHuman() == false) {
							int computer = compTurn();
							makeCircle(computer);
							
						}

					});
					//We let the thread die after receiving one message.
					//We'll create a new one on the next click.
				}
				catch(IOException | ClassNotFoundException e) {
					System.err.println("Something went wrong with serialization: " + e.getMessage());
				}
			});
			//This is when the thread begins running - so now. 
			t.start();
			//Even though this method is done, the code of the thread
			//keeps doing its job. We've just allowed the event handler
			//to return and the event loop to keep running.
		}
		catch(IOException e) {
			System.err.println("Something went wrong with the network! " + e.getMessage());
		}
	}

	/**
	 * Purpose: update new tokens into the board of client 
	 * 
	 * @param msg - the message from server
	 */
	private void sendMessage(Connect4MoveMessage msg) {
		if(!isConnected) { return; }

		//See the code above that does the same. Here we will send
		//a message and receive one in the new thread, avoiding
		//blocking the event loop.
		Thread t = new Thread(() -> {
			try {
				oos.writeObject(msg);
				oos.flush();
				Connect4MoveMessage otherMsg = (Connect4MoveMessage) ois.readObject();
				Platform.runLater(() -> {
					model.setMyTurn(true);
					model.updateTurn(otherMsg.getRow(), otherMsg.getColumn(), otherMsg.getColor());
					if(model.getHuman() == false) {
						int computer = compTurn();
						makeCircle(computer);
					}
				});
			}
			catch(IOException | ClassNotFoundException e) {
				System.err.println("Something went wrong with serialization: " + e.getMessage());
			}
		});
		t.start();
	}

	/**
	 * Purpose: update new tokens into the board of server, send message too the client
	 * 
	 * @param x - the column for current token
	 * 
	 */
	public void makeCircle(int x) {
		//Add the circle to our model
		int nextRow = model.isLegal(x);
		Connect4MoveMessage msg;
		model.updateTurn(nextRow, x, (isServer) ? Connect4MoveMessage.RED : Connect4MoveMessage.YELLOW);
		msg = new Connect4MoveMessage(nextRow, x, (isServer) ? Connect4MoveMessage.RED : Connect4MoveMessage.YELLOW);
		//Send the new circle to the other person
		sendMessage(msg);
	}

	/** 
	 * Purpose: place a new token into certain column.
	 * 
	 * @param col - the column we want to put the new token.
	 * 
	 * @param color - the color of the new element that will be put
	 * 
	 * @return boolean - if the move is valid or not.
	 */
	public boolean move(int col, int color) {
		boolean legal = model.putPieces(col, color);
		if (legal == true) {
			if (isGameOver() != 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/** 
	 * Purpose: find a random column to place a new token for computer
	 * 
	 * @return compCol - a legal random column to place a new token
	 */
	public int compTurn() {
		Random rand = new Random();
		int compCol = rand.nextInt(7) + 1;
		while(model.isLegal(compCol) == -1) {
			compCol = rand.nextInt(7) + 1;
		}
		return compCol;
	}

	/** 
	 * Purpose: check whether there is a win in column for current token
	 * 
	 * @return boolean - if there is a win in column
	 */
	private boolean checkColumn() {
		int row = model.row();
		int col = model.col();
		int count = 0;
		// checks pieces on the same column
		while (row < ROW-1 && model.getPiece(row, col) == model.getPiece(row+1, col)){			
			count++;
			row++;
		}
		if (count >= 3)
			return true;
		else
			return false;
	}

	/** 
	 * Purpose: check whether there is a win in row for current token
	 * 
	 * @return boolean - if there is a win in row
	 */
	private boolean checkRow(){
		int row = model.row();
		int col = model.col();
		int count = 0;

		// checks pieces on the same row (to the right)
		while (col < COL-1 && model.getPiece(row, col) == model.getPiece(row, col+1)){
			count++;
			col++;
		}


		row = model.row();
		col = model.col();
		// checks pieces on the same row (to the left)
		while (col > 0 && model.getPiece(row, col) == model.getPiece(row, col-1)){
			count++;
			col--;
		}
		if (count >= 3)
			return true;
		else
			return false;
	}

	/** 
	 * Purpose: check whether there is a win in diagonals for current token
	 * 
	 * @return boolean - if there is a win in diagonals
	 */
	private boolean checkDiagonals() {
		int row = model.row();
		int col = model.col();
		int count = 0;

		// check right diagonal (downwards)
		while (col > 0 && row < ROW-1 && model.getPiece(row, col) == model.getPiece(row+1, col-1)){
			count++;
			row++;
			col--;
		}	

		row = model.row();
		col = model.col();
		// check right diagonal (upwards)
		while (col < COL-1 && row > 0 && model.getPiece(row, col) == model.getPiece(row-1, col+1)){
			count++;
			row--;
			col++;
		}

		if (count >= 3)
			return true;


		row = model.row();
		col = model.col();
		count = 0;
		// check left diagonal (downwards)
		while (col < COL-1 && row < ROW-1 && model.getPiece(row, col) == model.getPiece(row+1, col+1)){
			count++;
			row++;
			col++;
		}

		row = model.row();
		col = model.col();
		// check left diagonal (upwards)
		while (col > 0 && row > 0 && model.getPiece(row, col) == model.getPiece(row-1,col-1)){
			count++;
			row--;
			col--;
		}
		if (count >= 3)
			return true;
		else
			return false;
	}

	/** 
	 * Purpose: check whether the board is full
	 * 
	 * @return boolean - whether the board is full
	 */
	public boolean fullBoard() {
		for (int i = 0; i<ROW; i++) {
			for (int j = 0; j<COL; j++) {
				if (model.getPiece(i, j) == 'W') {
					return false;
				}
			}
		}
		return true;
	}

	/** 
	 * Purpose: check whether the the game is over
	 * 
	 * @return values represent for the winning team.
	 */
	public int isGameOver() {
		if(checkRow() == true || checkColumn() == true || checkDiagonals() == true) {
			if(model.getTurn() == true) {
				return 1;  // player1 win return 1
			} else {
				return 2;  // player2 win return 2
			}
		} else if(fullBoard() == true) {
			return 3;  // board is full then return 3
		} 
		return 0; // if the game is not over then return 0
	}

	/** 
	 * Purpose: check the current row
	 * 
	 * @return model.row() - the current row
	 */
	public int currRow() {
		return model.row();
	}

	/** 
	 * Purpose: check the current column
	 * 
	 * @return model.col() - the current column
	 */
	public int currCol() {
		return model.col();
	}
}
