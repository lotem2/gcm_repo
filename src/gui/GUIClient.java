package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.AbstractClient;
import entity.Client;

public class GUIClient extends AbstractClient {
	public static Client currClient;
	/**
	 * The Login ID of the user.
	 */
	String loginID;

	List<ControllerListener> listeners = new ArrayList<>();

	public void addControllerListener(ControllerListener contollerListener) {
		listeners.add(contollerListener);
	}

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		for (ControllerListener listener : listeners) {
			listener.handleMessageFromServer(msg);
		} 
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
