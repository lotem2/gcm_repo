package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.LocalDate;

import javax.swing.JOptionPane;

//import com.google.protobuf.TextFormat.ParseException;

import common.*;
import entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterController implements ControllerListener {
	
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private AnchorPane RegisterWindow;
	@FXML
	private Text lblCardNumebr;
	@FXML
	private Text lblExpiryDate;
	@FXML
	private Text lblIDnumber;
	@FXML
	private Label lblRegistrationForm;
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

	/**
	 * @param event
	 */
	@FXML
	void Register(ActionEvent event) {
		// handle the event here
		try {
			Message myMessage;
			String firstName = null, lastName = null, userName = null, password = null, email = null,
					permission = "Client";
			long telephone = 0L, cardNumber = 0L, id = 0L;
			String expiryDate;

			ArrayList<Object> data = new ArrayList<Object>();

			try {
				String fullCardString = tfCreditCard1.getText() + tfCreditCard2.getText() + tfCreditCard3.getText()
				+ tfCreditCard4.getText();
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
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "One or more fields are either incorrect or empty", "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try {
				expiryDate = tfExpiryDate.getText();
				// Validate date is a valid string
				LocalDate.parse(expiryDate);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Date invalid - " + tfExpiryDate.getText(), "Registration error",
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
				myMessage = new Message(Action.REGISTER, data);
				MainGUI.GUIclient.sendToServer(myMessage);
			} else {
				JOptionPane.showMessageDialog(null, "One or more fields are either incorrect or empty", "",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private boolean isValidEmail(String email) {
		return email != null && email.contains("@") && email.lastIndexOf("@") != email.length() - 1;
	}
	
	/**
	 * @param event
	 */
	@FXML
	void Cancel(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map");
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
	}
	
	@FXML
	void initialize() {
	}

	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case REGISTER:
			if ((Integer) currMsg.getData().get(0) == 0) {
				JOptionPane.showMessageDialog(null, "Registration completed successfully", "",
						JOptionPane.INFORMATION_MESSAGE);
				MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
			}

			else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
}
