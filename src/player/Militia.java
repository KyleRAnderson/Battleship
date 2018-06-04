package player;

import javafx.scene.paint.Color;
import main.Game;

import java.util.HashMap;

import board.Board;
import javafx.scene.input.KeyCode;

public class Militia extends Player{
	
	HashMap<String, KeyCode> getKeyBindings() { 
		return new HashMap<String, KeyCode>() {
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
	}
	
	/**
	 * A militia player
	 * @param game the game that this player is playing
	 */
	public Militia(Game game) {
		super(game);
		START_X = Board.NUM_COLUMNS - 1;
		START_Y = Board.NUM_ROWS - 1;
		x = START_X;
		y = START_Y;
	}
	
	public Color getSelectionColour() { return Color.ORANGE; }
}
