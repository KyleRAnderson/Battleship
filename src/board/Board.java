package board;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.BattleshipGalactica;
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
	
	/**
	 * The possible directions for things to move.
	 */
	public static enum MoveDirection {
		up, down, left, right
	}
	
	/**
	 * The label for the current turn
	 */
	private final Text statusLabel = new Text();
	/**
	 * The message to be displayed on screen to help the player.
	 */
	private final Text message = new Text();
	
	// The players playing on this board.
	Player[] players;
	
	// The grid that controls the way things are laid out.
	BorderPane root;
	
	private static final double PADDING = 15;
	
	/**
	 * Instantiates a new board for the given game
	 * @param game The game for this board.
	 */
	public Board(Game game) {
		// Set the game object
		this.game = game;
		
		// Set up the grid pane.
		root = new BorderPane();
		root.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		getChildren().add(root);
		
		// Set up the turn label at the top of the board.
		statusLabel.setFont(BattleshipGalactica.HEADING_FONT);
		statusLabel.setFill(Color.RED);
		GridPane.setConstraints(statusLabel, 1, 0);
		
		// Now set up the message text
		message.setFont(BattleshipGalactica.CONTENT_FONT);
		message.setFill(Color.BLACK);
		GridPane.setConstraints(message, 0, 0);
		
		// Add the turn indicator and the message to the screen.
		root.setTop(new HBox(statusLabel, message));
		
		// Set up a gridpane for the squares
		GridPane squaresPane = new GridPane();
		squaresPane.setHgap(0);
		squaresPane.setVgap(0);
		squaresPane.setPadding(new Insets(0, 0, 0, 0));
		root.setCenter(squaresPane);
		
		// Let's get the squares objects rolling. Populate the squares array.
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < NUM_COLUMNS; x++) {
				// Make a new square object for this position.
				Square square = new Square(x, y, this);
				// Add the square to the row
				squaresPane.add(square, x, y);
				// Put the square in the squares array
				squares[x][y] = square;
			}
		}
		
		// Now generate ships for each player
		for (int playerNum = 0; playerNum < 2; playerNum++) {
			Player player = game.getPlayers()[playerNum];		
			
			for (int i = 0; i < NUMBER_OF_SHIPS_PER_PLAYER; i++) {
				// Make new ship object, giving it the player, the direction and the damage.
				Ship ship = new Ship(player, (i % 2 == 0) ? Ship.DirectionOfMovement.Diagonal : Ship.DirectionOfMovement.Horizontal);
				ships[playerNum * NUMBER_OF_SHIPS_PER_PLAYER + i] = ship;
				
				// Add the ship to the player's collection
				player.addShip(ship);
			}
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
	public static boolean isValidPosition(int x, int y) {
		return (0 <= x && x <= NUM_COLUMNS - 1 && 0 <= y && y <= NUM_ROWS - 1);
	}
	
	/**
	 * Gets all the ships on the board.
	 * @return All the ships on this board.
	 */
	public Ship[] getShips() {
		return ships;
	}
	
	/**
	 * Determines the distance between the two coordinates in squares, using the distance formula
	 * @param x1 The first x-coordinate
	 * @param y1 The first y-coordinate
	 * @param x2 The second x-coordinate
	 * @param y2 The second y-coordinate
	 * @return The distance between the two points in squares.
	 */
	public static int getDistanceBetween(int x1, int y1, int x2, int y2) {
		return (int) Math.round(Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2)));
	}
	
	/**
	 * Sets the turn message that is displayed on the board indicating which turn number it is.
	 * @param turn The current turn.
	 */
	public void setStatus(String status) {
		statusLabel.setText("STATUS: " + status);
	}
	
	/**
	 * Set the current message being displayed on the board to the given content.
	 * @param message The message to be displayed on screen.
	 */
	public void setMessage(String message) {
		this.message.setText(message);
	}
	
	/**
	 * Adds a ship to the board.
	 * @param ship The ship to add to the board.
	 */
	public void addShip(Ship ship) {
		Square squareToAddTo = ship.getSquare();
		// Now actually add the ship to the GUI
		((GridPane) root.getCenter()).add(ship, squareToAddTo.xCoordinate, squareToAddTo.yCoordinate);
		// Tell the square that a ship has been added on its position
		squareToAddTo.addShip(ship);
	}
	
	/**
	 * Removes the given ship from the board. Can only be called during the ship placement stage.
	 * @param ship The ship to remove from the board.
	 */
	public void removeShip(Ship ship) {
		// Make sure the square property of the ship isn't null
		if (ship.getSquare() != null && game.getState().equals(Game.GameState.ShipPlacement)) {
			Square square = ship.getSquare();
			// Remove the ship from the gridpane.
			((GridPane) root.getCenter()).getChildren().remove(ship);
			// Tell the square that that ship was removed.
			square.removeShip(ship);	
		}		
	}
	
	/**
	 * Moves the given ship from its old position to its new position.
	 * @param ship The ship to move
	 * @param oldPosition The old position that the ship was at.
	 */
	public void moveShip(Ship ship) {
		((GridPane) root.getCenter()).getChildren().remove(ship);
		addShip(ship);
	}
}

