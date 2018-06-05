package main;

import board.Board;
import manipulation.BoardManipulation;
import manipulation.ShipManipulation;
import player.IMC;
import player.Militia;
import player.Player;

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
	public final BoardManipulation boardManipulation;
	public final ShipManipulation shipManipulation;
	
	/**
	 * The current round number.
	 */
	private int turn = 0;
	
	/**
	 * The state of this current game.
	 */
	public static enum GameState {
		ShipPlacement, Firing, Player1Movement, Player2Movement;  
	}
	
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
		
		boardManipulation = new BoardManipulation();
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
	}
	
	/**
	 * Begins the next round of the game.
	 */
	public void nextTurn() {
		if (isWinner()) end();
		for (Player player : players) {
			player.resetShots();
		}
		
		turn++;
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
}
