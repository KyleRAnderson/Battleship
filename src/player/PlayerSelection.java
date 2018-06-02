package player;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import main.BattleshipGalacticaMain;

/**
 * The window for selecting your player
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class PlayerSelection extends Parent {
	
	ImageView IMC, militia;
	
	public PlayerSelection() {
		IMC = new ImageView("file:" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/imc.png");
		militia = new ImageView("file:" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/militia.png");
	}
}
