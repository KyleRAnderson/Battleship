package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import board.Board;
import board.Square;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.BattleshipGalactica;
import main.Game;
import main.InputHandler;
import manipulation.PlayerManipulation;
import manipulation.ShipManipulation;
import ships.Ship;

public abstract class Player {
	public static enum StartSide {
		TopLeft, BottomRight
	}
	
	// The current position (selection) of the player.
	public int x, y;
	
	// The starting position of the player
	private final int start_x, start_y;
	
	/**
	 * The amount of damage that this player does when hitting a ship with a cannon ball.
	 */
	protected int damage = 50;
	
	private StartSide startSide;
	
	/**
	 * The player's ships
	 */
	protected ArrayList<Ship> ships = new ArrayList<Ship>();
	
	// The ship currently selected by this player
	protected Ship selectedShip;
	
	protected static int NUM_SHOTS = 4, NUM_MOVES = 1;
	protected int numShotsLeft = NUM_SHOTS, numMovesLeft = NUM_MOVES ;
	
	// Some things displayed on the board to help the user know what's going on.
	protected Text keyBindingsHelp, shotsLeftDisplay, movesLeftDisplay, shipsLeftDisplay;
	
	// The board object on which this player is playing.
	protected Game game;
	
	public static final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT", ENTER = "ENTER", TOGGLE_HIDE = "TOGGLE_HIDE", CANCEL = "CANCEL";
	
	/**
	 * Instantiates a player object.
	 * @param game The game to which this player belongs
	 */
	public Player(Game game, StartSide side) {
		this.game = game;
		startSide = side;
		
		// Set the start position of this player based on the start side.
		if (side.equals(StartSide.BottomRight)) {
			start_x = Board.NUM_COLUMNS - 1;
			start_y = Board.NUM_ROWS - 1;
		}
		else {
			start_x = 0;
			start_y = 0;
		}
		
		InputHandler.addKeyBindings(getKeysUsed().values(), new Consumer<KeyCode>() {
			@Override
			public void accept(KeyCode t) {
				onKeyPressed(t);
			}
		});
	}
	
	/**
	 * Handles player specific key presses.
	 * @param key The key that was pressed.
	 */
	private void onKeyPressed(KeyCode key) {
		HashMap<String, KeyCode> keyBindings = getKeyBindings();
		if (key.equals(keyBindings.get(UP))) game.boardManipulation.move(this, Board.MoveDirection.up);
		else if (key.equals(keyBindings.get(DOWN))) game.boardManipulation.move(this, Board.MoveDirection.down);
		else if (key.equals(keyBindings.get(LEFT))) game.boardManipulation.move(this, Board.MoveDirection.left);
		else if (key.equals(keyBindings.get(RIGHT))) game.boardManipulation.move(this, Board.MoveDirection.right);
		else if (key.equals(keyBindings.get(ENTER))) ShipManipulation.enterPressed(this);
		else if (key.equals(keyBindings.get(TOGGLE_HIDE))) toggleHide();
		else if (key.equals(keyBindings.get(CANCEL))) setSelectedShip(null);
	}
	
	/**
	 * Gets the key bindings for the specific 
	 * @return
	 */
	abstract HashMap<String, KeyCode> getKeyBindings();
	
	/**
	 * Returns a list of the keys that this player uses
	 * @return The keys used by this player.
	 */
	public HashMap<String, KeyCode> getKeysUsed() {
		return getKeyBindings(); // TODO make a copy before returning.
	}
	
	/**
	 * Resets the player's position to the starting position.
	 */
	public void resetPosition() {
		x = start_x;
		y = start_y;
		PlayerManipulation.moveTo(this, x, y);
	}
	
	/**
	 * Get the colour that this player should glow when selecting a square
	 * @return The colour of this player's selection
	 */
	public abstract Color getSelectionColour();
	
	/**
	 * Gets the game that this player belongs to.
	 * @return The game object to which this player belongs.
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Gets the ImageView for this player's icon.
	 * @return This player's icon.
	 */
	public abstract ImageView getIcon();
	
	/**
	 * Adds the given ship to this player's list of ships
	 * @param ship The ship to add to the player's record.
	 */
	public void addShip(Ship ship) {
		ships.add(ship);
	}
	
	/**
	 * Gets all the ships that belong to this player
	 * @return This playe's ships.
	 */
	public Ship[] getShips() {
		return ships.toArray(new Ship[0]);
	}
	
	/**
	 * Gets the starting side of this Player, bottom right or top left
	 * @return The start side of this player.
	 */
	public StartSide getStartPosition() {
		return startSide;
	}
	
	/**
	 * Determines the square at this player's start position.
	 * @return The square at the player's start position.
	 */
	public Square getStartSquare() {
		// Get the square at start position.
		return game.getBoard().getSquare(start_x, start_y);
	}
	
	/**
	 * Determines the number of ships that this player has left.
	 * @return The number of ships that the player has left.
	 */
	public int getNumShipsLeft() {
		int left = 0;
		
		// Iterate through each ship and then add to the counter each time we find a ship that's alive.
		for (Ship ship : ships) {
			if (!ship.isDestroyed()) left++;
		}
		
		
		// Return the results.
		return left;
	}
	
	/**
	 * Shoots the given square 
	 * @param squareToShoot
	 */
	public void shoot(Square squareToShoot) {
		// Only shoot at a square if we have shots left and if that square is usable.
		if (canShoot() && squareToShoot.isUsable() && squareToShoot.hasEnemyOrNothing(this)) { 
			numShotsLeft--;
			squareToShoot.shoot(this);
			game.refreshState();
			setShotsLeftDisplay();
		}
	}
	
	/**
	 * Determines the number of shots that this player has left for this turn, keeping into account the number of ships
	 * the player has.
	 * @return The number of shots that the player has left.
	 */
	public int getShotsLeft() {
		return (getNumShipsLeft() > 0) ? numShotsLeft : 0;
	}
	
	/**
	 * Determines if the given player can still shoot
	 * @return True if the player can still shoot, false otherwise
	 */
	public boolean canShoot() { return getShotsLeft() > 0 && getNumShipsLeft() > 0; }
	
	/**
	 * Resets the number of shots that the player has for this turn as well as the number of moves
	 * in preparation for the next round.
	 */
	public void resetForNextRound() {
		numShotsLeft = NUM_SHOTS;
		numMovesLeft = NUM_MOVES;
		setMovesLeftDisplay();
		setShotsLeftDisplay();
	}
	
	/**
	 * Returns the amount of damage this player's cannon balls do.
	 * @return The amount of damage this player's cannon balls do.
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Gets the ship that the player currently has selected and is (likely) in the middle of moving.
	 * @return The ship that the player has selected.
	 */
	public Ship getSelectedShip() {
		return selectedShip;
	}
	
	/**
	 * Reset the player's selected ship to the given ship
	 * @param ship The ship to select.
	 */
	public void setSelectedShip(Ship ship) {
		// Only do stuff if the ship is null or its player is this player. DON'T MOVE OPPONENTS SHIPS!
		if (ship == null || (ship.player.equals(this) && canMove())) {
			// Call selection changed function (usually would be an event).
			PlayerManipulation.shipSelectionChanged(selectedShip, ship, this);
			
			selectedShip = ship;
		}
	}
	
	/**
	 * Determines whether or not this player has selected a ship
	 * @return True if the player has a ship selected, false otherwise.
	 */
	public boolean hasSelectedShip() {
		return selectedShip != null;
	}
	
	/**
	 * Toggles the visibility of this player's ships, hiding them from the other players or showing them again.
	 */
	public void toggleHide() {
		toggleHide(!hidden);
	}
	
	// Whether or not the player's ships are hidden.
	private boolean hidden;
	/**
	 * Hides or shows all of the player's hides, if the player would like to see their ships
	 * or if the player wishes to hide their ships. 
	 * @param hide True to hid the player's ships, false to show them again.
	 */
	public void toggleHide(boolean hide) {
		// If everything is already hidden or already being shown, we needn't bother do anything.
		if (hidden != hide) {
			hidden = hide;
		}
		
		// Now iterate through each ship and set their visibility appropriately.
		for (Ship ship : ships) {
			ship.setVisible(!hidden);
		}
	}
	
	/**
	 * Moves the player's selection to the given coordinates
	 * @param x The new x coordinate
	 * @param y The new y coordinate
	 */
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
		// Nullify the selected ship.
		setSelectedShip(null);
	}
	
	/**
	 * Called when one of this player's ships is moved in order to decrease the 
	 * move counter.
	 */
	public void shipMoved() {
		numMovesLeft--;
		setMovesLeftDisplay();
	}
	
	/**
	 * Determines the amount of ship moves this player has left
	 * @return The number of moves that this player has left in the turn.
	 */
	public int getMovesLeft() {
		return numMovesLeft;
	}
	
	/**
	 * Determines if the player can still move his/her ships
	 * @return True if the player can still move his/her ships, false otherwise.
	 */
	public boolean canMove() { return getMovesLeft() > 0; }
	
	/**
	 * Sets up the sidebar items for this player
	 */
	public void setupSideBar() {
		// Get the player's key bindings
		HashMap<String, KeyCode> keyBindings = getKeysUsed();
		
		// Format the help.
		String text = String.format(
						"%s: Show or hide your game pieces.\n" +
						"%s: Cancel the operation\n" +
						"%s: Select.\n" +
						"%s: Move the cursor up.\n" +
						"%s: Move the cursor down.\n" +
						"%s: Move the cursor left.\n" +
						"%s: Move the cursor right.\n",
						keyBindings.get(Player.TOGGLE_HIDE).toString(), 
						keyBindings.get(Player.CANCEL).toString(), 
						keyBindings.get(Player.ENTER).toString(),
						keyBindings.get(Player.UP).toString(),
						keyBindings.get(Player.DOWN).toString(),
						keyBindings.get(Player.LEFT).toString(),
						keyBindings.get(Player.RIGHT).toString()
				);
		// Make new text object to display this helpful stuff.
		keyBindingsHelp = new Text();
		keyBindingsHelp.setFont(BattleshipGalactica.CONTENT_FONT);
		keyBindingsHelp.setText(text);
		
		shotsLeftDisplay = new Text();
		shotsLeftDisplay.setFont(BattleshipGalactica.HEADING_FONT);
		setShotsLeftDisplay();
		
		movesLeftDisplay = new Text();
		movesLeftDisplay.setFont(BattleshipGalactica.HEADING_FONT);
		setMovesLeftDisplay();
		
		shipsLeftDisplay = new Text();
		shipsLeftDisplay.setFont(BattleshipGalactica.HEADING_FONT);
		setShipsLeftDisplay();
		
		// Set up the alignments of the text boxes properly. 
		if (getStartPosition().equals(StartSide.BottomRight)) {
			shipsLeftDisplay.setTextAlignment(TextAlignment.RIGHT);
			movesLeftDisplay.setTextAlignment(TextAlignment.RIGHT);
			shotsLeftDisplay.setTextAlignment(TextAlignment.RIGHT);
			keyBindingsHelp.setTextAlignment(TextAlignment.RIGHT);
		}
		else {
			shipsLeftDisplay.setTextAlignment(TextAlignment.LEFT);
			movesLeftDisplay.setTextAlignment(TextAlignment.LEFT);
			shotsLeftDisplay.setTextAlignment(TextAlignment.LEFT);
			keyBindingsHelp.setTextAlignment(TextAlignment.LEFT);
		}
		
		getGame().getBoard().setPlayerSidebar(this);
	}
	
	/**
	 * Returns an array of this player's sidebar nodes.
	 * @return An array of the nodes for this player's sidebar.
	 */
	public Node[] getSidebarItems() {
		return new Node[] { keyBindingsHelp, shotsLeftDisplay, movesLeftDisplay, shipsLeftDisplay };
	}
	
	private void setMovesLeftDisplay() {
		String text = formatTextForSidebar("Moves", String.valueOf(getMovesLeft()));
		movesLeftDisplay.setText(text);
	}
	
	private void setShotsLeftDisplay() {
		String text = formatTextForSidebar("Shots", String.valueOf(getShotsLeft()));
		shotsLeftDisplay.setText(text);
	}
	
	private void setShipsLeftDisplay() {
		String text = formatTextForSidebar("Ships Left", String.valueOf(getNumShipsLeft()));
		shipsLeftDisplay.setText(text);
	}
	
	/**
	 * Nicely formats text-labels so that the text part is always closest to the board itself and the label is always on the
	 * border of the screen.
	 * @param label The label text to be displayed.
	 * @param text The text to be displayed
	 * @return The label and the text seperated by two spaces on either side of a semicolon and in an order
	 * which depends on this player's side.
	 */
	private String formatTextForSidebar(String label, String text) {
		return getStartPosition().equals(StartSide.BottomRight) ? text + " : " + label : label + " : " + text;
	}
	
	/**
	 * Determines if this player has won the game by getting all of their ships in the 
	 * enemy's territory.
	 * @return True if the player has won the game, false otherwise.
	 */
	public boolean hasWon() {
		// We assume that this player has won until a ship turns out not to be in enemy territory.
		boolean hasWon = true;
		
		// Iterate through each ship and determine if it's in enemy territory.
		for (Ship ship : ships) {
			hasWon = ship.isInEnemyTerritory() && !ship.isDestroyed();
			if (!hasWon) break;
		}
		
		// Return results
		return hasWon;
	}
	
	/**
	 * Called when one of this player's ships are destroyed to update GUI elements..
	 * @param ship The ship that was destroyed
	 */
	public void shipDestroyed(Ship ship) {
		setShipsLeftDisplay();
	}
}
