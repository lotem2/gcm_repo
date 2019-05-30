package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class EditWindowController implements ControllerListener {

	GUIClient client;	
	
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane EditorWindow;
    @FXML
    private TitledPane accPaneCities;
    @FXML
    private TitledPane accPaneMap;
    @FXML
    private SplitMenuButton accessibiltyChoser;
    @FXML
    private MenuItem accessibleNo;
    @FXML
    private MenuItem accessibleYes;
    @FXML
    private Button btnBackToMain;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnSave;
    @FXML
    private SplitMenuButton cityChoser;
    @FXML
    private TableColumn<?, ?> col_estTime;
    @FXML
    private TableColumn<?, ?> col_order;
    @FXML
    private TableColumn<?, ?> col_siteDescription;
    @FXML
    private TableColumn<?, ?> col_siteName;
    @FXML
    private Accordion editingAccordion;
    @FXML
    private Label lbLocation;
    @FXML
    private Label lblAccessibilty;
    @FXML
    private Label lblCity;
    @FXML
    private Label lblCityChoose;
    @FXML
    private Label lblEditorTool;
    @FXML
    private Label lblMapChoose;
    @FXML
    private Label lblMapDescription;
    @FXML
    private Label lblMapName;
    @FXML
    private Label lblMapView;
    @FXML
    private Label lblRouteChoose;
    @FXML
    private Label lblRouteDescription;
    @FXML
    private Label lblRouteDetails;
    @FXML
    private Label lblSiteChoose;
    @FXML
    private Label lblSiteDescription;
    @FXML
    private Label lblSiteName;
    @FXML
    private Label lblWelcome;
    @FXML
    private MenuItem mapName1;
    @FXML
    private ImageView mapView;
    @FXML
    private SplitMenuButton mapsChoser;
    @FXML
    private MenuItem newCity;
    @FXML
    private MenuItem newMap;
    @FXML
    private AnchorPane paneCities;
    @FXML
    private AnchorPane paneMap;
    @FXML
    private SplitMenuButton routesChoser;
    @FXML
    private SplitMenuButton siteChoser;
    @FXML
    private MenuItem siteName1;
    @FXML
    private SplitMenuButton sitesChoser;
    @FXML
    private TableView<?> tableRouteDeatils;
    @FXML
    private TextField tfCityDescription;
    @FXML
    private TextField tfCityName;
    @FXML
    private TextField tfMapDescription;
    @FXML
    private TextField tfMapName;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfSiteDescription;
    @FXML
    private TextField tfSiteName;
    @FXML
    private TextField tfVersion;
    @FXML
    private TextField tfX;
    @FXML
    private TextField tfY;
    @FXML
    private TextField tfrouteDescription;
    
    @FXML
    void Browse(ActionEvent event) {
    }

    @FXML
    void Save(ActionEvent event) {
    }

    @FXML
    void backToMainGUI(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
    }

    @FXML
    void initialize() {
		lblWelcome.setText("Welcome " + MainGUI.currClient.getUserName() + "!");
    }
    
@Override
public void handleMessageFromServer(Object msg) {

}

}


