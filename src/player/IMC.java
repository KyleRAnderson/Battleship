package player;

import javafx.scene.paint.Color;
import main.BattleshipGalacticaMain;
import main.Game;

import java.util.HashMap;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class IMC extends Player {
	
	HashMap<String, KeyCode> getKeyBindings() {
		return new HashMap<String, KeyCode>() {
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
	}
	
	/**
	 * An IMC player
	 * @param game the game that this player is playing.
	 */
	public IMC(Game game) {
		super(game);
		x = 0;
		y = 0;
	}
	
	public Color getSelectionColour() { return Color.BLUE; }
	
	public ImageView getIcon() { return new ImageView("file:/" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/imc.png"); }
}
