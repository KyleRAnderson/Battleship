package main;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
	
	/**
	 * Wheter or not this input handler is currently monitoring inputs.
	 */
	private static boolean monitoring;
	
	/**
	 * Used to keep track of all the keys possible to be pressed and the method which must be called for each one.
	 */
	private static final HashMap<KeyCode, Consumer<KeyCode>> BINDINGS = new HashMap<KeyCode, Consumer<KeyCode>>();
	
	/**
	 * Sets the scene object for the InputHandler to handle input from.
	 * @param scene The scene to listen for key changes on.
	 */
	public static void setScene(Scene scene) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent> () {
			@Override
			public void handle(KeyEvent event) {
				// Determine which key was pressed.
				KeyCode key = event.getCode();
				
				// Now see if there's a binding for that key and if there is, call it.
				if (BINDINGS.containsKey(key) && monitoring) BINDINGS.get(key).accept(key);;
			}
			
		});
	}
	
	/**
	 * Used to add bindings when keys are pressed. If a binding for this key exists already, this will replace it.
	 * @param bindings the key to add the binding to.
	 * @param handler The function to call in order to handle the pressing of this button.
	 */
	public static void addKeyBinding(KeyCode binding, Consumer<KeyCode> handler) {
		BINDINGS.put(binding, handler);
	}
	
	/**
	 * Adds multiple key bindings at once so that when the key is pressed, the supplied function is called.
	 * If a binding for any of the keys exists already, this will replace it.
	 * @param bindings The key to which the binding should be added
	 * @param handler The function to handle the event of the binding.
	 */
	public static void addKeyBindings(Collection<KeyCode> bindings, Consumer<KeyCode> handler) {
		// Iterate through each key and add the binding to the list.
		for (KeyCode key : bindings) {
			// Add the key binding.
			addKeyBinding(key, handler);
		}
	}
	
	/**
	 * Removes the binding to the supplied key
	 * @param binding The key whose binding needs to be removed.
	 */
	public static void removeKeyBinding(KeyCode binding) {
		BINDINGS.remove(binding);
	}
	
	/**
	 * Removes all the bindings to the supplied keys
	 * @param bindings The keys whose bindings need to be removed.
	 */
	public static void removeKeyBindings(Collection<KeyCode> bindings) {
		for (KeyCode binding : bindings) {
			BINDINGS.remove(binding);
		}
	}
	
	/**
	 * Deletes all bindings set to this input handler.
	 */
	public static void resetBindings() {
		BINDINGS.clear();
	}
	
	/**
	 * Stops the monitoring of this input handler.
	 */
	public static void stopMonitoring() {
		monitoring = false;
	}
	
	/**
	 * Makes the input handler begin monitoring inputs and calling bindings when necessary
	 */
	public static void startMonitoring() {
		monitoring = true;
	}
}
