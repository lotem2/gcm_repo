package entity;

import java.util.ArrayList;

import common.*;

/**
 * Entity that represents a User object in the GCM system
 */
public abstract class User implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	// Variables
	private final String m_userName;
	private final String m_firstName;
	private final String m_lastName;
	private final String m_email;
	private final String m_password;
	private final long m_id;
	private final Permission m_permission;

	/**
	 * Constructor that builds the User entity
	 * @param username - Username of a client as it appears in database
	 * @param first - The user's first name
	 * @param last - The user's last name
	 * @param email - The user's email
	 * @param password - The user's password
	 * @param permission - The user's permission
	 * @param id - The user's id
	 */
	/* Constructor */
	public User(String username, String first, String last, String email, String password, Permission permission, long id) {
		m_userName = username;
		m_firstName = first;
		m_lastName = last;
		m_email = email;
		m_password = password;
		m_permission = permission;
		m_id = id;
	}

	/* Getters */

	/**
	 * Get username
	 * @return String
	 */
	public String getUserName() { return m_userName; }

	/**
	 * Get user's first name
	 * @return String
	 */
	public String getFirstName() { return m_firstName; }

	/**
	 * Get user's last name
	 * @return String
	 */
	public String getLastName() { return m_lastName; }

	/**
	 * Get user's email
	 * @return String
	 */
	public String getEmail() { return m_email; }

	/**
	 * Get user's password
	 * @return String
	 */
	public String getPassword() { return m_password; }

	/**
	 * Get user's id
	 * @return {@link Permission}
	 */
	public Permission getPermission() { return m_permission; }

	/**
	 * Get user's id
	 * @return long
	 */
	public long getID() { return m_id; }

	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		return "User name: " + m_userName + " First name: " + m_firstName + "\nLast name: " + m_lastName + "\nID: " + m_id;
	}
}