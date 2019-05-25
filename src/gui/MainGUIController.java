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

	public static Stage RegisterStage;


	GUIClient client;

	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private AnchorPane MapSearchWindow;
	@FXML
	private TableView<SearchTable> SearchResultsTable;
	@FXML
	private TableColumn<SearchTable, String> col_choicePanel;
	@FXML
	private TableColumn<SearchTable, String> col_CityName;
	@FXML
	private TableColumn<SearchTable, String> col_DescName;
	@FXML
	private TableColumn<SearchTable, String> col_SiteName;
	@FXML
	private Button btnDownload;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnRegister;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnShow;
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

	void setGUIClient(GUIClient client) {
		this.client = client;
		client.addControllerListener(this);
	}

	@FXML
	void Search(ActionEvent event) {
		try {
			Message myMessage;

			String cityName, siteName, mapDescription;
			ArrayList<Object> data = new ArrayList<Object>();
			cityName = tfCitySearch.getText();
			siteName = tfSiteSearch.getText();
			mapDescription = tfDesSearch.getText();
			if (!cityName.isEmpty()) {
				data.add("cityName");
				data.add(cityName);
			}
			if (!siteName.isEmpty()) {
				data.add("siteName");
				data.add(siteName);
			}
			if (!mapDescription.isEmpty()) {
				data.add("mapDescription");
				data.add(mapDescription);
			}
			if ((cityName.isEmpty()) && (siteName.isEmpty()) && (mapDescription.isEmpty())) {
				JOptionPane.showMessageDialog(null, "No paramaters were typed in.", "Error",
						JOptionPane.WARNING_MESSAGE);
			} else {
				myMessage = new Message(Action.SEARCH, data);
				client.sendToServer(myMessage);
			}
//  		for(int i=0; i<data.size();i++)
//  		{
//  			System.out.println(data.get(i).toString()+ " ,");
//  		}
//			System.out.println(data.size());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			// this.quit();
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
			client.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
			// quit();
		}
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
		tfUser.setText("");
		pfPassword.setText("");
		tfUser.setVisible(true);
		pfPassword.setVisible(true);
		btnLogin.setVisible(true);
		btnRegister.setVisible(true);
		lblWelcome.setVisible(false);
		btnLogout.setVisible(false);
	}

	@FXML
	void Register(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegisterScene.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();

			stage.setScene(new Scene(root));

			RegisterController controller = fxmlLoader.getController();
			controller.setGUIClient(client);
			this.RegisterStage = stage;
			stage.show();
		    MainGUI.MainsStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		lblWelcome.setVisible(false); 
	}

	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case LOGIN:
			if ((Integer) currMsg.getData().get(0) == 0) {
				// System.out.println(((Client)currMsg.getData().get(1)).toString());
				GUIClient.currClient = ((Client)currMsg.getData().get(1));
				tfUser.setVisible(false);
				pfPassword.setVisible(false);
				btnLogin.setVisible(false);
				btnRegister.setVisible(false);
				lblWelcome.setVisible(true);
				Platform.runLater(() -> {
				lblWelcome.setText("Welcome " + ((Client)currMsg.getData().get(1)).getUserName() + "!");
				});  
				btnLogout.setVisible(true);
				Permission permission = ((User) currMsg.getData().get(1)).getPermission();
				switch (permission) {
				case CLIENT:

					break;
				case EDITOR:

					break;
				case MANAGING_EDITOR:

					break;
				case CEO:

					break;
				}
			} else {
				// System.out.println((currMsg.getData().get(1)).toString());
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.INFORMATION_MESSAGE);
				tfUser.setText("");
				pfPassword.setText("");
			}
			break;
		case SEARCH:
			// if((Integer)currMsg.getData().get(0) == 0) {
			System.out.println(((Map) currMsg.getData().get(0)).toString());
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
		case SHOW_CLIENT_DETAILS:
			if ((Integer) currMsg.getData().get(0) == 0) {
				// clientUI.display(currMsg.getData().get(1).toString());
			} else {
				// clientUI.display(currMsg.getData().get(1).toString() + "\n");
			}
			break;
		}

	}

	public void setTableViewForMapsSearchResult(ArrayList<Map> maps) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObservableList<SearchTable> mapList = FXCollections.observableArrayList();
				col_CityName.setCellValueFactory(new PropertyValueFactory<SearchTable, String>("cityName"));
				col_SiteName.setCellValueFactory(new PropertyValueFactory<SearchTable, String>("siteName"));
				col_DescName.setCellValueFactory(new PropertyValueFactory<SearchTable, String>("description"));

				SearchResultsTable.getColumns().addAll(col_CityName, col_SiteName, col_DescName);
				SearchResultsTable.setItems(mapList);
			}
		});
	}
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
