package game.player;

import javafx.scene.paint.Color;
import main.BattleshipGalactica;

import java.util.HashMap;

import game.Game;
import javafx.scene.input.KeyCode;

public class Militia extends Player{
	
	public HashMap<String, KeyCode> getKeyBindings() { 
		return new HashMap<String, KeyCode>() {
			private static final long serialVersionUID = 1L;
			{
				put(UP, KeyCode.W);
				put(DOWN, KeyCode.S);
				put(LEFT ,KeyCode.A);
				put(RIGHT, KeyCode.D);
				put(ENTER, KeyCode.TAB);
				put(TOGGLE_HIDE, KeyCode.Q);
				put(CANCEL, KeyCode.ESCAPE);
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
	
	public String getIcon() { return BattleshipGalactica.getCorrectPath(BattleshipGalactica.RESOURCES_LOCATION + "/militia.png"); }

	@Override
	public String getName() {
		return "Militia";
	}
}
