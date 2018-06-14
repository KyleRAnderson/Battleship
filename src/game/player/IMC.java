package game.player;

import javafx.scene.paint.Color;
import main.BattleshipGalactica;

import java.util.HashMap;

import game.Game;
import javafx.scene.input.KeyCode;

public class IMC extends Player {
	
	public HashMap<String, KeyCode> getKeyBindings() {
		return new HashMap<String, KeyCode>() {
			private static final long serialVersionUID = 1L;

			{
					put(UP, KeyCode.UP);
					put(DOWN, KeyCode.DOWN);
					put(LEFT ,KeyCode.LEFT);
					put(RIGHT, KeyCode.RIGHT);
					put(ENTER, KeyCode.ENTER);
					put(TOGGLE_HIDE, KeyCode.H);
					put (CANCEL, KeyCode.BACK_SPACE);
			}};
	}
	
	/**
	 * An IMC player
	 * @param game the game that this player is playing.
	 */
	public IMC(Game game, Player.StartSide side) {
		super(game, side);
	}
	
	public Color getSelectionColour() { return Color.BLUE; }
	
	public String getIcon() { return "file:" + BattleshipGalactica.RESOURCES_LOCATION + "/imc.png"; }

	@Override
	public String getName() {
		return "IMC";
	}
}
