package player;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import main.InputHandler;
import manipulation.BoardManipulation;

public abstract class Player {		
	// The current position (selection) of the player.
	public int x, y;
	
	protected int START_X, START_Y;
	
	protected HashMap<String, KeyCode> keyBindings;
	
	public static final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT", ENTER = "ENTER", MOVE = "MOVE", TOGGLE_SHIP = "TOGGLE";
	
	public Player() {		
		InputHandler.addKeyBindings(getKeysUsed(), new Consumer<KeyCode>() {
			@Override
			public void accept(KeyCode t) {
				onKeyPressed(t);
			}
		});
		
		resetPosition();
	}
	
	/**
	 * Handles player specific key presses.
	 * @param key
	 */
	private void onKeyPressed(KeyCode key) {
		if (key.equals(keyBindings.get(UP))) BoardManipulation.move(this, BoardManipulation.MoveDirection.up);
		else if (key.equals(keyBindings.get(DOWN))) BoardManipulation.move(this, BoardManipulation.MoveDirection.down);
		else if (key.equals(keyBindings.get(LEFT))) BoardManipulation.move(this, BoardManipulation.MoveDirection.left);
		else if (key.equals(keyBindings.get(RIGHT))) BoardManipulation.move(this, BoardManipulation.MoveDirection.right);
	}
	
	/**
	 * Returns a list of the keys that this player uses
	 * @return The keys used by this player.
	 */
	public Collection<KeyCode> getKeysUsed() {
		return keyBindings.values();
	}
	
	/**
	 * Gets the key binding for the given type of action 
	 * @param name The name of the action
	 * @return The KeyCode for this action, or null if no such action exists.
	 */
	public KeyCode getBinding(String name) {
		return keyBindings.get(name);
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
}
