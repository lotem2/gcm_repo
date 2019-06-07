package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import entity.Purchase;
import entity.Purchase.PurchaseType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

public class StatisticsController implements ControllerListener{

    @FXML
    private TableView<?> tblCityPurchase;

    @FXML
    private TableColumn<?, ?> clmDescriptionPerCity;

    @FXML
    private TableColumn<?, ?> clmPurchaseDaily;

    @FXML
    private TableColumn<?, ?> clmCityReport;

    @FXML
    private TableColumn<?, ?> clmNumPerCity;

    @FXML
    private TableView<?> tblAllActivityReport;

    @FXML
    private Label lblNumOfRenewals;

    @FXML
    private Label lblLTS;

    @FXML
    private SplitMenuButton choiceBoxCity;

    @FXML
    private TableColumn<?, ?> clmNumReport;

    @FXML
    private Label lblOTS;

    @FXML
    private TableColumn<?, ?> clmCityDaily;

    @FXML
    private TableColumn<?, ?> clmDescriptionReport;

    @FXML
    private Label lblOneTimeSub;
	
    @FXML
	private Button btnBackToMain;

    @FXML
    void CityPurchaseTable(ActionEvent event) {

    }

    @FXML
    void CityDaily(ActionEvent event) {

    }

    @FXML
    void PurchaseDaily(ActionEvent event) {

    }

    @FXML
    void LongTermSub(ActionEvent event) {

    }

    

    @FXML
    void AllActivityReportTable(ActionEvent event) {

    }

    @FXML
    void CityReport(ActionEvent event) {

    }

    @FXML
    void DescriptionReport(ActionEvent event) {

    }

    @FXML
    void NumReport(ActionEvent event) {

    }


    @FXML
    void DescriptionPerCity(ActionEvent event) {

    }

    @FXML
    void NumPerCity(ActionEvent event) {

    }
    
	@FXML
	void backToMainGUI(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
	}

    @Override
	public void handleMessageFromServer(Object msg) {
    
    }
    
	@FXML
	void initialize() {
		
	}
}
