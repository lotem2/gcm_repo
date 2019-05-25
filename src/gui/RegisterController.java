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

import com.google.protobuf.TextFormat.ParseException;

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

	GUIClient client;

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
	

	void setGUIClient(GUIClient client) {
		this.client = client;
		client.addControllerListener(this);
	}

	@FXML
	void Register(ActionEvent event) {
		// handle the event here
		try {
			Message myMessage;
			String firstName = null, lastName = null, userName = null, password = null, email = null,
					permission = "Client";
			long telephone = 0L, cardNumber = 0L, id = 0L;
			String expiryDate, fullCardNum = tfCreditCard1.getText() + tfCreditCard2.getText() + tfCreditCard3.getText()
					+ tfCreditCard4.getText();
			LocalDate expireDate = null;

			ArrayList<Object> data = new ArrayList<Object>();

			firstName = tfFirstName.getText();
			lastName = tfLastName.getText();
			userName = tfUserName.getText();
			password = tfPassword.getText();
			email = tfEmail.getText();
			telephone = (tfphone.getText().isBlank()) ? 0L : Long.parseLong(tfphone.getText());
			cardNumber = (fullCardNum.equals("")) ? 0L : Long.parseLong(fullCardNum);
			id = (tfIDNumber.getText().equals("")) ? 0L : Long.parseLong(tfIDNumber.getText());

			try {
				expiryDate = tfExpiryDate.getText();
				// Validate date is a valid string
				expireDate = LocalDate.parse(expiryDate);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Date invalid - " + tfExpiryDate.getText(), "Registration error",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if ((!userName.isBlank()) && (!firstName.isBlank()) && (!lastName.isBlank()) && (!password.isBlank())
					&& (!email.isBlank()) && (telephone != 0L) && (cardNumber != 0L) && (!expiryDate.isBlank())
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
				client.sendToServer(myMessage);
			} else {
				JOptionPane.showMessageDialog(null, "One or more fields are either incorrect or empty", "",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
			// quit();
		}
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

			Platform.runLater(() -> {
				try {
				    MainGUIController.RegisterStage.close();
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainGUIScene.fxml"));
					Parent root = (Parent) fxmlLoader.load();
					Stage stage = new Stage();
					stage.setScene(new Scene(root));
					MainGUIController controller = fxmlLoader.getController();
					controller.setGUIClient(client);
					stage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		}

		else {
			JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	}
}
