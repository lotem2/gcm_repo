package gui;


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

import javafx.scene.paint.Color;

import java.awt.Point;
import java.awt.geom.Point2D;

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
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
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
	String pathToMap = "";
	static ArrayList<String> citiesList;
	ArrayList<String> routeList;
	ArrayList<Site> allSitesInTheCity;
	City currentCity = null;
	Site newSite = null;
	
	

	
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

    public void setCurrentCity(City city) {
    	currentCity = city;
    }

    
    @FXML
    void SaveCity(ActionEvent event) {
		try {
		String selection = cityChoser.getSelectionModel().getSelectedItem();
		Message myMessage = null;
		if (selection.equals("Add New City"))
			myMessage = new Message(Action.ADD_CITY,tfCityName.getText(),
					tfCityDescription.getText(),0,Float.parseFloat(tfPrice.getText()));
		MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "One or more parameters are not correct.",
					"Error", JOptionPane.WARNING_MESSAGE);
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
			Map newMap;
			byte[] image;
			
			String selection = mapChoser.getSelectionModel().getSelectedItem();
			if (selection.equals("Add New Map")) {
				newMap = new Map(0, tfMapName.getText(), tfMapDescription.getText(), 
						cityChoser.getSelectionModel().getSelectedItem(), 
						null, null, false);
				newMap.setImage(pathToMap);
				myMessage = new Message(Action.ADD_MAP,newMap.getName(), 
						newMap.getCityName(), newMap.getDescription(), newMap.getImageAsByte());
			}
			else
			{
				Map current = getCurrentMap(currentCity.getMaps(), 
						mapChoser.getSelectionModel().getSelectedItem());
				
				if(!pathToMap.equals(""))
					image = current.setImage(pathToMap);
				else
					image = current.getImageAsByte();
				
				newMap = new Map(0, tfMapName.getText(), tfMapDescription.getText(), 
						cityChoser.getSelectionModel().getSelectedItem(), 
						null, image, current.getIsActive());
				myMessage = new Message(Action.EDIT_MAP,newMap.getName(), newMap.getCityName(),
						newMap.getDescription(), newMap.getImageAsByte());
			}
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
			String city = cityChoser.getSelectionModel().getSelectedItem();
			String map = mapChoser.getSelectionModel().getSelectedItem();
			String name = tfSiteName.getText();
			String description = new String(tfSiteDescription.getText());
			String accessibleString = accessibilityChoser.getSelectionModel().getSelectedItem();
			Boolean accessible = (accessibleString.equals("Yes"))? true:false;
			Float visitDuration = Float.parseFloat(tfEstimatedTime.getText());
			Point2D.Double locationPoint = new Point2D.Double(Double.parseDouble(tfX.getText()),
					Double.parseDouble(tfY.getText())); 
			String location = (tfX.getText()+","+tfY.getText());
			String classification = categoryChoser.getSelectionModel().getSelectedItem();
			newSite = new Site(name,city,setClassification(classification),description,accessible,visitDuration,locationPoint);
			if (selection.equals("Add New Site")) {
				myMessage = new Message(Action.ADD_SITE, getCurrentMap(currentCity.getMaps(), map),name,city,classification,description,accessible,visitDuration,location);
				siteChoser.getItems().add(name);
				getCurrentMap(currentCity.getMaps(), map).getSites().add(newSite);
			}
			else {
				myMessage = new Message(Action.EDIT_SITE,name,city,classification,description,accessible,visitDuration,location);
				updateMapsAndRoutes(getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), name), newSite);
			}
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Could not send message to server. Terminating client.",
					"Error", JOptionPane.WARNING_MESSAGE);
			MainGUI.GUIclient.quit();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "One or more parameters are not corrent.",
					"Error", JOptionPane.WARNING_MESSAGE);	
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
	   Platform.runLater(new Runnable() {
		
		@Override
		public void run() {
			paneMap.getChildren().removeIf(filter -> filter instanceof Circle);
			writeImage(map);
		}
	});
   }
   
   /**
    * Writes the locations of the current map's sites onto the image
    */
   void writeImage(Map map) {
	   // Go through each site and print it's location
	   mapView.setImage(map.getMapImage());
	   for (Site site : map.getSites()) {
		   Circle currentSiteCircle = new Circle(mapView.getBoundsInParent().getMinX() + site.getLocation().getX(),
				   mapView.getBoundsInParent().getMinY() + site.getLocation().getY(), 2);
		   currentSiteCircle.setFill(Color.BLUE);
		   currentSiteCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
		         @Override
		         public void handle(MouseEvent event) {
		        	if(currentSiteCircle.contains(event.getX(), event.getY()))
		        		setSiteInfo(site);
		        	Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							siteChoser.getSelectionModel().select(site.getName());							
						}
					});
		         }
		      });
		   paneMap.getChildren().add(currentSiteCircle);
	}
   }

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
		   currentCity = city;
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
    	});}

    void addNewMap() {
    	tfMapName.setDisable(false);
    	tfMapDescription.setDisable(false);
    	tfMapName.clear();
    	tfMapDescription.clear();
    	tfPrice.clear();
    	btnUpdateVersion.setDisable(true);
    	clearSiteParameters();
    	siteChoser.setValue(null);

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
			case ADD_CITY:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					City newCity = new City(tfCityName.getText(),tfCityDescription.getText(),
							new ArrayList<Map>(),new ArrayList<Route>(),0,Float.parseFloat(tfPrice.getText()));
					setCurrentCity(newCity);
					cityChoser.getItems().add(newCity.getName());
					cityChoser.getSelectionModel().select(newCity.getName());
					JOptionPane.showMessageDialog(null, "The city have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case ADD_MAP:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					Map newMap = new Map(0, tfMapName.getText(), tfMapDescription.getText(), 
							cityChoser.getSelectionModel().getSelectedItem(), 
							null, null, false);
					newMap.setImage(pathToMap);
					currentCity.getMaps().add(newMap);
					mapChoser.getItems().add(newMap.getName());
					mapChoser.getSelectionModel().select(newMap.getName());
					JOptionPane.showMessageDialog(null, "The map have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case ADD_SITE:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					if(getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), newSite.getName()) == null) {
						allSitesInTheCity.add(newSite);
						existingSiteToMapChoser.getItems().add(newSite.getName());
						sitesChoserForRoutes.getItems().add(newSite.getName());
					}
					
					getCurrentMap(currentCity.getMaps(), 
							mapChoser.getSelectionModel().getSelectedItem()).getSites().add(newSite);
					siteChoser.getItems().add(newSite.getName());
					siteChoser.getSelectionModel().select(newSite.getName());
					JOptionPane.showMessageDialog(null, "The site have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case ADD_ROUTE:
			{	
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					ArrayList<Site> sites = new ArrayList<Site>();
					String[] route_sites = getNewRouteFromTableView().split(",");
					for (String site : route_sites) {
						sites.add(getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), site));
					}
					Route newRoute = new Route(0, tfRouteName.getText(), cityChoser.getSelectionModel().getSelectedItem(),
							sites, tfrouteDescription.getText());
					currentCity.getRoutes().add(newRoute);

					JOptionPane.showMessageDialog(null, "The route have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case EDIT_MAP:
			{
				if ((Integer) currMsg.getData().get(0) == 0) {
					Map newMap = new Map(0, tfMapName.getText(), tfMapDescription.getText(), 
							cityChoser.getSelectionModel().getSelectedItem(), 
							null, null, false);
					newMap.setImage(pathToMap);
					currentCity.getMaps().set(currentCity.getMaps().indexOf
							(getCurrentMap(currentCity.getMaps(), tfMapName.getText())), newMap);
					JOptionPane.showMessageDialog(null, "The changes to the map have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case EDIT_SITE:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					updateMapsAndRoutes(getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), 
							newSite.getName()), newSite);
					JOptionPane.showMessageDialog(null, "The changes to the site have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case EDIT_ROUTE:
			{
				if ((Integer) currMsg.getData().get(0) == 0){
					ArrayList<Site> sites = new ArrayList<Site>();
					String[] route_sites = getNewRouteFromTableView().split(",");
					for (String site : route_sites) {
						sites.add(getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), site));
					}
					Route newRoute = new Route(0, tfRouteName.getText(), cityChoser.getSelectionModel().getSelectedItem(),
							sites, tfrouteDescription.getText());
					
					currentCity.getRoutes().set(currentCity.getRoutes().indexOf(
							getCurrentRoute(currentCity.getRoutes(), newRoute.getName())), newRoute);
					JOptionPane.showMessageDialog(null, "The changes to the route have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			}
			case REMOVE_SITE:
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
					getCurrentMap(currentCity.getMaps(), 
							mapChoser.getSelectionModel().getSelectedItem()).getSites().remove(
							getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), 
									siteChoser.getSelectionModel().getSelectedItem()));
					siteChoser.getItems().remove(siteChoser.getSelectionModel().getSelectedItem());
					JOptionPane.showMessageDialog(null, "The changes to the site have been added succesfully", "Notification",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			default:
			{
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
		    		  clearCityParameters();
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
						clearSiteParameters();
						setMapInfo(getCurrentMap(currentCity.getMaps(), currMapName));
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
						setRouteInfo(getCurrentRoute(currentCity.getRoutes(), currRouteName));
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
                pathToMap = file.getAbsolutePath(); //saving the URL for using later
                mapView.setImage(image);
            } 
            catch (IOException ex) 
            {
					JOptionPane.showMessageDialog(null, "Couldn't load the image", "Error",
							JOptionPane.WARNING_MESSAGE);
            }
        }
    };
    
   

	/**
	 *
	 *Button handler to choose an image for a map
	 * @param <onMouseClicked>
	 *
	 */    
     @FXML
    public void paint(MouseEvent event) {
         double x = event.getSceneX(); double y = event.getSceneY();
         clearSiteParameters();
        	 Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					tfX.setText(Double.toString(x).substring(0, Double.toString(x).indexOf(".") + 2));
					tfY.setText(Double.toString(y).substring(0, Double.toString(y).indexOf(".") + 2));

				}
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
    	String mapName = mapChoser.getSelectionModel().getSelectedItem();
    	String siteName = siteChoser.getSelectionModel().getSelectedItem();
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(getCurrentMap(currentCity.getMaps(), mapName));
		data.add(siteName);
		GUIClient.sendActionToServer(Action.REMOVE_SITE,data);
    }
    
    @FXML
    void AddAnExistingSiteToMap(ActionEvent event) {
    	String siteName = existingSiteToMapChoser.getSelectionModel().getSelectedItem();
    	if(getCurrentMap(currentCity.getMaps(), mapChoser.getSelectionModel().getSelectedItem()).getSites().contains(
    			getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), siteName)))
    		JOptionPane.showMessageDialog(null, "This site already exists in the map", "Error",
					JOptionPane.WARNING_MESSAGE);
    	else {
	    	newSite = getCurrentSite(currentCity.getMaps(), currentCity.getRoutes(), siteName);
			ArrayList<Object> data = new ArrayList<Object>();
			data.add(getCurrentMap(currentCity.getMaps(), mapChoser.getSelectionModel().getSelectedItem()));
			data.add(siteName);
			GUIClient.sendActionToServer(Action.ADD_SITE,data);
    	}
    }
	

    @FXML
    void UpdatePrice(ActionEvent event) {
    	try {
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
    	catch(Exception e) {
    		JOptionPane.showMessageDialog(null, "Please enter a valid price.", "Error",
					JOptionPane.WARNING_MESSAGE);
    	}
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

	/**
	 * Private method to get current displayed map
	 * @param city_maps - city's list of maps
	 * @param name - name of the displayed map
	 * @return {@link Map}
	 */	
	private Map getCurrentMap(ArrayList<Map> city_maps, String name) {
	    for(Map currMap : city_maps){
	        if(currMap.getName() != null && currMap.getName().equals(name))
	        	return currMap;
	    }
		return null;
	}
	
	/**
	 * Private method to get current displayed route
	 * @param city_routes - city's list of routes
	 * @param name - name of the displayed route
	 * @return {@link Route}
	 */
	private Route getCurrentRoute(ArrayList<Route> city_routes, String name) {
    	ArrayList<Route> routes = city_routes;
	    for(Route currRoute : routes){
	        if(currRoute.getName() != null && currRoute.getName().contains(name))
	        	return currRoute;
	    }
		return null;
	}
	
	/**
	 * Get instance of site according to his name
	 * @param city_maps - list of city's maps
	 * @param city_routes - list of city's routes
	 * @param name - site's name
	 * @return - {@link Site}
	 */
	private Site getCurrentSite(ArrayList<Map> city_maps, ArrayList<Route> city_routes, String name) {
		for (Map map : city_maps) {
		    for(Site currSite : map.getSites()){
		        if(currSite.getName() != null && currSite.getName().equals(name))
		        	return currSite;
		    }	
		}
		
		for (Route route : city_routes) {
		    for(Site currSite : route.getSites()){
		        if(currSite.getName() != null && currSite.getName().equals(name))
		        	return currSite;
		    }	
		}
		return null;
	}
	
	/**
	 * Private method to update the details of the site in all the occurrences of it in the city
	 * @param currentSite - the old site's instance
	 * @param newSite - the updated site's instance
	 */
	private void updateMapsAndRoutes(Site currentSite, Site newSite) {
		for (Map map : currentCity.getMaps()) {
		    if(map.getSites().indexOf(currentSite) != -1)
		    	map.getSites().set(map.getSites().indexOf(currentSite), newSite);
		}
		
		for (Route route : currentCity.getRoutes()) {
			if(route.getSites().indexOf(currentSite) != -1)
				route.getSites().set(route.getSites().indexOf(currentSite), newSite);
		}
	}
}
