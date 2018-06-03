package main;
import board.Board;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manipulation.BoardManipulation;
import manipulation.ShipManipulation;
import player.*;
import player.Player;

/**
 * 
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class BattleshipGalacticaMain extends Application {
	static Player[] players;
	
	/**
	 * The file path for the resources used in this program.
	 */
	public static final String RESOURCES_LOCATION = "resources";
	private static Board board;
	
	/**
	 * Returns the current board object
	 * @return The current board object.
	 */
	public static Board getBoard() {
		return board;
	}
	
	
	/**
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// FIRST thing that needs to be done is the generation of the board. Everything else breaks without a board.
		Scene scene = new Scene(generateBoard());	
		
		// Now set up some window things
		primaryStage.setTitle("Battleship Galactica");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		// Now begin monitoring user input.
		InputHandler.setScene(scene);
		// Create and populate the player array.
		players = new Player[] { new IMC(),  new Militia() };
		
		BoardManipulation boardMan = new BoardManipulation();
		ShipManipulation shipMan = new ShipManipulation();
	}
	
	/**
	 * Generates the game board which generates all the objects necessary for the game to run as well.
	 * @return The Parent object that is the game board.
	 */
	private static Parent generateBoard() {		
		Board board = new Board();
		BattleshipGalacticaMain.board = board;
		
		return board;
	}
	
//	private static Parent playerSelection() {
//		// Begin with player selection		
//		Player IMC_player = new Player(Player.PlayerType.IMC), militia_player = new Player(Player.PlayerType.Militia);
//		
//		players = new Player[] { IMC_player, militia_player };
//		
//		return new PlayerSelection();
//	}
}
