package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import entity.Purchase;
import entity.Purchase.PurchaseType;
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

public class BuyController implements ControllerListener {

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
	private CheckBox checkBoxDownload;
	
	@FXML
	private TextField tfCityDescription;

	@FXML
	private TextField tfCityName;

	@FXML
	private TextField tfFrom;

	@FXML
	private TextField tfTo;

	@FXML
	void Subscription(ActionEvent event) {
		checkBoxDownload.setVisible(false);
		ChoiceBoxTerms.setDisable(false);
	}
	
	@FXML
	void OneTimeTerm(ActionEvent event) {
		checkBoxDownload.setVisible(true);
		ChoiceBoxTerms.setDisable(true);
	}
	
	@FXML
	void Download(ActionEvent event) {
	}
	
	@FXML
	void Buy(ActionEvent event) {
		try {
			Message myMessage;
			String m_cityName = "";
			PurchaseType m_purchaseType = null;
			LocalDate m_purchaseDate = LocalDate.now();
			LocalDate m_expirationDate = null;
			int m_renewCounter = 0;
			int m_Views = 0;
			int m_Downloads = 0;
			float m_price = 0;

			ArrayList<Object> data = new ArrayList<Object>();

			try {
				m_cityName = ChoiceBoxCities.getSelectionModel().getSelectedItem();

				if (rbBuyOnce.isSelected()) {
					m_purchaseType = Purchase.PurchaseType.SHORT_TERM_PURCHASE;
					m_expirationDate = LocalDate.now();
				} else if (rbSubscription.isSelected()) {
					m_purchaseType = Purchase.PurchaseType.LONG_TERM_PURCHASE;
					m_expirationDate = LocalDate.now().plusDays(10);
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
				data.add(m_price);

				myMessage = new Message(Action.BUY, data);
				//MainGUI.GUIclient.sendToServer(myMessage);
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

	@FXML
	void backToMainGUI(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
	}


	@Override
	public void handleMessageFromServer(Object msg) {
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) {
		case GET_CITY_PRICE:
			try {
				
		       
		        //assert(currMsg.getData().get(1) instanceof ArrayList<?>);
		       // ObservableList<String> list = FXCollections.observableArrayList(
		        //		(ArrayList<String>)currMsg.getData().get(1));
		        //ChoiceBoxCities.setItems(list);
			} catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case BUY:
			if ((Integer) currMsg.getData().get(0) == 0) {
				JOptionPane.showMessageDialog(null, "Order Complete!", "",
						JOptionPane.INFORMATION_MESSAGE);
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

	@FXML
	void initialize() {
		 ArrayList<String> cities = new ArrayList<String>();
	        cities.add("Nahariya");
	        cities.add("Haifa");
	        cities.add("Tel aviv");
	        ObservableList<String> list = FXCollections.observableArrayList(cities);
	        ChoiceBoxCities.setItems(list);
	        
			 ArrayList<String> terms = new ArrayList<String>();
			 terms.add("1 Month");
			 terms.add("3 Months");
			 terms.add("6 Months");
		       list = FXCollections.observableArrayList(terms);

		        ChoiceBoxTerms.setItems(list);
		        
		        ToggleGroup group = new ToggleGroup();
		        rbBuyOnce.setToggleGroup(group);
		        rbSubscription.setToggleGroup(group);
//		ArrayList<Object> data = new ArrayList<Object>();
//		data.add(0);
//		Message myMessage = new Message(Action.GET_CITIES, data);
//		try {
//			MainGUI.GUIclient.sendToServer(myMessage);
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message", "Error",
//					JOptionPane.WARNING_MESSAGE);
//		}
////        assert BuyWindow != null : "fx:id=\"BuyWindow\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert CoiceBoxCities != null : "fx:id=\"CoiceBoxCities\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert btnBackToMain != null : "fx:id=\"btnBackToMain\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert btnBuy != null : "fx:id=\"btnBuy\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert btnLogOut != null : "fx:id=\"btnLogOut\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblCityChoice != null : "fx:id=\"lblCityChoice\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblCityDescription != null : "fx:id=\"lblCityDescription\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblCityName != null : "fx:id=\"lblCityName\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblDiscount != null : "fx:id=\"lblDiscount\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblPurchase != null : "fx:id=\"lblPurchase\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblSubsriptionChoice != null : "fx:id=\"lblSubsriptionChoice\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblTo != null : "fx:id=\"lblTo\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblTotalPrice != null : "fx:id=\"lblTotalPrice\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblfrom != null : "fx:id=\"lblfrom\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert lblprice != null : "fx:id=\"lblprice\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert rbBuyOnce != null : "fx:id=\"rbBuyOnce\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert rbSubsrciption != null : "fx:id=\"rbSubsrciption\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert tfCityDescription != null : "fx:id=\"tfCityDescription\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert tfCityName != null : "fx:id=\"tfCityName\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert tfFrom != null : "fx:id=\"tfFrom\" was not injected: check your FXML file 'BuyScene.fxml'.";
////        assert tfTo != null : "fx:id=\"tfTo\" was not injected: check your FXML file 'BuyScene.fxml'.";
//
	}
}
