package gui;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import common.Services;
import entity.City;
import entity.Purchase;
import entity.Purchase.PurchaseType;
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class BuyController implements ControllerListener {

	static HashMap<String, Float> citiesAndPrices;
	static ArrayList<String> citiesList;
	static ArrayList<String> terms;
	static float currPrice;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane BuyWindow;

	@FXML
	private ChoiceBox<String> ChoiceBoxCities;

	@FXML
	private ChoiceBox<String> ChoiceBoxTerms;

	@FXML
	private Button btnBackToMain;

	@FXML
	private Button btnBuy;

	@FXML
	private Label lblCityChoice;

	@FXML
	private Label lblCityDescription;

	@FXML
	private Label lblCityName;

	@FXML
	private Label lblDiscount;

	@FXML
	private Label lblPurchase;

	@FXML
	private Label lblSubsriptionChoice;

	@FXML
	private Label lblTo;

	@FXML
	private Label lblTotalPrice;

	@FXML
	private Label lblfrom;

	@FXML
	private Label lblprice;

	@FXML
	private Label lblTerm;

	@FXML
	private RadioButton rbBuyOnce;

	@FXML
	private RadioButton rbSubscription;

	@FXML
	private TextField tfCityDescription;

	@FXML
	private TextField tfCityName;

	@FXML
	private TextField tfFrom;

	@FXML
	private TextField tfTo;

	@FXML
	private Label lblWelcome;

//	@FXML
//	void ShowPrice(ActionEvent event) {
//		setPrice(rbSubscription.isSelected());
//
//	}

	/**
	 * @param event
	 */
	@FXML
	void OneTimeTerm(ActionEvent event) {
		lblTotalPrice.setText("Total Price: ");
		ChoiceBoxTerms.setDisable(true);
		setPrice(rbSubscription.isSelected(), "", "");

	}

	/**
	 * @param event
	 */
	@FXML
	void Subscription(ActionEvent event) {
		lblTotalPrice.setText("Total Price: ");
		ChoiceBoxTerms.setDisable(false);
		// add a listener
		ChoiceBoxTerms.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			// if the item of the list is changed
			public void changed(ObservableValue ov, Number value, Number new_value) {
				String currTerm = ChoiceBoxTerms.getValue();
				setPrice(rbSubscription.isSelected(), "", terms.get(new_value.intValue()));
			}
		});

	}

	/**
	 * @param event
	 */
	@FXML
	void Buy(ActionEvent event) {
		try {
			Message myMessage;
			String m_cityName = "";
			String m_purchaseType = "";
			LocalDate m_purchaseDate = LocalDate.now();
			LocalDate m_expirationDate = null;
			int m_renewCounter = 0;
			int m_Views = 0;
			int m_Downloads = 0;

			ArrayList<Object> data = new ArrayList<Object>();

			try {
				m_cityName = ChoiceBoxCities.getSelectionModel().getSelectedItem();

				if (rbBuyOnce.isSelected()) {
					m_purchaseType = Purchase.PurchaseType.SHORT_TERM_PURCHASE.toString();
					m_expirationDate = LocalDate.now();
				} else if (rbSubscription.isSelected()) {
					m_purchaseType = Purchase.PurchaseType.LONG_TERM_PURCHASE.toString();
					String term = ChoiceBoxTerms.getValue();
					switch (term) {
					case "1 Month":
						m_expirationDate = LocalDate.now().plusDays(30);
						break;
					case "3 Months":
						m_expirationDate = LocalDate.now().plusDays(90);
						break;
					case "6 Months":
						m_expirationDate = LocalDate.now().plusDays(180);
						break;
					default:
					}
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "One or more fields are either incorrect or empty", "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if ((!m_cityName.isBlank()) && (m_purchaseType != null) && (m_expirationDate != null)
					&& (m_expirationDate != null)) {
				data.add(MainGUI.currClient.getUserName());
				data.add(m_cityName);
				data.add(m_purchaseType);
				data.add(m_purchaseDate);
				data.add(m_expirationDate);
				data.add(m_renewCounter);
				data.add(m_Views);
				data.add(m_Downloads);
				data.add(currPrice);

//				if (rbBuyOnce.isSelected()) {
				//	Message myTempMessage;
				//	ArrayList<Object> tempData = new ArrayList<Object>();
				//	tempData.add(ChoiceBoxCities.getValue());
				//	tempData.add(Purchase.PurchaseType.SHORT_TERM_PURCHASE);
				//	myTempMessage = new Message(Action.DOWNLOAD_PURCHASE, tempData);
				//	MainGUI.GUIclient.sendToServer(myTempMessage);
	//			}

				myMessage = new Message(Action.BUY, data);
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

	/**
	 * @param event
	 */
	@FXML
	void backToMainGUI(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map");
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
	}

	/**
	 * Get an Object
	 */
	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case GET_CITY_PRICE:
			try {
				citiesAndPrices = (HashMap<String, Float>) currMsg.getData().get(1);
				ArrayList<String> list = null;
				if (citiesAndPrices != null) {
					citiesList = new ArrayList<String>(citiesAndPrices.keySet());
				}
				ObservableList<String> currCitiesList = FXCollections.observableArrayList(citiesList);
				ChoiceBoxCities.setItems(currCitiesList);
				ChoiceBoxCities.setDisable(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case BUY:
			if ((Integer) currMsg.getData().get(0) == 0) {
				if (rbBuyOnce.isSelected()) {
					Platform.runLater(() -> {
						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Save Image");
						fileChooser.setInitialFileName("citymap.png");
						File file = fileChooser.showSaveDialog(MainGUI.MainStage);
						Services.writeCityToFile((City) (currMsg.getData().get(1)), file.getAbsolutePath());
					});
				} else {
					JOptionPane.showMessageDialog(null, "Order Complete!", "", JOptionPane.INFORMATION_MESSAGE);

				}
				MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
			}

			else {
				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "",
						JOptionPane.INFORMATION_MESSAGE);
			}
			break;

		default:
		}
	}

	/**
	 * @param isSubscription - boolean to indicate whether ther is a subscription
	 * @param city - city to set price to
	 * @param term - the length of the subscription
	 * @return String - updated price
	 */
	public String setPrice(boolean isSubscription, String city, String term) {
		double price;
		if (city.isBlank()) {
			city = ChoiceBoxCities.getValue();
		}
		price = citiesAndPrices.get(city);
		if (isSubscription) {
			if (term.isBlank()) {
				term = ChoiceBoxTerms.getValue();
			}
			switch (term) {
			case "1 Month":
				price *= 1.8;
				break;
			case "3 Months":
				price *= 2.8;
				break;
			case "6 Months":
				price *= 5.5;
				break;
			default:
			}
		}
		price = Double.parseDouble(new DecimalFormat("##.####").format(price));
		currPrice = (float) price;
		String totalPrice = String.valueOf(price);
		lblTotalPrice.setText("Total Price: " + totalPrice);
		return totalPrice;
	}

	@FXML
	void initialize() {
		lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
		ChoiceBoxCities.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue ov, Number value, Number new_value) {

				rbBuyOnce.setDisable(false);
				rbSubscription.setDisable(false);
				setPrice(rbSubscription.isSelected(), citiesList.get(new_value.intValue()), "");

			}
		});

		terms = new ArrayList<String>();
		terms.add("1 Month");
		terms.add("3 Months");
		terms.add("6 Months");
		ObservableList<String> list = FXCollections.observableArrayList(terms);
		ChoiceBoxTerms.setItems(list);

		ToggleGroup group = new ToggleGroup();
		rbBuyOnce.setToggleGroup(group);
		rbSubscription.setToggleGroup(group);

		ArrayList<Object> data = new ArrayList<Object>();
		data.add(MainGUI.currUser.getPermission());
		Message myMessage = new Message(Action.GET_CITY_PRICE, data);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message", "Error",
					JOptionPane.WARNING_MESSAGE);
		}

	}
}
