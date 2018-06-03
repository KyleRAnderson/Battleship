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
	
	ImageView IMC, militia, background;
	
	public PlayerSelection() {
		IMC = new ImageView("file:" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/imc.png");
		IMC.setScaleX(0.2);
		IMC.setScaleY(0.2);		
		
		militia = new ImageView("file:" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/militia.png");
		militia.setScaleX(0.2);
		militia.setScaleY(0.2);
		
		
		background = new ImageView("file:" + BattleshipGalacticaMain.RESOURCES_LOCATION + "/main_background.png");
		background.setScaleX(1);
		background.setScaleY(1);
	}
}
