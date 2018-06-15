package game.board;

import game.Game;
import game.player.Player;
import game.ships.Ship;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.BattleshipGalactica;

/**
 * The board class for all board wide operations
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class Board extends BorderPane {
	/**
	 * The Game object to which this board belongs.
	 */
	private Game game;
	private GridPane playingBoard;
	
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
	
	private static final double PADDING = 15;
	
	/**
	 * Instantiates a new board for the given game
	 * @param game The game for this board.
	 */
	public Board(Game game) {
		// Set the game object
		this.game = game;
		
		setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
		
		// Set up the turn label at the top of the board.
		statusLabel.setFont(BattleshipGalactica.HEADING_FONT);
		statusLabel.setFill(Color.RED);
		
		// Now set up the message text
		message.setFont(BattleshipGalactica.HEADING_FONT);
		message.setFill(Color.BLACK);
		
		// Add the turn indicator and the message to the screen.
		HBox header = new HBox(statusLabel, message);
		header.setSpacing(50);
		setTop(header);
		
		// Set up a gridpane for the squares
		playingBoard = new GridPane();
		playingBoard.setHgap(0);
		playingBoard.setVgap(0);
		playingBoard.setPadding(new Insets(0, 0, 0, 0));
		setCenter(new Pane(playingBoard));
		
		// Let's get the squares objects rolling. Populate the squares array.
		for (int y = 0; y < NUM_ROWS; y++) {
			for (int x = 0; x < NUM_COLUMNS; x++) {
				// Make a new square object for this position.
				Square square = new Square(x, y, this);
				// Add the square to the row
				playingBoard.add(square, x, y);
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
		
		// Also add a battle zone at the bottom
		Label battleZoneLabel = new Label("Battle zone: ");
		battleZoneLabel.setFont(Battle.BATTLE_FONT);
		battleZoneLabel.setTextFill(Color.WHITE);
		HBox battleZone = new HBox(battleZoneLabel);
		battleZone.setSpacing(10);
		setBottom(battleZone);
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
		playingBoard.add(ship, squareToAddTo.xCoordinate, squareToAddTo.yCoordinate);
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
			playingBoard.getChildren().remove(ship);
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
		playingBoard.getChildren().remove(ship);
		addShip(ship);
	}
	
	/**
	 * Sets up a sidebar on the player's side of the board with a listing of their controls.
	 * @param player The player to set the controls of.
	 */
	public void setPlayerSidebar(Player player) {		
		// Make a Vertical box node for the sidebar items.
		VBox sideBar = new VBox(player.getSidebarItems());
		
		// Now just figure out where to put the text and put it there.
		if (player.getStartPosition().equals(Player.StartSide.BottomRight)) {
			sideBar.setAlignment(Pos.CENTER_RIGHT);
			setRight(sideBar);
		}
		else {
			sideBar.setAlignment(Pos.CENTER_LEFT);
			setLeft(sideBar);
		}
	}
}

