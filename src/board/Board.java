package board;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Game;
import player.Player;
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
	 * The current turn, 0 to start.
	 */
	private int turn = 0;
	
	/**
	 * The Game object to which this board belongs.
	 */
	private Game game;
	
	/**
	 * The number of ships that each player has to deal with.
	 */
	public static final int NUMBER_OF_SHIPS_PER_PLAYER = 10, NUM_ROWS = 10, NUM_COLUMNS = 20;
	
	private final Square[][] squares = new Square[NUM_COLUMNS][NUM_ROWS];
	/**
	 * List of all the ships on the board, whether or not they're alive.
	 */
	private final Ship[] ships = new Ship[NUMBER_OF_SHIPS_PER_PLAYER * 2];
	
	// The ship selection object, used to select which ship the player is controlling
	public final ShipSelection[] shipSelectors = new ShipSelection[2];
	
	
	// We need to have states to keep track of what state we're currently in
	public static enum State {
		Initial, Battle
	}
	
	// The players playing on this board.
	Player[] players;
	
	// The current state of the board.
	private static State state = State.Initial;
	
	/**
	 * Instantiates a new board for the given game
	 * @param game The game for this board.
	 */
	public Board(Game game) {
		// Set the game object
		this.game = game;
		
		// Let's get the squares objects rolling. Populate the squares array.
		for (int y = 0; y < NUM_ROWS; y++) {
			HBox row = new HBox();
			for (int x = 0; x < NUM_COLUMNS; x++) {
				// Make a new square object for this position.
				Square square = new Square(x, y, this);
				// Add the square to the row
				row.getChildren().add(square);
				// Put the square in the squares array
				squares[x][y] = square;
			}
			// Add the row to the rows.
			rows.getChildren().add(row);
		}
		// Add the board to the children.
		getChildren().add(rows);
		
		// Now generate ships for each player
		for (int playerNum = 0; playerNum < 2; playerNum++) {
			Player player = game.getPlayers()[playerNum];		
			
			for (int i = 0; i < NUMBER_OF_SHIPS_PER_PLAYER; i++) {
				// Make new ship object, giving it the player, the direction and the damage.
				Ship ship = new Ship(player, (i % 2 == 0) ? Ship.DirectionOfMovement.Diagonal : Ship.DirectionOfMovement.Horizontal, i);
				ships[i * playerNum + 1] = ship;
				
				// Add the ship to the player's collection
				player.addShip(ship);
			}
			
			// While we're looping through players, make a ship selector for the player.
			ShipSelection selector = new ShipSelection(player);
			shipSelectors[playerNum] = selector;
			getChildren().add(selector);
		}
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
	public Square getSquare(int x, int y) {
		// The object to be returned at the end
		Square squareAtPosition = null;
		
		// Make sure the coordinates exist
		if (isValidPosition(x, y)) {
			squareAtPosition = squares[x][y];
		}
		
		// Return result
		return squareAtPosition;
	}
	
	public static State getState() {
		return state;
	}
	
	/**
	 * Gets the game that this board belongs to
	 * @return The game object for this board.
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Determines if the given position is a valid position on the board
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * @return True if it's a valid position, false otherwise.
	 */
	public boolean isValidPosition(int x, int y) {
		return (0 <= x && x <= NUM_COLUMNS - 1 && 0 <= y && y <= NUM_ROWS - 1);
	}
	
	/**
	 * Gets all the ships on the board.
	 * @return All the ships on this board.
	 */
	public Ship[] getShips() {
		return ships;
	}
}

