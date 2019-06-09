package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	static GUIClient GUIclient;

	static Client currClient;
	public static Employee currEmployee;
	public static User currUser;

	@Override
	public void start(Stage primaryStage) throws IOException {
		MainGUI.MainStage = primaryStage;
		GUIclient = new GUIClient();
		GUIclient.setHost(InetAddress.getLocalHost().getHostAddress().toString());
		//GUIclient.setHost("132.74.210.35");
		GUIclient.setPort(5555);
		GUIclient.openConnection();
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void openScene(SceneType sceneType) {
		openScene(sceneType, false);
	}

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
