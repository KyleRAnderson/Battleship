package manipulation;

import board.ShipSelection;
import player.Player;

public class ShipManipulation {

	/**
	 * Increase the given player's selected ship, such that the ship they're controlling is
	 * the next one in the list
	 * @param player the Player whose selection shall be changed.
	 */
	public static void increaseSelectedShip(Player player) {
		// Increase the selection of the player's ship
		player.increaseSelection();
		// Now simply do a refresh on it.
		for (ShipSelection selector : player.getGame().getBoard().shipSelectors) {
			// Find the selector that belongs to the player who changed their selection and then refresh it.
			if (selector.player.equals(player)) {
				selector.refresh();
				break;
			}
		}
	}
	
}
