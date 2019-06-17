package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.net.InetAddress;
import java.awt.Label;
import java.io.*;
import common.*;
import entity.Client;
import entity.Employee;
import entity.User;

public class MainGUI extends Application {

	/**
	 * Enum of all scene types.
	 */
	enum SceneType {
		MAIN_GUI,
		REGISTER,
		BUY,
		ClientProfile,
		ClientsManagement,
		Edit,
		Statistics,
		Inbox
	}

	/**
	 * Map between SceneType enum to FXML resource file name.
	 */
	static final Map<SceneType, String> sceneFxmlLocationMapping = Map.ofEntries(
	    Map.entry(SceneType.MAIN_GUI, "/MainGUIScene.fxml"),
	    Map.entry(SceneType.REGISTER, "/RegisterScene.fxml"),
	    Map.entry(SceneType.BUY, "/BuyScene.fxml"),
	    Map.entry(SceneType.ClientProfile, "/ClientProfileScene.fxml"),
	    Map.entry(SceneType.ClientsManagement, "/ClientsManagementScene.fxml"),
	    Map.entry(SceneType.Edit, "/EditScene.fxml"),
	    Map.entry(SceneType.Statistics, "/StatisticsScene.fxml"),
	    Map.entry(SceneType.Inbox, "/InboxScene.fxml")

	);
	
	static final Map<SceneType, Pair<Scene, ControllerListener>> sceneMapping = new HashMap<>();

	static Stage MainStage;
	public static GUIClient GUIclient;

	static Client currClient;
	public static Employee currEmployee;
	public static User currUser;

	/**
	 * Set up connection to server. If arguments were passed in, use those, otherwise
	 * assume the server is on the same IP with port 5555.
	 * If connection was not established correctly, show an error message and
	 * close the application.
	 * @param primaryStage - the given stage
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		MainGUI.MainStage = primaryStage;
		try {
			GUIclient = new GUIClient();
			Parameters parameters = getParameters();
			List<String> paramsList = parameters.getUnnamed();
			if (paramsList.size() == 0) {
				// No Params passed in, use local host and 5555 port.		
				GUIclient.setHost(InetAddress.getLocalHost().getHostAddress().toString());
				GUIclient.setPort(5555);
			} else if (paramsList.size() == 2){
				GUIclient.setHost(paramsList.get(0));
				GUIclient.setPort(Integer.parseInt(paramsList.get(1)));
			} else {
				throw new RuntimeException("Bad amount of args");
			}
			GUIclient.openConnection();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Connection to server failed, please check the IP address in" +
					" the command line and try to restart the application.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// set icon of the application
		// Image applicationIcon = new Image(getClass().getResourceAsStream(""));
        // primaryStage.getIcons().add(applicationIcon);
		
		primaryStage.setTitle("Global City Map");
		primaryStage.setResizable(false);

		openScene(SceneType.MAIN_GUI);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		MainStage.setY(primaryScreenBounds.getMinY());
		MainStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() / 4);
		primaryStage.show();
	}
	
	@Override
	public void stop(){
		try {
			MainGUI.GUIclient.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Helper function when wanting to create a new scene and not pass a variable.
	 * @param sceneType		given scence type
	 */
	public static void openScene(SceneType sceneType) {
		openScene(sceneType, false);
	}

	/**
	 * Open the specified scene type on the main stage, if restorePreviousScene is set
	 * to true, will re-use previous scene, otherwise will create a new scene.
	 * @param sceneType - given scene type
	 * @param restorePreviousScene - a previous scene to restore to
	 */
	public static void openScene(SceneType sceneType, boolean restorePreviousScene) {
		Platform.runLater(() -> {
			try {
				Pair<Scene, ControllerListener> sceneController;
				if ((restorePreviousScene || sceneType == SceneType.MAIN_GUI) && sceneMapping.get(sceneType) != null) {
					sceneController = sceneMapping.get(sceneType);
				} else {
					FXMLLoader fxmlLoader = new FXMLLoader(
							MainGUI.class.getResource(sceneFxmlLocationMapping.get(sceneType)));
					AnchorPane root = (AnchorPane) fxmlLoader.load();
					sceneController = new Pair<Scene, ControllerListener>(new Scene(root), fxmlLoader.getController());
					sceneMapping.put(sceneType, sceneController);
				}
				GUIclient.setCurrentControllerListener(sceneController.getValue());
				MainGUI.MainStage.setScene(sceneController.getKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// final public static int DEFAULT_PORT = 5555;
	public static void main(String[] args) {
		launch(args);
	}

}
