package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import common.Services;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

/**
 * GUI for establishing the connection between server-clients
 *
 */
public class ServerController {
	// Variables
	private Server m_Server;
	private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@FXML
    private Button btnStopServer;

    @FXML
    private AnchorPane apServer;

    @FXML
    private TextField tfPort;

    @FXML
    private Button btnStartServer;

    @FXML
    private TextField tfIP;

    @FXML
    private TextArea taLog;

	@FXML
	void initialize() {
		taLog.setWrapText(true);
		taLog.setEditable(false);
		tfPort.setText("5555");
		tfIP.setText(getCurrentIP());
		tfIP.setEditable(false);
		btnStopServer.setDisable(true);
	}

    /**
     * Responsible to start the connection when "Start" button is clicked
     */
    @FXML
    void StartServer(ActionEvent event) {
    	// Start running the server
    	try {
    		taLog.clear();
    		Server.runServer(this, tfPort.getText());
    	}
    	catch(Exception e) {
    		setMsg(e.getMessage());
    	}
    }

    /**
     * Responsible to close the connection of the server when "Stop" button is clicked
     */
    @FXML
    void StopServer(ActionEvent event) {
    	// Stops the server using stopServer method
    	stopServer();
    }
    
    /**
     * Responsible to update the gui after the server established
     * @param msg - message to display in log
     */
    public void serverConnected(String msg) {
    	btnStartServer.setDisable(true);
    	btnStopServer.setDisable(false);
    	tfPort.setEditable(false);
    	setMsg(msg);
    	Platform.runLater(new Runnable() {

			@Override
			public void run() {
				tfPort.setText(msg.split(" ")[msg.split(" ").length - 1]);
			}
    	});
    }
    
    /**
     * Responsible to update the gui after the server stopped
     * @param msg - message to display in log
     */
    public void serverStopped(String msg) {
    	btnStartServer.setDisable(false);
    	btnStopServer.setDisable(true);
    	tfPort.setEditable(true);
    	setMsg(msg);
    }

    /**
     * Appends message to the log
     * @param msg - the message to be displayed
     */
    public void setMsg(String msg) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				taLog.appendText("[" + sdf.format(Calendar.getInstance().getTime()) + "] " + msg);
				taLog.appendText("\n");
			}
		});
    }

    /**
     * Sets instance of server
     * @param server - instance of the server
     */
    public void setServer(Server server) {
    	m_Server = server;
     }
    
    /**
     * Stops the server from running
     */
    public void stopServer() {
    	try {
			m_Server.close();
		} catch (IOException e) {
			setMsg(e.getMessage());
		}
     }
    
    /**
     * Private method to get the local host's ip address
     * @return String - represents local host's ip
     */
    private String getCurrentIP() {
    	try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }

}
