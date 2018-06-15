package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
	 * Properly gets all the files in the given folder. 
	 * @param folderPath The path to the folder where the files are located.
	 * @param acceptableExtensions The extensions that are valid 
	 * @return An arraylist of the files in the given folder.
	 */
	public static ArrayList<String> getAllFilesInFolder(String folderPath, ArrayList<String> acceptableExtensions) {
		ArrayList<String> files = new ArrayList<String>();
		try {
			// First convert the folder path to a correct path, then get all the string filenames in that path,
			ArrayList<String> fileStrings = getResourceFiles("/" + folderPath);
			
			// Iterate through each file path and get the actual file object for it.
			for (String filePath : fileStrings) {
				// Make sure that the extension is correct.
				String extension = filePath.substring(filePath.lastIndexOf('.'));
				if (acceptableExtensions.contains(extension)) {
					// Add the file if it's got the proper extension.
					files.add(filePath);
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// Return the result.
		return files;
	}
	
	/**
	 * Gets a list of the filenames of the files in the given folder.
	 * @param path The path to the folder 
	 * @return An arraylist of all the files in the given folder 
	 * @throws IOException Happens when stuff goes
	 */
	private static ArrayList<String> getResourceFiles(String path) throws IOException {
		// Arraylist for the file names once we've determined what they are.
		ArrayList<String> filenames = new ArrayList<>();

		// TODO remove
		System.out.println("PATH: " + path);
		
		try(
				InputStream in = getResourceAsStream(path);
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;
			
			// TODO remove debug prints
			System.out.println("BR: " + br);

			while((resource = br.readLine()) != null) {
				filenames.add( resource );
			}
		}

		return filenames;
	}

	/**
	 * Gets the given resource as a stream
	 * @param resource The resource
	 * @return An inputstream for the given resource.
	 */
	private static InputStream getResourceAsStream(String resource) {
		final InputStream in
		= getContextClassLoader().getResourceAsStream(resource);

		InputStream returnValue =  in == null ? BattleshipGalactica.class.getResourceAsStream(resource) : in;
		
		// TODO remove debug prints
		System.out.println("returnValue: " + returnValue);
		
		return returnValue;
	}

	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Resizes the screen automatically based on the content.
	 */
	public static void resize() {
		stage.sizeToScene();
		stage.centerOnScreen();
	}
}
