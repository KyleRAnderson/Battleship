package manipulation;

import board.Board;
import board.Square;
import main.Game;
import player.Player;
import ships.Ship;

public class ShipManipulation {

	/**
	 * Called when the given player presses the enter key
	 * @param player The player who sent the request
	 */
	public static void enterPressed(Player player) {
		// Get the player's game as well as their board so that we can begin doing stuff.
		Game playersGame = player.getGame();
		Board board = playersGame.getBoard();
		
		// If we're in the ship placement stage, place one of the player's ships.
		if (playersGame.getState().equals(Game.GameState.ShipPlacement)) {
			boolean shipPlaced = false;
			for (Ship ship : player.getShips()) {
				Square squareAtPosition = board.getSquare(player.x, player.y);
				
				// Make sure that the given ship has not been 
				if (!ship.hasBeenPlaced() && squareAtPosition.getShipOnSquare().equals(null)) 
					ship.move(board.getSquare(player.x, player.y));
			}
		}
	}
	
	public static void addShip(Board board, Ship ship) {
		
	}
}
