package gui;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import java.awt.Point;

import common.Action;
import common.Classification;
import common.Message;
import common.Permission;
import entity.City;
import entity.Client;
import entity.Employee;
import entity.Map;
import entity.Purchase;
import entity.Route;
import entity.Site;
import entity.User;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
//import sun.security.util.IOUtils;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class EditWindowController implements ControllerListener {

	GUIClient client;
	URL URLImage;
	static ArrayList<String> citiesList;
	ArrayList<String> routeList;
	ArrayList<Site> allSitesInTheCity;

	
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
    private Button btnAddAnExistingSiteToMap;
    @FXML
    private Button btnBackToMain;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnDeleteSite;
    @FXML
    private Button btnSaveCity;
    @FXML
    private Button btnSaveMap;
    @FXML
    private Button btnSaveRoute;
    @FXML
    private Button btnSaveSite;
    @FXML
    private Button btnUpdatePrice;
    @FXML
    private Button btnUpdateVersion;
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
    private ChoiceBox<String> existingSiteToMapChoser;
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
    private Label lblEstimatedTime;
    @FXML
    private Label lblExistingSiteToMapChoser;
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
    private TextField tfEstimatedTime;
    @FXML
    private TextField tfMapDescription;
    @FXML
    private TextField tfMapName;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfRouteName;
    @FXML
    private TextField tfSiteDescription;
    @FXML
    private TextField tfSiteName;
    @FXML
    private TextField tfX;
    @FXML
    private TextField tfY;
    @FXML
    private TextField tfrouteDescription;
    @FXML
    private Separator seperator;
    @FXML
	private AnchorPane AncPane;
    @FXML 
	private ProgressIndicator progressIndicator;
	private PauseTransition delayTimeout;

	/**
	 *
	 *gets the parameters of the map to and sends it to the server
	 *
	 *
	 */  
    
    @FXML
    void SaveCity(ActionEvent event) {
		try {
		String selection = cityChoser.getSelectionModel().getSelectedItem();
		Message myMessage = null;
		String cityName = tfCityName.getText();
		String cityDescription = tfCityDescription.getText();
		String price = tfPrice.getText();
		if (selection.equals("Add New City"))
			myMessage = new Message(Action.ADD_CITY,cityName,cityDescription,0,price);
		MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
	/**
	 *
	 *gets the parameters of the map to and sends it to the server
	 *
	 *
	 */  
    @FXML
    void SaveMap(ActionEvent event) {
		try {
			Message myMessage;
			String selection = mapChoser.getSelectionModel().getSelectedItem();
			String cityName = cityChoser.getSelectionModel().getSelectedItem();
			String mapName = tfMapName.getText();
			String mapDescription = tfMapDescription.getText();
			if (selection.equals("Add New Map"))
				myMessage = new Message(Action.ADD_MAP,mapName,cityName,mapDescription,URLImage);
			else
				myMessage = new Message(Action.EDIT_MAP,mapName,cityName,mapDescription,URLImage);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
	/**
	 *
	 *gets the parameters of the site to and sends it to the server
	 *
	 *
	 */  
    @FXML
    void SaveSite(ActionEvent event) {
		try {
			Message myMessage;
			String selection = siteChoser.getSelectionModel().getSelectedItem();
			String map = mapChoser.getSelectionModel().getSelectedItem();
			String name = tfSiteName.getText();
			String description = tfSiteDescription.getText();
			String accessibleString = accessibilityChoser.getSelectionModel().getSelectedItem();
			Boolean accessible = (accessibleString.equals("Yes"))? true:false;
			Float visitDuration = Float.parseFloat(tfEstimatedTime.getText());
			Point locationPoint = new Point(); 
			locationPoint.x=Integer.parseInt(tfX.getText());
			locationPoint.y=Integer.parseInt(tfY.getText());
			String location = (tfX.getText()+","+tfY.getText());
			String classification = categoryChoser.getSelectionModel().getSelectedItem();
			Site currSite = new Site(name,selection,setClassification(classification),description,accessible,visitDuration,locationPoint);
			if (selection.equals("Add New Site"))
				myMessage = new Message(Action.ADD_SITE,currSite,name,selection,setClassification(classification),description,accessible,visitDuration,location);
			else
				myMessage = new Message(Action.EDIT_SITE,name,selection,setClassification(classification),description,accessible,visitDuration,location);
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
	/**
	 *
	 *gets the parameters of the route to and sends it to the server
	 *
	 *
	 */  
    @FXML
    void SaveRoute(ActionEvent event) {
		try {
			Message myMessage;
			String selection = routesChoser.getSelectionModel().getSelectedItem();
			String routeName = tfRouteName.getText();
			String cityName = cityChoser.getSelectionModel().getSelectedItem();
			String description = tfrouteDescription.getText();
			if (selection.equals("Add New Route"))
				myMessage = new Message(Action.ADD_ROUTE,routeName,cityName,description,getNewRouteFromTableView());
			else
				myMessage = new Message(Action.EDIT_ROUTE,selection,cityName,description,getNewRouteFromTableView());
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
    }
    
	/**
	 *
	 *preparing the string of the route that is edited/added to be sent back to the server
	 *
	 *
	 */  
    String getNewRouteFromTableView() {
    	ObservableList<Site> currSitesList = tableRouteDeatils.getItems();
	    StringBuilder sb = new StringBuilder();
	    String routeListString= "";
	    for(Site currSite : currSitesList){
	        if(currSite.getName() != null)
	        	routeListString = sb.append(currSite.getName()).append(",").toString();
	    }
	    return routeListString;
    }
    
	/**
	 *
	 *loading the main window
	 *
	 *
	 */  

    @FXML
    void backToMainGUI(ActionEvent event) {
    	Platform.runLater(() -> {
    		MainGUI.MainStage.setTitle("Global City Map");
    	});
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
    }
	/**
	 *
	 *initializing the edit/show window according to the permission of the user
	 *
	 *
	 */  

    @FXML
    void initialize() {
    	Platform.runLater(() -> {
    		lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
        	btnBrowse.setOnAction(btnLoadEventListener);
        	setButtonsBooleanBinding();
        	setLists();
    	});
		Permission permission = MainGUI.currUser.getPermission();
		switch (permission) {
		case CLIENT:
	    	Platform.runLater(() -> {
			lblEditorTool.setText("Map Viewer");
			setShowWindow();		
			if (ClientProfileController.currentPurchase!=null)
			{
				ArrayList<Object> data = new ArrayList<Object>();
				String currCityName=ClientProfileController.currentPurchase.getCityName();
				data.add(MainGUI.currUser.getPermission());
				data.add(currCityName);
				GUIClient.sendActionToServer(Action.GET_CITY,data);
				cityChoser.setValue(currCityName);
				cityChoser.setDisable(true);
			}
			});
			break;
		case EDITOR:
			GUIClient.sendActionToServer(Action.GET_CITY_PRICE);
//	    	Platform.runLater(() -> {
//			tfPrice.setDisable(true);
//			btnDeleteSite.setDisable(false);
//			btnUpdatePrice.setVisible(false);
//			//btnUpdateVersion.setVisible(true);
//			});
			break;
		case MANAGING_EDITOR:
			GUIClient.sendActionToServer(Action.GET_CITY_PRICE);
//	    	Platform.runLater(() -> {
//			btnUpdatePrice.setDisable(false);
//			//btnUpdateVersion.setDisable(false);
//			});
			break;
		case CEO:
			GUIClient.sendActionToServer(Action.GET_CITY_PRICE);
//	    	Platform.runLater(() -> {
//			//btnUpdatePrice.setDisable(false);
//			//btnUpdateVersion.setDisable(false);
//			});
			break;
		default:
		}
    }

	/**
	 *
	 *method to start on all the ready lists: accessibilty and categories
	 *
	 *
	 */ 
    void setLists() {
        setCategoriesList();
        setAccessibleList();
    }
	/**
	 *
	 *method to start on all the binding while initializing.
	 *
	 *
	 */ 
    void setButtonsBooleanBinding() {
        setSaveCityBooleanBinding();
        setSaveMapBooleanBinding();
        setSaveSiteBooleanBinding();
        setSaveRouteBooleanBinding();
        setAddSiteBooleanBinding();
        //setUpdatePriceBooleanBinding();
    }
	/**
	 *
	 *method to put the site info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   
   void setSiteInfo(Site site) {
	   Platform.runLater(() -> {
		   tfSiteName.setText(site.getName());
		   tfSiteDescription.setText(site.getDescription());
		   tfEstimatedTime.setText(Float.toString(site.getVisitTime()));
		   tfX.setText(Double.toString(site.getLocation().getX()));
		   tfY.setText(Double.toString(site.getLocation().getY()));
		   categoryChoser.setValue(getClassification(site));
	   if(site.isAccessible())
		   accessibilityChoser.setValue("Yes");
	   else
		   accessibilityChoser.setValue("No");
	   });
   }
	/**
	 *
	 *method to set the enum to the Category it belongs to in the choice box
	 *the return value is the classification saved on the server
	 *
	 */  
   String getClassification(Site site) {
	   String currChoice = null;
	   switch(site.getClassification())
	   {
	   case HISTORIC:
		   currChoice = "Historic";
		   break;
	   case MUSEUM:
		   currChoice = "Museum";
		   break;
	   case HOTEL:
		   currChoice = "Hotel";
		   break;
	   case RESTAURANT:
		   currChoice = "Restaurant";
		   break;
	   case PUBLIC_INSTTUTION:
		   currChoice = "Public Institution";
		   break;
	   case PARK:
		   currChoice = "Park";
		   break;
	   case SHOP:
		   currChoice = "Shop";
		   break;
	   case CINEMA:
		   currChoice = "Cinema";
		   break;
	   case MALL:
		   currChoice = "Mall";
		   break;
	   case UNIVERSITY:
		   currChoice = "University";
		   break;
	   default:
		break;
	   }
	   return currChoice;
   }
	/**
	 *
	 *method to set the choice of the category to the enum that it belongs to.
	 *used to send it back to the server.
	 *
	 */  
   Classification setClassification(String classificationChoice) {
	   Classification classification = null;
	   switch(categoryChoser.getSelectionModel().getSelectedItem())
	   {
	   case "Historic":
		   classification = Classification.HISTORIC;
		   break;
	   case "Museum":
		   classification = Classification.MUSEUM;
		   break;
	   case "Hotel":
		   classification = Classification.HOTEL;
		   break;
	   case "Restaurant":
		   classification = Classification.RESTAURANT;
		   break;
	   case "Public Institution":
		   classification = Classification.PUBLIC_INSTTUTION;
		   break;
	   case "Park":
		   classification = Classification.PARK;
		   break;
	   case "Shop":
		   classification = Classification.SHOP;
		   break;
	   case "Cinema":
		   classification = Classification.CINEMA;
		   break;
	   case "Mall":
		   classification = Classification.MALL;
		   break;
	   case "University":
		   classification = Classification.UNIVERSITY;
		   break;
	   default:
		break;
	   }
	   return classification;
   }
   
	/**
	 *
	 *method to put the map info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setMapInfo(Map map) {
	   setSitesChoiceBox(map);
	   sitesChoiceBoxListener(map);
	   btnBrowse.setVisible(false);
	   Platform.runLater(() -> {
		   tfMapName.setText(map.getName());
		   tfMapDescription.setText(map.getDescription());
	   });
	   //loadImage(map.getImageAsByte());
   }
   
   void loadImage(byte[] imageBytesToAdd) {
//	   Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//	   ImageView image = (ImageView) findViewById(R.id.imageView1);
//
//	   image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
//	                   image.getHeight(), false));
   }
   
//   byte[] sendImage()
//   {
//   	   BitMap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//	   ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	   bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//	   byte[] byteArray = stream.toByteArray();
//	   return byteArray;
//   }
	/**
	 *
	 *method to put the route info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setRouteInfo(Route route) {
	   Platform.runLater(() -> {
		   tfRouteName.setText(route.getName());
		   tfrouteDescription.setText(route.getDescription());
		   ArrayList<Site> sites = route.getSites();
		   ObservableList<Site> currSitesList = FXCollections.observableArrayList(sites);
		   setTableViewForRouteSites(currSitesList);
	   });
   }
	/**
	 *
	 *method to put the city info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setCityInfo(City city) {
	   Platform.runLater(() -> {
		   tfCityName.setText(city.getName());
		   tfCityDescription.setText(city.getDescription());
		   tfPrice.setText(Float.toString(city.getPrice()));
		   setMapsChoiceBox(city);//loading the maps in the city to the choice box
		   setRoutesChoiceBox(city);//loading the routes in the city to the choice box
		   
	   });
	   ArrayList<Object> data = new ArrayList<Object>();
	   String cityName = city.getName();
	   Permission permission = MainGUI.currUser.getPermission();
	   data.add(permission);
	   data.add(cityName);
	   GUIClient.sendActionToServer(Action.GET_ALL_SITES_LIST,data);//requests the list of the sites in the city
   }
    

	/**
	 *
	 *method to clear the fields, in the case that the editor wishes to add a new city
	 *
	 *
	 */ 
    void clearCityParameters() {
    	Platform.runLater(() -> {
	    	tfCityName.clear();
	    	tfCityDescription.clear();
	    	tfrouteDescription.clear();
	    	tableRouteDeatils.getItems().clear();
	    	tfCityName.setDisable(false);
	    	tfCityDescription.setDisable(false);
	    	tfPrice.clear();
	    	tfPrice.setDisable(false);
	    	clearMapParameters();
	    	clearRouteParameters();
	    	mapChoser.setValue(null);
	    	routesChoser.setValue(null);
	    	btnUpdatePrice.setDisable(true);
    	});
    }

	/**
	 *
	 *method to clear the fields, in the case that the editor wishes to add a new map
	 *
	 *
	 */ 
    void clearMapParameters() {
    	Platform.runLater(() -> {
	    	tfMapName.setDisable(false);
	    	tfMapDescription.setDisable(false);
	    	tfMapName.clear();
	    	tfMapDescription.clear();
	    	tfPrice.clear();
	    	btnUpdateVersion.setDisable(true);
	    	clearSiteParameters();
	    	siteChoser.setValue(null);
    	});
    }

	/**
	 *
	 *method to clear the fields, in the case that the editor wishes to add a new Route
	 *
	 *
	 */ 
    void clearRouteParameters() {
    	Platform.runLater(() -> {
    		tfRouteName.clear();
    		tfrouteDescription.clear();
    		tableRouteDeatils.getItems().clear();
    		tfrouteDescription.setDisable(false);
    	});
    }
    
	/**
	 *
	 *method to clear the fields, in the case that the editor wishes to add a new site
	 *
	 *
	 */ 
    void clearSiteParameters() {
    	Platform.runLater(() -> {
	    	tfSiteName.clear();
	    	tfSiteDescription.clear();
	    	tfX.clear();
	    	tfY.clear();
	    	btnDeleteSite.setDisable(true);
	    	tfEstimatedTime.clear();
	    	categoryChoser.setValue(null);
	    	accessibilityChoser.setValue(null);
    	});
    }
	/**
	 *
	 *method to initialize the map View for the client
	 *
	 *
	 */ 
	void setShowWindow() 
	{
		Platform.runLater(() -> {	
			btnAddSiteToRoute.setVisible(false);
			btnBrowse.setVisible(false);
			btnSaveCity.setVisible(false);
			btnSaveMap.setVisible(false);
			btnSaveRoute.setVisible(false);
			btnSaveSite.setVisible(false);
			btnDeleteSite.setVisible(false);
			btnUpdatePrice.setVisible(false);
			btnUpdateVersion.setVisible(false);
			btnAddAnExistingSiteToMap.setVisible(false);
			sitesChoserForRoutes.setVisible(false);
			categoryChoser.setDisable(true);
			accessibilityChoser.setDisable(true);
			existingSiteToMapChoser.setVisible(false);
			tfPrice.setEditable(false);
			tfX.setVisible(false);
			tfY.setVisible(false);
			lbLocation.setVisible(false);
			lblExistingSiteToMapChoser.setVisible(false);
			tfCityDescription.setEditable(false);
			tfCityName.setEditable(false);
			tfMapDescription.setEditable(false);
			tfMapName.setEditable(false);
			tfSiteDescription.setEditable(false);
			tfSiteName.setEditable(false);
			tfrouteDescription.setEditable(false);
			tfRouteName.setEditable(false);
			tfEstimatedTime.setEditable(false);
			//seperator.setVisible(false);
		});
	}
	/**
	 *
	 *method to initialize the binding to change the city details
	 *
	 *
	 */ 
	void setSaveCityBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty());
		btnSaveCity.disableProperty().bind(booleanBind);
	}
	/**
	 *
	 *method to initialize the binding to update the price of the city
	 *
	 *
	 */ 
//	void setUpdatePriceBooleanBinding() 
//	{
//		BooleanBinding booleanBind;
//		booleanBind = (tfPrice.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty());
//		btnUpdatePrice.disableProperty().bind(booleanBind);
//	}
	/**
	 *
	 *method to initialize the binding to change the map details
	 *
	 *
	 */ 
	void setSaveMapBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfMapName.textProperty().isEmpty()).or(tfMapDescription.textProperty().isEmpty()).or(tfPrice.textProperty().isEmpty()).or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty());
		btnSaveMap.disableProperty().bind(booleanBind);
	}
	/**
	 *
	 *method to initialize the binding to change the site details
	 *
	 *
	 */ 
	void setSaveSiteBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = (tfSiteName.textProperty().isEmpty()).or(tfSiteDescription.textProperty().isEmpty()).or(tfX.textProperty().isEmpty()).or(tfY.textProperty().isEmpty());
		btnSaveSite.disableProperty().bind(booleanBind);
	}
	
	/**
	 *
	 *method to initialize the binding to change the route
	 *
	 *
	 */  
	void setSaveRouteBooleanBinding() 
	{
		BooleanBinding booleanBind;
		booleanBind = tfRouteName.textProperty().isEmpty().or(tfrouteDescription.textProperty().isEmpty().or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty()));
		btnSaveRoute.disableProperty().bind(booleanBind);
	}
	/**
	 *
	 *method to initialize the binding of the add sites to the route
	 *
	 *
	 */  
    void setAddSiteBooleanBinding()	{
		BooleanBinding booleanBind;
		booleanBind = (tfrouteDescription.textProperty().isEmpty().or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty()));
		btnAddSiteToRoute.disableProperty().bind(booleanBind);
	}
    
	/**
	 *
	 *method to initialize the choice box of the categories
	 *
	 *
	 */  
    void setCategoriesList()
    {
	 ArrayList<String> categories = new ArrayList<String>();
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
    
	/**
	 *
	 *method to initialize the choice box of the accessibility
	 *
	 *
	 */  
    void setAccessibleList()
    {
	 ArrayList<String> accessible = new ArrayList<String>();
	 accessible.add("Yes");
	 accessible.add("No");
     ObservableList<String> list = FXCollections.observableArrayList(accessible);
     Platform.runLater(() -> {
    	 accessibilityChoser.setItems(list);
     });
    }
	/**
	 *
	 *handle from server the responses for getting the cities list, the city object and the sites list
	 *
	 *
	 */  
	@Override
	public void handleMessageFromServer(Object msg) 
	{
		Message currMsg = (Message) msg;
		switch (currMsg.getAction()) 
		{
//			case GET_CITIES_LIST:
//			if ((Integer) currMsg.getData().get(0) == 0) 
//			{
//				    	ArrayList<String> citiesList = (ArrayList<String>) currMsg.getData().get(1);
//				    	citiesList.add(0, "Add New City");
//						ObservableList<String> currCitiesList = FXCollections.observableArrayList(citiesList);
//						cityChoser.setItems(currCitiesList);
//			}
//			else 
//				JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
//						JOptionPane.WARNING_MESSAGE);
			case GET_CITY_PRICE:
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					Platform.runLater(() -> {
						HashMap<String, Float> citiesAndPrices = new HashMap<String, Float>();
						citiesAndPrices = (HashMap<String, Float>) currMsg.getData().get(1);
						setCityChoiceBox(citiesAndPrices);	
					});
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			case GET_CITY:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					clearCityParameters();
			    	City currCity = (City) currMsg.getData().get(1);
			    	disableProressIndicator();
			    	Platform.runLater(() -> {
			    		setCityInfo(currCity);

			    		setUpdateVersions();
			    	});
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			}
			break;	
			case GET_ALL_SITES_LIST:
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					allSitesInTheCity = (ArrayList<Site>) currMsg.getData().get(1);
					ObservableList<Site> currSitesList = FXCollections.observableArrayList(allSitesInTheCity);
					Platform.runLater(() -> {
						setAddSitesToRouteAndMapsChoiceBox(allSitesInTheCity);
					});
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			case REQUEST_PRICE_CHANGE:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					JOptionPane.showMessageDialog(null, "Your request to update the price was sent!", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
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
	void setUpdateVersions() {
		if((MainGUI.currUser.getPermission().equals(Permission.CEO))||(MainGUI.currUser.getPermission().equals(Permission.MANAGING_EDITOR)))
		{
			Platform.runLater(() -> {
				tfPrice.setEditable(true);
				btnUpdateVersion.setDisable(false);
				btnUpdatePrice.setDisable(false);
			});
		}
	}

	/**
	 *
	 *method to listen to the city choice box
	 * @param citiesList 
	 * 
	 *
	 */  
	void cityChoiceBoxListener() 
	{
		cityChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  Platform.runLater(() -> {
		    		  String currCityName = (cityChoser.getItems().get((Integer) number2));
		    		  clearSiteParameters();
		    		  setCurrentCity(currCityName);
		    	  });
			}
		});
	}
		void mapsChoiceBoxListener(City currCity) 
		{
		mapChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  Platform.runLater(() -> {
		    	  String currMapName = (mapChoser.getItems().get((Integer) number2));
					if (currMapName.equals("Add New Map"))
					{
						clearMapParameters();
						btnBrowse.setVisible(true);
					}
					else
					{
				    	ArrayList<Map> maps = currCity.getMaps();
					    for(Map currMap : maps){
					        if(currMap.getName() != null && currMap.getName().contains(currMapName))
					        	Platform.runLater(() -> {
					        		setMapInfo(currMap);
					        	});
					    }
						
					}
		    	  });
			}
		});
		}
		
		void sitesChoiceBoxListener(Map currMap) 
		{
			siteChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  Platform.runLater(() -> {
		    	  String currSiteName = (siteChoser.getItems().get((Integer) number2));
					if (currSiteName.equals("Add New Site"))
					{
						
						clearSiteParameters();
					}
					else
					{
				    	ArrayList<Site> sites = currMap.getSites();
					    for(Site currSite : sites){
					        if(currSite.getName() != null && currSite.getName().contains(currSiteName))
					        	Platform.runLater(() -> {
					        		setSiteInfo(currSite);
					        	});
					    }
						
					}
		    	 });
			}
		});

	}
		void routesChoiceBoxListener(City currCity) 
		{
			routesChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
			{
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) 
		      {
		    	  Platform.runLater(() -> {
		    	  String currRouteName = (routesChoser.getItems().get((Integer) number2));
					if (currRouteName.equals("Add New Route"))
					{
						clearRouteParameters();
					}
					else
					{
				    	ArrayList<Route> routes = currCity.getRoutes();
					    for(Route currRoute : routes){
					        if(currRoute.getName() != null && currRoute.getName().contains(currRouteName))
					        	setRouteInfo(currRoute);
					    }
					}
		    	  });
		      }
		  });
	}

		/**
		 *
		 *method to add sites to the Route
		 * 
		 *
		 */  
	    @SuppressWarnings("unlikely-arg-type")
		@FXML
		void addSiteToRoute(ActionEvent event) 
		{
	    	String currSiteName = sitesChoserForRoutes.getSelectionModel().getSelectedItem();
	    	Site currSite = null;
	    	Iterator<Site> itr = allSitesInTheCity.iterator();
	    			      while(itr.hasNext()) 
	    			      {
					    	  int  currSiteIndex = allSitesInTheCity.indexOf(itr.next());
					    	  String currentSiteName = allSitesInTheCity.get(currSiteIndex).getName();
					    	  if (currentSiteName.equals(currSiteName))
					    	  {
						    	  currSite = allSitesInTheCity.get(currSiteIndex);
					    	  }
	    			      }
	    	if(!tableRouteDeatils.getItems().contains(currSite))
	    		  tableRouteDeatils.getItems().add(currSite);
	    	else
				JOptionPane.showMessageDialog(null, ("This site is already in the route."), "Error",
						JOptionPane.WARNING_MESSAGE);
	    		
		}

	/**
	 *
	 *method to initialize the choice box of the cities
	 * 
	 *
	 */  
	void setCurrentCity(String currCityName) {
				if (currCityName.equals("Add New City"))
				{
					clearCityParameters();
					btnBrowse.setVisible(true);
				}
				else
				{
					ArrayList<Object> data = new ArrayList<Object>();
					data.add(MainGUI.currUser.getPermission());
					data.add(currCityName);
					enableProressIndicator();
					GUIClient.sendActionToServer(Action.GET_CITY,data);
				}
			}

	void setCityChoiceBox(HashMap<String, Float> citiesAndPrices)
	{
			if (citiesAndPrices != null) {
				citiesList = new ArrayList<String>(citiesAndPrices.keySet());
			}
		if((MainGUI.currUser.getPermission()!= Permission.CLIENT))
			citiesList.add(0, "Add New City");
		ObservableList<String> currCitiesList = FXCollections.observableArrayList(citiesList);
		Platform.runLater(() -> {
			cityChoser.setItems(currCitiesList);
		});
		
		cityChoiceBoxListener(); 
	}
	/**
	 *
	 *method to initialize the choice box of the maps
	 * 
	 *
	 */  
	void setMapsChoiceBox(City currCity) 
	{
    	ArrayList<Map> maps = currCity.getMaps();
    	ArrayList<String> mapsList = new ArrayList<String>();
		if (maps != null) {
		      Iterator<Map> itr = maps.iterator();
		      while(itr.hasNext()) {
		    	  int  currMapIndex = maps.indexOf(itr.next());
		    	  mapsList.add(maps.get(currMapIndex).getName());
		      }
		}
		if((MainGUI.currUser.getPermission()!= Permission.CLIENT))
			mapsList.add(0, "Add New Map");
		ObservableList<String> currMapsList = FXCollections.observableArrayList(mapsList);
		Platform.runLater(() -> {
			mapChoser.setItems(currMapsList);
			mapsChoiceBoxListener(currCity);
		});
	}
	/**
	 *
	 *method to initialize the choice box of the routes
	 * 
	 *
	 */  
	void setRoutesChoiceBox(City currCity) 
	{
    	ArrayList<Route> routes = currCity.getRoutes();
    	ArrayList<String> routesList = new ArrayList<String>();
		if (routes != null) {
		      Iterator<Route> itr = routes.iterator();
		      while(itr.hasNext()) {
		    	  int  currRouteIndex = routes.indexOf(itr.next());
		    	  routesList.add(routes.get(currRouteIndex).getName());
		      }
		}
		if((MainGUI.currUser.getPermission()!= Permission.CLIENT))
			routesList.add(0, "Add New Route");
		ObservableList<String> currRoutesList = FXCollections.observableArrayList(routesList);
		Platform.runLater(() -> {
			routesChoser.setItems(currRoutesList);
			routesChoiceBoxListener(currCity);
		});
	}
	
	/**
	 *
	 *method to initialize the choice box of the sites
	 * 
	 *
	 */  
	void setSitesChoiceBox(Map currMap) 
	{
    	ArrayList<Site> sites = currMap.getSites();
    	ArrayList<String> sitesList = new ArrayList<String>();
		if (sites != null) {
		      Iterator<Site> itr = sites.iterator();
		      while(itr.hasNext()) {
		    	  int  currSiteIndex = sites.indexOf(itr.next());
		    	  sitesList.add(sites.get(currSiteIndex).getName());
		      }
		}
		if((MainGUI.currUser.getPermission()!= Permission.CLIENT))
			sitesList.add(0, "Add New Site");
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		Platform.runLater(() -> {
			siteChoser.setItems(currSitesList);
		});
	}
	
	/**
	 *
	 *method for setting up the Route List
	 *
	 */
	void setSitesOnRouteChoiceBox(Route currRoute) 
	{
    	ArrayList<Site> sites = currRoute.getSites();
    	ArrayList<String> sitesList = new ArrayList<String>();
		if (sites != null) {
		      Iterator<Site> itr = sites.iterator();
		      while(itr.hasNext()) {
		    	  int  currSiteIndex = sites.indexOf(itr.next());
		    	  sitesList.add(sites.get(currSiteIndex).getName());
		      }
		}
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		Platform.runLater(() -> {
			routesChoser.setItems(currSitesList);
		});
	}
	
	/**
	 *
	 *method for setting up the sites that can be added to the route
	 *
	 */
	void setAddSitesToRouteAndMapsChoiceBox(ArrayList<Site> allSitesList) 
	{
    	ArrayList<String> sitesList = new ArrayList<String>();
		if (allSitesList != null) {
		      Iterator<Site> itr = allSitesList.iterator();
		      while(itr.hasNext()) {
		    	  int  currSiteIndex = allSitesList.indexOf(itr.next());
		    	  sitesList.add(allSitesList.get(currSiteIndex).getName());
		      }
		}
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		Platform.runLater(() -> {
			sitesChoserForRoutes.setItems(currSitesList);
			existingSiteToMapChoser.setItems(currSitesList);
		});
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
            FileChooser.ExtensionFilter extFilterPNG = 
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG,extFilterPNG);
 
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                URLImage = file.toURI().toURL();//saving the URL for using later
                mapView.setImage(image);
            } 
            catch (IOException ex) 
            {
					JOptionPane.showMessageDialog(null, "Couldn't load the image", "Error",
							JOptionPane.WARNING_MESSAGE);
            }
        }
    };
    
    void setImageView()
    {
//        // Load an image in the background
//        String imageUrl = "https://docs.oracle.com/javafx/javafx/images/javafx-documentation.png";
//        Image image = new Image(imageUrl);
// 
//        // Create three WritableImage instances
//        // One Image will be a darker, one brighter, and one semi-transparent
//        WritableImage darkerImage = new WritableImage(mapView.get, height);
//        WritableImage semiTransparentImage = new WritableImage(width, height);      
//         
//        // Copy source pixels to the destinations
//        this.createImages(image, darkerImage, semiTransparentImage,width,height);
//         
//        // Create the ImageViews
//        ImageView imageView = new ImageView(image);
//        ImageView darkerView = new ImageView(darkerImage);
//        // Create the VBox for the Original Image
//        VBox originalViewBox = new VBox();
//        // Add ImageView to the VBox
//        originalViewBox.getChildren().addAll(mapView, new Text("Original"));
//         
//        // Create the VBox for the Darker Image
//        VBox darkerViewBox = new VBox();
//        // Add ImageView to the VBox
//        darkerViewBox.getChildren().addAll(darkerView, new Text("Darker"));
//         
//        // Create the HBox
//        HBox root = new HBox(10);
//        // Add VBoxes to the HBox
//        root.getChildren().addAll(originalViewBox);
    }
    

	/**
	 *
	 *Button handler to choose an image for a map
	 * @param <onMouseClicked>
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

	public void setTableViewForRouteSites(ObservableList<Site> currSitesList) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				//col_order.setCellValueFactory(new PropertyValueFactory<Site, String>(" "));
				col_siteName.setCellValueFactory(new PropertyValueFactory<Site, String>("name"));
				col_siteDescription.setCellValueFactory(new PropertyValueFactory<Site, String>("description"));
				col_estTime.setCellValueFactory(new PropertyValueFactory<Site, String>("visitTime"));

				tableRouteDeatils.setItems(currSitesList);
			}
		});
	}
    

    @FXML
    void DeleteSite(ActionEvent event) {
    	int siteIndex = siteChoser.getSelectionModel().getSelectedIndex();
    	String cityName = cityChoser.getSelectionModel().getSelectedItem();
    	String mapName = mapChoser.getSelectionModel().getSelectedItem();
    	String siteName = siteChoser.getSelectionModel().getSelectedItem();
    	siteChoser.getItems().remove(siteIndex);
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(cityName);
		data.add(mapName);
		data.add(siteName);
		GUIClient.sendActionToServer(Action.REMOVE_SITE,data);
    }
    
    @FXML
    void AddAnExistingSiteToMap(ActionEvent event) {
    	String cityName = cityChoser.getSelectionModel().getSelectedItem();
    	String mapName = mapChoser.getSelectionModel().getSelectedItem();
    	String mapDescription =tfMapDescription.getText();
    	String siteName = siteChoser.getSelectionModel().getSelectedItem();
    	siteChoser.getItems().add(siteName);
    	/*(int id, String mapname, String description, 
    			String cityname, ArrayList<Site> sites, byte[] image, boolean is_active)*/
    	//Map map= new Map(mapName,mapDescription,cityName,URLImage,1);
		ArrayList<Object> data = new ArrayList<Object>();
		//data.add(map);
		data.add(siteName);
		GUIClient.sendActionToServer(Action.ADD_SITE,data);
    }
	

    @FXML
    void UpdatePrice(ActionEvent event) {
		ArrayList<Object> data = new ArrayList<Object>();
		String cityName = String.valueOf(tfCityName.getText());
    	float newPrice = Float.valueOf(tfPrice.getText());
		if (!tfPrice.getText().isBlank() && newPrice>0 && !tfCityName.getText().isBlank())
		{
			data.add(cityName);
			data.add(newPrice);
			data.add(MainGUI.currUser);
			GUIClient.sendActionToServer(Action.REQUEST_PRICE_CHANGE,data);
		}
		else
			JOptionPane.showMessageDialog(null, ("Please enter a valid price."), "Error",
					JOptionPane.WARNING_MESSAGE);
    }

    @FXML
    void UpdateVersion(ActionEvent event) {
		ArrayList<Object> data = new ArrayList<Object>();
		String cityName = cityChoser.getSelectionModel().getSelectedItem();
		if (cityName!=null)
		{
		data.add(cityName);
		data.add(MainGUI.currUser);
		GUIClient.sendActionToServer(Action.REQUEST_NEW_VER_APPROVAL,data);
		}
		else
			JOptionPane.showMessageDialog(null, ("Please choose a city to update"), "Error",
					JOptionPane.WARNING_MESSAGE);
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
