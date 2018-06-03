package player;

import javafx.scene.paint.Color;

import java.util.HashMap;

import board.Board;
import javafx.scene.input.KeyCode;

public class Militia extends Player{
	
	// The start position of the player's selection.
	int START_X = Board.NUM_COLUMNS, START_Y = Board.NUM_ROWS;
	
	HashMap<String, KeyCode> keyBindings = new HashMap<String, KeyCode>() {
		private static final long serialVersionUID = 1L;

	{
		put(UP, KeyCode.W);
		put(DOWN, KeyCode.S);
		put(LEFT ,KeyCode.A);
		put(RIGHT, KeyCode.D);
		put(ENTER, KeyCode.TAB);
		put(MOVE, KeyCode.SHIFT);
		put(TOGGLE_SHIP, KeyCode.L);
	}};
	
	public Militia() {
		super();	
	}
	
	public Color getSelectionColour() { return Color.ORANGE; }
}
