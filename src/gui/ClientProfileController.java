package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import common.Permission;
import entity.Client;
import entity.User;
import gui.MainGUI.SceneType;
import entity.Map;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


	
public class ClientProfileController implements ControllerListener {

GUIClient client;

    @FXML 
    private ResourceBundle resources;
    @FXML 
    private URL location;
    @FXML 
    private AnchorPane ClientProfileWindow;
    @FXML 
    private TabPane TabPaneMyProfile;
    @FXML
    private Button btnMain;
    @FXML 
    private Button btnLogOut;
    @FXML 
    private Button btnSave;
    @FXML
    private Button btnSavePayment;
    @FXML
    private Text lblCardNumebr;
    @FXML
    private Text lblExpiryDate;
    @FXML
    private Text lblIDnumber;
    @FXML
    private Label lblMyProfile;
    @FXML
    private Label lblWelcome;
    @FXML
    private Tab tabPaymentDetails;
    @FXML
    private Tab tabPersonalInfo;
    @FXML
    private Tab tabPurchases;
    @FXML
    private TextField tfCreditCard1;
    @FXML
    private TextField tfCreditCard2;
    @FXML
    private TextField tfCreditCard3;
    @FXML
    private TextField tfCreditCard4;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfExpiryDate;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfIDNumber;
    @FXML
    private TextField tfLastName;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfphone;
    @FXML
    private Button btnWatchMap;
    @FXML
    private TableColumn<Map,String> col_city;
    @FXML
    private TableColumn<Map,String> col_cityDescription;
    @FXML
    private TableColumn<Map,String> col_map;
    @FXML
    private TableColumn<Map,String> col_mapDescription;
    @FXML
    private TableColumn<Map,String> col_price;
    @FXML
    private TableColumn<Map,String> col_typeOfAccount;
    @FXML
    private TableColumn<Map,String> col_version;
    @FXML
    private TableView<Map> purchasesTable;


    @FXML
    void LogOut(ActionEvent event) {
		ArrayList<Object> data = new ArrayList<Object>();
		String userName = MainGUI.currClient.getUserName();
		data.add(userName);
		Message myMessage = new Message(Action.LOGOUT, data);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainGUI.openScene(SceneType.MAIN_GUI);
    }

	@FXML
	void Save(ActionEvent event) {
		try {
			Message myMessage;
			String firstName = null, lastName = null, userName = null, password = null, email = null,
					permission = "Client";
			long telephone = 0L, cardNumber = 0L, id = 0L;
			String expiryDate;
			String fullCardString = tfCreditCard1.getText() + tfCreditCard2.getText() + tfCreditCard3.getText()
			+ tfCreditCard4.getText();
			ArrayList<Object> data = new ArrayList<Object>();
			firstName = tfFirstName.getText();
			lastName = tfLastName.getText();
			userName = tfUserName.getText();
			password = tfPassword.getText();
			email = tfEmail.getText();
			// verify all numbers have correct amount of digits.			
			telephone = (tfphone.getText().isBlank() || tfphone.getText().length() <= 10) ?
					Long.parseLong(tfphone.getText()) : 0L;
			id = (tfIDNumber.getText().isBlank() || tfIDNumber.getText().length() <= 9) ?
					Long.parseLong(tfIDNumber.getText()) : 0L;
			cardNumber = (fullCardString.equals("") || fullCardString.length() >= 17) 
					? 0L : Long.parseLong(fullCardString);	
			try {
				expiryDate = tfExpiryDate.getText();
				// Validate date is a valid string
				LocalDate.parse(expiryDate);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Date invalid - " + tfExpiryDate.getText(), "Update Information error",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if ((!userName.isBlank()) && (!firstName.isBlank()) && (!lastName.isBlank()) && (!password.isBlank())
					&& isValidEmail(email) && (telephone != 0L) && (cardNumber != 0L) && (!expiryDate.isBlank())
					&& (id != 0L)) {
				data.add(firstName);
				data.add(lastName);
				data.add(userName);
				data.add(password);
				data.add(0);
				data.add(email);
				data.add(permission);
				data.add(telephone);
				data.add(cardNumber);
				data.add(id);
				data.add(expiryDate);
				myMessage = new Message(Action.EDIT_USER_DETAILS, data);
				client.sendToServer(myMessage);
			} else {
				JOptionPane.showMessageDialog(null, "One or more fields are either incorrect or empty", "",
						JOptionPane.INFORMATION_MESSAGE);
			}
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
			client.quit();
		}
	}
	
	private boolean isValidEmail(String email) {
		return email != null && email.contains("@") && email.lastIndexOf("@") != email.length() - 1;
	}

	@FXML
	void initialize() {
		//set the initial details of the user in the fields
		lblWelcome.setText("Welcome " + MainGUI.currClient.getUserName() + "!");
		String telephoneAsString = String.valueOf(MainGUI.currClient.getTelephone());
		Permission permission = (MainGUI.currClient.getPermission());
				switch(permission) 
				{
					case CLIENT:
					{
						tfUserName.setText(MainGUI.currClient.getUserName());
						tfFirstName.setText(MainGUI.currClient.getFirstName());
						tfLastName.setText(MainGUI.currClient.getLastName());
						tfEmail.setText(MainGUI.currClient.getEmail());
						tfphone.setText(telephoneAsString);
						break;
					}
					case CEO:
					{
						lblMyProfile.setText(MainGUI.currClient.getUserName() + "'s Profile");
						tfUserName.setEditable(false);
						tfFirstName.setEditable(false);
						tfLastName.setEditable(false);
						tfEmail.setEditable(false);
						tfphone.setEditable(false);
						btnSave.setVisible(false);
						break;
					}
				}
}
	


    @FXML
    void backToMainGUI(ActionEvent event) {
    	MainGUI.openScene(SceneType.MAIN_GUI);
    }

	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case EDIT_USER_DETAILS:
			if ((Integer) currMsg.getData().get(0) == 0) {
				JOptionPane.showMessageDialog(null, "All the changes have been saved succesfully", "",
						JOptionPane.INFORMATION_MESSAGE);
			} 
			else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.WARNING_MESSAGE);
			}
		case LOGOUT:
			if ((Integer) currMsg.getData().get(0) == 0) {
				JOptionPane.showMessageDialog(null, "You are now logged out of the system.", "",
						JOptionPane.INFORMATION_MESSAGE);
			} 
			else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.WARNING_MESSAGE);
			}
		
	}
	}
	
	public void setTableViewForMaps(ArrayList<Map> maps) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObservableList<Map> mapsList = FXCollections.observableArrayList();
				col_map.setCellValueFactory(new PropertyValueFactory<Map,String>("Map"));
				col_mapDescription.setCellValueFactory(new PropertyValueFactory<Map,String>("mapDescription"));
				col_city.setCellValueFactory(new PropertyValueFactory<Map,String>("city"));
				col_cityDescription.setCellValueFactory(new PropertyValueFactory<Map,String>("cityDescription"));
				col_price.setCellValueFactory(new PropertyValueFactory<Map,String>("price"));
				col_version.setCellValueFactory(new PropertyValueFactory<Map,String>("version"));
				col_typeOfAccount.setCellValueFactory(new PropertyValueFactory<Map,String>("typeOfAccount"));

				purchasesTable.getColumns().addAll(col_map, col_mapDescription, col_city,col_cityDescription,col_price,col_version,col_typeOfAccount);
				purchasesTable.setItems(mapsList);
			}
		});
	}

    @FXML
    void Watch(ActionEvent event) {
    }
	
	
}

