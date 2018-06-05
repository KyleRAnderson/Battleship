package board;

import java.util.HashMap;
import java.util.Map.Entry;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import player.Player;
import ships.Ship;

/**
 * Class for the players to change which ship they have selected and are currently controlling.
 * @author Kyle Anderson
 * 2018-06-06
 * ICS3U
 */
public class ShipSelection extends Group {
	// The player to whom this selection belongs
	public final Player player;
	
	HashMap<Ship, Group> shipGroup;
	
	/**
	 * Instantiates new selection object
	 * @param player The player to whom the ship selection object belongs.
	 */
	public ShipSelection(Player player) {
		this.player = player;
		
		shipGroup = new HashMap<Ship, Group>();
		
		Ship[] ships = player.getShips();
		for (int i = 0; i < ships.length; i++) {
			Ship ship = ships[i];
			Group group = new Group();
			group.getChildren().add(new ImageView(ship.image_location));
			Text shipText = new Text(String.valueOf(i + 1));
			shipText.setFont(Font.font(12));
			group.getChildren().add(new Text());
			
			// Add this to the hash map 
			shipGroup.put(ship, group);
			
			// Add this to the entire children array
			getChildren().add(group);
		}
	}
	
	/**
	 * Refreshes which ship is selected depending on what the player has selected.
	 */
	public void refresh() {				
		// Iterate through all the entries in the list and check if the ship is selected or not.
		for (Entry<Ship, Group> entry : shipGroup.entrySet()) {
			if (entry.getKey().isSelected()) {
				entry.getValue().getChildren().add(new Rectangle(30, 30));
			}
			else {
				entry.getValue().getChildren().remove(new Rectangle(30, 30));
			}
		}
	}

}
