package entity;

import common.*;

public abstract class User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private final String m_userName;
	private final String m_firstName;
	private final String m_lastName;
	private final String m_email;
	private final String m_password;
	private final byte[] m_salt;
	private final Permission m_permission;
	
	/* Constructor */
	public User(String username, String first, String last, String email, String password, byte[] salt, Permission permission) {
		m_userName = username;
		m_firstName = first;
		m_lastName = last;
		m_email = email;
		m_password = password;
		m_salt = salt;
		m_permission = permission;
	}
	
	/* Getters */
	public String getUserName() { return m_userName; }
	public String getFirstName() { return m_firstName; }
	public String getLastName() { return m_lastName; }
	public String getEmail() { return m_email; }
	public String getPassword() { return m_password; }
	public byte[] getSalt() { return m_salt; }
	public Permission getPermission() { return m_permission; }
	
	@Override
	public String toString() {
		return "User name: " + m_userName + " First name: " + m_firstName + "\nLast name: " + m_lastName;
	}
}
