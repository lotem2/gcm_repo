package gui;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import common.Action;
import common.Message;
import common.Permission;
import common.Status;
import entity.*;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

public class InboxController implements ControllerListener {
	@FXML
	private TableView<InboxMessage> tblInbox;
	@FXML
	private TableColumn<InboxMessage ,String> clmMessages;
	@FXML
	private TableColumn<InboxMessage, LocalDate> clmDate;
	
	@FXML
	private Pane paneApproveDecline;
	@FXML
	private Button btnApprove;
	@FXML
	private Button btnDecline;
	@FXML
	private Button btnClose;
	@FXML
	private Button btnRefresh;
	@FXML
	private Label lblWelcome;
	
	InboxMessage m_selectedMessage;
	
	/**
	 * @param isApproved
	 */
	private void sendApprovalOrDeclineMessage(boolean isApproved) {
		ArrayList<Object> data = new ArrayList<Object>();
		//data.add(isApproved ? Status.APPROVED.toString() : Status.DECLINED.toString());
		//data.add(m_selectedMessage);
		Action action;
		if(m_selectedMessage.getContent().contains("price"))
		{
			action = Action.HANDLE_PRICE_CHANGE_REQ;
			data.add(isApproved ? Status.APPROVED.toString() : Status.DECLINED.toString());
			data.add(m_selectedMessage);
			data.add(m_selectedMessage.getContent().split("to ")[1]);
			data.add((m_selectedMessage.getContent().split("Approve ")[1]).split(" new")[0]); //cityname
		}
		else // m_selectedMessage.getContent().contains("version"))
		{
			action = Action.HANDLE_NEW_VER_REQ;
			data.add(isApproved ? Status.APPROVED.toString() : Status.DECLINED.toString());
			data.add(m_selectedMessage);
			data.add(m_selectedMessage.getContent().split("city of ")[1]);
			data.add(MainGUI.currUser);
		}
		try {
			MainGUI.GUIclient.sendToServer(new Message(action, data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param event
	 */
	@FXML
	public void Approve(ActionEvent event) {
		sendApprovalOrDeclineMessage(true);
		getMessagesFromServer();
	}
	
	/**
	 * @param event
	 */
	@FXML
	public void Decline(ActionEvent event) {
		sendApprovalOrDeclineMessage(false);
		getMessagesFromServer();
	}

	/**
	 * @param event
	 */
	@FXML
	public void Refresh(ActionEvent event) {
		getMessagesFromServer();
	}

	private void getMessagesFromServer() {
		try {
			ArrayList<Object> data = new ArrayList<Object>();
			data.add(MainGUI.currUser.getUserName());
			data.add(MainGUI.currUser.getPermission().toString());
			MainGUI.GUIclient.sendToServer(new Message(Action.GET_INBOX_MESSAGES, data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param event
	 */
	@FXML
	public void Close(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map");
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
	}

	@Override
	public void handleMessageFromServer(Object message) {
		Message currMsg = (Message) message;
		switch (currMsg.getAction()) 
		{
			case GET_INBOX_MESSAGES:
				if ((Integer) currMsg.getData().get(0) == 0) {
					updateTable((List<InboxMessage>) currMsg.getData().get(1));
				}
				break;
			case HANDLE_PRICE_CHANGE_REQ:
				if ((Integer) currMsg.getData().get(0) == 0) {
					updateTable((List<InboxMessage>) currMsg.getData().get(1));
				}
				break;
			case HANDLE_NEW_VER_REQ:
				if ((Integer) currMsg.getData().get(0) == 0) {
					updateTable((List<InboxMessage>) currMsg.getData().get(1));
				}
				break;
			default:
				System.out.println("Error - got unsupported action - " + currMsg.getAction());
		}
	}
	
	@FXML
	void initialize() {
		lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
		getMessagesFromServer();
	}
	
	private void updateTable(List<InboxMessage> messages) {
		ObservableList<InboxMessage> observableList = FXCollections.observableArrayList(messages);
		
		tblInbox.setRowFactory(tv -> {
	        TableRow<InboxMessage> row = new TableRow<InboxMessage>() {
		    @Override
		    public void updateItem(InboxMessage item, boolean empty) {
		        super.updateItem(item, empty) ;
		        if (item == null) {
		            setStyle("");
		        } else if (item.getStatus() == Status.APPROVED) {
		            setStyle("-fx-background-color: lightgreen ;");
		        } else if (item.getStatus() == Status.DECLINED) {
		            setStyle("-fx-background-color: lightsalmon;");
		        } else {
		            setStyle("");
		        }
		    }};
        if (MainGUI.currUser.getPermission() != Permission.CLIENT) {
        	row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                	m_selectedMessage = row.getItem();
                    if (m_selectedMessage.getStatus() == Status.NEW) {
                		paneApproveDecline.setVisible(true);
                    } else {
                		paneApproveDecline.setVisible(false);
                    }
                }
            });
        }
        return row;
		});
		
		clmMessages.setCellValueFactory(new PropertyValueFactory<InboxMessage, String>("Content"));
		clmDate.setCellValueFactory(new PropertyValueFactory<InboxMessage, LocalDate>("ReceiveDate"));
		tblInbox.setItems(observableList);
	}
}
