package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import common.Action;
import common.Message;
import common.Permission;
import common.Status;
import entity.Client;
import entity.InboxMessage;
import entity.Map;
import entity.Purchase;
import gui.MainGUI.SceneType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	private Button btnStatistics;
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
    private Button btnEditMaps;
    @FXML
    private Button btnInbox;
    @FXML
    private Button btnWatchPurchases;
    @FXML
    private Label lblWelcome;

    @FXML
    void backToMainGUI(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map");
		MainGUI.openScene(SceneType.MAIN_GUI);
    }
    
	@FXML
	void Statistics(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - Statistics");
		MainGUI.openScene(MainGUI.SceneType.Statistics);
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
    	lblWelcome.setText("Welcome " + MainGUI.currEmployee.getUserName() + "!");
		GUIClient.sendActionToServer(Action.SHOW_ALL_CLIENTS);
		InboxController.getMessagesFromServer();
    }

	/**
	 * Handle message from server by getting all the clients' details.
	 *
	 */
	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
			case GET_INBOX_MESSAGES:
				if ((Integer) currMsg.getData().get(0) == 0) {
					List<InboxMessage> messages = (List<InboxMessage>)currMsg.getData().get(1);
					int newMsg = 0;
					for (InboxMessage message : messages)
					{
						if(message.getStatus().equals(Status.NEW) )
							newMsg++;
					}
					String inboxTxt = "Inbox("+ newMsg +")";
					Platform.runLater(() -> {
						btnInbox.setText(inboxTxt);
					});
				}
				break;
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
	 *@param  clients - clients list 
	 * 
	 */
	public void setTableViewForClients(ArrayList<Client> clients) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObservableList<Client> clientsList = FXCollections.observableArrayList(clients);
				col_UserName.setCellValueFactory(new PropertyValueFactory<Client, String>("UserName"));
				col_FirstName.setCellValueFactory(new PropertyValueFactory<Client, String>("FirstName"));
				col_LastName.setCellValueFactory(new PropertyValueFactory<Client, String>("LastName"));
				col_Telephone.setCellValueFactory(new PropertyValueFactory<Client, String>("Telephone"));
				col_Email.setCellValueFactory(new PropertyValueFactory<Client, String>("Email"));

				clientsTable.setItems(clientsList);
			}
		});
	}
	
	@FXML
	void OpenInbox(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - Inbox");
		MainGUI.openScene(MainGUI.SceneType.Inbox);
	}
	
	@FXML
    void EditMaps(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - Edit Maps");
		MainGUI.openScene(SceneType.Edit);
		MainGUIController.selectedMap = 0;
    }

    @FXML
    void WatchPurchases(ActionEvent event) {
		Client currentWatchedClient = clientsTable.getSelectionModel().getSelectedItem();
		MainGUI.currClient = currentWatchedClient;
		if (currentWatchedClient!=null) 
		{
		MainGUI.MainStage.setTitle("Global City Map - Client's Profile");
		MainGUI.openScene(SceneType.ClientProfile);
		}
		else
			JOptionPane.showMessageDialog(null, "You haven't selected any client to view.", "Error",
					JOptionPane.WARNING_MESSAGE);
    }
	
	
}


