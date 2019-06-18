package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.*;

import entity.*;
import entity.Purchase.PurchaseType;
import javafx.scene.chart.PieChart.Data;
import server.CityDB;
import server.InboxDB;
import server.PurchaseDB;
import server.SQLController;

/**
 * Represents services to support the regular ongoing work of the GCM system 
 */
public final class Services extends TimerTask {
	// Variables
	static Timer Tasks_timer =  new Timer();

	/**
	 * Writes city into a file 
	 * @param cityToDownload - requested city
	 * @param path - path where to save the file that's been downloaded
	 * @return boolean - Success/Failure of writing 
	 */
	public static boolean writeCityToFile(City cityToDownload, String path) {
		// Variables
		boolean isWritingSuccessul = true;

		try {
			// Create a folder that match the city' name
			File directory = new File(path);

			// Check if directory already exists - delete it and create it again
			if(directory.exists()) directory.delete();

			directory.mkdir();	// Create the directory with the name given above
			
			// Go through each map and create the image
			for (entity.Map city_map : cityToDownload.getMaps()) {
				// Read image from byte array
				if(city_map.getImageAsByte() != null) {
					BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(city_map.getImageAsByte()));
					File newImage = new File(directory.getPath() + "\\" + city_map.getName().replace(" ", "_") + ".jpg"); // Create output file				
				    ImageIO.write(bImage, "png", newImage); // Write image to the specified path	
				}
			}

		    // Go through each map and write its toString method to the file
		    try (FileWriter file = new FileWriter(directory.getPath() + 
		    		"\\" + cityToDownload.getName() + "_Maps.txt", false)) {
		        for (entity.Map currentMap : cityToDownload.getMaps()) {
		        	if(!currentMap.toString().equals("")) {
						file.write(currentMap.toString());
						file.write(System.getProperty("line.separator"));
		        	}
				}
		    } catch (Exception e) {
		        throw new Exception("There was a problem downloading the city.");
		    }
		} catch(Exception e) {
		    e.printStackTrace();
		    isWritingSuccessul = false;
		}

		return isWritingSuccessul;
	}
	
	/**
	 * Restore city from a file 
	 * @param path - path where the file was saved
	 * @return {@link City} representing the city
	 */
	public static City restoreCityFromFile(String path) {
		// Variables
		City restoredCity = null;

		try {
			// Reads from file using FileInputStream
			FileInputStream fis = new FileInputStream(path);

		    // Reads the city_maps object using ObjectOutputStream
			ObjectInputStream ois = new ObjectInputStream(fis);   

		    // Reading the object 
			restoredCity = (City)ois.readObject();

		    // Close both ObjectInputStream and FileInputStream
		    ois.close(); 
		    fis.close();
		} catch(Exception e) {
		    e.printStackTrace();
		}

		return restoredCity;
	}

	/**
	 * Responsible to send a reminder mail to clients about their purchases which are about to expire
	 * @param sendToClients - {@link HashMap} of pairs {@link User}-{@link Purchase}, each user his purchase
	 */
	public static void sendReminderMail(HashMap<Purchase, String> sendToClients) {
		// Variables
		String USER_NAME = "gcm.system.mail";  // Gmail user name - the perfix from the gmail address
	    String PASSWORD = "gcm123456"; // Gmail password
	    String RECIPIENT = "gcm.user@gmail.com"; // user's Gmail address
	    
	     Properties props = System.getProperties(); // Get system properties
	     String host = "smtp.gmail.com";
	     props.put("mail.smtp.starttls.enable", "true");
	     props.put("mail.smtp.host", host);
	     props.put("mail.smtp.user", USER_NAME);
	     props.put("mail.smtp.password", PASSWORD);
	     props.put("mail.smtp.port", "587");
	     props.put("mail.smtp.auth", "true");
	    
	     Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator(){
	         protected PasswordAuthentication getPasswordAuthentication() {
	             return new PasswordAuthentication(
	            		 USER_NAME + "@gmail.com", PASSWORD);// Specify the user name and the password
	         }
	     	}); // Get the default Session object

	     try {
		     MimeMessage message = new MimeMessage(session); // Create a default MimeMessage object
	         message.setFrom(new InternetAddress(USER_NAME)); // Set sender address
	         message.setSubject("Your purchase is about to expire!");

	         // Create messages for each user and sends it to him
	         for(Map.Entry<Purchase, String> entry : sendToClients.entrySet()) {
		         // Create array of InternetAddress for the recipients as the number of users 
		         message.setRecipient(Message.RecipientType.TO, new InternetAddress(entry.getValue().split(",")[1]));
		         
	        	 // Create content for the message, including details about the user and the purchase
	        	 // using HTML tags
	        	 String content = "<p><i>Dear " + entry.getValue().split(",")[0] +"</i>,<br></br><br>The Purchase: <br><b>" + 
	        	 entry.getKey().toString() + "</b><br> is about to expire in 3 days.<br/>"
	        	 		+ "<b><font color=red>Note, you can enjoy 10% discount when renewing your subscription.</b><br/> "
	        	 		+ "After the expiry date of your subscription you will have to pay the full price.</p>"; 	
	         
		         message.setContent(content,"text/html"); // Set content of message

		         // Open session to enable transport and create subject for the message
		         Transport transport = session.getTransport("smtp");
		         transport.connect(host, USER_NAME, PASSWORD);
		         Transport.send(message, message.getAllRecipients()); // sends message
		         transport.close(); // Close transport
	         }
	     }
	     catch (AddressException ae) {
	         ae.printStackTrace();
	     }
	     catch (MessagingException me) {
	         me.printStackTrace();
	     }
	     catch (Exception e) {
	         e.printStackTrace();
	     }
	}

	/**
	 * handle a request for approval of a new city price.
	 * Approval is granted by the CEO.
	 * @param params - Contains a string which contains if request was approved and
	 * false otherwise,the inbox message, the new price and the requested city name.
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public static common.Message handleCityPriceRequest(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data        = new ArrayList<Object>();
		common.Message    replyMsg    = null;
		String            msgContent  = "";
		String            content     = "";
		String            status      = "";

		try {
			//Get city name from content
			String cityName = params.get(3).toString();

			// Check if a new price change message is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status",
						"Approve " + ((InboxMessage)params.get(1)).getSenderUserName() + " new city price for "+
								cityName + " to "  + params.get(2).toString(), "APPROVED"))
				throw new Exception("New version is under approval, cannot create publish request.");

			msgContent  = ((InboxMessage)params.get(1)).getSenderUserName() + " new city price for "+
					cityName + " to "  + params.get(2).toString();

			// Edit content according to the CEO's decision
			if(params.get(0).equals("APPROVED"))
			{

				//Insert city name and new price to update the new city price
				data.add(cityName); data.add(params.get(2).toString());
				try
				{
					replyMsg = CityDB.getInstance().UpdateCityPriceAfterApproval(data);	
				}
				catch (Exception e)
				{
					return replyMsg;
				}
				// Update success to message's data
				data.add(new Integer(0));
				content = "Approved ".concat(msgContent);
				status  = "APPROVED";
			}
			else
			{
				content = "Declined ".concat(msgContent);
				status  = "DECLINED";
			}

			// Insert new Inbox message to managers with the approval request of a city's new price
			common.Message msg = InboxDB.getInstance().AddInboxMessage(
					"DanA",
					Permission.CEO.toString(),
					((InboxMessage)params.get(1)).getSenderUserName(),
					Permission.MANAGING_EDITOR.toString(),
					content,
					status,
					LocalDate.now());

			// Check if insertion was successful
			if((Integer)msg.getData().get(0) == 1) throw new Exception("Request for approval was not successful");
			try
			{
				//Change the sender's message status
				data.clear(); data.add(status); data.add(((InboxMessage)params.get(1)).getID());
				common.Message message;
				message = InboxDB.EditInboxMessageStatus(data);
				data.add(message);
			}
			catch (Exception e)
			{
				throw new Exception(e);
			}
		}
		catch (SQLException e) {
			data.clear(); data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
			return new common.Message(Action.HANDLE_PRICE_CHANGE_REQ, data);
		}
		catch(Exception e) {
			data.clear(); data.add(new Integer(1)); data.add(e.getMessage());
			return new common.Message(Action.HANDLE_PRICE_CHANGE_REQ, data);
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return replyMsg;
	}

	/**
	 * Create a request for approval of a new city price.
	 * Approval is granted by the CEO.
	 * @param params - Contains city name, a new city price and the sender's user entity
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public static common.Message changeCityPriceRequest(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data  = new ArrayList<Object>();

		try {
			// Check if a new price change message is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status",
						"Approve " + params.get(0).toString() + " new city price to " + params.get(1).toString(), "New"))
				throw new Exception("New version is under approval, cannot create publish request.");

			// Prepare statement to insert new map
			String content = "Approve " + params.get(0).toString() + " new city price to " + params.get(1).toString();

			// Insert new Inbox message to managers with the approval request of a city's new price
			common.Message msg = InboxDB.getInstance().AddInboxMessage(
					((User)params.get(2)).getUserName(),
					//((User)params.get(2)).getPermission().toString(),
					Permission.MANAGING_EDITOR.toString(),
					"DanA",
					Permission.CEO.toString(),
					content,
					new String("New"),
					LocalDate.now());

			// Check if insertion was successful
			if((Integer)msg.getData().get(0) == 1) throw new Exception("Request for approval was not successful");

			// Create data to match the success pattern
			data.add(new Integer(0)); data.add(new String("Requset for approval submmited successfuly."));
		}
		catch (SQLException e) {
			data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			data.add(new Integer(1)); data.add(e.getMessage());
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return new common.Message(null, data);
	}

	@Override
	public void run() {
		// Daily functions - reminder to clients which expiry date of their purchases is 3 days from today
		PurchaseDB.getInstance().sendReminders(LocalDate.now().plusDays(3)); 
	}

	/**
	 * Private method to set the timer variables to run at the start of every day - 12am
	 */
	public static void setTimer() {
		Calendar current_time = Calendar.getInstance();

		// Set current time to 12am
		current_time.set(Calendar.HOUR_OF_DAY, 24);
		current_time.set(Calendar.MINUTE, 0);
		current_time.set(Calendar.SECOND, 0);

		// Set new task to the timer to run at the start of every day
		Tasks_timer.scheduleAtFixedRate(new Services(), current_time.getTime(), 
				TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
	}

	
	/**
	 * Notify registered clients on a new version of their subscriptions.
	 * @param params - Contains the approver's User entity and city name
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */

	public static common.Message notifyClientsOnNewVersion(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data           = new ArrayList<Object>();
		ArrayList<Object> param          = new ArrayList<Object>();
		common.Message    replyMsg       = null;
		User 			  currentManager = (User)params.get(0);

		try
		{
		// Get list of purchases of users who purchased the city
		param = new ArrayList<Object>(params.subList(1, params.size()));
		data = PurchaseDB.getInstance().getPurchasesByCity(param).getData();

		if((Integer)data.get(0) != 1) {
			ArrayList<Purchase> cityPurchases = (ArrayList<Purchase>)data.get(1);

			// Write message to user who purchases city's map
			String content = "Got an update for city " + params.get(1).toString();

			// Go through each user's purchase and send them a message about a map's update
			for (Purchase purchase : cityPurchases) {
				// Insert new Inbox message to clients letting them know there is a new version for the map
				if(purchase.getPurchaseType() == PurchaseType.LONG_TERM_PURCHASE) {
					common.Message msg = InboxDB.getInstance().AddInboxMessage(
							Permission.GCM_SYSTEM.toString(),
							Permission.GCM_SYSTEM.toString(),
							purchase.getUserName(),
							Permission.CLIENT.toString(),
							content,
							new String("INFO"),
							LocalDate.now());
					// Check if we got an error while notifying users
					if((Integer)msg.getData().get(0) == 1)
						throw new Exception("Error while updating users about the map's version");
				}
			}
			data.clear(); data.add(new Integer(0));
		}
		else
			throw new Exception("Error while updating users about the map's version");
		}
		catch(Exception e)
		{
			data.clear(); data.add(new Integer(1));
			return new common.Message (null, data);
		}
	return new common.Message (null, data);
}

	/**
	 * handle a new version request from the editors to the managing editors for approval.
	 * @param params - Contains a String which represents if a new version was approved 
	 * and false otherwise, the inbox message, the city's name and approver's User entity
	 * @return {@link Message} - Indicating success/failure with corresponding message
	 */
	public static common.Message handleNewVersionRequest(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data        = new ArrayList<Object>();
		common.Message    replyMsg    = null;
		String            msgContent  = "Approve " + params.get(2).toString() + " new version requested by " +
										((InboxMessage)params.get(1)).getReceiverUserName() + " was ";
		String            content     = "";
		String            status      = "";

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status",
						"Approve " + params.get(2).toString() + " new version", "APPROVED"))
				throw new Exception("New version is under approval, cannot create publish request.");

			// Update success to message's data
			data.add(new Integer(0));

			// Edit content according to the CEO's decision
			if(params.get(0).equals("APPROVED"))
			{
				content = msgContent.concat("Approved");
				status  = "APPROVED";
			}
			else
			{
				content = msgContent.concat("Declined");
				status  = "DECLINED";
			}

			ArrayList<Object> response = new ArrayList<>(params.subList(0, 1));
			response.add(params.get(2));
			replyMsg = CityDB.getInstance().publishMapsCollection(response);

			if((Integer)replyMsg.getData().get(0) == 1)
				throw new Exception(replyMsg.getData().get(1).toString());

			// Insert new Inbox message to managers with the approval request of a city's new price
			common.Message msg = InboxDB.getInstance().AddInboxMessage(
					((InboxMessage)params.get(1)).getReceiverUserName(),
					Permission.MANAGING_EDITOR.toString(),
					((InboxMessage)params.get(1)).getSenderUserName(),
					Permission.EDITOR.toString(),
					content,
					status,
					LocalDate.now());

			//Update the sender's message status
			try
			{
				response.clear(); response.add(status); response.add(((InboxMessage)params.get(1)).getID());
				common.Message message;
				message = InboxDB.EditInboxMessageStatus(response);
				if((Integer)message.getData().get(0) == 1) throw new Exception((String)message.getData().get(1));
			}
			catch (Exception e)
			{
				throw new Exception(e);
			}

			// Check if insertion was successful
			if((Integer)msg.getData().get(0) == 1) throw new Exception("Request for approval was not successful");
			try
			{
				// Add user entity and city name to data
				response.clear(); response.add((User)params.get(3));response.add(params.get(2).toString()); 
				common.Message message;
				if(status.equals("APPROVED")) {
					message = notifyClientsOnNewVersion(response);
					if((Integer)message.getData().get(0) == 1) throw new Exception((String)message.getData().get(1));
				}
			}
			catch (Exception e)
			{
				throw new Exception(e);
			}
		}
		catch (SQLException e) {
			data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
			return new common.Message(Action.HANDLE_PRICE_CHANGE_REQ, data);
		}
		catch(Exception e) {
			data.add(new Integer(1)); data.add(e.getMessage());
			return new common.Message(Action.HANDLE_PRICE_CHANGE_REQ, data);
		}
		finally {
			// Disconnect DB
			SQLController.Disconnect(null);
		}

		return new common.Message(Action.HANDLE_PRICE_CHANGE_REQ, 
				new Integer(0), "Publishing new version was completed successfuly");
	}

	/**
	* Create a request for approval of a city's new version
	* @param params - Contain city's name, the sender's user entity
	* @return {@link Message} - Indicating success/failure with corresponding message
	*/
	public static common.Message createNewVersionRequest(ArrayList<Object> params){
		// Variables
		ArrayList<Object> data  = new ArrayList<Object>();

		try {
			// Check if a new map version is currently under management approval
			if(SQLController.DoesRecordExist("Inbox","content", "status", 
						"Approve " + params.get(0).toString() + "'s new version", "New"))
				throw new Exception("New version is under approval, cannot create publish request.");

			// Prepare statement to insert new map
			String content = "Approve " + params.get(0).toString() + "'s new version";

			// Insert new Inbox message to managers with the approval request of map's new version
			common.Message msg = InboxDB.getInstance().AddInboxMessage(
					((User)params.get(1)).getUserName(),
					Permission.EDITOR.toString(),
					"BROADCAST_EDITORS",
					Permission.MANAGING_EDITOR.toString(),
					content,
					new String("New"),
					LocalDate.now());

			// Check if insertion was successful
			if((Integer)msg.getData().get(0) == 1) throw new Exception("Request for approval was not successful");

			// Create data to match the success pattern
			data.add(new Integer(0)); data.add(new String("Requset for approval submmited successfuly."));
		}
		catch (SQLException e) {
			data.add(new Integer(1)); data.add(new String("There was a problem with the SQL service."));
		}
		catch(Exception e) {
			data.add(new Integer(1)); data.add(e.getMessage());
		}

		return new common.Message(null, data);
	}
}
