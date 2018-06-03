package manipulation;

import board.Board;
import main.BattleshipGalacticaMain;
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
	public static void move(Player player, MoveDirection direction) {
		Board board = BattleshipGalacticaMain.getBoard();
		board.getSquare(player.x, player.y).deselect();
		
		// If we're moving left, go left if possible.
		if (direction.equals(MoveDirection.left) && player.x > 0) player.x--;
		else if (direction.equals(MoveDirection.right) && player.x < Board.NUM_COLUMNS) player.x++;
		else if (direction.equals(MoveDirection.up) && player.y < Board.NUM_ROWS) player.y++;
		else if (direction.equals(MoveDirection.down) && player.x > 0) player.y--;
		
		board.getSquare(player.x, player.y).select(player);
	}
	
	public static void moveTo(Player player, int x, int y) {		
		Board board = BattleshipGalacticaMain.getBoard();
		board.getSquare(player.x, player.y).deselect();
		player.x = x;
		player.y = y;
		board.getSquare(player.x, player.y).select(player);
	}
}
