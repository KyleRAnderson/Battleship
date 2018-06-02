package board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	
	/**
	 * Instantiates a square and makes a square on the UI.
	 * @param x The x coordinate of the square
	 * @param y The y coordiante of the square.
	 */
	public Square(int x, int y) {
		// Actually make a rectangle now.
		super(30, 30);
		
		xCoordinate = x;
		yCoordinate = y;
		
		// Set the colours for the square
		setFill(Color.LIGHTBLUE);
		setStroke(Color.AQUA);
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
		else if (currentShip.player != ship.player) {
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
}
