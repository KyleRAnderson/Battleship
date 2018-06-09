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
	 * The size of each player's territory, used both to restrict where player's may place their ships at the
	 * start of the game and to determine if the other player's ship is in the enemy's territory 
	 * at the end of the game.
	 */
	public static final int TERRITORY_SIZE = 2;
	
	/**
	 * Determines whether the given point is within the territory of the provided side.
	 * @param territorySide The side to look at.
	 * @param x The x-value for object.
	 * @param y The y-value for the object.
	 * @return True if the object is within the specified territory, false otherwise.
	 */
	public static final boolean isWithinTerritory(Player.StartSide territorySide, int x, int y) {
		int territoryX = territorySide.equals(Player.StartSide.BottomRight) ? Board.NUM_COLUMNS - 1 : 0;
		return  Board.getDistanceBetween(x, y, territoryX, y) < TERRITORY_SIZE;
	}
	
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
		setFillToDefault();
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
	 * Determines if this square has an enemy ship or nothing on it.
	 * @param player The player to whom to determine if there's an enemy.
	 * @return True if there's an enemy or nothing on the square, false if the provided
	 * player has a ship on this square.
	 */
	public boolean hasEnemyOrNothing(Player player) {
		return currentShip == null || !currentShip.player.equals(player);
	}
	
	/**
	 * Shoots the ship at this position if there is one and then applies damage to it if it's hit
	 * @param shooter The player shooting at this square
	 */
	public void shoot(Player shooter) {
		if (currentShip != null) {
			currentShip.hit(shooter.getDamage());
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
			setFillToDefault();
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
			setFillToDefault();
		}
	}
	
	/**
	 * Determines whether or not this square is usable, as if there's a shipwreck on
	 * this square it's not usable.
	 */
	public boolean isUsable() {
		return currentShip == null || !currentShip.isDestroyed();
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
		else setFillToDefault();
	}
	
	/**
	 * Gets the x coordinate for the pixels of the center of this square
	 * @return The center x coordinate for this square
	 */
	public double getCenterX() {
		return getX() - getWidth() / 2;
	}
	
	/**
	 * Gets the y coordinate for the pixels of the center of this square.
	 * @return The center y coordinate for this square.
	 */
	public double getCenterY() {
		return getY() + getWidth() / 2;
	}
	
	/**
	 * Highlights this square with the player's highlight colour
	 * @param player The player highlighting this square
	 */
	public void highlight(Player player) {
		setStroke(player.getSelectionColour());
	}
	
	/**
	 * Un-highlights this square, resetting its properties.
	 */
	public void clearHighlight() {
		setStroke(DEFAULT_STROKE);
	}
	
	/**
	 * Determines if the given ship can move to this square
	 * @param ship The ship that wishes to move to this square
	 * @return True if it can move, false otherwise.
	 */
	public boolean canMoveToSquare(Ship ship) {
		return currentShip == null || !currentShip.player.equals(ship.player);
	}
	
	/**
	 * Sets the fill properly to whatever the default is.
	 */
	private void setFillToDefault() {
		if (isUsable()) setFill(DEFAULT_FILL);
		else setFill(Color.RED);
	}
}
