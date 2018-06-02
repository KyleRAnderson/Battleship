package main;
import board.Board;
import board.Board.State;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import player.Player;

/**
 * 
 * @author Kyle Anderson
 * 2018-06-05
 * ICS3U
 */
public class BattleshipGalacticaMain extends Application {
	static Board board;
	static Player[] players;
	
	/**
	 * The file path for the resources used in this program.
	 */
	public static final String RESOURCES_LOCATION = "resources";
	
	
	/**
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
		
		Player IMC_player = new Player(Player.PlayerType.IMC), militia_player = new Player(Player.PlayerType.Militia);
		
		players = new Player[] { IMC_player, militia_player };		
		
		EventHandler<MouseEvent> squareClickHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Decide what to do about this based on what state we're in.
				if (Board.getState() == Board.State.Initial) {
					
				}
			}
		};
		
		board = new Board(squareClickHandler);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
