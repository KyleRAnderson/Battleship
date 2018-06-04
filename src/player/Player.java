package player;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import main.Game;
import main.InputHandler;
import manipulation.BoardManipulation;

public abstract class Player {
	// The current position (selection) of the player.
	public int x, y;
	
	// The starting position of the player
	protected int START_X, START_Y;
	
	// The board object on which this player is playing.
	protected Game game;
	
	public static final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT", ENTER = "ENTER", MOVE = "MOVE", TOGGLE_SHIP = "TOGGLE";
	
	/**
	 * Instantiates a player object.
	 * @param game The game to which this player belongs
	 */
	public Player(Game game) {
		this.game = game;
		InputHandler.addKeyBindings(getKeysUsed(), new Consumer<KeyCode>() {
			@Override
			public void accept(KeyCode t) {
				onKeyPressed(t);
			}
		});
	}
	
	/**
	 * Handles player specific key presses.
	 * @param key
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
		x = START_X;
		y = START_Y;
		BoardManipulation.moveTo(this, x, y);
	}
	
	public abstract Color getSelectionColour();
	
	/**
	 * Gets the game that this player belongs to.
	 * @return The game object to which this player belongs.
	 */
	public Game getGame() {
		return game;
	}
}
