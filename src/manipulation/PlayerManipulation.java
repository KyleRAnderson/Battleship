package manipulation;

import game.board.Board;
import game.board.Square;
import game.player.Player;
import game.ships.Ship;

public class PlayerManipulation {	
	/**
	 * Moves the cursor selection of the player one square in the given direction.
	 * @param player The player whose cursor needs to be moved.
	 * @param direction The direction of the movement.
	 */
	public void move(Player player, Board.MoveDirection direction) {
		
		// Determine if the player has a ship selected. If they do, we're moving the ship, not the selection
		if (!player.hasSelectedShip()) {
			int newX = player.x, newY = player.y;
			// Make sure that it's possible to move in the direction, then move in that direction.
			if (direction.equals(Board.MoveDirection.left) && player.x > 0) newX--;
			else if (direction.equals(Board.MoveDirection.right) && player.x < Board.NUM_COLUMNS - 1) newX++;
			else if (direction.equals(Board.MoveDirection.up) && player.y > 0) newY--;
			else if (direction.equals(Board.MoveDirection.down) && player.y < Board.NUM_ROWS - 1) newY++;
			
			// Only bother to do something if the player's position has changed.
			if (newX != player.x || newY != player.y) { 
				// Use static method to actually do the move.
				moveTo(player, newX, newY);
			}
		}
		else {
			// Move the ship if the selected ship isn't null.
			ShipManipulation.moveShip(player, direction);
		}
	}
	
	/**
	 * Moves the given player's selection to the provided coordinates
	 * @param player The player whose selection needs to move
	 * @param x The new x position of the player
	 * @param y The new y position of the player
	 */
	public static void moveTo(Player player, int x, int y) {
		// Get the player's board
		Board board = player.getGame().getBoard();
		// Determine if the supplied coordinates are valid, and if so continue
		if (Board.isValidPosition(x, y)) {
			// Get the old square before we move so we can refresh it
			Square oldSquare = board.getSquare(player.x, player.y);

			// Change the player's coordinates
			player.moveTo(x, y);
			
			// Get the new square to which the player belongs
			Square newSquare = board.getSquare(x, y);
			
			// Refresh the selection on the squares
			oldSquare.refreshFill();
			newSquare.refreshFill();
		}
	}
	
	/**
	 * Called by the player object when the selected ship is changed.
	 */
	public static void shipSelectionChanged(Ship oldSelection, Ship newSelection, Player player) {
		// Clear the old ship's selection, and set the new ship's selection
		if (oldSelection != null) ShipManipulation.selectShip(oldSelection, false);
		if (newSelection != null) ShipManipulation.selectShip(newSelection, true);
	}
}
