package music;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import main.BattleshipGalactica;

public class MusicPlayer {
	
	private static final String[] SUPPORTED_FORMAT_EXTENSIONS = new String[] { ".wav", ".mp3" };
	/**
	 * An array containing all of the names of the music files to be used in this game.
	 */
	private static File[] musicFiles;
	
	/**
	 * Whether or not the music is currently playing.
	 */
	private static boolean isPlaying;
	
	// An arraylist of the song queue to have.
	private static ArrayList<File> songQueue;
	
	/**
	 * The Media Player that will play the music.
	 */
	private static MediaPlayer player;
	
	// Need to initialize the musicFiles array
	static {
		// Get a file for the music folder.
		File musicFolder = new File(BattleshipGalactica.RESOURCES_LOCATION + "/music");
		// Populate our array of music files with the files in the folder.
		musicFiles = musicFolder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean shouldAccept = false;
				for (String extension : SUPPORTED_FORMAT_EXTENSIONS) {
					// Loop until we discover that this audio format is accepted.
					if (name.endsWith(extension)) {
						shouldAccept = true;
						break;
					}
				}
				// Return the results of the processing.
				return shouldAccept;
			}
		});
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
			Media media = new Media(songQueue.get(0).toURI().toASCIIString());
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
			ArrayList<File> availableSongs = new ArrayList<File>(Arrays.asList(musicFiles));
			
			// Now for the random part of this
			songQueue = new ArrayList<File>();
			
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
