package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import client.AbstractClient;
import common.Action;
import common.Message;

public class GUIClient extends AbstractClient {
	/**
	 * The Login ID of the user.
	 */
	String loginID;

	ControllerListener currListener;

	/**
	 * @param contollerListener
	 */
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
			e.printStackTrace();
			System.out.println(e);
			System.out.println(msg);
			System.out.println(currListener);

		}
			
	}
	
	/**
	 * @param action
	 * @param data
	 */
	public static void sendActionToServer(Action action,ArrayList<Object> data) {
		Message myMessage = new Message(action,data);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message to Server", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * @param action
	 */
	public static void sendActionToServer(Action action) {
		Message myMessage = new Message(action);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message to Server", "Error",
					JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	protected void connectionClosed() {
	}
}
