package ships;

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
	
	// The player that this ship belongs to.
	public final Player player;
	
	// The current position of the game piece.
	private Square currentPosition;
	
	// The current position of the ship on the grid.
	private int currentX, currentY;
	
	// The direction that this type of ship can move in.
	private final DirectionOfMovement moveDirection;
	
	// The range of the ship's cannons, in squares. Also, the health of the ship.
	private int cannonRange, health = 100;
	
	// The damage that this ship's cannons do.
	public final int damage;
	
	/**
	 * The ships's id, from 1 to [number of ships]
	 */
	public final int id;
	
	/**
	 * Instantiates a new ship object
	 * @param position The initial square position of the ship on the board
	 * @param player The player to whom this ship belongs
	 * @param moveDirection The direction that this ship is allowed to move.
	 */
	public Ship(Square position, Player player, DirectionOfMovement moveDirection, int damage, int id) {
		currentPosition = position;
		this.player = player;
		this.moveDirection = moveDirection;
		this.damage = damage;
		this.id = id;
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
	
	public void getHit(int damage) {
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
}
