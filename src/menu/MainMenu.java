package menu;

import game.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import main.BattleshipGalactica;
import music.MusicPlayer;

/**
 * The main menu for the game so that the user can change preferences, start a new game, etc.
 * @author Kyle Anderson
 * 2018-06-11
 * ICS3U
 */
public class MainMenu extends Parent {
	
	/**
	 * The current game that's occuring
	 */
	private Game currentGame;
	
	/**
	 * The organizer for the menu.
	 */
	private GridPane organizer;
	
	/**
	 * Creates a new main menu object used by the user for preferences, new games, etc.
	 */
	public MainMenu() {
		// Begin the music right away
		MusicPlayer.play();
		
		organizer = new GridPane();
		getChildren().add(organizer);
		
		Button newGameButton = new Button("New Game");
		GridPane.setConstraints(newGameButton, 0, 0);
		organizer.getChildren().add(newGameButton);
		
		newGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newGame();				
			}
		});
	}
	
	/**
	 * Makes a new game and then starts it.
	 */
	public void newGame() {
		currentGame = new Game();
		currentGame.start();
		
		changeRoots(organizer, currentGame.getBoard());
	}
	
	/**
	 * Shows the menu after the provided game ends
	 * @param game The game that just ended
	 */
	public void gameEnded(Game game) {
		// Only do something if the provided game is the one we're currently showing
		if (game != null && currentGame != null && currentGame.equals(game)) {
			changeRoots(game.getBoard(), organizer);
			currentGame = null;
		}
	}
	
	/**
	 * Changes what is being displayed on screen 
	 * @param oldRoot The old root to be removed from the screen
	 * @param newRoot The new root to be added to the screen.
	 */
	private void changeRoots(Node oldRoot, Node newRoot) {
		getChildren().remove(oldRoot);
		getChildren().add(newRoot);
		
		// Size the window properly.
		BattleshipGalactica.resize();
	}
}
