package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import common.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class BuyController implements ControllerListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane BuyWindow;

    @FXML
    private ChoiceBox<?> CoiceBoxCities;

    @FXML
    private Button btnBackToMain;

    @FXML
    private Button btnBuy;

    @FXML
    private Button btnLogOut;

    @FXML
    private Label lblCityChoice;

    @FXML
    private Label lblCityDescription;

    @FXML
    private Label lblCityName;

    @FXML
    private Label lblPurchase;

    @FXML
    private Label lblSubsriptionChoice;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private Label lblfrom;

    @FXML
    private RadioButton rbBuyOnce;

    @FXML
    private RadioButton rbSubsrciption;

    @FXML
    private TextField tfCityDescription;

    @FXML
    private TextField tfCityName;


    @FXML
    void Buy(ActionEvent event) {
    }

    @FXML
    void LogOut(ActionEvent event) {
    }

    @FXML
    void backToMainGUI(ActionEvent event) {
    }

    @FXML
    void tfCityDescription(ActionEvent event) {
    }

    @FXML
    void tfCityName(ActionEvent event) {
    }

    @FXML
    void initialize() {
        assert BuyWindow != null : "fx:id=\"BuyWindow\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert CoiceBoxCities != null : "fx:id=\"CoiceBoxCities\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert btnBackToMain != null : "fx:id=\"btnBackToMain\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert btnBuy != null : "fx:id=\"btnBuy\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert btnLogOut != null : "fx:id=\"btnLogOut\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblCityChoice != null : "fx:id=\"lblCityChoice\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblCityDescription != null : "fx:id=\"lblCityDescription\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblCityName != null : "fx:id=\"lblCityName\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblPurchase != null : "fx:id=\"lblPurchase\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblSubsriptionChoice != null : "fx:id=\"lblSubsriptionChoice\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblTotalPrice != null : "fx:id=\"lblTotalPrice\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert lblfrom != null : "fx:id=\"lblfrom\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert rbBuyOnce != null : "fx:id=\"rbBuyOnce\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert rbSubsrciption != null : "fx:id=\"rbSubsrciption\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert tfCityDescription != null : "fx:id=\"tfCityDescription\" was not injected: check your FXML file 'BuyScene.fxml'.";
        assert tfCityName != null : "fx:id=\"tfCityName\" was not injected: check your FXML file 'BuyScene.fxml'.";


    }

	@Override
	public void handleMessageFromServer(Object msg) {
	
	}

}
