package main;

import board.Board;
import manipulation.PlayerManipulation;
import manipulation.ShipManipulation;
import player.IMC;
import player.Militia;
import player.Player;
import ships.Ship;

/**
 * Game object that represents a game being played
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class Game {
	/**
	 * The board that belongs to this game.
	 */
	Board board;
	public final PlayerManipulation boardManipulation;
	public final ShipManipulation shipManipulation;
	
	/**
	 * The current round number.
	 */
	private int turn = 0;
	
	/**
	 * The state of this current game.
	 */
	public static enum GameState {
		ShipPlacement, Firing, Movement;  
	}
	
	// The current game state. Start it in ship placement by default.
	private GameState state = GameState.ShipPlacement;
	
	/**
	 * The players of this game.
	 */
	Player[] players;
	
	/**
	 * Instantiates a new game object and readies for the playing of a new game.
	 */
	public Game() {
		
		// Reset input from any previous games (if any)
		InputHandler.stopMonitoring();
		InputHandler.resetBindings();
		
		// Make the players
		players = new Player[] { new IMC(this, Player.StartSide.BottomRight), new Militia(this, Player.StartSide.TopLeft) };
		
		// Make the board
		board = new Board(this);

		// Set the player help controls.
		for (Player player : getPlayers()) {
			player.setupSideBar();
		}
		
		boardManipulation = new PlayerManipulation();
		shipManipulation = new ShipManipulation();
	}
	
	/**
	 * Begins the game
	 */
	public void start() {
		// Begin by positioning all players in their start positions
		for (Player player : players) {
			player.resetPosition();
		}
		
		// Begin monitoring input
		InputHandler.startMonitoring();
		
		startPlacement();
	}
	
	/**
	 * Begin the placement stage of the game
	 */
	private void startPlacement() {
		// Set the state to ship placement.
		state = GameState.ShipPlacement;
		
		// Give the user a little help with what to do
		board.setMessage("Place your ships on the board.");
	}
	
	/**
	 * Begin the firing stage of the game
	 */
	private void startFiring() {
		state = GameState.Firing;
		updateTurn();
		
		board.setMessage("Firing stage: Hit your opponents!");
	}
	
	/**
	 * Begin the movement stage of the game.
	 */
	private void startMovement() {
		state = GameState.Movement;
		updateTurn();
		// Set a message for the users.
		board.setMessage("Select your ships and move them with the move keys!");
	}
	
	/**
	 * Begins the next round of the game.
	 */
	public void updateTurn() {
		// Set a stage display message
		String stage = getState().equals(GameState.Firing) ? "Firing " : "Movement ";
		
		// Only actually advance the round if it's a movement stage.
		if (getState().equals(GameState.Firing)) {
			for (Player player : players) {			
				player.resetForNextRound();
			}
			
			turn++;
		}
		
		if (isWinner()) end();
		board.setStatus(stage + " turn " + turn);
	}
	
	public boolean isWinner() {
		// TODO replace with real code
		return false;
	}
	
	/**
	 * Ends this game, displaying the victor
	 */
	public void end() {
		
	}
	
	/**
	 * Gets the players participating in this game
	 * @return An array of the players in this game.
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Gets the board on which this game is being played
	 * @return The board on which this game is being played.
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Returns the state of the current game, what's going on.
	 * @return The game's state.
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Refreshes the game's current state.
	 */
	public void refreshState() {
		GameState state = getState();
		// If we're in ship placement, check if all ships for each player have been placed.
		if (state.equals(GameState.ShipPlacement)) {
			boolean unplacedShip = false;
			for (Ship ship : getBoard().getShips()) {
				if (!ship.hasBeenPlaced()) {
					unplacedShip = true;
					break;
				}
			}
			
			// If there are no unplaced ships, let's move on to first round of firing. 
			if (!unplacedShip) { 
				startFiring();
			}
		}
		else if (state.equals(GameState.Firing)) {
			boolean canFire = false;
			// Iterate through each player and see if any of them can stil move
			for (Player player : getPlayers()) {
				canFire = player.canShoot();
				if (canFire) break;
			}
			
			if (!canFire) startMovement();
		}
		else if (state.equals(GameState.Movement)) {
			boolean canMove = false;
			// Iterate through each player and see if any of them can stil move
			for (Player player : getPlayers()) {
				canMove = player.canMove();
				if (canMove) break;
			}
			
			if (!canMove) startFiring();
		}
		
	}
}
