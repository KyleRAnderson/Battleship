package ships;

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
	
	// The possible types of ships
	public static enum TypeOfShip {
		Destroyer, Aircraft, Battleship, Submarine, Small 
	}
	
	public final String image_location;
	
	/**
	 * List of damages that each ship will have.
	 */
	public static final int[] DAMAGES = new int[] { 80, 80, 50, 50, 40, 40, 40, 20, 20, 10 };
	public static final String[] IMAGE_LOCATIONS =  new String[] { "destroyer.png", "destroyer.png", 
			"aircraft_Carrier.png", "aircraft_carrier.png", "battleship.png", "battleship.png", 
			"battleship.png", "small.png", "small.png", "submarine.png" };
	
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
	
	// The damage that this ship's cannons do.
	public final int damage;
	
	/**
	 * Instantiates a new ship object
	 * @param player The player to whom this ship belongs
	 * @param moveDirection The direction that this ship is allowed to move.
	 * @param id The nth number of player's ships that this ship corresponds to.
	 */
	public Ship(Player player, DirectionOfMovement moveDirection, int id) {
		this.player = player;
		this.moveDirection = moveDirection;
		damage = DAMAGES[id];
		image_location = "file:/resources/ships/" + IMAGE_LOCATIONS[id];
	}
	
	/**
	 * Gets this ship's cannons' range in squares. The range is inversely proportional to the damage, 
	 * so that the further the ship CAN shoot, the less damage it does.
	 * @return The range of the ship in squares.
	 */
	public int getRange() {
		// Convert to percentage and mulitply by the number of rows in the table.
		return (int)Math.round((100 - damage) / 100.0 * Board.NUM_ROWS);  
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
	
	/**
	 * Determines if the player who owns this ship has this ship selected
	 * @return True if this ship is selected, false otherwise.
	 */
	public boolean isSelected() {
		return player.getSelectedShip().equals(this);
	}
}
