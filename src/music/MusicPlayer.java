package music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.BattleshipGalactica;

public class MusicPlayer {
	
//	private static final String[] SUPPORTED_FORMAT_EXTENSIONS = new String[] { ".wav", ".mp3" };
	/**
	 * An array containing all of the names of the music files to be used in this game.
	 */
	private static String[] musicFiles;
	
	/**
	 * Whether or not the music is currently playing.
	 */
	private static boolean isPlaying;
	
	// An arraylist of the song queue to have.
	private static ArrayList<String> songQueue;
	
	/**
	 * The Media Player that will play the music.
	 */
	private static MediaPlayer player;
	
	// Need to initialize the musicFiles array
	static {
//		musicFiles = BattleshipGalactica.getAllFilesInFolder(BattleshipGalactica.RESOURCES_LOCATION + "/music", new ArrayList<String>(Arrays.asList(SUPPORTED_FORMAT_EXTENSIONS))).toArray(new String[0]);
		musicFiles = new String[] { "Grab yer sword.wav", "Hunt for blackbeard's booty.wav", "Royal Navy March.mp3", "Treasure hunt.wav", "United States Navy March_ Crosby.mp3" };
	}
	
	
	/**
	 * Begins the playing of the music.
	 */
	public static void play() {
		// If the current sound is null, go to the next sound.
		if (player == null) {
			nextSong();			
		}
		else {
			player.play();
			isPlaying = true;
		}
	}
	
	public static void pause() {
		if (player != null) {
			player.pause();
			isPlaying = false;
		}
	}
	
	/**
	 * Stops the player if it exists
	 */
	public static void stop() {
		if (player != null) player.stop();
	}
	/**
	 * Plays music if pause, or pauses music if playing.
	 */
	public static void toggleState() {
		if (isPlaying) pause();
		else play();
	}
	
	/**
	 * Determines whether or not music is currently playing 
	 * @return True if music is playing, false otherwise.
	 */
	public static boolean isPlaying() {
		return isPlaying;
	}
	
	/**
	 * Skips the current song and continues to the next song.
	 */
	public static void nextSong() {
		// If there's no song queue, populate a new one.
		if (songQueue == null || songQueue.size() <= 0) populateSongQueue();
		
		// If it's still null or empty, don't do anything.
		if (songQueue != null && songQueue.size() > 0) {
			// Instantiate a new media player for the playing of the media.
			
			Media media = new Media(BattleshipGalactica.getCorrectPath(BattleshipGalactica.RESOURCES_LOCATION + "/music/" + songQueue.get(0)));
			// Stop the old player so songs don't play on top of each other.
			stop();
			player = new MediaPlayer(media);
			player.setVolume(1);
			// Make sure we handle end of the media.
			player.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					mediaEnded();
				}
			});
			
			// Remove the song from the queue so we don't play it again.
			songQueue.remove(0);
			// Begin playing the new song.
			play();
		}	
	}
	
	/**
	 * Subscribed to event handler to handle when a song ends.
	 */
	private static void mediaEnded() {
		nextSong();
	}
	
	/**
	 * Randomly populate the song queue in order to get songs rolling in
	 * a randomized order.
	 */
	private static void populateSongQueue() {
		// Only populate if we can, if there's music to be played.
		if (musicFiles != null && musicFiles.length > 0) {
			// Convert the musicFiles array to an arraylist so we can manipulate it easier.
			ArrayList<String> availableSongs = new ArrayList<String>(Arrays.asList(musicFiles));
			
			// Now for the random part of this
			songQueue = new ArrayList<String>();
			
			Random rand = new Random();
			while (availableSongs.size() > 0) {
				// Get a random number and then get the file in the array at that index number.
				int index = rand.nextInt(availableSongs.size());
				
				// Add the song
				songQueue.add(availableSongs.get(index));
				// Then remove it from the list of available ones.
				availableSongs.remove(index);
			}
		}
	}
}
