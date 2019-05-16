package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class RegisterController {

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


    @FXML
    void Register(ActionEvent event) {
    }

    @FXML
    void initialize() {


    }

}
