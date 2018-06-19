package game.board;

import java.util.HashMap;
import java.util.function.Consumer;

import game.player.Player;
import game.ships.Ship;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import main.InputHandler;

public class Battle {
	private static final long ONE_SECOND = 1000000000;
	
	public static final Font BATTLE_FONT = Font.font("Centaur", 30);
	
	/**
	 * The board that will be dealt with.
	 */
	Board board;
	
	Label instructionText, countdownText, contesterLabel, defenderLabel;
	
	/**
	 * The HBox in which all the elements will be positioned.
	 */
	HBox pane;
	
	// The ship defending the square and the ship attacking/contesting the square.
	Ship defender, contester;
	
	// The number of clicks that each of the two ships did.
	int defenderClicks = 0, contesterClicks = 0;
	
	/**
	 * Begins a new batle between the two ships
	 * @param defender The ship defending the square
	 * @param attacker The ship attacking the square.
	 */
	public Battle(Ship defender, Ship contester) {
		// Stop monitoring user input immediately.
		InputHandler.stopMonitoring();
		
		this.defender = defender;
		this.contester = contester;
		
		// Hide all of the player's ships so that the other player doesn't see them.
		defender.player.toggleHide(true);
		contester.player.toggleHide(true);
		
		// Make the ships that are battling visible to one another
		defender.setVisible(true);
		contester.setVisible(true);
		
		board = defender.player.getGame().getBoard();
		pane = (HBox)board.getBottom();
		
		
		// Make labels that tell the players their score compared to the other player.
		defenderLabel = new Label("0");
		defenderLabel.setFont(BATTLE_FONT);
		defenderLabel.setTextFill(defender.player.getSelectionColour());
		contesterLabel = new Label("0");
		contesterLabel.setFont(BATTLE_FONT);
		contesterLabel.setTextFill(contester.player.getSelectionColour());
		
		// Need to decide which label to add first.
		Label left_side_label = (defender.player.getStartPosition().equals(Player.StartSide.TopLeft)) ? defenderLabel : contesterLabel;
		Label right_side_label = (left_side_label.equals(defenderLabel)) ? contesterLabel : defenderLabel;
		
		// Add the left side label first so it's on the left.
		pane.getChildren().add(left_side_label);
		
		countdownText = new Label();
		// Add the text to the screen.
		pane.getChildren().add(countdownText);
		countdownText.setFont(BATTLE_FONT);
		countdownText.setTextFill(Color.WHITE);
		
		// Make instructional label for the player as well.
		instructionText = new Label();
		pane.getChildren().add(instructionText);
		instructionText.setFont(BATTLE_FONT);
		instructionText.setTextFill(Color.WHITE);
		instructionText.setText("Prepare for battle! Both players get to the keyboard.");
		
		// Now add the right side label so that it's on the r ight
		pane.getChildren().add(right_side_label);
		
		AnimationTimer countdownTimer = new AnimationTimer() {
			long lastRunTime = 0;
			int number = 10;
			@Override
			public void handle(long now) {
				// Only run this every second
				if (now - lastRunTime >= ONE_SECOND) {
					lastRunTime = now;
					number--;
					
					// If the countdown has reached 0, stop counting down.
					if (number < 0) {
						stop();
						onStartTimerEnd();
					}
					else {
						// Otherwise, set the countdown text.
						countdownText.setText(String.valueOf(number));
					}
				}
				
			}
		};
		
		countdownTimer.start();
	}
	/**
	 * Called when the animation timer for the countdown ends.
	 */
	private void onStartTimerEnd() {
		// Make a new hashmap for the new key bindings.
		HashMap<KeyCode, Consumer<KeyCode>> newBindings = new HashMap<KeyCode, Consumer<KeyCode>>();
		
		newBindings.put(defender.player.getKeyBindings().get(Player.ENTER), new Consumer<KeyCode>() {
			@Override
			public void accept(KeyCode t) {
				// Simply add to the number of clicks that the contester did
				defenderClicks++;				
				defenderLabel.setText(String.valueOf(defenderClicks));
			}
		});
		
		newBindings.put(contester.player.getKeyBindings().get(Player.ENTER), new Consumer<KeyCode>() {
			@Override
			public void accept(KeyCode t) {
				// Add to the number of clicks that the constester did.
				contesterClicks++;
				contesterLabel.setText(String.valueOf(contesterClicks));
			}
		});
		
		// Add a little bit of instructional text so the user knows what to do.
		instructionText.setText("Press your select key more than the opponent!");
		
		// Make new animation countdown to let the player know how much time is left in the battle.
		AnimationTimer actionCountdown = new AnimationTimer() {
			long lastTime = 0;
			int secondsLeft = 10;
			
			@Override
			public void handle(long now) {
				if (now - lastTime >= ONE_SECOND) { 
					lastTime = now;
					secondsLeft--;
					
					// If there's no time left, end this timer.
					if (secondsLeft < 0) {
						stop();
						determineWinner();
					}
					else {
						// Set the time lefto on screen.
						countdownText.setText(String.valueOf(secondsLeft));
					}
				}
			}
		};
		
		// Override the regular key bindings so the player can't do what they usually can.
		InputHandler.addOverrideBindings(newBindings);
		
		InputHandler.startMonitoring();
		actionCountdown.start();
	}
	
	/**
	 * Determines the winner of the battle and effectively ends the battle.
	 */
	private void determineWinner() {
		/* The winner is the defender if they outclicked or matched the contester, or the contester if they outclicked the defender.
		 * The slight bias towards the defender here is intentional, since they would technically be more prepared. It's
		 * however unlikely that the two players will match each other in clicks.
		 */
		Ship winner = (defenderClicks >= contesterClicks) ? defender : contester;
		Ship loser = (defenderClicks >= contesterClicks) ? contester : defender;
		
		
		// Fill the area with the winner's colour.
		Color fill = winner.player.getSelectionColour();
		pane.setBackground(new Background(new BackgroundFill(fill, CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		// Set the clearBattle method for three seconds from this time.
		Timeline endAnimation = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {

		    @Override
		    public void handle(ActionEvent event) {
		        clearBattle(winner, loser);
		    }
		}));
		endAnimation.play();
	}
	
	/**
	 * Function to clear all the remnants from the battle on screen. 
	 */
	private void clearBattle(Ship winner, Ship loser) {
		InputHandler.clearOverrideBindings();
		
		// Remove the overlay nodes.
		removeAll();
		
		// Carry out the proper actions for destroying the loser
		loser.destroy();
		
		// Set the selected ships of both players null
		loser.player.setSelectedShip(null);
		winner.player.setSelectedShip(null);
		
		// Clear the pane's background.
		pane.setBackground(Background.EMPTY);
	}
	
	/**
	 * Removes all of the battle's nodes from the screen.
	 */
	private void removeAll() {		
		// Remove the pane from the screen.
		pane.getChildren().remove(countdownText);
		pane.getChildren().remove(instructionText);
		pane.getChildren().remove(defenderLabel);
		pane.getChildren().remove(contesterLabel);
	}
}
