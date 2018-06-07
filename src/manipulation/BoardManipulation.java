package manipulation;

import board.Board;
import board.Square;
import player.Player;

public class BoardManipulation {
	
	/**
	 * The possible directions for the player to move.
	 */
	public static enum MoveDirection {
		up, down, left, right
	}
	
	/**
	 * Moves the cursor selection of the player one square in the given direction.
	 * @param player The player whose cursor needs to be moved.
	 * @param direction The direction of the movement.
	 */
	public void move(Player player, MoveDirection direction) {
		
		// Determine if the player has a ship selected. If they do, we're moving the ship, not the selection
		if (player.getSelectedShip() == null) {
			int newX = player.x, newY = player.y;
			// Make sure that it's possible to move in the direction, then move in that direction.
			if (direction.equals(MoveDirection.left) && player.x > 0) newX--;
			else if (direction.equals(MoveDirection.right) && player.x < Board.NUM_COLUMNS - 1) newX++;
			else if (direction.equals(MoveDirection.up) && player.y > 0) newY--;
			else if (direction.equals(MoveDirection.down) && player.y < Board.NUM_ROWS - 1) newY++;
			
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
			player.x = x;
			player.y = y;
			
			// Get the new square to which the player belongs
			Square newSquare = board.getSquare(x, y);
			
			// Refresh the selection on the squares
			oldSquare.refreshSelection();
			newSquare.refreshSelection();
		}
	}
}
