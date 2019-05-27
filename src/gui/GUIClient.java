package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.AbstractClient;

public class GUIClient extends AbstractClient {
	/**
	 * The Login ID of the user.
	 */
	String loginID;

	ControllerListener currListener;

	public void setCurrentControllerListener(ControllerListener contollerListener) {
		currListener = contollerListener;
	}

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		try {
			currListener.handleMessageFromServer(msg);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(msg);
			System.out.println(currListener);

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
