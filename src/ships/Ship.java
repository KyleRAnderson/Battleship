package ships;

import java.util.ArrayList;

import board.Board;
import board.Square;
import javafx.scene.shape.Ellipse;
import manipulation.ShipManipulation;
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
	
	public static final double SIZE_X = 15, SIZE_Y = 5;
	
	// The player that this ship belongs to.
	public final Player player;
	
	// The current position of the game piece.
	private Square currentPosition;
	
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
		
		// Set up the size and fill of the ship.
		setRadiusX(SIZE_X);
		setRadiusY(SIZE_Y);
		setFill(player.getSelectionColour());
		
		// If it's a diagonal ship, rotate it so the user knows that it is.
		if (moveDirection.equals(DirectionOfMovement.Diagonal)) setRotate(45);
	}
	
	/**
	 * Moves the piece to the provided new position. 
	 * @param direction The direction in which to move the ship.
	 */
	public void move(Board.MoveDirection direction) {
		// Determine what the square in front is.
		Square newPosition = getSquareInDirection(direction);
		
		// Do null check before proceeding
		if (newPosition != null && canMove()) {
			// Before we move, clear selection
			ShipManipulation.selectShip(this, false);
			// Move to new position
			move(newPosition);
			// Notify player that ship was moved.
			player.shipMoved();
			
//			// TODO get working somehow Now begin the nice transition for what we're doing.
//			TranslateTransition transition = new TranslateTransition(Duration.millis(1000), this);
//			transition.setByX(1);
//			transition.setByY(1);
//			transition.play();
			player.getGame().getBoard().moveShip(this);
		}
	}
	
	/**
	 * Moves the piece to the provided new position.
	 * @param newPosition The new position of the ship
	 */
	public void move(Square newPosition) {
		// Attempt to add this ship to the given position.
		boolean didAdd = newPosition.addShip(this);
		
		// If it succeeded, move the ship
		if (didAdd) {
			// Get this ship off of it's old square.
			if (currentPosition != null) currentPosition.removeShip(this);
			currentPosition = newPosition;
		}
	}
	/**
	 * Determine if this ship can move, legally
	 * @return True if the ship is allowed to move, false otherwise.
	 */
	public boolean canMove() {
		return player.getMovesLeft() > 0 && !isDestroyed();
	}
	
	private Square getSquareInDirection(Board.MoveDirection direction) {
		// New coordinates for this ship, in theory.
		int newX = -1, newY = -1;
		
		if (moveDirection.equals(DirectionOfMovement.Diagonal)) {
			// Going up will always be the top left possible square.
			if (direction.equals(Board.MoveDirection.up)) {
				newX = currentPosition.xCoordinate - 1;
				newY = currentPosition.yCoordinate - 1;
			}
			// Going down is the bottom right possible square
			else if (direction.equals(Board.MoveDirection.down)) {
				newX = currentPosition.xCoordinate + 1;
				newY = currentPosition.yCoordinate + 1;
			}
			// The right option is the top right square.
			else if (direction.equals(Board.MoveDirection.right)) {
				newX = currentPosition.xCoordinate + 1;
				newY = currentPosition.yCoordinate - 1;
			}
			// Left is the bottom left square.
			else if (direction.equals(Board.MoveDirection.left)) {
				newX = currentPosition.xCoordinate - 1;
				newY = currentPosition.yCoordinate + 1;
			}
		}
		else {
			// Going up will be upwards square
			if (direction.equals(Board.MoveDirection.up)) {
				newX = currentPosition.xCoordinate;
				newY = currentPosition.yCoordinate - 1;
			}
			// Going down is the bottom possible square
			else if (direction.equals(Board.MoveDirection.down)) {
				newX = currentPosition.xCoordinate;
				newY = currentPosition.yCoordinate + 1;
			}
			// The right option is the right square.
			else if (direction.equals(Board.MoveDirection.right)) {
				newX = currentPosition.xCoordinate + 1;
				newY = currentPosition.yCoordinate;
			}
			// Left is the left square.
			else if (direction.equals(Board.MoveDirection.left)) {
				newX = currentPosition.xCoordinate - 1;
				newY = currentPosition.yCoordinate;
			}
		}
		// Get the square at the new coordinates.
		Square newSquare = player.getGame().getBoard().getSquare(newX, newY);
		
		return newSquare;
	}
	
	/**
	 * Determines the squares that this ship can possibly move to. 
	 * @return An arraylist of the squares that this ship can possibly move to.
	 */
	public ArrayList<Square> getPossibleSquares() {
		ArrayList<Square> possibleSquares = null;
			
		if (currentPosition != null && !isDestroyed()) {
			// If we move diagonally, calculate the squares.
			if (moveDirection.equals(DirectionOfMovement.Diagonal)) {
				// Instantiate new ArrayList
				possibleSquares = new ArrayList<Square>();
				
				for (int x = -1; x <= 1; x += 2) {
					for (int y = -1; y <= 1; y += 2) { 
						// Determine the coordinates of the square we're looking for and then get the square
						Square squareToAdd = player.getGame().getBoard().getSquare(currentPosition.xCoordinate + x, currentPosition.yCoordinate + y);
						// If the square exists, it won't be null and we'll add it to the possible squares.
						if (isPossibleSquare(squareToAdd)) possibleSquares.add(squareToAdd);
					}
				}
			}
			// If the ship moves horizontally, do a different calculation for the squares.
			else if (moveDirection.equals(DirectionOfMovement.Horizontal)) {
				// Instantiate new ArrayList
				possibleSquares = new ArrayList<Square>();
				
				for (int x = -1; x <= 1; x += 2) {
					// Determine the coordinates of the square we're looking for and then get the square
					Square squareToAdd = player.getGame().getBoard().getSquare(currentPosition.xCoordinate + x, currentPosition.yCoordinate);
					// If the square exists, it won't be null and we'll add it to the possible squares.
					if (isPossibleSquare(squareToAdd)) possibleSquares.add(squareToAdd);
				}
				for (int y = -1; y <= 1; y+= 2) {
					// Determine the coordinates of the square we're looking for and then get the square
					Square squareToAdd = player.getGame().getBoard().getSquare(currentPosition.xCoordinate, currentPosition.yCoordinate + y);
					// If the square exists, it won't be null and we'll add it to the possible squares.
					if (isPossibleSquare(squareToAdd)) possibleSquares.add(squareToAdd);
				}
			}
		}

		// Return results, even if it's null.
		return possibleSquares;
	}
	/**
	 * Determines if it's possible for the given ship to move to the given square
	 * @param square The square to move to
	 * @return True if the ship can move there, false otherwise.
	 */
	private boolean isPossibleSquare(Square square) {
		return isPossibleSquare(square, this);
	}
	
	/**
	 * Determines if it's possible for the given ship to move to the given square
	 * @param square The square to move to
	 * @param ship The ship that wishes to move.
	 * @return True if the ship can move there, false otherwise.
	 */
	private static boolean isPossibleSquare(Square square, Ship ship) {
		return square != null && square.canMoveToSquare(ship);
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
	 * Carries out the proper actions that need to take place if the ship is removed from the board.
	 */
	public void removeFromBoard() {
		currentPosition = null;
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
		return currentPosition != null;
	}
	
	/**
	 * Gets the square location of this ship
	 * @return The square location of this ship
	 */
	public Square getSquare() {
		return currentPosition;
	}
}
