package menu;

import game.Game;
import game.board.Square;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	 * Help text displayed to the user when they press the help button.
	 */
	private static final String HELP_TEXT = 
			"Welcome to Battleship Galactica! A hybrid of the classic game of chess and\n"
			+ "the interesting game of battleship created by Kyle Anderson.\n"
			+ "The goal of the game is simple: either knock out all of your opponent's ships,\n"
			+ "using your cannons, or get all of your own ships to the other person's end of the game\n"
			+ "(within " + Square.TERRITORY_SIZE + " squares of their side). Start by placing your ships within\n"
			+ "your own territory using the select key and the directional keys for your playing position,\n"
			+ "in what is known as the ship placement stage.\n"
			+ "Then move on to firing stage, where you use the directional keys and the select key to hit the\n"
			+ "enemy's ship. It's a good idea to hide your ships from your opponent using the hide key.\n"
			+ "You'll know you hit it if the square flashes red. Otherwise the square flashes yellow.\n"
			+ "Then, you get to move your ships using the select key and the directional keys, by selecting\n"
			+ "the ship you want to move and then using the directional keys to indicate the direction. Possible\n"
			+ "squares that the ship can travel to will be highlighted in your colour. Then the cycle repeats\n"
			+ "with the firing stage once again.\n"
			+ "The key bindings for your player are listed on your player's side. Good luck, and have fun."
			;
	
	/**
	 * Creates a new main menu object used by the user for preferences, new games, etc.
	 */
	public MainMenu() {
		// Begin the music right away
		MusicPlayer.play();
		
		organizer = new BorderPane();
		getChildren().add(organizer);
		
		// Set a nice image background for the entire window.
		organizer.setBackground(new Background(new BackgroundImage(new Image(BattleshipGalactica.getCorrectPath(
		BattleshipGalactica.RESOURCES_LOCATION + "/main_background.png"), 1000, 1000, false, true), 
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		
		// Start a new game to start things
		newGame();
		
		// Game controls will go on top of the game, in a horizontal box.
		Button newGameButton = new Button("New Game");
		// Make the button unfocusable so that it won't be selected by accident during the game.
		newGameButton.setFocusTraversable(false);
		
		newGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				newGame();				
			}
		});
		
		
		// Music controls go on the right side of the organizer
		Button pausePlayMusic = new Button("Pause Music");
		// Make the button unfocusable so that it won't be selected by accident during the game.
		pausePlayMusic.setFocusTraversable(false);
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
		// Make the button unfocusable so that it won't be selected by accident during the game.
		nextSong.setFocusTraversable(false);
		nextSong.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MusicPlayer.nextSong();
				if (MusicPlayer.isPlaying()) {
					pausePlayMusic.setText("Pause Music");
				}
				else pausePlayMusic.setText("Play Music");
			}
		});
		
		Button helpButton = new Button("Help");
		helpButton.setFocusTraversable(false);
		helpButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Simply show the help text to the user.
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Help");
				alert.setHeaderText("Battleship Galactica Quick Start");
				alert.setContentText(HELP_TEXT);

				alert.showAndWait();
			}
		});
		
		// Add all of the buttons to the screen
		organizer.setTop(new HBox(newGameButton, helpButton, pausePlayMusic, nextSong));
	}
	
	/**
	 * Makes a new game and then starts it.
	 */
	public void newGame() {
		if (currentGame != null) {
			currentGame.end();
		}
		currentGame = new Game();
		currentGame.start();
		
		// Put the game on screen.
		organizer.setCenter(currentGame.getBoard());
	}
}
