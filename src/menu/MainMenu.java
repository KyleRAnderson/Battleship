package menu;

import game.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
	private BorderPane organizer;
	
	/**
	 * Creates a new main menu object used by the user for preferences, new games, etc.
	 */
	public MainMenu() {
		// Begin the music right away
		MusicPlayer.play();
		
		organizer = new BorderPane();
		getChildren().add(organizer);
		
		organizer.setBackground(new Background(new BackgroundImage(new Image("file:" + 
		BattleshipGalactica.RESOURCES_LOCATION + "/main_background.png", 1000, 1000, false, true), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		// Start a new game to start things
		newGame();
		
		// Game controls will go on top of the game, in a horizontal box.
		Button newGameButton = new Button("New Game");
		
		newGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newGame();				
			}
		});
		
		organizer.setTop(new HBox(newGameButton));
		
		
		// Music controls go on the right side of the organizer
		Button pausePlayMusic = new Button("Pause Music");
		pausePlayMusic.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MusicPlayer.toggleState();
				if (MusicPlayer.isPlaying()) {
					pausePlayMusic.setText("Pause Music");
				}
				else pausePlayMusic.setText("Play Music");
			}
		});
		
		// Button to skip this song and go to the next one.
		Button nextSong = new Button("Next Song");
		nextSong.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MusicPlayer.nextSong();				
			}
		});
		
		// Add the button to the screen.
		organizer.setRight(new VBox(pausePlayMusic, nextSong));
	}
	
	/**
	 * Makes a new game and then starts it.
	 */
	public void newGame() {
		currentGame = new Game();
		currentGame.start();
		
		// Put the game on screen.
		organizer.setCenter(currentGame.getBoard());
	}
}
