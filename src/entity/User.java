package entity;

import common.*;

public abstract class User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private final String m_firstName;
	private final String m_lastName;
	private final String m_email;
	private final String m_password;
	private final Permission m_permission;
	
	/* Constructor */
	public User(String first, String last, String email, String password, Permission permission) {
		m_firstName = first;
		m_lastName = last;
		m_email = email;
		m_password = password;
		m_permission = permission;
	}
	
	/* Getters */
	public String getFirstName() { return m_firstName; }
	public String getLastName() { return m_lastName; }
	public String getEmail() { return m_email; }
	public String getPassword() { return m_password; }
	public Permission getPermission() { return m_permission; }
	
	@Override
	public String toString() {
		return "First name: " + m_firstName + "\nLast name: " + m_lastName;
	}
}
