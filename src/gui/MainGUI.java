package gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
	}
	
	static final Map<SceneType, String> sceneFxmlLocationMapping = Map.ofEntries(
	    Map.entry(SceneType.MAIN_GUI, "/MainGUIScene.fxml"),
	    Map.entry(SceneType.REGISTER, "/RegisterScene.fxml"),
	    Map.entry(SceneType.BUY, "/BuyScene.fxml"),
	    Map.entry(SceneType.ClientProfile, "/ClientProfileScene.fxml"),
	    Map.entry(SceneType.ClientsManagement, "/ClientManagementScene.fxml")
	);
	
	static final Map<SceneType, Scene> sceneMapping = new HashMap<>();
	
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
		GUIclient.setPort(5555);
		GUIclient.openConnection();
		primaryStage.setTitle("Global City Map");
		openScene(SceneType.MAIN_GUI);
		
		primaryStage.show();
	}
	
	public static void openScene(SceneType sceneType) {
		openScene(sceneType, false);
	}
	
	public static void openScene(SceneType sceneType, boolean restorePreviousScene) {
		Platform.runLater(() -> {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(MainGUI.class.getResource(sceneFxmlLocationMapping.get(sceneType)));
				AnchorPane root = (AnchorPane) fxmlLoader.load();
				Scene scene;
				if ((restorePreviousScene || sceneType == SceneType.MAIN_GUI) && sceneMapping.get(sceneType) != null) {
					scene =  sceneMapping.get(sceneType);
				} else {
					scene = new Scene(root);
					sceneMapping.put(sceneType, scene);
				}
				MainGUI.MainStage.setScene(scene);
				GUIclient.setCurrentControllerListener(fxmlLoader.getController());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

    //final public static int DEFAULT_PORT = 5555;
	public static void main(String[] args) {
		launch(args);
		
	}

}
