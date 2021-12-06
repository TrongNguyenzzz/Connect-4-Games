/**
 * FILE: Connect4View.java
 * Purpose: This is the class to create GUI view for the users, this class will also
 * use the information from the controller and model to create and update new tokens
 * based on the demands of the users
 * 
 * @author Trong Nguyen
 * 
 * @author Kailai huang
 */

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Connect4View extends Application implements Observer{
	private Connect4Model model;
	private Connect4Controller controller;
	private GridPane gridPane;
	private EventHandler<MouseEvent> sceneHandler;
	private EventHandler<MouseEvent> stageHandler;
	private EventHandler<MouseEvent> okHandler;
	private EventHandler<MouseEvent> cancelHandler;
	private RadioButton selected1;
	private RadioButton selected2;
	private Connect4MoveMessage message;
	private boolean canClick = true;
	private double x_coor;
	
	public Connect4View() {
		model = new Connect4Model();
		controller = new Connect4Controller(model);
	}
	
	/**
     * Purpose: Sets up the stage.
	 * 
	 * @param primaryStage - the primaryStage
     */
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		restartGame(primaryStage);
	}

	/**
	 * This method is used to start the game.
	 * 
	 * @param primaryStage: This is the main stage that we use to 
	 * draw the graphics on.
	 * 
	 * @throws Exception throws suitable exception
	 */
	private void restartGame(Stage primaryStage) throws Exception{
		// Border pane, which contains center and bottom
		BorderPane borderPane = new BorderPane();
		gridPane = new GridPane();
		Background background = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
		gridPane.setBackground(background);
		borderPane.setCenter(gridPane);
		Scene scene = new Scene(borderPane, 350, 320);
		sceneHandler = mouseHandler(scene, primaryStage);
		primaryStage.setTitle("Connect4");
		MenuItem item1 = new MenuItem("New Game");
		stageHandler = handleStage(scene, primaryStage, item1);
		Menu file = new Menu("File");
		file.getItems().add(item1);
		MenuBar menu = new MenuBar();
		menu.getMenus().add(file);
		borderPane.setTop(menu);
		primaryStage.setScene(scene);
		startGame(primaryStage);
		primaryStage.show();
		model.addObserver(this);
	}
	
	/**
     * Purpose: put the columns of tokens on the board
	 * 
	 * @param primaryStage - the primaryStage
     */
	private void startGame(Stage primaryStage) {
		// Drawing the initial circles
		gridPane.setVgap(8);
		gridPane.setHgap(8);
		for (int i = 1; i <= 6; i++) {
			for (int j = 1; j <= 7; j ++) {
				Circle circle = new Circle();
				circle.setFill(Color.WHITE);
				circle.setRadius(20);
				gridPane.add(circle, j, i);
			}
		}
	}
	
	/**
	 *  Purpose: This one will be called to create the network set up and to store
	 *  the options/role of the players
	 *  
	 * @param scene - the current scene of the GUI
	 * 
	 * @param primaryStage - the primaryStage
	 * 
	 * @param item1 - this is the button might be clicked so that this handler will be called
	 * 
	 * @return the handler
	 */
	private EventHandler<MouseEvent> handleStage(Scene scene, Stage primaryStage, MenuItem item1){
		item1.setOnAction((event)-> {
			Stage dialog = new Stage();
			dialog.setTitle("Network Setup");
			BorderPane newPane = new BorderPane();
			Scene newScene = new Scene(newPane, 440, 180);
			GridPane newGridPane = new GridPane();
			Background background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
			newGridPane.setBackground(background);
			newPane.setCenter(newGridPane);
			ToggleGroup group = new ToggleGroup();
			RadioButton server = new RadioButton("Server");
			RadioButton client = new RadioButton("Client");
			server.setToggleGroup(group);
			client.setToggleGroup(group);
			HBox hbox = new HBox(10, server, client);
			Label labela = new Label("");
			newGridPane.add(labela, 0, 0);
			Label label1 = new Label("  Create:   ");
			newGridPane.add(label1, 0, 1);
			newGridPane.add(hbox, 1, 1);
			Label labelb = new Label("");
			newGridPane.add(labelb, 0, 2);
			Label label2 = new Label("  Play as:   ");
			newGridPane.add(label2, 0, 3);
			ToggleGroup group1 = new ToggleGroup();
			RadioButton human = new RadioButton("Human");
			RadioButton computer = new RadioButton("Computer");
			human.setToggleGroup(group1);
			computer.setToggleGroup(group1);
			HBox hbox1 = new HBox(10, human, computer);
			newGridPane.add(hbox1, 1, 3);
			Label labelc = new Label("");
			newGridPane.add(labelc, 0, 4);
			Label label3 = new Label("  Server ");
			newGridPane.add(label3, 0 , 5);
			TextField host = new TextField();
			host.setText("localhost");
			newGridPane.add(host, 1, 5);
			Label label4 = new Label("  Port  ");
			newGridPane.add(label4, 2, 5);
			TextField port = new TextField();
			port.setText("4000");
			newGridPane.add(port, 3, 5);
			Button ok = new Button("OK");
			okHandler = okButton(ok, group, group1, dialog, primaryStage, scene);
			Button cancel = new Button("Cancel");
			cancelHandler = cancelButton(cancel, dialog);
			Label labeld = new Label("");
			newGridPane.add(labeld, 0, 6);
			newGridPane.add(ok, 0, 7);
			newGridPane.add(cancel, 1, 7);
			dialog.setScene(newScene);
			dialog.initOwner(primaryStage);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.showAndWait();
		});
		return stageHandler;
	}
	
	/**
	 * Purpose: This function will be called when the user click on the okButton
	 * 
	 * @param ok - The ok Button
	 * 
	 * @param group1 - choices between server/clients
	 * 
	 * @param group2 - choices between human/computer
	 * 
	 * @param dialog - the current dialog
	 * 
	 * @param primaryStage - the current stage
	 * 
	 * @param scene - the current scene of the primary stage
	 * 
	 * @return the handler
	 */
	private EventHandler<MouseEvent> okButton (Button ok, ToggleGroup group1, ToggleGroup group2, Stage dialog, Stage primaryStage, Scene scene){
		ok.setOnAction((event) ->{
			if(group1.getSelectedToggle() != null) {
				selected1 = (RadioButton) group1.getSelectedToggle();
			} 
			if(group2.getSelectedToggle() != null) {
				selected2 = (RadioButton) group2.getSelectedToggle();
			} 
			if(selected1 != null && selected2 != null) {
				if(selected1.getText().equals("Server")) {
					primaryStage.setTitle("SERVER");
					
					if(selected2.getText().equals("Human")) {
						model.setHuman(true);
					} else {
						model.setHuman(false);
					}
					controller.startServer();
					dialog.close();
				} else if(selected1.getText().equals("Client")) {
					primaryStage.setTitle("CLIENT");
					canClick = false;
					
					if(selected2.getText().equals("Human")) {
						model.setHuman(true);
					} else {
						model.setHuman(false);
					}
					controller.startClient();
					dialog.close();
				}
			}
		});
		return okHandler;
	}
	
	/**
	 * Purpose: This function will be called when the users click on the 
	 * cancelButton
	 * @param cancel - This is the cancel Button
	 * @param dialog - this is the current dialog
	 * @return the handler
	 */
	private EventHandler<MouseEvent> cancelButton (Button cancel, Stage dialog){
		cancel.setOnAction((event) ->{
			dialog.close();
		});
		return cancelHandler;
	}
	
	/**
	 * Purpose: This function will be called when the users click on the scene
	 * @param scene - the scene of the primaryStage
	 * @param primaryStage - the current Stage
	 * @return the handler
	 */
	private EventHandler<MouseEvent> mouseHandler (Scene scene, Stage primaryStage){
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		  @Override
		  public void handle(MouseEvent event) {
			if(!canClick) { return; }
			x_coor = event.getSceneX();
			int column = getCol(x_coor);
			int legalMove = model.isLegal(column);
			if (legalMove == -1) {
				errorColumn();
				return;
			} else {
				int x = getCol(event.getX());
				controller.makeCircle(x);
				canClick = false;
				if(controller.isGameOver() == 1) {
					win();
				} else if (controller.isGameOver() == 2) {
					lose();
				} else if (controller.isGameOver() == 3) {
					tie();
				}
			}
		  }
		});
		return sceneHandler; 
	}
	
	/** 
	 * Purpose: Show the error alert for full column
	 */
	private void errorColumn() {
		//Creating a dialog
		Alert alert = new Alert(Alert.AlertType.ERROR);
		//Setting the title
		alert.setContentText("Column full, pick somewhere else!");
		//Adding buttons to the dialog pane
		alert.showAndWait();
	}

	/** 
	 * Purpose: Show the tie message
	 */
	private void tie() {
		//Creating a dialog
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//Setting the title
		alert.setContentText("No Winner!");
		//Adding buttons to the dialog pane
		alert.showAndWait();
		canClick = false;
	}
	
	/** 
	 * Purpose: Show the lose message
	 */
	private void lose() {
		//Creating a dialog
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//Setting the title
		alert.setContentText("You lose!");
		//Adding buttons to the dialog pane
		alert.showAndWait();
		canClick = false;
	}
	
	/** 
	 * Purpose: Show the win message
	 */
	private void win() {
		//Creating a dialog
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//Setting the title
		alert.setContentText("You won!");
		//Adding buttons to the dialog pane
		alert.showAndWait();
		canClick = false;
	}
	
	/**
	 * Purpose: This function will return the suitable column based on
	 * what coordinate it receives from the scene.
	 * @param x - the coordinate it receive from the scene.
	 * @return the suitable column.
	 */
	private int getCol(double x) {
		if(x <= 52.0) {
			return 1;
		} else if( 52.0 < x && x <= 100.0) {
			return 2;
		} else if ( 100.0 < x && x <= 148.0) {
			return 3;
		} else if ( 148.0 < x && x <= 196.0) {
			return 4;
		} else if ( 196.0 < x && x <= 244.0) {
			return 5;
		} else if ( 244.0 < x && x <= 292.0) {
			return 6;
		} else {
			return 7;
		}
	}

	/**
	 * Purpose: This function will be used to updated the View of the board
	 * @param o - Observale o
	 * @param arg - The elements that we want to change
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		model = (Connect4Model) o;
		message= (Connect4MoveMessage) arg;
		int color = message.getColor();
		controller.move(message.getColumn(), color);
		int currRow = message.getRow();
		int currCol = message.getColumn();
		ObservableList<Node> childrens = gridPane.getChildren();
		Node result = null;
		for (Node node : childrens) {
	        if(gridPane.getRowIndex(node) == (currRow + 1) && gridPane.getColumnIndex(node) == (currCol)) {
	            result = node;
	            break;
	        }
	    }
		Circle toChange = (Circle) result;
		if (color == 2) {
			toChange.setFill(Color.RED);
		} else {
			toChange.setFill(Color.YELLOW);
		}
		if(model.getTurn() == true) {
			canClick = true;
		}
	}
}
