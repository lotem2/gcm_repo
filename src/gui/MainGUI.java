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

public class MainGUI extends Application 
{
@Override
public void start(Stage primaryStage) throws IOException 
{
	// Constructing our scene
    FXMLLoader loader=new FXMLLoader(getClass().getResource("/MainGUIScene.fxml"));
	AnchorPane pane = loader.load();
    MainGUIController controller = loader.getController();
    controller.setHost(InetAddress.getLocalHost().getHostAddress().toString());
    controller.setPort(5555);
    controller.openConnection();

    
	Scene scene = new Scene(pane);
	// setting the stage
	primaryStage.setScene(scene);
	primaryStage.setTitle("Global City Map");
	primaryStage.show();
	 }
//final public static int DEFAULT_PORT = 5555;
public static void main(String[] args) 
	{
	launch(args);
	/*
	 String host = "";
	    int port = 0;  //The port number
	    String loginID = "";
	    try
	    {
	      loginID = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      System.out.println("usage: java ClientConsole loginID [host [port]]");
	      System.exit(1);
	    }
	    try
	    {
	      host = args[1];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	    }
	    try {
	      port = Integer.parseInt(args[2]);
	    } catch (ArrayIndexOutOfBoundsException e){
	      port = DEFAULT_PORT;
	    }
	    */
	  }
	 
	}
