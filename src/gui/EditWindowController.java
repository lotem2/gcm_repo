package gui;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import common.Action;
import common.Message;
import common.Permission;
import entity.City;
import entity.Client;
import entity.Employee;
import entity.Map;
import entity.Route;
import entity.Site;
import entity.User;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;


public class EditWindowController implements ControllerListener {

	GUIClient client;
	URL URLImage;
	
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
    private ChoiceBox<String> accessibilityChoser;
    @FXML
    private Button btnAddSiteToRoute;
    @FXML
    private Button btnBackToMain;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnSaveCity;
    @FXML
    private Button btnSaveMap;
    @FXML
    private Button btnSaveRoute;
    @FXML
    private Button btnSaveSite;
    @FXML
    private ChoiceBox<String> categoryChoser;
    @FXML
    private CheckBox checkBoxShowOnMap;
    @FXML
    private ChoiceBox<String> cityChoser;
    @FXML
    private TableColumn<Site, String> col_estTime;
    @FXML
    private TableColumn<Site, String> col_order;
    @FXML
    private TableColumn<Site, String> col_siteDescription;
    @FXML
    private TableColumn<Site, String> col_siteName;
    @FXML
    private Accordion editingAccordion;
    @FXML
    private Label lbLocation;
    @FXML
    private Label lblAccessibilty;
    @FXML
    private Label lblCategory;
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
    private ChoiceBox<String> mapChoser;
    @FXML
    private ImageView mapView;
    @FXML
    private AnchorPane paneCities;
    @FXML
    private AnchorPane paneMap;
    @FXML
    private ChoiceBox<String> routesChoser;
    @FXML
    private ChoiceBox<String> siteChoser;
    @FXML
    private ChoiceBox<String> sitesChoserForRoutes;
    @FXML
    private TableView<Site> tableRouteDeatils;
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
    void SaveCity(ActionEvent event) {
		try {
		String selection = cityChoser.getSelectionModel().getSelectedItem();
		Message myMessage;
		String cityName = tfCityName.getText();
		String cityDescription = tfCityDescription.getText();
		if (selection.equals("Add New City"))
			myMessage = new Message(Action.ADD_CITY,cityName,cityDescription);
		else
			myMessage = new Message(Action.EDIT_CITY,cityName,cityDescription);
		MainGUI.GUIclient.sendToServer(myMessage);
	} catch (IOException e) {
		JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
				"Error", JOptionPane.WARNING_MESSAGE);
		MainGUI.GUIclient.quit();
	}
    }
    
    @FXML
    void SaveMap(ActionEvent event) {
		try {
			Message myMessage;
			String selection = mapChoser.getSelectionModel().getSelectedItem();
			//String mapName = tfMapName.getText();
			String mapDescription = tfMapDescription.getText();
			if (selection.equals("Add New Map"))
				myMessage = new Message(Action.ADD_MAP,/*mapName,*/mapDescription,URLImage);
			else
				myMessage = new Message(Action.EDIT_MAP,/*mapName,*/mapDescription,URLImage);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
    
    @FXML
    void SaveSite(ActionEvent event) {
		try {
			Message myMessage;
			String selection = siteChoser.getSelectionModel().getSelectedItem();
			String siteName = tfSiteName.getText();
			String siteDescription = tfSiteDescription.getText();
			String x = tfX.getText();
			String y = tfY.getText();
			if (selection.equals("Add New Site"))
				myMessage = new Message(Action.ADD_SITE,siteName,siteDescription,x,y);
			else
				myMessage = new Message(Action.EDIT_SITE,siteName,siteDescription,x,y);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
    
    @FXML
    void SaveRoute(ActionEvent event) {
		try {
			Message myMessage;
			String selection = routesChoser.getSelectionModel().getSelectedItem();
			//String routeName = tfRouteName.getText();
			String routeDescription = tfCityDescription.getText();
			if (selection.equals("Add New Route"))
				myMessage = new Message(Action.ADD_ROUTE,/*routeName,*/routeDescription);
			else
				myMessage = new Message(Action.EDIT_ROUTE,/*routeName,*/routeDescription);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
    

    @FXML
    void backToMainGUI(ActionEvent event) {
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
    }

    @FXML
    void initialize() {
		lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
        btnBrowse.setOnAction(btnLoadEventListener);
        setSaveCityBooleanBinding();
        setSaveMapBooleanBinding();
        setSaveSiteBooleanBinding();
        setSaveRouteBooleanBinding();
        setAddSiteBooleanBinding();
        setCategoriesList();
        setAccessibleList();
		Message myMessage = new Message(Action.GET_CITIES_LIST);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
		Permission permission = MainGUI.currClient.getPermission();
		switch (permission) {
		case CLIENT:
			lblEditorTool.setText("Map Viewer");
			setShowWindow();
			break;
		case EDITOR:
			tfPrice.setDisable(true);
			tfVersion.setDisable(true);
			break;
		case MANAGING_EDITOR:

			break;
		case CEO:

			break;
		default:

		}
    }
    
    @FXML
    void addNewCity() {
    	tfCityName.clear();
    	tfCityDescription.clear();
    	tfrouteDescription.clear();
    	tableRouteDeatils.getItems().clear();
    }

    @FXML
    void addNewMap() {
    	tfMapName.clear();
    	tfMapDescription.clear();
    	tfVersion.clear();
    	tfPrice.clear();
    	tfX.clear();
    	tfY.clear();
  	
    }

    @FXML
    void addNewRoute() {
    	tfrouteDescription.clear();
    	tableRouteDeatils.getItems().clear();
    }
    
    @FXML
    void addNewSite() {
    	tfSiteName.clear();
    	tfSiteDescription.clear();
    	tfX.clear();
    	tfY.clear();
    }
    
	void setShowWindow() 
	{		
		btnAddSiteToRoute.setVisible(false);
		btnBrowse.setVisible(false);
		btnSaveCity.setVisible(false);
		btnSaveMap.setVisible(false);
		btnSaveRoute.setVisible(false);
		btnSaveSite.setVisible(false);
		sitesChoserForRoutes.setVisible(false);
		categoryChoser.setDisable(true);
		accessibilityChoser.setDisable(true);
		tfPrice.setDisable(true);
		tfVersion.setDisable(true);
		tfX.setVisible(false);
		tfY.setVisible(false);
		lbLocation.setVisible(false);
		tfCityDescription.setDisable(true);
		tfCityName.setDisable(true);
		tfMapDescription.setDisable(true);
		tfMapName.setDisable(true);
		tfSiteDescription.setDisable(true);
		tfSiteName.setDisable(true);
		tfrouteDescription.setDisable(true);
	}
	
	void setSaveCityBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty());
		btnSaveCity.disableProperty().bind(booleanBind);
	}
	void setSaveMapBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfMapName.textProperty().isEmpty()).or(tfMapDescription.textProperty().isEmpty()).or(tfVersion.textProperty().isEmpty()).or(tfPrice.textProperty().isEmpty()).or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty());
		btnSaveMap.disableProperty().bind(booleanBind);
	}
	void setSaveSiteBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfSiteName.textProperty().isEmpty()).or(tfSiteDescription.textProperty().isEmpty()).or(tfX.textProperty().isEmpty()).or(tfY.textProperty().isEmpty());
		btnSaveSite.disableProperty().bind(booleanBind);
	}
	void setSaveRouteBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfrouteDescription.textProperty().isEmpty().or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty()));
		btnSaveRoute.disableProperty().bind(booleanBind);
	}
    void setAddSiteBooleanBinding()	{
		BooleanBinding booleanBind;
		booleanBind = (tfrouteDescription.textProperty().isEmpty().or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty()));
		btnSaveRoute.disableProperty().bind(booleanBind);
	}
      
    void setCategoriesList()
    {
	 ArrayList<String> categories = new ArrayList<String>();
	 categories.add("Add New Category");
	 categories.add("Cinema");
	 categories.add("Historic Place");
	 categories.add("Hotel");
	 categories.add("Mall");
	 categories.add("Museum");
	 categories.add("Public Institution");
	 categories.add("Restaurant");
	 categories.add("Shop");
	 categories.add("University");
     ObservableList<String> list = FXCollections.observableArrayList(categories);
     categoryChoser.setItems(list);
    }
    
    void setAccessibleList()
    {
	 ArrayList<String> accessible = new ArrayList<String>();
	 accessible.add("Yes");
	 accessible.add("No");
     ObservableList<String> list = FXCollections.observableArrayList(accessible);
     accessibilityChoser.setItems(list);
    }
   
	@Override
	public void handleMessageFromServer(Object msg) 
	{
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) 
		{
			case GET_CITIES_LIST:
			{
			    	ArrayList<String> cities = (ArrayList<String>) currMsg.getData().get(1);
			    	cities.add(0, "Add New City");
					ObservableList<String> currCitiesList = FXCollections.observableArrayList(cities);
					cityChoser.setItems(currCitiesList);
			}
			break;
			case GET_CITY:
			{
			    	City currCity = (City) currMsg.getData().get(1);
			    	setMapsChoiceBox(currCity);
			    	setRoutesChoiceBox(currCity);
			}
			break;
			default:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
					JOptionPane.showMessageDialog(null, "All the changes have been saved succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	void setAllChoiceBoxes(ArrayList<String> cities) 
	{
		cityChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
		{
			// if the item of the list is changed
			public void changed(ObservableValue ov,Number number1, Number number2) 
			{
				String currCityName = cityChoser.getValue();
				if (currCityName=="Add New City")
					addNewCity();
				else
				{
					Message myMessage = new Message(Action.GET_CITY,currCityName);					
					try {
						MainGUI.GUIclient.sendToServer(myMessage);
					}
					catch (Exception e) 
					{
						JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message to the Server", "Error",
								JOptionPane.WARNING_MESSAGE);
					}
					
				}
			
			}
		});
	}
	
	void setMapsChoiceBox(City currCity) 
	{
    	ArrayList<Map> maps = currCity.getMaps();
    	ArrayList<String> mapsList = new ArrayList<String>();
		if (maps != null) {
		      Iterator<Map> itr = maps.iterator();
		      mapsList.add("Add New Map");
		      while(itr.hasNext()) {
		    	  mapsList.add(maps.get(0).getDescription());
		      }
		}
		ObservableList<String> currMapsList = FXCollections.observableArrayList(mapsList);
		mapChoser.setItems(currMapsList);
	
	}
	
	void setRoutesChoiceBox(City currCity) 
	{
    	ArrayList<Route> routes = currCity.getRoutes();
    	ArrayList<String> routesList = new ArrayList<String>();
		if (routes != null) {
		      Iterator<Route> itr = routes.iterator();
		      routesList.add("Add New Route");
		      while(itr.hasNext()) {
		    	  routesList.add(routes.get(0).getDescription());
		      }
		}
		ObservableList<String> currRoutesList = FXCollections.observableArrayList(routesList);
		routesChoser.setItems(currRoutesList);
	}
	
	void setSitesChoiceBox(Map currMap) 
	{
    	ArrayList<Site> sites = currMap.getSites();
    	ArrayList<String> sitesList = new ArrayList<String>();
		if (sites != null) {
		      Iterator<Site> itr = sites.iterator();
		      sitesList.add("Add New Site");
		      while(itr.hasNext()) {
		    	  sitesList.add(sites.get(0).getName());
		      }
		}
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		siteChoser.setItems(currSitesList);
	}
	
	void setSitesOnRouteChoiceBox(Route currRoute) 
	{
    	ArrayList<Site> sites = currRoute.getSites();
    	ArrayList<String> sitesList = new ArrayList<String>();
		if (sites != null) {
		      Iterator<Site> itr = sites.iterator();
		      while(itr.hasNext()) {
		    	  sitesList.add(sites.get(0).getName());
		      }
		}
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		sitesChoserForRoutes.setItems(currSitesList);
	}
	
	
	
	/**
	 *
	 *Button handler to choose an image for a map
	 *
	 */
    EventHandler<ActionEvent> btnLoadEventListener
    = new EventHandler<ActionEvent>(){
  
        @Override
        public void handle(ActionEvent t) {
            FileChooser fileChooser = new FileChooser();
              
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = 
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg = 
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG = 
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG);
 
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                URLImage = file.toURI().toURL();//saving the URL for using later
                mapView.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(EditWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

	/**
	 *
	 *Button handler to choose an image for a map
	 *
	 */    
    @FXML
    void paint(MouseEvent event) {
    	mapView.setOnMouseClicked(e -> 
	    {
	        //System.out.println("["+e.getX()+", "+e.getY()+"]")
	        Circle c = new Circle(e.getX(), e.getY(), 5, javafx.scene.paint.Color.RED);
	        paneMap.getChildren().add(c);
	        mapView.setOnMouseClicked(null);
	    });
    }
    
}


