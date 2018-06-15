package manipulation;

import java.util.ArrayList;

import game.Game;
import game.board.Board;
import game.board.Square;
import game.player.Player;
import game.ships.Ship;

public class ShipManipulation {

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
			// Make sure that the player is within three squares of their side.
			if (Square.isWithinTerritory(player.getStartPosition(), player.x, player.y)) {
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
		// If the game is in movement stage, select the ship (if there is one) and move it.
		else if (state.equals(Game.GameState.Movement)) {
			// Get the ship on the selected square
			Ship shipOnSquare = selectedSquare.getShipOnSquare();
			
			// Make sure that there's actually a ship on the square before we do anything.
			if (shipOnSquare != null) {
				// Set the player's selected ship.
				player.setSelectedShip(shipOnSquare);
			}
		}
		// If the game is in the firing stage, fire!
		else if (state.equals(Game.GameState.Firing)) {
			player.shoot(selectedSquare);
		}
	}
	
	/**
	 * Moves the ship that the player has selected the given direction
	 * @param player The player to move the ship of
	 * @param direction The direction to move the ship.
	 */
	public static void moveShip(Player player, Board.MoveDirection direction) {
		// Make sure the player has selected a ship before we try to move it.
		if (player.hasSelectedShip()) {
			// Attempt to move the ship in this round.
			player.getSelectedShip().move(direction);
			player.getGame().refreshState();
			// Nullify the player's selected ship.
			player.setSelectedShip(null);
		}
	}
	
	/**
	 * Moves the given ship to the new position
	 * @param shipToMove The ship to move
	 * @param newSquare The new square to move the ship to
	 */
	public static void moveShip(Ship shipToMove, Square newSquare) {
		// Nullify the player's selected ship
		shipToMove.player.setSelectedShip(null);
		
		// Move the ship
		shipToMove.move(newSquare, true);
		shipToMove.player.getGame().refreshState();
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
			ship.teleport(board.getSquare(ship.player.x, ship.player.y));
			board.addShip(ship);
			
			// Refresh the game state.
			board.getGame().refreshState();
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
			
			// Refresh the game state
			board.getGame().refreshState();
		}
	}
	
	/**
	 * Selects or deselects the squares that this ship can possible travel to.
	 * @param ship The ship to select or deselect the squares of 
	 * @param select Set to true to select, false to deselect.
	 */
	public static void selectShip(Ship ship, boolean select) {
		// Determine possible squares for the ship to move to
		ArrayList<Square> possibleSquares = ship.getPossibleSquares();
		
		// Only iterate if it's possible
		if (possibleSquares != null && possibleSquares.size() > 0) {
			// Iterate through each of the ship's possible squares.
			for (Square square : possibleSquares) {
				// Highlight if necessary, or clear highlight if requested.
				if (select) square.highlight(ship.player);
				else square.clearHighlight();
			}
		}
		
	}
}
