package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.*;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import java.util.HashMap;
import java.util.Map;

import common.*;
import entity.*;
import entity.Purchase.PurchaseType;
import server.PurchaseDB;

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
			// Write to file using FileOutputStream
		    FileOutputStream fos = new FileOutputStream(path + "\\" + cityToDownload.getName() + ".txt", false);

		    // Write the city_maps object using ObjectOutputStream
		    ObjectOutputStream oos = new ObjectOutputStream(fos);   

		    // Writing the city's maps and 
		    oos.writeObject(cityToDownload.getMaps());

		    // Close both ObjectOutputStream and FileOutputStream
		    oos.close(); 
		    fos.close();
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
	    
	     Session session = Session.getDefaultInstance(props); // Get the default Session object

	     try {
		     MimeMessage message = new MimeMessage(session); // Create a default MimeMessage object
	         message.setFrom(new InternetAddress(USER_NAME)); // Set sender address
	         
	         // Open session to enable transport and create subject for the message
	         Transport transport = session.getTransport("smtp");
	         transport.connect(host, USER_NAME, PASSWORD);
	         message.setSubject("Your purchase is about to expire!");

	         // Create messages for each user and sends it to him
	         for(Map.Entry<Purchase, String> entry : sendToClients.entrySet()) {
		         // Create array of InternetAddress for the recipients as the number of users 
		         message.setRecipient(Message.RecipientType.TO, new InternetAddress(entry.getValue().split(",")[1]));
		         
	        	 // Create content for the message, including details about the user and the purchase
	        	 // using HTML tags
	        	 String content = "<p><i>Dear " + entry.getValue().split(",")[0] +"</i><br/>,The Purchase: <br><b>" + 
	        	 entry.getKey().toString() + "</b><br> is about to expire in 3 days.<br/>"
	        	 		+ "<b><font color=read>Note, you can enjoy 10% discount when renewing your subscription.</b><br/> "
	        	 		+ "After the expiry date of your subscription you will have to pay the full price.</p>"; 	
	         
		         message.setContent(content,"text/html"); // Set content of message
		         Transport.send(message); // sends message
	         }

	         transport.close(); // Close transport
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
}
