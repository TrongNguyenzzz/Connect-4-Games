/**
 * FILE: Connect4.java
 * Purpose: This is the main class to call the View class when the users
 * want to play the game. When the View is called, the window of the game
 * will be presented and the users can start playing.
 * 
 * @author Trong Nguyen
 *  
 * @author Kailai huang
 */
import javafx.application.Application;

public class Connect4 {
	public static void main(String[] args) {
		Application.launch(Connect4View.class, args);
	}
}
