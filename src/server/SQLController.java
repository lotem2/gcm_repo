package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLController {
	// Variables
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static private final String DB = "5Baax2TgMs";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	static private final String USER = "5Baax2TgMs";
	static private final String PASS = "QXS3ti0Zia";
	
	static private Connection conn 			   = null;
	static private PreparedStatement prep_stmt = null;
	
	public static synchronized void Connect() {
		// Connect to DB
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
			e.printStackTrace();
		}
	}
	
	public static synchronized ResultSet ExecuteQuery(String sql, ArrayList<Object> input) {
		try {
			// Create statement from connection
			prep_stmt = conn.prepareStatement(sql);
			
			// Set parameters to the statement
			for (int i = 0; i < input.size(); i++) {
				prep_stmt.setObject(i + 1, input.get(i));
			}
		
			// Execute query, return result
			return prep_stmt.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// In case there was an exception returning null
		return null;
	}
	
	public static synchronized int ExecuteUpdate(String sql, ArrayList<Object> input) {
		try {
			// Create statement from connection
			prep_stmt = conn.prepareStatement(sql);
			
			// Set parameters to the statement
			for (int i = 0; i < input.size(); i++) {
				prep_stmt.setObject(i + 1, input.get(i));
			}
		
			// Execute query, return number of rows changed
			return prep_stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// In case there was an exception returning 0
		return 0;
	}
}
