package ships;

import java.util.ArrayList;

import board.Board;
import board.Square;
import javafx.scene.shape.Ellipse;
import player.Player;

/**
 * The ship class representing a player's ship.
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class Ship extends Ellipse {	
	// The types of movement possible for a ship
	public static enum DirectionOfMovement {
		Horizontal, Diagonal
	}
	
	/**
	 * The default amount of damage that a ship does.
	 */
	public static int DEFAULT_DAMAGE = 50;
	
	// The player that this ship belongs to.
	public final Player player;
	
	// The current position of the game piece.
	private Square currentPosition;
	
	// The current position of the ship on the grid.
	private int currentX, currentY;
	
	// The direction that this type of ship can move in.
	private final DirectionOfMovement moveDirection;
	
	// The health of the ship.
	private int health = 100;
	
	/**
	 * Instantiates a new ship object
	 * @param player The player to whom this ship belongs
	 * @param moveDirection The direction that this ship is allowed to move.
	 */
	public Ship(Player player, DirectionOfMovement moveDirection) {
		this.player = player;
		this.moveDirection = moveDirection;
	}
	
	/**
	 * Moves the piece to the provided new position.
	 * @prarma newPosition The new position of the ship
	 */
	public void move(Square newPosition) {
		// Attempt to add this ship to the given position.
		boolean didAdd = newPosition.addShip(this);
		
		// If it succeeded, move the ship
		if (didAdd) {
			currentPosition = newPosition;
		}
	}
	
	/**
	 * Determines the squares that this ship can possibly move to. 
	 * @return An arraylist of the squares that this ship can possibly move to.
	 */
	public ArrayList<Square> getPossibleSquares() {
		ArrayList<Square> possibleSquares = null;
		
		// If we move diagonally, calculate the squares.
		if (moveDirection.equals(DirectionOfMovement.Diagonal)) {
			// Instantiate new ArrayList
			possibleSquares = new ArrayList<Square>();
			
			for (int x = -1; x <= 1; x += 2) {
				for (int y = -1; y <= 1; y += 2) { 
					// Determine the coordinates of the square we're looking for and then get the square
					Square squareToAdd = player.getGame().getBoard().getSquare(currentX + x, currentY + y);
					// If the square exists, it won't be null and we'll add it to the possible squares.
					if (!squareToAdd.equals(null)) possibleSquares.add(squareToAdd);
				}
			}
		}
		// If the ship moves horizontally, do a different calculation for the squares.
		else if (moveDirection.equals(DirectionOfMovement.Horizontal)) {
			// Instantiate new ArrayList
			possibleSquares = new ArrayList<Square>();
			
			for (int x = -1; x <= 1; x += 2) {
				// Determine the coordinates of the square we're looking for and then get the square
				Square squareToAdd = player.getGame().getBoard().getSquare(currentX + x, currentY);
				// If the square exists, it won't be null and we'll add it to the possible squares.
				if (!squareToAdd.equals(null)) possibleSquares.add(squareToAdd);
			}
			for (int y = -1; y <= 1; y+= 2) {
				// Determine the coordinates of the square we're looking for and then get the square
				Square squareToAdd = player.getGame().getBoard().getSquare(currentX, currentY + y);
				// If the square exists, it won't be null and we'll add it to the possible squares.
				if (!squareToAdd.equals(null)) possibleSquares.add(squareToAdd);
			}
		}
			
		// Return results, even if it's null.
		return possibleSquares;
	}
	
	public void hit(int damage) {
		// Subtract what's possible to be subtracted from the health.
		health -= (damage > health) ? health : damage;
		
		// If the ship has no more health left, you sunk my battleship! Destroy it!
		if (isDestroyed()) destroy();
	}
	

	/**
	 * Destroys the current ship, setting its square to a red fill and making it unusable.
	 */
	public void destroy() {
		health = 0;
		// Tell the square that we were on that we were sunk.
		currentPosition.shipDestroyed(this);
	}
	
	/**
	 * Determines if this ship has been sunk
	 * @return True if the ship's been sunk, false otherwise.
	 */
	public boolean isDestroyed() {
		return health <= 0;
	}
	
	/**
	 * Gets this ship's current health
	 * @return The health of the ship, between 0 and 100.
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Determines if this ship has been placed or not.
	 * @return True if the ship has been placed, false otherwise.
	 */
	public boolean hasBeenPlaced() {
		// Just make sure that the position is valid.
		return Board.isValidPosition(currentX, currentY);
	}
}
