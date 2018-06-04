package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class BattleshipGalacticaMain extends Application {	
	/**
	 * The file path for the resources used in this program.
	 */
	public static final String RESOURCES_LOCATION = "resources";
	// The current game being played.
	private static Game currentGame;	
	
	/**
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		currentGame = new Game();
		// FIRST thing that needs to be done is the generation of the board. Everything else breaks without a board.
		Scene scene = new Scene(currentGame.getBoard());
		
		// Now set up some window things
		primaryStage.setTitle("Battleship Galactica");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		// Now begin monitoring user input.
		InputHandler.setScene(scene);
		
		currentGame.start();
	}
}
