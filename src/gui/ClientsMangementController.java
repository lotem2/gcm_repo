package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import entity.Client;
import entity.Map;
import gui.MainGUI.SceneType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ClientsMangementController implements ControllerListener {
	
GUIClient client;
	
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane ClientsMangementWindow;
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> col_Email;
    @FXML
    private TableColumn<Client, String> col_FirstName;
    @FXML
    private TableColumn<Client, String> col_LastName;
//    @FXML
//    private TableColumn<Client, String> col_Purchases;
    @FXML
    private TableColumn<Client, String> col_Telephone;
    @FXML
    private TableColumn<Client, String> col_UserName;
    @FXML
    private Button btnMain;
    @FXML
    private Button btnShowDetails;
    @FXML
    private Label lblWelcome;


    @FXML
    void backToMainGUI(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map");
		MainGUI.openScene(SceneType.MAIN_GUI);
    }

    @FXML
    void ShowDetails(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - User's Profile");
		MainGUI.openScene(MainGUI.SceneType.ClientProfile);
    }
	/**
	 *
	 *	initializing the scene, sending the request of getting the clients list from the server.
	 *
	 * @param textField - textField.
	 */
    @FXML
    void initialize() 
    {
    	//setTableViewForClients(clients);
    	lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
		Message myMessage = new Message(Action.SHOW_ALL_CLIENTS);					
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message to the Server", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
    	
    }
	/**
	 *handling message from server, by getting all the clients details.
	 *	
	 *
	 * 
	 */
	@Override
	public void handleMessageFromServer(Object msg) {
		// TODO Auto-generated method stub
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case SHOW_ALL_CLIENTS:
			if ((Integer) currMsg.getData().get(0) == 0) {
				setTableViewForClients((ArrayList<Client>)currMsg.getData().get(1));
			} 
			else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			break;
		default:
	
		}	
	}
	/**
	 *
	 *Setting the table for the client list
	 *
	 * @param textField - textField.
	 */
	public void setTableViewForClients(ArrayList<Client> clients) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObservableList<Client> clientsList = FXCollections.observableArrayList();
				col_UserName.setCellValueFactory(new PropertyValueFactory<Client, String>("userName"));
				col_FirstName.setCellValueFactory(new PropertyValueFactory<Client, String>("FirstName"));
				col_LastName.setCellValueFactory(new PropertyValueFactory<Client, String>("LastName"));
				col_Telephone.setCellValueFactory(new PropertyValueFactory<Client, String>("Telephone"));
				col_Email.setCellValueFactory(new PropertyValueFactory<Client, String>("Email"));
				//col_Purchases.setCellValueFactory(new PropertyValueFactory<Client, String>("Purchases"));

				clientsTable.getColumns().addAll(col_UserName, col_FirstName, col_LastName,col_Telephone,col_Email/*,col_Purchases*/);
				clientsTable.setItems(clientsList);
			}
		});
	}

}


