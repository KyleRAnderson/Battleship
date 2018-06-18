package main;
import java.net.URL;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import menu.MainMenu;

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


	private static Stage stage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		
		// Create the main menu right away and get the menuing going.
		Scene scene = new Scene(new MainMenu());
		
		// Now set up some window things
		primaryStage.setTitle("Battleship Galactica");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
		// Set an icon for this application
		primaryStage.getIcons().add(new Image(getCorrectPath("resources/battleship.png")));
		
		// Now begin monitoring user input.
		InputHandler.setScene(scene);
		
		// Show the scene after all the rendering is done.
		primaryStage.show();
	}
	
	/**
	 * Gets the correct path for a file using the get resource methid
	 * @param location The path to the file, formatted properly.
	 * @return The correct string 
	 */
	public static String getCorrectPath(String location) {
		return getRawURL(location).toString();
	}
	
	/**
	 * Gets the URL of a resource 
	 * @param location The path of this resource from this class
	 * @return The URL for that resource
	 */
	public static URL getRawURL(String location) {
		return BattleshipGalactica.class.getResource("/" + location);
	}

	/**
	 * Resizes the screen automatically based on the content.
	 */
	public static void resize() {
		stage.sizeToScene();
		stage.centerOnScreen();
	}
}
