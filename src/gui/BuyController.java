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
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BuyController implements ControllerListener {

	@FXML
	private Button btnBuy;

	@FXML
	void Buy(ActionEvent event) {
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
