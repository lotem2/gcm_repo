package gui;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.InetAddress;
import java.awt.Label;
import java.io.*;
import common.*;


public class MainGUI extends Application {

	static Stage MainsStage;
 
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		this.MainsStage = primaryStage;
		// Constructing our scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGUIScene.fxml"));
		AnchorPane pane = loader.load();
		MainGUIController controller = loader.getController();
		GUIClient client = new GUIClient();
		client.setHost(InetAddress.getLocalHost().getHostAddress().toString());
		client.setPort(5555);
		client.openConnection();
		controller.setGUIClient(client);

		Scene scene = new Scene(pane);
		// setting the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Global City Map");
		primaryStage.show();
	}

//final public static int DEFAULT_PORT = 5555;
	public static void main(String[] args) {
		launch(args);
		
	}

}
