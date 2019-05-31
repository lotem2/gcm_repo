package gui;

import client.*;
import entity.*;
import gui.MainGUI.SceneType;
import common.*;
import java.io.*;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import common.Action;
import common.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.swing.JOptionPane;//library for popup messages

public class MainGUIController implements ControllerListener {

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private AnchorPane MapSearchWindow;
	@FXML
	private TableView<Map> SearchResultsTable;
//	@FXML
//	private TableColumn<SearchTable, String> col_choicePanel;
//	@FXML
//	private TableColumn<SearchTable, String> col_CityName;
//	@FXML
//	private TableColumn<SearchTable, String> col_DescName;
//	@FXML
//	private TableColumn<Map, String> col_SiteName;
	@FXML
	private TableColumn<Map, String> col_city;
	@FXML
	private TableColumn<Map, String> col_cityDescription;
	@FXML
	private TableColumn<Map, String> col_map;
	@FXML
	private TableColumn<Map, String> col_mapDescription;
	@FXML
	private TableColumn<Map, String> col_price;
	@FXML
	private TableColumn<Map, String> col_sitesNumber;
	@FXML
	private TableColumn<Map, String> col_version;
    @FXML
    private Button btnControl;
    @FXML
    private Button btnEditMaps;
	@FXML
	private Button btnDownload;
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
	private Button btnShow;
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
	private Label lblWelcome;

	/**
	 * @param event making the data to send to the server
	 */
	@FXML
	void Search(ActionEvent event) {
		try {
			Message myMessage;
			String cityName, siteName, mapDescription;
			ArrayList<Object> data = new ArrayList<Object>();
			cityName = tfCitySearch.getText();
			siteName = tfSiteSearch.getText();
			mapDescription = tfDesSearch.getText();
			if (!cityName.isEmpty())
				data.add(cityName);
			else
			   data.add("null");
			
			if (!siteName.isEmpty()) 
				data.add(siteName);
			else
			   data.add("null");
			
			if (!mapDescription.isEmpty())
				data.add(mapDescription);
			else
			   data.add("null");
			myMessage = new Message(Action.SEARCH, data);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
	}

	@FXML
	void Login(ActionEvent event) {
		// handle the event here
		try {
			Message myMessage;
			String userName = "", password;
			ArrayList<Object> data = new ArrayList<Object>();
			userName = tfUser.getText();
			password = pfPassword.getText();
			if ((userName != null) && (password != null)) {
				data.add(userName);
				data.add(password);
			} else {
				JOptionPane.showMessageDialog(null, "Incorrect username or password, please try again.", "",
						JOptionPane.INFORMATION_MESSAGE);
				tfUser.setText("");
				pfPassword.setText("");
			}
			myMessage = new Message(Action.LOGIN, data);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
			// quit();
		}
	}

	@FXML
	void Buy(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.BUY);
	}

	@FXML
	void Download(ActionEvent event) {
		// handle the event here
	}

	@FXML
	void Show(ActionEvent event) {
		// handle the event here
	}

	@FXML
	void Logout(ActionEvent event) {
		ArrayList<Object> data = new ArrayList<Object>();
		String userName = MainGUI.currClient.getUserName();
		data.add(userName);
		Message myMessage = new Message(Action.LOGOUT, data);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	@FXML
	void Register(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.REGISTER);
	}

	@FXML
	void MyProfile(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - My Profile");
		MainGUI.openScene(SceneType.ClientProfile);
	}

	@FXML
	void initialize() {
		setPersonalInfoBooleanBinding();
	}

	@Override
	public void handleMessageFromServer(Object msg) {
		try {
			Message currMsg = (Message) msg;
			switch (currMsg.getAction()) {
			case LOGIN:
				if ((Integer) currMsg.getData().get(0) == 0) {
					tfUser.setVisible(false);
					pfPassword.setVisible(false);
					btnLogin.setVisible(false);
					btnRegister.setVisible(false);
					btnLogout.setVisible(true);
					Permission permission = ((User) currMsg.getData().get(1)).getPermission();
					switch (permission) {
					case CLIENT:
						MainGUI.currClient = (Client) currMsg.getData().get(1);
						btnMyProfile.setVisible(true);
						break;
					case EDITOR:
						MainGUI.currEmployee = (Employee) currMsg.getData().get(1);
						btnEditMaps.setVisible(true);
						btnBuy.setVisible(false);
						break;
					case MANAGING_EDITOR:
						MainGUI.currEmployee = (Employee) currMsg.getData().get(1);
						btnEditMaps.setVisible(true);
						btnBuy.setVisible(false);
						break;
					case CEO:
						MainGUI.currEmployee = (Employee) currMsg.getData().get(1);
						btnManage.setVisible(true);
						btnEditMaps.setVisible(true);
						btnBuy.setVisible(false);
						break;
					default:

					}
					Platform.runLater(() -> {
						String name = ((User) currMsg.getData().get(1)).getUserName();
						lblWelcome.setText("Welcome " + name + "!");
					});
				} else {
					// System.out.println((currMsg.getData().get(1)).toString());
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
							JOptionPane.INFORMATION_MESSAGE);
					tfUser.setText("");
					pfPassword.setText("");
				}
				break;
			case LOGOUT:
				try {
					tfUser.setText("");
					pfPassword.setText("");
					tfUser.setVisible(true);
					pfPassword.setVisible(true);
					btnLogin.setVisible(true);
					btnRegister.setVisible(true);
					btnLogout.setVisible(false);
					btnMyProfile.setVisible(false);
					Platform.runLater(() -> {
						lblWelcome.setText("Welcome");
					});
					JOptionPane.showMessageDialog(null, "Disconnected successfully", "Notification",
							JOptionPane.DEFAULT_OPTION);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.toString() + "The log out failed.", "Error",
							JOptionPane.WARNING_MESSAGE);
				}
				break;
			case SEARCH:
			 if((Integer)currMsg.getData().get(0) == 0) {
				//ArrayList<Map> maps = new ((Map) currMsg.getData().get(1));
				//setTableViewForMapsSearchResult(maps);
				//System.out.println(((Map) currMsg.getData().get(0)).toString());
			 }
//	      	 else {
//	      		 clientUI.display(currMsg.getData().get(1).toString() + "\n"
//	      				 + "The message was not sent to the gui.Please retry\"");
//	      	 }
				break;
			case ADD_PURCHASE:
				if ((Integer) currMsg.getData().get(0) == 0) {
					// clientUI.display("Purchase added successfully\n");
				} else {
					// clientUI.display(currMsg.getData().get(1).toString() + "\n");
				}
				break;
     			default:
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setTableViewForMapsSearchResult(ArrayList<Map> maps) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObservableList<Map> mapsList = FXCollections.observableArrayList();
				//col_map.setCellValueFactory(new PropertyValueFactory<Map, String>("Map"));
				col_mapDescription.setCellValueFactory(new PropertyValueFactory<Map, String>("mapDescription"));
				col_city.setCellValueFactory(new PropertyValueFactory<Map, String>("city"));
				col_cityDescription.setCellValueFactory(new PropertyValueFactory<Map, String>("cityDescription"));
				col_price.setCellValueFactory(new PropertyValueFactory<Map, String>("price"));
				col_version.setCellValueFactory(new PropertyValueFactory<Map, String>("version"));
				col_sitesNumber.setCellValueFactory(new PropertyValueFactory<Map, String>("sitesNumber"));

				SearchResultsTable.getColumns().addAll(col_map, col_mapDescription, col_city, col_cityDescription,
						col_price, col_version, col_sitesNumber);
				SearchResultsTable.setItems(mapsList);
			}
		});
	}

	@FXML
	void Manage(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - Clients Management");
		MainGUI.openScene(SceneType.ClientsManagement);
	}
//<<<<<<< HEAD
    @FXML
    void EditMaps(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - Edit Tool");
		MainGUI.openScene(SceneType.Edit);
    }
    @FXML
    void UserList(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map - UserList");
		MainGUI.openScene(SceneType.ClientsManagement);
    }
    
	void setPersonalInfoBooleanBinding() {
		BooleanBinding booleanBind;
		booleanBind = (tfCitySearch.textProperty().isEmpty()).and(tfSiteSearch.textProperty().isEmpty()).and(tfDesSearch.textProperty().isEmpty());
		btnSearch.disableProperty().bind(booleanBind);
	}
	
	
//=======

//>>>>>>> branch 'master' of https://github.com/lotem2/gcm_repo
	/*
	 * public void windowClosing(WindowEvent e) {
	 * 
	 * // if my Server exist
	 * 
	 * if(server != null) {
	 * 
	 * try {
	 * 
	 * server.stop(); // ask the server to close the conection
	 * 
	 * }
	 * 
	 * catch(Exception eClose) {
	 * 
	 * }
	 * 
	 * server = null;
	 * 
	 * }
	 * 
	 * // dispose the frame
	 * 
	 * dispose();
	 * 
	 * System.exit(0); }
	 */
}
