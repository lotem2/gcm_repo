package gui;

import client.*;
import entity.*;
import common.*;
import java.io.*;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
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

public class MainGUIController extends AbstractClient {
//	MainGuiClient client;
	
    public MainGUIController() {
		super();
		// TODO Auto-generated constructor stub
	}

	@FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane MapSearchWindow; 
    @FXML private TableView<SearchTable> SearchResultsTable; 
    @FXML private TableColumn<SearchTable, String> col_choicePanel;
    @FXML private TableColumn<SearchTable, String> col_CityName;
    @FXML private TableColumn<SearchTable, String> col_DescName; 
    @FXML private TableColumn<SearchTable, String> col_SiteName;   
    @FXML private Button btnDownload; 
    @FXML private Button btnLogin; 
    @FXML private Button btnSearch; 
    @FXML private Button btnShow;  
    @FXML private TextField tfUser; 
    @FXML private PasswordField pfPassword; 
    @FXML private TextField tfCitySearch; 
    @FXML private TextField tfDesSearch; 
    @FXML private TextField tfSiteSearch; 
    @FXML private Text txtMapsCatalog; 
    
    /**
     * @param event
     * making the data to send to the server
     */
    
//    @FXML
//    void Search(ActionEvent event) 
//    {
//    try
//	    {
//	  	Message myMessage;
//	  	String cityName,siteName,description;
//	  	ArrayList<Object> data = new ArrayList<Object>();
//	  	cityName=tfCitySearch.getText();
//	  	siteName=tfSiteSearch.getText();
//	  	description=tfDesSearch.getText();
//		  	if (cityName!=null)
//		  	{
//			         data.add("cityName");
//				     data.add(cityName);
//				     if(siteName!=null)
//				     {
//				       	data.add("siteName");
//				       	data.add(siteName);
//				     }
//				     if(description!=null) 
//				     {
//				    	 data.add("description");
//			    	     data.add(description);
//				     }
//		  	}
//		  	else
//		  	{
//		  		JOptionPane.showMessageDialog(null, "No paramaters were typed in.", "Error", JOptionPane.WARNING_MESSAGE);
//		  	}
//	  	myMessage = new Message(Action.SEARCH,data);
//		client.sendToServer(myMessage);
//	    }
//	catch(IOException e)
//		{
//			   JOptionPane.showMessageDialog(null,e.toString()+"Could not send message to server. Terminating client.", 
//			                  "Error", JOptionPane.WARNING_MESSAGE);
//					          client.quit();
//		}
//    }

    
    @FXML
    void Login(ActionEvent event) {
        // handle the event here
      try
      {
  	  Message myMessage;
  	  String userName="", password;
  	  ArrayList<Object> data = new ArrayList<Object>();
      userName=tfUser.getText();
  	  password=pfPassword.getText();
	  	  if((userName!=null) ||(password!=null))
		  	  {
				      data.add(userName);
				      data.add(password);
		  	  }
			     myMessage = new Message(Action.LOGIN,data);
			     this.sendToServer(myMessage);
			    
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
    
    @FXML
    void Search(ActionEvent event) {
        // handle the event here
    }
    @FXML
    void download(ActionEvent event) {
        // handle the event here
    }

    @FXML
    void show(ActionEvent event) {
        // handle the event here
    }
    
    public void connectToServer() {
    	try {
			this.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML 
    void initialize() 
    {

    }

	@Override
	protected void handleMessageFromServer(Object msg) {
		Message currMsg = (Message)msg;
		if (currMsg.getAction() == null) {
			System.out.println("Null action retuned from server.");
		}
		switch (currMsg.getAction()) {
		  case LOGIN:
	      	// if((Integer)currMsg.getData().get(0) == 0) 
		 
	      		 System.out.println(((Client)currMsg.getData().get(1)).toString());
	    	  break;
		  case SEARCH:
	      	 if((Integer)currMsg.getData().get(0) == 0) {
	      		 //sendDataToGUI(currMsg);
	      	     System.out.println("The message was sent to the gui");
	      	 }
	      	 else {
	      		// clientUI.display(currMsg.getData().get(1).toString() + "\n"
	      			//	 + "The message was not sent to the gui.Please retry\"");
	      	 }
	    	  break;
		  case ADD_PURCHASE:
	      	 if((Integer)currMsg.getData().get(0) == 0) {
	      	//	 clientUI.display("Purchase added successfully\n");
	      	 }
	      	 else {
	      	//	 clientUI.display(currMsg.getData().get(1).toString() + "\n");
	      	 }
	      	  break;
		  case SHOW_CLIENT_DETAILS:
	      		if((Integer)currMsg.getData().get(0) == 0) {
	      	//		clientUI.display(currMsg.getData().get(1).toString());	      			
	      		}
	      		else {
	      		//	clientUI.display(currMsg.getData().get(1).toString() + "\n");
	      		}
	      	  break;
		default:
			System.out.println("No action handler defined");
			break;
		  }
		
	}

		
//		public void setTableViewForMapsSearchResult(ArrayList<Map> maps) {
//			Platform.runLater(new Runnable() {
//				@SuppressWarnings("unchecked")
//				@Override
//				public void run() {
//			      	ObservableList<SearchTable> mapList = FXCollections.observableArrayList();
//	      	        col_CityName.setCellValueFactory(new PropertyValueFactory<SearchTable,String>("cityName"));
//	      	        col_SiteName.setCellValueFactory(new PropertyValueFactory<SearchTable,String>("siteName"));
//	      	        col_DescName.setCellValueFactory(new PropertyValueFactory<SearchTable,String>("description"));
//	      	        
//	      	        SearchResultsTable.getColumns().addAll(col_CityName, col_SiteName, col_DescName);           
//                    SearchResultsTable.setItems(mapList);
//				}
//			});
}




