package player;

public class Player {
	// The type of this player.
	final PlayerType type;
	
	/**
	 * The type of players
	 */
	public static enum PlayerType {
		IMC, Militia
	}
	
	public Player(PlayerType player) {
		type = player;
	}
}
