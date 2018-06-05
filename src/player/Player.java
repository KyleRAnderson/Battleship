package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import board.Board;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import main.Game;
import main.InputHandler;
import manipulation.BoardManipulation;
import manipulation.ShipManipulation;
import ships.Ship;

public abstract class Player {
	public static enum StartSide {
		TopLeft, BottomRight
	}
	
	// The current position (selection) of the player.
	public int x, y;
	
	// The starting position of the player
	private int START_X, START_Y;
	
	private StartSide startSide;
	
	/**
	 * The player's ships
	 */
	protected ArrayList<Ship> ships = new ArrayList<Ship>();
	
	/**
	 * The player's selected ship's index.
	 */
	protected int selectedShip = 0;
	
	// The board object on which this player is playing.
	protected Game game;
	
	public static final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT", ENTER = "ENTER", MOVE = "MOVE", TOGGLE_SHIP = "TOGGLE";
	
	/**
	 * Instantiates a player object.
	 * @param game The game to which this player belongs
	 */
	public Player(Game game, StartSide side) {
		this.game = game;
		
		// Set the start position of this player based on the start side.
		if (side.equals(StartSide.BottomRight)) {
			START_X = Board.NUM_COLUMNS - 1;
			START_Y = Board.NUM_ROWS - 1;
		}
		else {
			START_X = 0;
			START_Y = 0;
		}
		
		InputHandler.addKeyBindings(getKeysUsed(), new Consumer<KeyCode>() {
			@Override
			public void accept(KeyCode t) {
				onKeyPressed(t);
			}
		});
	}
	
	/**
	 * Handles player specific key presses.
	 * @param key The key that was pressed.
	 */
	private void onKeyPressed(KeyCode key) {
		HashMap<String, KeyCode> keyBindings = getKeyBindings();
		if (key.equals(keyBindings.get(UP))) game.boardManipulation.move(this, BoardManipulation.MoveDirection.up);
		else if (key.equals(keyBindings.get(DOWN))) game.boardManipulation.move(this, BoardManipulation.MoveDirection.down);
		else if (key.equals(keyBindings.get(LEFT))) game.boardManipulation.move(this, BoardManipulation.MoveDirection.left);
		else if (key.equals(keyBindings.get(RIGHT))) game.boardManipulation.move(this, BoardManipulation.MoveDirection.right);
		else if (key.equals(keyBindings.get(TOGGLE_SHIP))) ShipManipulation.increaseSelectedShip(this);
	}
	
	/**
	 * Gets the key bindings for the specific 
	 * @return
	 */
	abstract HashMap<String, KeyCode> getKeyBindings();
	
	/**
	 * Returns a list of the keys that this player uses
	 * @return The keys used by this player.
	 */
	public Collection<KeyCode> getKeysUsed() {
		return getKeyBindings().values();
	}
	
	/**
	 * Resets the player's position to the starting position.
	 */
	public void resetPosition() {
		x = START_X;
		y = START_Y;
		BoardManipulation.moveTo(this, x, y);
	}
	
	/**
	 * Get the colour that this player should glow when selecting a square
	 * @return The colour of this player's selection
	 */
	public abstract Color getSelectionColour();
	
	/**
	 * Gets the game that this player belongs to.
	 * @return The game object to which this player belongs.
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Gets the ImageView for this player's icon.
	 * @return This player's icon.
	 */
	public abstract ImageView getIcon();
	
	/**
	 * Adds the given ship to this player's list of ships
	 * @param ship The ship to add to the player's record.
	 */
	public void addShip(Ship ship) {
		ships.add(ship);
	}
	
	/**
	 * Increases the player's currently selected ship, or goes back to the start
	 * if it's at the end
	 */
	public void increaseSelection() {
		selectedShip++;
		if (selectedShip >= ships.size()) selectedShip = 0;
	}
	
	/**
	 * Gets the ship that the player is currently selecting
	 * @return The ship that the player is currently selecting.
	 */
	public Ship getSelectedShip() {
		return ships.get(selectedShip);
	}
	
	/**
	 * Gets all the ships that belong to this player
	 * @return This playe's ships.
	 */
	public Ship[] getShips() {
		return ships.toArray(new Ship[0]);
	}
	
	/**
	 * Gets the starting side of this Player, bottom right or top left
	 * @return The start side of this player.
	 */
	public StartSide getStartPosition() {
		return startSide;
	}
}
