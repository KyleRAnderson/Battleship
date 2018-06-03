package player;

import javafx.scene.paint.Color;

import java.util.HashMap;

import javafx.scene.input.KeyCode;

public class IMC extends Player {
	
	int START_X = 0, START_Y = 0;
	
	HashMap<String, KeyCode> keyBindings = new HashMap<String, KeyCode>() {
		private static final long serialVersionUID = 1L;

	{
			put(UP, KeyCode.UP);
			put(DOWN, KeyCode.DOWN);
			put(LEFT ,KeyCode.LEFT);
			put(RIGHT, KeyCode.RIGHT);
			put(ENTER, KeyCode.ENTER);
			put(MOVE, KeyCode.M);
			put(TOGGLE_SHIP, KeyCode.BACK_SLASH);
	}};
	
	public IMC() {
		super();
		x = 0;
		y = 0;
	}
	
	public Color getSelectionColour() { return Color.BLUE; }
}
