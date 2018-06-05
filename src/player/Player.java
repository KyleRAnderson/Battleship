package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import board.Board;
import board.Square;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import main.Game;
import main.InputHandler;
import manipulation.BoardManipulation;
import ships.Ship;

public abstract class Player {
	public static enum StartSide {
		TopLeft, BottomRight
	}
	
	// The current position (selection) of the player.
	public int x, y;
	
	// The starting position of the player
	private final int start_x, start_y;
	
	/**
	 * The amount of damage that this player does when hitting a ship with a cannon ball.
	 */
	protected int damage = 50;
	
	private StartSide startSide;
	
	/**
	 * The player's ships
	 */
	protected ArrayList<Ship> ships = new ArrayList<Ship>();
	
	protected static int NUM_SHOTS = 4;
	protected int numShotsLeft = NUM_SHOTS;
	
	// The board object on which this player is playing.
	protected Game game;
	
	public static final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT", ENTER = "ENTER", MOVE = "MOVE";
	
	/**
	 * Instantiates a player object.
	 * @param game The game to which this player belongs
	 */
	public Player(Game game, StartSide side) {
		this.game = game;
		
		// Set the start position of this player based on the start side.
		if (side.equals(StartSide.BottomRight)) {
			start_x = Board.NUM_COLUMNS - 1;
			start_y = Board.NUM_ROWS - 1;
		}
		else {
			start_x = 0;
			start_y = 0;
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
		x = start_x;
		y = start_y;
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
	
	/**
	 * Determines the number of ships that this player has left.
	 * @return The number of ships that the player has left.
	 */
	public int getNumShipsLeft() {
		int left = 0;
		
		// Iterate through each ship and then add to the counter each time we find a ship that's alive.
		for (Ship ship : ships) {
			if (!ship.isDestroyed()) left++;
		}
		
		
		// Return the results.
		return left;
	}
	
	/**
	 * Shoots the given square 
	 * @param squareToShoot
	 */
	public void shoot(Square squareToShoot) {
		// Only shoot at a square if we have shots left and if that square is usable.
		if (getShotsLeft() > 0 && squareToShoot.isUsable()) { 
			numShotsLeft--;
			
		}
	}
	
	/**
	 * Determines the number of shots that this player has left for this turn, keeping into account the number of ships
	 * the player has.
	 * @return The number of shots that the player has left.
	 */
	public int getShotsLeft() {
		return (getNumShipsLeft() > 0) ? numShotsLeft : 0;
	}
	
	/**
	 * Resets the number of shots that the player has for this turn.
	 */
	public void resetShots() {
		numShotsLeft = NUM_SHOTS;
	}
	
	/**
	 * Returns the amount of damage this player's cannon balls do.
	 * @return The amount of damage this player's cannon balls do.
	 */
	public int getDamage() {
		return damage;
	}
}
