package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import music.MusicPlayer;

/**
 * 
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class BattleshipGalactica extends Application {	
	/**
	 * The file path for the resources used in this program.
	 */
	public static final String RESOURCES_LOCATION = "resources";
	// The current game being played.
	private static Game currentGame;	
	
	/**
	 * The font for all headings in the game.
	 */
	public static final Font HEADING_FONT = Font.font("Impact", 20), CONTENT_FONT = Font.font("Impact", 10);
	
	/**
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// Begin the music right away
		MusicPlayer.play();
		
		currentGame = new Game();
		// FIRST thing that needs to be done is the generation of the board. Everything else breaks without a board.
		Scene scene = new Scene(currentGame.getBoard());
		
		// Now set up some window things
		primaryStage.setTitle("Battleship Galactica");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
		// Now begin monitoring user input.
		InputHandler.setScene(scene);
		
		currentGame.start();
		
		// Show the scene after all the rendering is done.
		primaryStage.show();
	}
}
