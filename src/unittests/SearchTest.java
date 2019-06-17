package unittests;

import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TableViewMatchers;

import common.Action;
import common.Message;
import gui.GUIClient;
import gui.MainGUI;
import gui.MainGUIController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchTest extends ApplicationTest {

    @Mock GUIClient mockGUIclient;
	private MainGUIController controller;
    
	/**
	 *	Generates the view, replaced GUIclient with a mock and saves the tested
	 *  MainGUIController instance.
	 */
	@Override
	public void start (Stage stage) throws Exception {
	  MainGUI.GUIclient = mockGUIclient;
	  
	  FXMLLoader loader = new FXMLLoader(
			  MainGUI.class.getResource("/MainGUIScene.fxml"));
	  Parent mainNode = loader.load();
      controller = loader.getController();

	  stage.setScene(new Scene(mainNode));
	  stage.show();
	  stage.toFront();
	}
	
	@Before
	public void setUp () throws Exception {
	}

	@After
	public void tearDown () throws Exception {
	  FxToolkit.hideStage();
	  release(new KeyCode[]{});
	  release(new MouseButton[]{});
	}
	
	@Test
    public void testSearchSendMessageToServer_inputCity () throws IOException {
      clickOn("#tfCitySearch");
      write("city1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems("city1", null, null));
    }
	
	@Test
    public void testSearchSendMessageToServer_inputSiteName () throws IOException {
      clickOn("#tfSiteSearch");
      write("site1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems(null, "site1", null));
    }
	
	@Test
    public void testSearchSendMessageToServer_inputDescription() throws IOException {
      clickOn("#tfDesSearch");
      write("des1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems(null, null, "des1"));
    }
	
	@Test
    public void testSearchSendMessageToServer_inputCityAndSite() throws IOException {
	  clickOn("#tfCitySearch");
      write("city1");
      clickOn("#tfSiteSearch");
      write("site1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems("city1", "site1", null));
    }
	
	@Test
    public void testSearchSendMessageToServer_inputSiteAndDescription() throws IOException {
	  clickOn("#tfDesSearch");
      write("des1");
      clickOn("#tfSiteSearch");
      write("site1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems(null, "site1", "des1"));
    }
	
	@Test
    public void testSearchSendMessageToServer_inputCityAndDescription() throws IOException {
	  clickOn("#tfCitySearch");
      write("city1");
	  clickOn("#tfDesSearch");
      write("des1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems("city1", null, "des1"));
    }
	
	@Test
    public void testSearchSendMessageToServer_inputAllFields() throws IOException {
	  clickOn("#tfCitySearch");
      write("city1");
	  clickOn("#tfDesSearch");
      write("des1");
      clickOn("#tfSiteSearch");
      write("site1");
      clickOn("#btnSearch");
      
      Mockito.verify(mockGUIclient, Mockito.times(1))
      .sendActionToServer(Action.SEARCH, 
    		  createArrayListOfItems("city1", "site1", "des1"));
    }
	
	@Test
    public void handleMessageFromServer_updatesUIForCitySearch() throws IOException, TimeoutException, InterruptedException {
		HashMap<Integer, String> map = createMapHashMap("desc1,5", "desc2,3");
		Object message = new Message(Action.SEARCH, 0, map, 11);
		
		controller.handleMessageFromServer(message);
		Thread.sleep(500);
		
		FxAssert.verifyThat("#SearchResultsTable",
				TableViewMatchers.containsRowAtIndex(0, "desc1", "5"));
		FxAssert.verifyThat("#SearchResultsTable",
				TableViewMatchers.containsRowAtIndex(1, "desc2", "3"));		
        FxAssert.verifyThat("#lblMapsNum", LabeledMatchers.hasText("Maps number: 2"));
        FxAssert.verifyThat("#lblRoutesNum", LabeledMatchers.hasText("Routes number: 11"));
	}
	
	@Test
    public void handleMessageFromServer_updatesUISiteAndDescriptionSearch() throws IOException, TimeoutException, InterruptedException {
		HashMap<Integer, String> map = createMapHashMap("desc1,city1", "desc2,city2");
		Object message = new Message(Action.SEARCH, 0, map);
		
		controller.handleMessageFromServer(message);
		Thread.sleep(500);

		FxAssert.verifyThat("#SearchResultsTable",
				TableViewMatchers.containsRowAtIndex(0, "city1", "desc1"));
		FxAssert.verifyThat("#SearchResultsTable",
				TableViewMatchers.containsRowAtIndex(1, "city2", "desc2"));		
        FxAssert.verifyThat("#lblMapsNum", LabeledMatchers.hasText("Maps number: 2"));
        FxAssert.verifyThat("#lblRoutesNum", NodeMatchers.isInvisible());
	}
	
	@Test
    public void handleMessageFromServer_clearsTableIfInvalidResponseFromServer() throws IOException, TimeoutException, InterruptedException {
		HashMap<Integer, String> map = createMapHashMap("desc1,5", "desc2,3");
		Object message = new Message(Action.SEARCH, 0, map, 11);
	
		// Fill table
		controller.handleMessageFromServer(message);
		Thread.sleep(500);
		
		FxAssert.verifyThat("#SearchResultsTable",
				TableViewMatchers.containsRowAtIndex(0, "desc1", "5"));
		FxAssert.verifyThat("#SearchResultsTable",
				TableViewMatchers.containsRowAtIndex(1, "desc2", "3"));		
        FxAssert.verifyThat("#lblMapsNum", LabeledMatchers.hasText("Maps number: 2"));
        FxAssert.verifyThat("#lblRoutesNum", LabeledMatchers.hasText("Routes number: 11"));
        
        // Invalid response
		Object emptyMessage = new Message(Action.SEARCH, 1, null);
		controller.handleMessageFromServer(emptyMessage);
		Thread.sleep(500);
		
		// Verify table and labels are empty.	
		FxAssert.verifyThat("#SearchResultsTable", TableViewMatchers.hasNumRows(0));
        FxAssert.verifyThat("#lblMapsNum", NodeMatchers.isInvisible());
        FxAssert.verifyThat("#lblRoutesNum", NodeMatchers.isInvisible());
	}
	
	/**
	 * Create HashMap from two String args.
	 * @param entry1
	 * @param entry2
	 * @return
	 */
	private static HashMap<Integer, String> createMapHashMap(String entry1, String entry2) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		map.put(0, entry1);
		map.put(1, entry2);
		return map;
	}
	
	/**
	 * Create ArrayList from String args
	 * @param var1
	 * @param var2
	 * @param var3
	 * @return
	 */
	private static ArrayList<Object> createArrayListOfItems(String var1, String var2, String var3) {
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(var1);
		arr.add(var2);
		arr.add(var3);
		return arr;
	}
	
	public static void main(String[] args) throws Exception {                    
	       JUnitCore.main("unittests.SearchTest");            
	}
}
