import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Connect4ControllerTest {

	@Test
	void test() {
		// Test1
		Connect4Model model = new Connect4Model();
		Connect4Controller controller = new Connect4Controller(model);
		controller.currRow();
		controller.currCol();
		// Moving test
		controller.move(1,2);
		controller.move(1,2);
		controller.move(1,2);
		controller.move(1,2);
		controller.move(1,2);
		controller.move(1,2);
		model.changeTurn();
		controller.move(2,1);
		controller.move(3,1);
		controller.move(4,1);
		controller.move(5,1);
		controller.move(6,1);
		controller.move(7,1);
		controller.move(7,1);
		controller.move(7,1);
		controller.move(6,1);
		model.changeTurn();
		controller.move(2,2);
		controller.move(3,2);
		controller.move(3,2);
		controller.move(3,2);
		controller.move(3,2);
		controller.move(4,2);
		controller.move(4,2);
		controller.move(4,2);
		controller.move(5,2);
		controller.move(5,2);
		controller.move(6,2);
		model.changeTurn();
		controller.move(6,1);
		controller.move(5,1);
		controller.move(5,1);
		controller.move(4,1);
		controller.move(4,1);
		controller.compTurn();
		controller.move(2,1);
		controller.move(2,1);
		controller.move(2,1);
		controller.move(2,1);
		controller.move(3,1);
		controller.move(5,1);
		controller.compTurn();
		controller.move(6,1);
		controller.move(7,1);
		controller.move(7,1);
		controller.move(7,1);
		controller.move(controller.compTurn(),1);
		// Testing prinout the board
		model.printBoard();
		
		// Second test
		model = new Connect4Model();
		controller = new Connect4Controller(model);
		controller.move(1,2);
		controller.move(1,2);
		controller.compTurn();
		controller.move(1,2);
		controller.move(3,2);
		controller.move(3,2);
		controller.move(3,2);
		controller.move(5,2);
		controller.compTurn();
		controller.move(5,2);
		controller.move(5,2);
		controller.move(7,2);
		controller.move(7,2);
		controller.move(7,2);
		model.changeTurn();
		controller.move(2,1);
		controller.move(2,1);
		controller.move(2,1);
		controller.move(4,1);
		controller.move(4,1);
		controller.compTurn();
		controller.move(4,1);
		controller.move(6,1);
		controller.compTurn();
		controller.move(6,1);
		controller.move(6,1);
		controller.compTurn();
		model.changeTurn();
		controller.move(2,2);
		controller.move(2,2);
		controller.move(2,2);
		controller.move(4,2);
		controller.move(4,2);
		controller.move(4,2);
		controller.move(6,2);
		controller.move(6,2);
		controller.move(6,2);
		model.changeTurn();
		controller.move(1,1);
		controller.move(1,1);
		controller.move(1,1);
		controller.move(1,1);
		controller.move(3,1);
		controller.move(3,1);
		controller.move(3,1);
		controller.move(5,1);
		controller.move(5,1);
		controller.move(5,1);
		controller.move(7,1);
		controller.move(7,1);
		controller.move(7,1);
		// Checking is game over
		controller.isGameOver();
		model.printBoard();
	}

}
