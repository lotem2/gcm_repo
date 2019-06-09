package gui;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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

import common.Action;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
//import sun.security.util.IOUtils;


public class EditWindowController implements ControllerListener {

	GUIClient client;
	URL URLImage;
	static ArrayList<String> citiesList;
	
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
    private Label lblEstimatedTime;
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
			String name = tfSiteName.getText();
			String description = tfSiteDescription.getText();
			String acessible = accessibilityChoser.getSelectionModel().getSelectedItem();
			String x = tfX.getText();
			String y = tfY.getText();
			String visitDuration = tfEstimatedTime.getText();
			String location = (x+","+y);
			String classification = categoryChoser.getSelectionModel().getSelectedItem();
			if (selection.equals("Add New Site"))
				myMessage = new Message(Action.ADD_SITE,name,classification,description,x,y);
			else
				myMessage = new Message(Action.EDIT_SITE,name,classification,description,x,y);
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
    void addSiteToRoute(ActionEvent event) {
//    	sitesChoserForRoutes.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() 
//		{
//			// if the item of the list is changed
//			public void changed(ObservableValue ov,Number number1, Number number2) 
//			{
//				String currSiteName = sitesChoserForRoutes.getValue();
//				
//				
//			}
//		});
//    	Site newSite = new Site();
//    	tableRouteDeatils.getItems().add(newSite);
    }

    @FXML
    void backToMainGUI(ActionEvent event) {
		MainGUI.MainStage.setTitle("Global City Map");
		MainGUI.openScene(MainGUI.SceneType.MAIN_GUI);
    }

    @FXML
    void initialize() {
    	//Platform.runLater(() -> {
		lblWelcome.setText("Welcome " + MainGUI.currUser.getUserName() + "!");
    	//});
        btnBrowse.setOnAction(btnLoadEventListener);
        setButtonsBooleanBinding();
        //getCitiesFromServer(Action.GET_CITY_PRICE);
		//ArrayList<Object> data = new ArrayList<Object>();
		//data.add(0);
		//GUIClient.sendActionToServer(Action.GET_CITY_PRICE,data);
		//GUIClient.sendActionToServer(Action.GET_ALL_SITES_LIST);
        //setAllChoiceBoxes();
        //setCityChoiceBox();
        setLists();
		Permission permission = MainGUI.currUser.getPermission();
		switch (permission) {
		case CLIENT:
			lblEditorTool.setText("Map Viewer");
			setShowWindow();
			break;
		case EDITOR:
			tfPrice.setDisable(true);
			break;
		case MANAGING_EDITOR:

			break;
		case CEO:

			break;
		default:

		}
    }
    void getCitiesFromServer(Action action) {
		Message myMessage = new Message(action);
		try {
			MainGUI.GUIclient.sendToServer(myMessage);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString() + "Couldn't send message to Server", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
    }

	/**
	 *
	 *method to start on all the ready lists: accesssibilty and categories
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
    }
	/**
	 *
	 *method to put the site info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setSiteInfo(Site site) {
	   tfSiteName.setText(site.getName());
	   tfSiteDescription.setText(site.getDescription());
	   tfEstimatedTime.setText(Float.toString(site.getVisitTime()));
	   tfX.setText(Double.toString(site.getLocation().getX()));
	   tfY.setText(Double.toString(site.getLocation().getY()));
	   categoryChoser.setValue(site.getClassification().toString());
	   if(site.isAccessible())
		   accessibilityChoser.setValue("Yes");
	   else
		   accessibilityChoser.setValue("No");
   }
	/**
	 *
	 *method to put the map info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setMapInfo(Map map) {
	   setSitesChoiceBox(map);
	   tfMapName.setText(map.getName());
	   tfMapDescription.setText(map.getDescription());
	   //loadImage(map.getImageAsByte());
	   
   }
   
//   void loadImage(byte[] imageBytesToAdd) {
//	   	   InputStream is = null;
//	   try {
//	     is = URLImage.openStream ();
//	     byte[] imageBytes = IOUtils.toByteArray(is);
//	   }
//	   catch (IOException e) {
//	     System.err.printf ("Failed while reading bytes from %s: %s", URLImage.toExternalForm(), e.getMessage());
//	     e.printStackTrace ();
//	     // Perform any other exception handling that's appropriate.
//	   }
////	   finally {
////	   if(is != null)	{ is.close(); }
////	   }
//   }
   
	/**
	 *
	 *method to put the route info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setRouteInfo(Route route) {
	   //tfRouteName.setText(route.getName());
	   tfrouteDescription.setText(route.getDescription());
   }
	/**
	 *
	 *method to put the city info in the fields, gotten from the server, for the editor
	 *
	 *
	 */ 
   void setCityInfo(City city) {
	   tfCityName.setText(city.getName());
	   tfCityDescription.setText(city.getDescription());
	   tfPrice.setText(Float.toString(city.getPrice()));
   }
    

	/**
	 *
	 *method to initialize the fields, in the case that the editor wishes to add a new city
	 *
	 *
	 */ 
    void addNewCity() {
    	tfCityName.clear();
    	tfCityDescription.clear();
    	tfrouteDescription.clear();
    	tableRouteDeatils.getItems().clear();
    	tfCityName.setDisable(false);
    	tfCityDescription.setDisable(false);
    }

	/**
	 *
	 *method to initialize the fields, in the case that the editor wishes to add a new map
	 *
	 *
	 */ 
    void addNewMap() {
    	tfMapName.clear();
    	tfMapDescription.clear();
    	tfPrice.clear();
    	tfX.clear();
    	tfY.clear();
  	
    }

	/**
	 *
	 *method to initialize the fields, in the case that the editor wishes to add a new Route
	 *
	 *
	 */ 
    void addNewRoute() {
    	tfrouteDescription.clear();
    	tableRouteDeatils.getItems().clear();
    }
    
	/**
	 *
	 *method to initialize the fields, in the case that the editor wishes to add a new site
	 *
	 *
	 */ 
    void addNewSite() {
    	tfSiteName.clear();
    	tfSiteDescription.clear();
    	tfX.clear();
    	tfY.clear();
    }
	/**
	 *
	 *method to initialize the map View for the client
	 *
	 *
	 */ 
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
		booleanBind = (tfrouteDescription.textProperty().isEmpty().or(tfCityName.textProperty().isEmpty()).or(tfCityDescription.textProperty().isEmpty()));
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
     accessibilityChoser.setItems(list);
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
					HashMap<String, Float> citiesAndPrices = new HashMap<String, Float>();
					citiesAndPrices = (HashMap<String, Float>) currMsg.getData().get(1);
					//ArrayList<String> citiesList = null;
					if (citiesAndPrices != null) {
						citiesList = new ArrayList<String>(citiesAndPrices.keySet());
					}
		    	citiesList.add(0, "Add New City");
				ObservableList<String> currCitiesList = FXCollections.observableArrayList(citiesList);
				cityChoser.setItems(currCitiesList);
				//setAllChoiceBoxes(citiesList);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			break;
			case GET_CITY:
			{
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
			    	City currCity = (City) currMsg.getData().get(1);
			    	setCityInfo(currCity);
			    	setMapsChoiceBox(currCity);
			    	setRoutesChoiceBox(currCity);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
			}
			break;
			case GET_ALL_SITES_LIST:
				if ((Integer) currMsg.getData().get(0) == 0) 
				{
			    	ArrayList<Site> sites = (ArrayList<Site>) currMsg.getData().get(1);
					ObservableList<Site> currSitesList = FXCollections.observableArrayList(sites);
					setTableViewForRouteSites(currSitesList);
				}
				else 
					JOptionPane.showMessageDialog(null, (currMsg.getData().get(1)).toString(), "Error",
							JOptionPane.WARNING_MESSAGE);
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
	
	/**
	 *
	 *method to listen to the city choice box
	 * 
	 *
	 */  
	void setAllChoiceBoxes() 
	{
		cityChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
		    	  String currCityName = (cityChoser.getItems().get((Integer) number2));
		    	  System.out.println(currCityName);
		    	  setCityChoiceBox(currCityName);

			}
		});
		
//		mapChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//		      @Override
//		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//		    	  String currMapName = (mapChoser.getItems().get((Integer) number2));
//					if (currMapName.equals("Add New Map"))
//					{
//						System.out.print(currMapName);
//						addNewMap();
//					}
//					else
//					{
//						//setMapInfo(currMap);
//					}
//
//			}
//		});
//		siteChoser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//		      @Override
//		      public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//		    	  String currSiteName = (siteChoser.getItems().get((Integer) number2));
//			}
//		});

	}
	/**
	 *
	 *method to initialize the choice box of the cities
	 * 
	 *
	 */  
	void setCityChoiceBox(String currCityName) {
//		Platform.runLater(new Runnable() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void run() {
				//String currCityName = cityChoser.getValue();
				if (currCityName.equals("Add New City"))
				{
					System.out.print(currCityName);
					addNewCity();
				}
				else
				{
					ArrayList<Object> data = new ArrayList<Object>();
					data.add(currCityName);
					Message myMessage = new Message(Action.GET_CITY,data);					
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
//			});
		//}
	
	
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
		    	  mapsList.add(maps.get(0).getName());
		      }
		}
		mapsList.add(0, "Add New Map");
		ObservableList<String> currMapsList = FXCollections.observableArrayList(mapsList);
		mapChoser.setItems(currMapsList);
	
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
		    	  routesList.add(routes.get(0).getDescription());
		      }
		}
		routesList.add(0, "Add New Route");
		ObservableList<String> currRoutesList = FXCollections.observableArrayList(routesList);
		routesChoser.setItems(currRoutesList);
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
		    	  sitesList.add(sites.get(0).getName());
		      }
		}
		sitesList.add(0, "Add New Site");
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		siteChoser.setItems(currSitesList);
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
		    	  sitesList.add(sites.get(0).getName());
		      }
		}
		ObservableList<String> currSitesList = FXCollections.observableArrayList(sitesList);
		routesChoser.setItems(currSitesList);
	}
	
	/**
	 *
	 *method for setting up the sites that can be added to the route
	 *
	 */
	void setAddSitesToRouteChoiceBox(ArrayList<Site> allSitesList) 
	{
    	ArrayList<String> sitesList = new ArrayList<String>();
		if (allSitesList != null) {
		      Iterator<Site> itr = allSitesList.iterator();
		      while(itr.hasNext()) {
		    	  sitesList.add(allSitesList.get(0).getName());
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

	/**
	 *
	 *Button handler to choose an image for a map
	 * @param <onMouseClicked>
	 *
	 */    
 //    @FXML
//    void paint(MouseEvent event) {
//    	mapView.setOnMouseClicked(e -> 
//	    {
//	        //System.out.println("["+e.getX()+", "+e.getY()+"]")
//	        Circle c = new Circle(e.getX(), e.getY(), 5, javafx.scene.paint.Color.RED);
//	        paneMap.getChildren().add(c);
//	        mapView.setOnMouseClicked(null);
//	    });
//    }
//<onMouseClicked>
//  void paint(MouseEvent event) {
//
//  }
    
   
    
    
	public void setTableViewForRouteSites(ObservableList<Site> currSitesList) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				//ObservableList<Site> sitesList = FXCollections.observableArrayList();
				col_order.setCellValueFactory(new PropertyValueFactory<Site, String>(" "));
				col_siteName.setCellValueFactory(new PropertyValueFactory<Site, String>("name"));
				col_siteDescription.setCellValueFactory(new PropertyValueFactory<Site, String>("description"));
				col_estTime.setCellValueFactory(new PropertyValueFactory<Site, String>("visitDuration"));

				//tableRouteDeatils.getColumns().addAll(col_order, col_siteName, col_siteDescription,col_estTime);
				tableRouteDeatils.setItems(currSitesList);
			}
		});
	}
    
}


