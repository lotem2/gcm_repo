package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class MainGUIController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="MapSearchWindow"
    private AnchorPane MapSearchWindow; // Value injected by FXMLLoader

    @FXML // fx:id="SearchResultsTable"
    private TableView<?> SearchResultsTable; // Value injected by FXMLLoader

    @FXML // fx:id="btnDownload"
    private Button btnDownload; // Value injected by FXMLLoader

    @FXML // fx:id="btnLogin"
    private Button btnLogin; // Value injected by FXMLLoader

    @FXML // fx:id="btnSearch"
    private Button btnSearch; // Value injected by FXMLLoader

    @FXML // fx:id="btnShow"
    private Button btnShow; // Value injected by FXMLLoader

    @FXML // fx:id="choicePanel"
    private TableColumn<?, ?> choicePanel; // Value injected by FXMLLoader

    @FXML // fx:id="pfPassword"
    private PasswordField pfPassword; // Value injected by FXMLLoader

    @FXML // fx:id="tcCityName"
    private TableColumn<?, ?> tcCityName; // Value injected by FXMLLoader

    @FXML // fx:id="tcDescName"
    private TableColumn<?, ?> tcDescName; // Value injected by FXMLLoader

    @FXML // fx:id="tcSiteName"
    private TableColumn<?, ?> tcSiteName; // Value injected by FXMLLoader

    @FXML // fx:id="tfCitySearch"
    private TextField tfCitySearch; // Value injected by FXMLLoader

    @FXML // fx:id="tfDesSearch"
    private TextField tfDesSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tfSiteSearch"
    private TextField tfSiteSearch; // Value injected by FXMLLoader

    @FXML // fx:id="tfUser"
    private TextField tfUser; // Value injected by FXMLLoader

    @FXML // fx:id="txtMapsCatalog"
    private Text txtMapsCatalog; // Value injected by FXMLLoader


    // Handler for PasswordField[fx:id="pfPassword"] onAction
    // Handler for TextField[fx:id="tfUser"] onAction
    @FXML
    void Login(ActionEvent event) {
        // handle the event here
    }

    // Handler for TextField[fx:id="tfCitySearch"] onAction
    // Handler for TextField[fx:id="tfDesSearch"] onAction
    // Handler for TextField[fx:id="tfSiteSearch"] onAction
    @FXML
    void Search(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="btnDownload"] onAction
    @FXML
    void download(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="btnLogin"] onAction
    @FXML
    void login(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="btnSearch"] onAction
    @FXML
    void search(ActionEvent event) {
        // handle the event here
    }

    // Handler for Button[fx:id="btnShow"] onAction
    @FXML
    void show(ActionEvent event) {
        // handle the event here
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert MapSearchWindow != null : "fx:id=\"MapSearchWindow\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert SearchResultsTable != null : "fx:id=\"SearchResultsTable\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnDownload != null : "fx:id=\"btnDownload\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnSearch != null : "fx:id=\"btnSearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert btnShow != null : "fx:id=\"btnShow\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert choicePanel != null : "fx:id=\"choicePanel\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert pfPassword != null : "fx:id=\"pfPassword\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tcCityName != null : "fx:id=\"tcCityName\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tcDescName != null : "fx:id=\"tcDescName\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tcSiteName != null : "fx:id=\"tcSiteName\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfCitySearch != null : "fx:id=\"tfCitySearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfDesSearch != null : "fx:id=\"tfDesSearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfSiteSearch != null : "fx:id=\"tfSiteSearch\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert tfUser != null : "fx:id=\"tfUser\" was not injected: check your FXML file 'MainGUIScene.fxml'.";
        assert txtMapsCatalog != null : "fx:id=\"txtMapsCatalog\" was not injected: check your FXML file 'MainGUIScene.fxml'.";

        // Initialize your logic here: all @FXML variables will have been injected

    }

}


