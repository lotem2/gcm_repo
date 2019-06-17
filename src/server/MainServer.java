package server;

//import java.io.IOException;

import gui.MainGUI;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainServer extends Application {
	// Variables
	private static ServerController m_currentServer;
	//@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServerScene.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Scene scene = new Scene(root);
		m_currentServer = (ServerController) loader.getController();
		
		// Set stage
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.setTitle("GCM Server");

		// Set listener for onClose request
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		      public void handle(WindowEvent we) {
		          // Close server when pressing the exit button
		    	  try {
			    	  m_currentServer.stopServer();
		    	  }
		    	  catch(Exception e) {
		    	  }
		    	  finally {
			    	  System.exit(0);
		    	  }
		      }
		  }); 

		primaryStage.show();
	}
	
	/**
	 * Start of MainServer
	 * @param args -given arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	//@Override
	public void stop(){
		try {
			// Close server when pressing the exit button
	    	m_currentServer.stopServer();
		} catch (Exception e) {
		}
		finally {
	    	  System.exit(0);
		}
	}
}
