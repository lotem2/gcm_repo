package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Represents a class which connects with MySQL server and is responsible for the execution of queries
 */
public class SQLController {
	// Variables
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static private final String DB = "5Baax2TgMs";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	static private final String USER = "5Baax2TgMs";
	static private final String PASS = "QXS3ti0Zia";
	
	static private Connection conn 			   = null;
	static private PreparedStatement prep_stmt = null;

	/**
	 * Synchronized method which is responsible of the SQL connection
	 */
	public static synchronized void Connect() {
		// Connect to DB
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	/**
	 * Synchronized method which is responsible of the SQL disconnection
	 * @param rs - ResultSet that needs to be closed
	 */
	public static synchronized void Disconnect(ResultSet rs) {
		// Close connection to DB
		try {
			if(rs != null)
				rs.close();
			if(prep_stmt != null) {
				prep_stmt.close();
				prep_stmt = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	}

	/**
	 * Synchronized method which is responsible of the SQL SELECT queries
	 * @param sql - a SELECT sql query
	 * @param input - {@link ArrayList} of type {@link Object} which are needed to complete the query
	 * @return ResultSet - a result table for the requested query
	 */
	public static synchronized ResultSet ExecuteQuery(String sql, ArrayList<Object> input) {
		try {
			// Create statement from connection
			prep_stmt = conn.prepareStatement(sql);

			if (input != null) {
				// Set parameters to the statement
				for (int i = 0; i < input.size(); i++) {
					prep_stmt.setObject(i + 1, input.get(i));
				}	
			}
		
			// Execute query, return result
			return prep_stmt.executeQuery();
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		// In case there was an exception returning null
		return null;
	}

	/**
	 * Synchronized method which is responsible of the SQL UPDATE/INSERT/DELETE queries
	 * @param sql - a UPDATE/INSERT/DELETE sql query
	 * @param input - {@link ArrayList} of type {@link Object} which are needed to complete the query
	 * @return int - a number which represents the affected rows by the query
	 */
	public static synchronized int ExecuteUpdate(String sql, ArrayList<Object> input) {
		try {
			// Create statement from connection
			prep_stmt = conn.prepareStatement(sql);
			
			if(input != null) {
				// Set parameters to the statement
				for (int i = 0; i < input.size(); i++) {
					prep_stmt.setObject(i + 1, input.get(i));
				}	
			}

			// Execute query, return number of rows changed
			return prep_stmt.executeUpdate();
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		// In case there was an exception returning 0
		return 0;
	}

	/**
	 * Synchronized method which is responsible to check if a record exists in the database
	 * @param inputs - {@link ArrayList} of type {@link Object} which are needed to complete the query
	 * @return boolean - true if the row exists, false otherwise
	 */
	public static synchronized boolean DoesRecordExist(Object...inputs) {
		// Variables
		int 			  index;
		ArrayList<Object> params;
		boolean 		  RecordExists = false;
		ArrayList<Object> data 	   	   = new ArrayList<Object>();

		try {
			Connect();	// Connected to database

			// Build array list for the ExecuteQuery method
			for (Object object : inputs) {
				data.add(object);
			}

			// Get list of parameters from inputs given
			params = new ArrayList<Object>((data.subList(((data.size()/2) + 1), data.size())));

			String sql = "SELECT 1 FROM " + data.get(0) + " WHERE ";	// Prepare SQL query
			
			for (int i = 1; i <= data.size()/2; i++) {
				if (data.get(i).equals("description"))
					sql += data.get(i) + " LIKE ? AND ";
				else	
					sql += data.get(i) + " = ? AND ";
			}
			
			sql = sql.substring(0, sql.length() - (sql.length() - sql.lastIndexOf("AND")));

			// Execute query using the ExecuteQuery method with the input and the above query
			ResultSet rs = ExecuteQuery(sql, params);
			
			// Check if row exists - change boolean to true
			if(rs.next()) RecordExists = true;

		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		// Return result
		return RecordExists;
	}
}
