package gui;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainGUI extends Application 
{
@Override
public void start(Stage primaryStage) throws IOException 
{
	// constructing our scene
	URL url = getClass().getResource("/MainGUIScene.fxml");
	AnchorPane pane = FXMLLoader.load(url);
	Scene scene = new Scene(pane);
	// setting the stage
	primaryStage.setScene(scene);
	primaryStage.setTitle("Global City Map");
	primaryStage.show();
}
public static void main(String[] args) 
	{
	launch(args);
	}
}
