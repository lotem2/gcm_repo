package gui;

import client.*;
import entity.*;
import common.*;
import java.io.*;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import common.Action;
import common.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;//library for popup messages

public class MainGUIController {
	PurchaseClientController client;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="MapSearchWindow"
    private AnchorPane MapSearchWindow; // Value injected by FXMLLoader

    @FXML // fx:id="SearchResultsTable"
    private TableView<SearchTable> SearchResultsTable; // Value injected by FXMLLoader
    
    @FXML // fx:id="choicePanel"
    private TableColumn<SearchTable, String> col_choicePanel; // Value injected by FXMLLoader
    
    @FXML // fx:id="tcCityName"
    private TableColumn<SearchTable, String> col_CityName; // Value injected by FXMLLoader

    @FXML // fx:id="tcDescName"
    private TableColumn<SearchTable, String> col_DescName; // Value injected by FXMLLoader

    @FXML // fx:id="tcSiteName"
    private TableColumn<SearchTable, String> col_SiteName; // Value injected by FXMLLoader
    
    @FXML // fx:id="btnDownload"
    private Button btnDownload; // Value injected by FXMLLoader

    @FXML // fx:id="btnLogin"
    private Button btnLogin; // Value injected by FXMLLoader

    @FXML // fx:id="btnSearch"
    private Button btnSearch; // Value injected by FXMLLoader

    @FXML // fx:id="btnShow"
    private Button btnShow; // Value injected by FXMLLoader
    
    @FXML // fx:id="tfUser"
    private TextField tfUser; // Value injected by FXMLLoader
    
    @FXML // fx:id="pfPassword"
    private PasswordField pfPassword; // Value injected by FXMLLoader

    @FXML // fx:id="tfCitySearch"
    private TextField tfCitySearch; // Value injected by FXMLLoader

    @FXML // fx:id="tfDesSearch"
    private TextField tfDesSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tfSiteSearch"
    private TextField tfSiteSearch; // Value injected by FXMLLoader

    @FXML // fx:id="txtMapsCatalog"
    private Text txtMapsCatalog; // Value injected by FXMLLoader


    // Handler for PasswordField[fx:id="pfPassword"] onAction
    // Handler for TextField[fx:id="tfUser"] onAction
    // Handler for Button[fx:id="btnLogin"] onAction
       
    @FXML
    void Login(ActionEvent event) {
        // handle the event here
      try
      {
  	  Message myMessage;
  	  String userName=""/*,password*/;
  	  ArrayList<Object> data = new ArrayList<Object>();
  	  userName=tfCitySearch.getText();
  	  //password=tfCitySearch.getText();
	  	  if((userName!=null)/*||(password!=null)*/)
		  	  {
				      data.add(userName);
				      //data.add(password);
		  	  }
			     myMessage = new Message(Action.LOGIN,data);
			     client.sendToServer(myMessage);
      }
	  catch(IOException e)
	  {
		  JOptionPane.showMessageDialog(null, 
				  e.toString()+"Could not send message to server.  Terminating client.", 
                  "Error", 
                  JOptionPane.WARNING_MESSAGE);
		        //quit();
	  }
    }

    
    // Handler for TextField[fx:id="tfCitySearch"] onAction
    // Handler for TextField[fx:id="tfDesSearch"] onAction
    // Handler for TextField[fx:id="tfSiteSearch"] onAction
    // Handler for Button[fx:id="btnSearch"] onAction
    /**
     * @param event
     */
    /*
    @FXML
    void Search(ActionEvent event) 
    {
    try
	    {
	  	Message myMessage;
	  	String cityName,siteName,myDescription;
	  	ArrayList<Object> data = new ArrayList<Object>();
	  	cityName=tfCitySearch.getText();
	  	siteName=tfSiteSearch.getText();
	  	myDescription=tfDesSearch.getText();
		  	if (cityName!=null)
		  	{
			         data.add("cityName");
				     data.add(cityName);
				     if(siteName!=null)
				     {
				       	data.add("siteName");
				       	data.add(siteName);
				     }
				     if(myDescription!=null) 
				     {
				    	 data.add("myDescription");
			    	     data.add(myDescription);
				     }
		  	}
		  	else
		  	{
		  		JOptionPane.showMessageDialog(null, "No paramaters were typed in.", "Error", JOptionPane.WARNING_MESSAGE);
		  	}
	  	myMessage = new Message(Action.SEARCH,data);
		client.sendToServer(myMessage);
	    }
	catch(IOException e)
		{
			   JOptionPane.showMessageDialog(null,e.toString()+"Could not send message to server. Terminating client.", 
			                  "Error", JOptionPane.WARNING_MESSAGE);
					          client.quit();
		}
    }*/

    // Handler for Button[fx:id="btnDownload"] onAction
    @FXML
    void Search(ActionEvent event) {
        // handle the event here
    }
    // Handler for Button[fx:id="btnDownload"] onAction
    @FXML
    void download(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="btnShow"] onAction
    @FXML
    void show(ActionEvent event) {
        // handle the event here
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert MapSearchWindow != null : "fx:id=\"MapSearchWindow\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert SearchResultsTable != null : "fx:id=\"SearchResultsTable\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnDownload != null : "fx:id=\"btnDownload\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnSearch != null : "fx:id=\"btnSearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert col_choicePanel != null : "fx:id=\"choicePanel\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert pfPassword != null : "fx:id=\"pfPassword\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert col_CityName != null : "fx:id=\"tcCityName\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert col_DescName != null : "fx:id=\"tcDescName\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert col_SiteName != null : "fx:id=\"tcSiteName\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfCitySearch != null : "fx:id=\"tfCitySearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfDesSearch != null : "fx:id=\"tfDesSearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfSiteSearch != null : "fx:id=\"tfSiteSearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfUser != null : "fx:id=\"tfUser\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert txtMapsCatalog != null : "fx:id=\"txtMapsCatalog\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
    }

/*
       
		public void initialize(URL url, ResourceBundle rb) {
            // TODO
           
      	  Message currMsg = (Message)msg;
      	  ObservableList<SearchTable> list = FXCollections.observableArrayList(currMsg);
    	  if (currMsg.getAction()==Action.SEARCH) {
    	      	 if((Integer)currMsg.getData().get(0) == 0) 
    	      	 {
    	      	        col_CityName.setCellValueFactory(new PropertyValueFactory<SearchTable,String>("cityName"));
    	      	        col_SiteName.setCellValueFactory(new PropertyValueFactory<SearchTable,String>("siteName"));
    	      	        col_DescName.setCellValueFactory(new PropertyValueFactory<SearchTable,String>("description"));
    	      	        
    	      	        SearchResultsTable.getColumns().addAll(col_CityName, col_SiteName, col_DescName);           
                        SearchResultsTable.setItems(list);
    	      	 }
    	      	 else {
    	      		    System.out.println("The message was not sent to the gui");
    	      	 }
        	

               
    }*/
}




