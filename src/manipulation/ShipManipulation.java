package manipulation;

import board.Board;
import board.Square;
import main.Game;
import player.Player;
import ships.Ship;

public class ShipManipulation {
	
	/**
	 * The distance that the ship has to be within when it starts the game.
	 */
	public final static int SHIP_START_DISTANCE = 3;

	/**
	 * Called when the given player presses the enter key
	 * @param player The player who sent the request
	 */
	public static void enterPressed(Player player) {
		// Get the player's game as well as their board so that we can begin doing stuff.
		Game playersGame = player.getGame();
		Board board = playersGame.getBoard();
		Square selectedSquare = board.getSquare(player.x, player.y);
		
		Game.GameState state = playersGame.getState();
		// If we're in the ship placement stage, place one of the player's ships.
		if (state.equals(Game.GameState.ShipPlacement)) {
			int playerStartXCoordinate = player.getStartSquare().xCoordinate;
			
			// Make sure that the player is within three squares of their side.
			if (Board.getDistanceBetween(selectedSquare.xCoordinate, selectedSquare.yCoordinate, playerStartXCoordinate, selectedSquare.yCoordinate) < SHIP_START_DISTANCE) {
				// Make sure there's no ship on that square before the player adds it to that square.
				Ship shipOnSelectedSquare = selectedSquare.getShipOnSquare();
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
		
		else if (state.equals(Game.GameState.Movement)) {
			// Get the ship on the selected square
			Ship shipOnSquare = selectedSquare.getShipOnSquare();
			
			// Make sure that there's actually a ship on the square before we do anything.
			if (shipOnSquare != null) {
				// Set the player's selected ship.
				player.setSelectedShip(shipOnSquare);
			}
		}
	}
	
	/**
	 * Moves the ship that the player has selected the given direction
	 * @param player The player to move the ship of
	 * @param direction The direction to move the ship.
	 */
	public static void moveShip(Player player, BoardManipulation.MoveDirection direction) {
		if (player.getSelectedShip() != null) {
			
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
