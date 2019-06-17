package gui;

import client.*;

import entity.*;
import gui.MainGUI.SceneType;
import common.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Callback;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import javax.swing.JOptionPane;//library for popup messages

public class MainGUIController implements ControllerListener {
	static int selectedMap = 0;
	protected HashMap<Integer, String> searchMaps;
	protected Map<String, String> currentWatchedMap = null;

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private AnchorPane MapSearchWindow;
	@FXML
	private TableView<Map> SearchResultsTable;
	@FXML
	private TableColumn<Map, String> col_cityName;
	@FXML
	private TableColumn<Map, String> col_description;
	@FXML
	private TableColumn<Map, String> col_mapsNumber;
	@FXML
	private TableColumn<Map, String> col_sitesNumber;
	@FXML
	private TableColumn<Map, String> col_routesNumber;
//	@FXML
//	private TableColumn<Map, String> col_sitesNumber;
//	@FXML
//	private TableColumn<Map, String> col_version;
    @FXML
    private Button btnEditMaps;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnManage;
	@FXML
	private Button btnMyProfile;
	@FXML
	private Button btnRegister;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnBuy;
	@FXML
	private TextField tfUser;
	@FXML
	private PasswordField pfPassword;
	@FXML
	private TextField tfCitySearch;
	@FXML
	private TextField tfDesSearch;
	@FXML
	private TextField tfSiteSearch;
	@FXML
	private Text txtMapsCatalog;
	@FXML
    private Button btnInbox;
	@FXML
	private Label lblWelcome;
	@FXML
	private Label lblMapsNum;
	@FXML
	private Label lblRoutesNum;
	@FXML
	private Label lblClientMenu;
	@FXML 
	private ProgressIndicator progressIndicator;
	@FXML
	private AnchorPane AncPane;
	private PauseTransition delayTimeout;

	/**
	 * @param event making the data to send to the server
	 */
	@FXML
	void Search(ActionEvent event) {
		String cityName, siteName, mapDescription;
		cityName = tfCitySearch.getText();
		siteName = tfSiteSearch.getText();
		mapDescription = tfDesSearch.getText();
		
		enableProressIndicator();
		MainGUI.GUIclient.sendActionToServer(Action.SEARCH,
				createDataForSearchAction(cityName, siteName, mapDescription));
	}
	
	private static ArrayList<Object> createDataForSearchAction(
			String cityName, String siteName, String mapDescription) {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(!cityName.isEmpty() ? cityName : null);
		data.add(!siteName.isEmpty() ? siteName : null);
		data.add(!mapDescription.isEmpty() ? mapDescription : null);
		return data;
	}

	/**
	 * @param event
	 */
	@FXML
	void Login(ActionEvent event) {
		clearSearch();
			String userName = "", password;
			ArrayList<Object> data = new ArrayList<Object>();
			userName = tfUser.getText();
			password = pfPassword.getText();
			if ((userName != null) && (password != null)) {
				data.add(userName);
				data.add(password);

			} 

			MainGUI.GUIclient.sendActionToServer(Action.LOGIN,data);
	}
	
	/**
	 * @param event
	 */
	@FXML
	void Inbox(ActionEvent event) {
		clearSearch();
		MainGUI.MainStage.setTitle("Global City Map - Inbox");
		MainGUI.openScene(MainGUI.SceneType.Inbox);
	}

	/**
	 * @param event
	 */
	@FXML
	void Buy(ActionEvent event) {
		clearSearch();
		MainGUI.MainStage.setTitle("Global City Map - Purchase");
		MainGUI.openScene(MainGUI.SceneType.BUY);
	}

	/**
	 * @param event
	 */
	@FXML
	void Logout(ActionEvent event) {
		clearSearch();
		ArrayList<Object> data = new ArrayList<Object>();
		String userName = MainGUI.currUser.getUserName();
		data.add(userName);
		MainGUI.GUIclient.sendActionToServer(Action.LOGOUT,data);
		MainGUI.currUser = null;
	}

	/**
	 * @param event
	 */
	@FXML
	void Register(ActionEvent event) {
		clearSearch();
		MainGUI.MainStage.setTitle("Global City Map - Registration");
		MainGUI.openScene(MainGUI.SceneType.REGISTER);
	}

	/**
	 * Opening My Profile Window
	 * @param event
	 */
	@FXML
	void MyProfile(ActionEvent event) {
		clearSearch();
		MainGUI.MainStage.setTitle("Global City Map - My Profile");
		MainGUI.openScene(SceneType.ClientProfile);
	}

	/**
	 * Initializing the Main Window. Setting the boolean binding for the search input.
	 * @param event
	 */
	@FXML
	void initialize() {
		setSearchInfoBooleanBinding();
		SearchResultsTable.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessageFromServer(Object msg) {
		try {
			Message currMsg = (Message) msg;
			switch (currMsg.getAction()) {
			case LOGIN:
				disableProressIndicator();
				if ((Integer) currMsg.getData().get(0) == 0) {
						tfUser.setVisible(false);
						pfPassword.setVisible(false);
						btnLogin.setVisible(false);
						btnRegister.setVisible(false);
						btnLogout.setVisible(true);
						MainGUI.currUser = (User) currMsg.getData().get(1);
						Permission permission = ((User) currMsg.getData().get(1)).getPermission();
						switch (permission) {
						case CLIENT:
							MainGUI.currClient = (Client) currMsg.getData().get(1);
							Platform.runLater(() -> {
							lblClientMenu.setVisible(true);
							btnMyProfile.setVisible(true);
							//btnEditMaps.setText("Show Maps");
							//btnEditMaps.setVisible(true);
							btnBuy.setVisible(true);
							InboxController.getMessagesFromServer();
							btnInbox.setVisible(true);
							//btnInbox.setOn
							});
							break;
						case EDITOR:
							MainGUI.currEmployee = (Employee) currMsg.getData().get(1);
							Platform.runLater(() -> {
							lblClientMenu.setVisible(true);
							lblClientMenu.setText("Menu:");
							//btnEditMaps.setText("Edit Maps");
							btnEditMaps.setVisible(true);
							InboxController.getMessagesFromServer();
							btnInbox.setVisible(true);
							});
							break;
						case MANAGING_EDITOR:
							MainGUI.currEmployee = (Employee) currMsg.getData().get(1);
							Platform.runLater(() -> {
							lblClientMenu.setVisible(true);
							lblClientMenu.setText("Menu:");
							btnEditMaps.setText("Edit Maps");
							btnEditMaps.setVisible(true);
							InboxController.getMessagesFromServer();
							btnInbox.setVisible(true);
							});
							break;
						case CEO:
							MainGUI.currEmployee = (Employee) currMsg.getData().get(1);
							Platform.runLater(() -> {
							btnManage.setVisible(true);
						});
							break;
						default:
						}
						String name = ((User) currMsg.getData().get(1)).getUserName();
						Platform.runLater(() -> {
							lblWelcome.setText("Welcome " + name + "!");
						});
				} else {
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
					JOptionPane.INFORMATION_MESSAGE);
					Platform.runLater(() -> {
						tfUser.setText("");
						pfPassword.setText("");
					});
				}
				break;
			case LOGOUT:
				try {
					Platform.runLater(() -> {
						tfUser.setText("");
						pfPassword.setText("");
						lblWelcome.setText("Welcome");
					});
						tfUser.setVisible(true);
						pfPassword.setVisible(true);
						btnLogin.setVisible(true);
						btnRegister.setVisible(true);
						btnLogout.setVisible(false);
						btnMyProfile.setVisible(false);
						btnManage.setVisible(false);
						btnEditMaps.setVisible(false);
						btnBuy.setVisible(false);
						btnInbox.setVisible(false);
						lblClientMenu.setVisible(false);
						clearSearch();
					JOptionPane.showMessageDialog(null, "Disconnected successfully", "Notification",
							JOptionPane.DEFAULT_OPTION);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.toString() + "The log out failed.", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
				break;
			case GET_INBOX_MESSAGES:
				if ((Integer) currMsg.getData().get(0) == 0) {
					List<InboxMessage> messages = (List<InboxMessage>)currMsg.getData().get(1);
					int newMsg = 0;
					for (InboxMessage message : messages)
					{
						if(message.getStatus().equals(Status.NEW) )
							newMsg++;
					}
					String inboxTxt = MainGUI.currUser.getPermission() == Permission.CEO ? 
							"Manage(" + newMsg + ")": "Inbox("+ newMsg +")";
					Platform.runLater(() -> {
						if(MainGUI.currUser.getPermission() == Permission.CEO)
							btnManage.setText(inboxTxt);
						btnInbox.setText(inboxTxt);
					});
				}
				break;
			case SEARCH:
			 if((Integer)currMsg.getData().get(0) == 0) 
			 {
				 HashMap<Integer, String> maps = new HashMap<>();
				 maps = (HashMap<Integer, String>) currMsg.getData().get(1);
				 int dataSize = ((ArrayList<Object>) currMsg.getData()).size();		
				 int hashZize = maps.size();
				 setTableViewForMapsSearchResult(maps, dataSize);
				 Platform.runLater(() -> {
					 if(dataSize !=2) {
						 lblMapsNum.setText("Maps number: " + hashZize);
						 lblMapsNum.setVisible(true);
						 lblRoutesNum.setText("Routes number: " + (Integer)currMsg.getData().get(2));
						 lblRoutesNum.setVisible(true);
					 } else {
						 lblMapsNum.setText("Maps number: " + hashZize);
						 lblMapsNum.setVisible(true);
						 lblRoutesNum.setVisible(false);
					 }
				 });
		     }
				 else {
					 setTableViewForEmptySearchResult();
				 }
				 disableProressIndicator();				 
				 break;
			default:
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * @param maps
	 */
	private void setTableViewForMapsSearchResult(HashMap<Integer, String> maps, int dataSize) 
	{
		SearchResultsTable.setRowFactory(tv -> {
		    TableRow<Map> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (!row.isEmpty()) {
		        	currentWatchedMap = row.getItem();
		        	showMap();
		        }
		    });
		    return row ;
		});

		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				searchMaps = maps;
				//Create Map object and insert the Value properties from the search HashMap
				Map<String, String> maps_string  = new HashMap<>();
				for  (Integer currmap  :  maps.keySet())  {
				maps_string.put(maps.get(currmap).split(",")[0], maps.get(currmap).split(",")[1]);
				}
				// Create ObservableList of type Map
				ObservableList<Map>  keys  =  FXCollections.observableArrayList();
				// Insert every pair according to the names of columns
				if (dataSize != 2)
				{
					for  (Integer key  :  maps.keySet())  
					{
					Map<String, String>  m  =  new  HashMap<String, String>();
					m.put("mapDescription",  maps.get(key).split(",")[0]);
					m.put("sitesNumber", maps.get(key).split(",")[1]);
					keys.add(m);
					}
					SearchResultsTable.setVisible(true);
					// Create the columns necessary for the current search  of city
					col_cityName  = new  TableColumn<>("Map Description");
					col_cityName.setPrefWidth(409);
					col_cityName.setCellValueFactory(new MapValueFactory("mapDescription"));
					col_description  =  new  TableColumn<>("Sites Number");
					col_description.setCellValueFactory(new MapValueFactory("sitesNumber"));
					col_description.setMinWidth(137);
				}
				else
				{
					for  (Integer key  :  maps.keySet())  
					{
					Map<String, String>  m  =  new  HashMap<String, String>();
					m.put("mapDescription",  maps.get(key).split(",")[0]);
					m.put("cityName", maps.get(key).split(",")[1]);
					keys.add(m);
					}
					SearchResultsTable.setVisible(true);
					// Create the columns necessary for the current search  of site (or site with other param)
					col_cityName  =  new  TableColumn<>("City Name");
					col_cityName.setMinWidth(137);
					col_cityName.setCellValueFactory(new MapValueFactory("cityName"));
					col_description  = new  TableColumn<>("Map Description");
					col_description.setCellValueFactory(new MapValueFactory("mapDescription"));
					col_description.setPrefWidth(409);
				}

				// Set columns as children of the table view
				SearchResultsTable.getColumns().setAll(col_cityName, col_description/*,col_mapsNumber,col_sitesNumber,col_routesNumber*/);
				// Set the ObservableList<Map> as items
				SearchResultsTable.setItems(keys);
				}
			//}
		});
	}
	/**
	 * Clears the search fields and the table
	 * 
	 */
	void clearSearch() {
		SearchResultsTable.getItems().clear();
		lblMapsNum.setVisible(false);
		lblRoutesNum.setVisible(false);
		tfCitySearch.clear();
		tfDesSearch.clear();
		tfSiteSearch.clear();
		SearchResultsTable.setVisible(false);
	}
	
	
	/**
	 * Called when there are no search results for maps.
	 * 
	 */
	private void setTableViewForEmptySearchResult() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				clearSearch();
				SearchResultsTable.setVisible(true);
			}
		});
	}

	/**
	 * Opening the control panel of the CEO, that includes all his options of management
	 * 
	 * @param event
	 */
	@FXML
	void Manage(ActionEvent event) {

		MainGUI.MainStage.setTitle("Global City Map - Control Panel");
		MainGUI.openScene(SceneType.ClientsManagement);
	}

	/**
	 * Opening the Edit Maps according to the permission. The user will see the show maps window,
	 * while the employees will see the edit maps window.
	 */
    void showMap() {
		if(MainGUI.currUser != null) {
			clearSearch();
			Permission permission = (MainGUI.currUser.getPermission());
			selectedMap = getKey(currentWatchedMap);
			switch(permission) 
			{
				case CLIENT:
				{
					if(selectedMap == 0)
					{
						JOptionPane.showMessageDialog(null,"Plaese choose a map to watch", "Error",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						MainGUI.MainStage.setTitle("Global City Map - View Maps");
						MainGUI.openScene(SceneType.Edit);
					}
					break;
				}
				default:
					MainGUI.MainStage.setTitle("Global City Map - Edit Maps");
					MainGUI.openScene(SceneType.Edit);
			}
		}
    }
    
    @FXML
    void EditMaps(ActionEvent event) {
		clearSearch();
		MainGUI.MainStage.setTitle("Global City Map - Edit Maps");
		MainGUI.openScene(SceneType.Edit);
    }
    
	/**
	 * Setting the boolean binding for the search input.
	 * @param event
	 */
	void setSearchInfoBooleanBinding() {
		BooleanBinding booleanBind;
		booleanBind = (tfCitySearch.textProperty().isEmpty()).and(tfSiteSearch.textProperty().isEmpty()).and(tfDesSearch.textProperty().isEmpty());
		btnSearch.disableProperty().bind(booleanBind);
	}

	/**
	 * Loads the loading animation and freeze the rest of the screen. <br>
	 * Waits for a answer from the server for 20 seconds. If there is no answer calling the fucntion: {@link #timedOut()}
	 * @param showOrHide - Disable\Enable the screen for the user.
	 */
	private void loadingAnimation(Boolean showOrHide) {
		if (showOrHide == true) {
			delayTimeout = new PauseTransition(Duration.seconds(20));
			delayTimeout.setOnFinished(event -> timedOut());
			delayTimeout.play();
		} else if (delayTimeout != null){
			// stopDelayTimeout();
			delayTimeout.getStatus();
			delayTimeout.stop();
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				progressIndicator.setVisible(showOrHide);
				AncPane.setDisable(showOrHide);
			}
		});
	}
	
	private void enableProressIndicator() {
		loadingAnimation(true);
	}
	
	private void disableProressIndicator() {
		loadingAnimation(false);
	}

	private Animation.Status getDelayTimeoutStatus() {
		return delayTimeout.getStatus();
	}

	private void stopDelayTimeout() {
		delayTimeout.stop();
	}

	/**
	 * Occurs when we received no answer from the server, show an error message for the user with the message "Request timed out"
	 */
	private void timedOut() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				delayTimeout.stop();
				progressIndicator.setVisible(false);
				AncPane.setDisable(false);
				loadingAnimation(false);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Request timed out.");
				alert.showAndWait();
			}
		});
	}
	
	private int getKey(Map value) {
		ArrayList<String> values = new ArrayList<>();
		for (Object key : value.keySet()) {
			values.add(value.get(key).toString());
		}
		
		String map_value = values.get(1) + "," + values.get(0);
		
		for (Entry<Integer, String> mapID : searchMaps.entrySet()) {
			if(mapID.getValue().equals(map_value))
				return mapID.getKey();
		}
		return 0;
	}
}
