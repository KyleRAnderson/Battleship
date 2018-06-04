package board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import player.Player;
import ships.Ship;

/**
 * The square class, representing a square on the board.
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class Square extends Rectangle {
	// The x and y coordinate of this square.
	public final int xCoordinate, yCoordinate;
	
	// The ship currently on this square
	private Ship currentShip;
	// The board that this square belongs to.
	private Board board;
	
	static final Color DEFAULT_FILL = Color.ALICEBLUE, DEFAULT_STROKE = Color.BLACK;
	static final double DEFAULT_THICKNESS = 1.0, SELECTED_THICKNESS = 2.0;
	
	/**
	 * Whether or not this square is currently selected by a player
	 */
	private boolean isSelected;
	
	/**
	 * Instantiates a square and makes a square on the UI.
	 * @param x The x coordinate of the square
	 * @param y The y coordiante of the square.
	 */
	public Square(int x, int y, Board board) {
		// Actually make a rectangle now.
		super(30, 30);
		
		// Set up the board object
		this.board = board;
		// Also set the coordinates of this square.
		xCoordinate = x;
		yCoordinate = y;
		
		// Set the colours for the square
		setFill(DEFAULT_FILL);
		setStroke(DEFAULT_STROKE);
	}
	
	/**
	 * Returns the ship on this square
	 * @return The ship on this square
	 */
	public Ship getShipOnSquare() {
		return currentShip;
	}
	
	/**
	 * Shoots the ship at this position if there is one and then applies damage to it if it's hit
	 * @param shooter The ship shooting at the other ship.
	 */
	public void shoot(Ship shooter) {
		if (currentShip != null) {
			currentShip.getHit(shooter.damage);
		}
	}
	
	/**
	 * Attempts to add the ship to this position, adding it if possible, or
	 * entering battle mode if this square is being contested.
	 * @param ship
	 * @return
	 */
	public boolean addShip(Ship ship) {
		boolean shipAdded = false;
		
		// Only add the ship to this location if it's possible to do so.
		if (isUsable() && currentShip == null) {
			currentShip = ship;
			
			shipAdded = true;
		}
		/* If there's an enemy ship on this position already, this square is being contested
		 * so we need to enter battle mode
		 */
		else if (!currentShip.player.equals(ship.player)) {
			currentShip = Board.battle(currentShip, ship);
			shipAdded = currentShip.equals(ship);
		}
		
		return shipAdded;
	}
	
	/**
	 * Removes the given ship object from this square if it is in fact the one on this square
	 * @param ship The ship to be removed from this square.
	 */
	public void removeShip(Ship ship) {
		if (ship.equals(currentShip)) currentShip = null;
	}
	
	public void shipDestroyed(Ship ship) {
		// Only do stuff if the ship that was destroyed is the one that's on this square
		if (ship.equals(currentShip)) {
			setFill(Color.RED);
		}
	}
	
	/**
	 * Determines whether or not this square is usable, as if there's a shipwreck on
	 * this square it's not usable.
	 */
	public boolean isUsable() {
		return currentShip.isDestroyed();
	}
	
	/**
	 * Determines if this square should be selected, and if so selects it. 
	 * If not, goes back to default border.
	 */
	public void refreshSelection() {
		// Set selection variable to false
		isSelected = false;
		
		// The number of players on this square
		int numberOfPlayers = 0;
		// Iterate through all the players in the game
		for (Player player : board.getGame().getPlayers()) {
			if (player.x == xCoordinate && player.y == yCoordinate) {
				setSelected(player.getSelectionColour(), true);
				isSelected = true;
				numberOfPlayers++;
			}
		}
		
		// If there's more than one player on the grid, set to brown selection.
		if (numberOfPlayers > 1) setSelected(Color.BROWN, true);
		
		// If the selection variable is still false, reset fill to default
		if (!isSelected) setSelected(null, false);
	}
	
	/**
	 * Sets the thickness and the colour of selection of this rectangle
	 * @param colour The colour of the selection 
	 * @param selected True if the square is selected, false otherwise.
	 */
	private void setSelected(Color colour, boolean selected) {
		if (selected) setFill(colour);
		else setFill(DEFAULT_FILL);
	}
}
