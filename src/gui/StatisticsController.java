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
import entity.Client;
import entity.Purchase;
import entity.Purchase.PurchaseType;
import entity.Report;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Duration;
import entity.Report;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class StatisticsController implements ControllerListener {

	static HashMap<String, Float> citiesAndPrices;
	static ArrayList<String> citiesList;

	@FXML
	private DatePicker dpFromAll;

	@FXML
	private TableView<Map.Entry<String, Integer>> tblCityPurchase;

	@FXML
	private Button btnbackToMainGUI;

	@FXML
	private DatePicker dpFrom;

	@FXML
	private DatePicker dpTo;

	@FXML
	private Label lblView;

	@FXML
	private TableColumn<Map.Entry<String, Integer>, String> clmPurchaseDaily;

	@FXML
	private TableColumn<Report, String> clmCityReport;

	@FXML
	private Button btnShowStatistics;

	@FXML
	private TableView<Report> tblAllActivityReport;

	@FXML
	private Label lblNumOfRenewals;

	@FXML
	private Label lblLTS;

	@FXML
	private Label lblMembers;

	@FXML
	private Button btnShowStatisticsAll;

	@FXML
	private ChoiceBox<String> choiceBoxCity;

	@FXML
	private Label lblRenewals;

	@FXML
	private Label lblOtsPerCity;

	@FXML
	private Label lblOTS;

	@FXML
	private TableColumn<Map.Entry<String, Integer>, String> clmCityDaily;

	@FXML
	private DatePicker dpToAll;

	@FXML
	private Label lblLtsPerCity;
	
	@FXML
	private Label lblWelcome;

	@FXML
	private Label lblDownloads;

	@FXML
	private TableColumn<Report, Integer> clmMembers;
	
    @FXML
    private TableColumn<Report, Integer> clmOneTimePurchases;
    
    @FXML
    private TableColumn<Report, Integer> clmLongTermPurchases;
    
    @FXML
    private TableColumn<Report, Integer> clmRenewals;
    
    @FXML
    private TableColumn<Report, Integer> clmWatches;

    @FXML
    private TableColumn<Report, Integer> clmDownloads;
	@FXML 
	private ProgressIndicator progressIndicator;
	@FXML
	private AnchorPane AncPane;
	private PauseTransition delayTimeout;
	
	/**
	 * @param event
	 */
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
			enableProressIndicator();
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					e.toString() + " Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @param event
	 */
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
			enableProressIndicator();
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not send message to server.  Terminating client.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * @param event
	 */
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
				String LongTerm = (String) (currMsg.getData().get(3)).toString();
				String OneTime = (String) (currMsg.getData().get(4)).toString();

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

				ObservableList<Entry<String, Integer>> items = FXCollections.observableArrayList(map.entrySet());
				tblCityPurchase.setItems(items);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case CITY_ACTIVITY_REPORT:
			disableProressIndicator();
			if ((Integer) currMsg.getData().get(0) == 0) {
				Report report = ((Report) currMsg.getData().get(1));
				Platform.runLater(() -> {
					lblMembers.setText("Number of members: " + report.getNumOfMembers());
					lblRenewals.setText("Number of renewals: " + report.getNumOfRenew());
					lblView.setText("Number of views: " + report.getNumOfViews());
					lblDownloads.setText("Number of downloads: " + report.getNumOfDownloads());
					lblOtsPerCity.setText("Number of one time subscriptions: " + report.getNumOfOTP());
					lblLtsPerCity.setText("Number of long term subscriptions: " + report.getNumOfLTP());
				});
			} else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.INFORMATION_MESSAGE);
			}
			break;
		case ACTIVITY_REPORT:
			disableProressIndicator();
			if ((Integer) currMsg.getData().get(0) == 0) {
				ArrayList<Report> reports = (ArrayList<Report>) (currMsg.getData().get(1));
				ObservableList<Report> reportsList = FXCollections.observableArrayList(reports);
				clmCityReport.setCellValueFactory(new PropertyValueFactory<Report, String>("CityName"));
				clmLongTermPurchases.setCellValueFactory(new PropertyValueFactory<Report, Integer>("NumOfLTP"));
				clmOneTimePurchases.setCellValueFactory(new PropertyValueFactory<Report, Integer>("NumOfOTP"));
				clmMembers.setCellValueFactory(new PropertyValueFactory<Report, Integer>("NumOfMembers"));
				clmRenewals.setCellValueFactory(new PropertyValueFactory<Report, Integer>("NumOfRenew"));
				clmWatches.setCellValueFactory(new PropertyValueFactory<Report, Integer>("NumOfViews"));
				clmDownloads.setCellValueFactory(new PropertyValueFactory<Report, Integer>("NumOfDownloads"));
				tblAllActivityReport.setItems(reportsList);
			} else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.INFORMATION_MESSAGE);
			}
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
		lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(MainGUI.currUser.getPermission());
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
	
	   /**
		 * Loads the loading animation and freeze the rest of the screen. <br>
		 * Waits for a answer from the server for 20 seconds. If there is no answer calling the fucntion: {@link #timedOut()}
		 * @param showOrHide - Disable\Enable the screen for the user.
		 */
		public void loadingAnimation(Boolean showOrHide) {

			if (showOrHide == true) {
				delayTimeout = new PauseTransition(Duration.seconds(30));
				delayTimeout.setOnFinished(event -> timedOut());
				delayTimeout.play();
			} else {
				// stopDelayTimeout();
				delayTimeout.getStatus();
				delayTimeout.stop();
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					progressIndicator.setVisible(showOrHide);
					AncPane.setDisable(showOrHide);
				}
			});
		}
		
		public void enableProressIndicator() {
			loadingAnimation(true);
		}
		
		public void disableProressIndicator() {
			loadingAnimation(false);
		}

		public Animation.Status getDelayTimeoutStatus() {
			return delayTimeout.getStatus();
		}

		public void stopDelayTimeout() {
			delayTimeout.stop();
		}

		/**
		 * Occurs when we received no answer from the server, show an error message for the user with the message "Request timed out"
		 */
		private void timedOut() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					delayTimeout.stop();
					progressIndicator.setVisible(false);
					AncPane.setDisable(false);
					loadingAnimation(false);
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText("Request timed out.");
					alert.showAndWait();
				}
			});
		}
}