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
			
			// Make sure there's no ship on that square before the player adds it to that square.
			Ship shipOnSelectedSquare = board.getSquare(player.x, player.y).getShipOnSquare();
			if (shipOnSelectedSquare == null) {
				for (Ship ship : player.getShips()) {				
					if (!ship.hasBeenPlaced()) {
						addShip(board, ship);
					}
				}
			}
			else {
				removeShip(board, shipOnSelectedSquare);
			}
		}
	}
	
	/**
	 * Adds the given ship to the board.
	 * @param board The board to add the ship to
	 * @param ship The ship to be added.
	 */
	private static void addShip(Board board, Ship ship) {
		Square squareAtPosition = board.getSquare(ship.player.x, ship.player.y);
		// Make sure that the given ship has not been placed already and that the square exists and is empty.
		if (!ship.hasBeenPlaced() && squareAtPosition.getShipOnSquare() == null) {
			ship.move(board.getSquare(ship.player.x, ship.player.y));
			board.addShip(ship);
		}
	}
	
	/**
	 * Removes the given ship from the board
	 * @param board The board to handle the removal of the ship on.
	 * @param shipToRemove The ship to remove from the board
	 */
	private static void removeShip(Board board, Ship shipToRemove) {
		// Make sure that the square property for the ship isn't null before removing the ship.
		if (shipToRemove.getSquare() != null) {
			board.removeShip(shipToRemove);
			shipToRemove.removeFromBoard();
		}
	}
}
