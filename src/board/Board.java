package board;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ships.Ship;

/**
 * The board class for all board wide operations
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class Board extends Parent {
	private VBox rows = new VBox();
	
	/**
	 * The number of ships that each player has to deal with.
	 */
	public static final int NUMBER_OF_SHIPS_PER_PLAYER = 10, NUM_ROWS = 10, NUM_COLUMNS = 20;
	
	private static final Square[][] SQUARES = new Square[NUM_ROWS][NUM_COLUMNS];
	/**
	 * List of all the ships on the board, whether or not they're alive.
	 */
	private static final Ship[] SHIPS = new Ship[NUMBER_OF_SHIPS_PER_PLAYER * 2];
	
	
	// We need to have states to keep track of what state we're currently in
	public static enum State {
		Initial, Battle
	}
	
	// The current state of the board.
	private static State state = State.Initial;
	
	public Board(EventHandler<MouseEvent> squareClickHandler) {	
		// Let's get the squares objects rolling. Populate the squares array.
		for (int y = 0; y < NUM_ROWS; y++) {
			HBox row = new HBox();
			for (int x = 0; x < NUM_COLUMNS; x++) {
				// Make a new square object for this position.
				Square square = new Square(x, y);
				square.setOnMouseClicked(squareClickHandler);
				// Add the square to the row
				row.getChildren().add(square);
				// Put the square in the squares array
				SQUARES[x][y] = square;
			}
			// Add the row to the rows.
			rows.getChildren().add(row);
		}
		// Add the board to the children.
		getChildren().add(rows);
	}
	
	/**
	 * Performs the necessary actions to get the two given ships into battle mode
	 * @param defender The ship defending this square
	 * @param contester The ship contesting this square
	 * @return The ship that wins the battle.
	 */
	public static Ship battle(Ship defender, Ship contester) {
		// TODO replace with actual code.
		Ship winner = defender;
		
		// Return the ship that won the battle
		return winner;
	}
	
	/**
	 * Determines the square at the given coordinates and returns the Square object 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The Square object at the provided coordinates, if the coordinates exist. Else,
	 * returns null.
	 */
	public static Square getSquare(int x, int y) {
		// The object to be returned at the end
		Square squareAtPosition = null;
		
		// Make sure the coordinates exist
		if (0 <= x && x <= SQUARES.length && 0 <= y && y <= SQUARES[x].length) {
			squareAtPosition = SQUARES[x][y];
		}
		
		// Return result
		return squareAtPosition;
	}
	
	public static State getState() {
		return state;
	}
}
