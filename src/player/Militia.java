package player;

import javafx.scene.paint.Color;
import main.BattleshipGalacticaMain;
import main.Game;

import java.util.HashMap;

import javafx.scene.image.ImageView;
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
				put(TOGGLE_SHIP, KeyCode.getKeyCode("BACK_QUOTE"));
			}};
	}
	
	/**
	 * A militia player
	 * @param game the game that this player is playing
	 */
	public Militia(Game game, StartSide side) {
		super(game, side);
	}
	
	public Color getSelectionColour() { return Color.ORANGE; }
	
	public ImageView getIcon() { return new ImageView("file:/" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/militia.png"); }
}
