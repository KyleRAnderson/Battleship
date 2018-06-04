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
	 * The players of this game.
	 */
	Player[] players;
	
	/**
	 * Instantiates a new game object and readies for the playing of a new game.
	 */
	public Game() {
		board = new Board(this);
		players = new Player[] { new IMC(this), new Militia(this) };
		
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
