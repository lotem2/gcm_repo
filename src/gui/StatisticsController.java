package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import entity.Purchase;
import entity.Purchase.PurchaseType;
import entity.Report;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class StatisticsController implements ControllerListener {

	static HashMap<String, Float> citiesAndPrices;
	static ArrayList<String> citiesList;

	@FXML
	private TableView<Map.Entry<String, Integer>> tblCityPurchase;

	@FXML
	private TableColumn<?, ?> clmDescriptionPerCity;

	@FXML
	private TableColumn<Map.Entry<String, Integer>, String> clmPurchaseDaily;

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
	private ChoiceBox<String> choiceBoxCity;

	@FXML
	private TableColumn<?, ?> clmNumReport;

	@FXML
	private Label lblOTS;

	@FXML
	private TableColumn<Map.Entry<String, Integer>, String> clmCityDaily;

	@FXML
	private TableColumn<?, ?> clmDescriptionReport;

	@FXML
	private Label lblOneTimeSub;

	@FXML
	private Button btnBackToMain;
	
	@FXML
	private Button btnShowStatisticsAll;
	
	@FXML
	private Button btnShowStatistics;

	@FXML
	private DatePicker dpFrom;

	@FXML
	private DatePicker dpTo;
	
	@FXML
	private DatePicker dpFromAll;
	
	@FXML
	private DatePicker dpToAll;
	

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
	void ShowStatistics(ActionEvent event) {
		try {
			String city = choiceBoxCity.getSelectionModel().getSelectedItem();
			LocalDate fromDate = dpFrom.getValue();
			LocalDate toDate = dpTo.getValue();
			DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Message myMessage;

			ArrayList<Object> data = new ArrayList<Object>();
			myMessage = new Message(Action.CITY_ACTIVITY_REPORT, data);
			data.add(dTF.format(fromDate));
			data.add(dTF.format(toDate));
			data.add(city);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	@FXML
	void ShowStatisticsAll(ActionEvent event) {
		try {
			LocalDate fromDate = dpFromAll.getValue();
			LocalDate toDate = dpToAll.getValue();
			DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Message myMessage;

			ArrayList<Object> data = new ArrayList<Object>();
			myMessage = new Message(Action.ACTIVITY_REPORT, data);
			data.add(dTF.format(fromDate));
			data.add(dTF.format(toDate));
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) { 
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	@FXML
	void backToMainGUI(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
	}

	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case DAILY_REPORT:
			try {
				String totalRenewals = (String) (currMsg.getData().get(2)).toString();
				String OneTime = (String) (currMsg.getData().get(3)).toString();
				String LongTerm = (String) (currMsg.getData().get(4)).toString();

				Platform.runLater(() -> {
					lblNumOfRenewals.setText("Total number of renewals: " + totalRenewals);
					lblOTS.setText("One time subscription: " + OneTime);
					lblLTS.setText("Long term subscription: " + LongTerm);
				});
				HashMap<String, Integer> map = (HashMap<String, Integer>) (currMsg.getData().get(1));
				clmCityDaily.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(
									TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> p) {
								// this callback returns property for just one cell, you can't use a loop here
								// for first column we use key
								return new SimpleStringProperty(p.getValue().getKey());
							}
						});
				clmPurchaseDaily.setCellValueFactory(
						new Callback<TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(
									TableColumn.CellDataFeatures<Map.Entry<String, Integer>, String> p) {
								// for second column we use value
								return new SimpleStringProperty(p.getValue().getValue().toString());
							}
						});

				ObservableList<Entry<String,Integer>> items = FXCollections.observableArrayList(map.entrySet());
				tblCityPurchase.setItems(items);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case ACTIVITY_REPORT:
			ArrayList<Report> array = new ArrayList<Report>();
			array = (ArrayList<Report>) (currMsg.getData().get(1));
			break;
		case GET_CITY_PRICE:
			try {
				citiesAndPrices = (HashMap<String, Float>) currMsg.getData().get(1);
				ArrayList<String> list = null;
				if (citiesAndPrices != null) {
					citiesList = new ArrayList<String>(citiesAndPrices.keySet());
				}
				ObservableList<String> currCitiesList = FXCollections.observableArrayList(citiesList);
				choiceBoxCity.setItems(currCitiesList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
		}
	}

	@FXML
	void initialize() {
		ArrayList<Object> data = new ArrayList<Object>();
		Message myMessage = new Message(Action.DAILY_REPORT, data);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
			myMessage = new Message(Action.GET_CITY_PRICE, data);
			MainGUI.GUIclient.sendToServer(myMessage);
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message", "Error",
					JOptionPane.WARNING_MESSAGE);
		}

		choiceBoxCity.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue ov, Number value, Number new_value) {

			}
		});
	}
}