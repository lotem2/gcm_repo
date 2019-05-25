package entity;

import common.Permission;

public class Employee extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private final String m_role;
	private final int m_id;
	
	/* Constructor */
	public Employee(String first, String last, String password, byte[] salt, String username, String role,
			String email, Permission permission, int id) {
		super(username, first, last, email, password, salt, permission);
		m_role = role;
		m_id = id;
	}
	
	/* Getters */
	public String getRole() { return m_role; }
	public int getId() { return m_id; }

	@Override
	public String toString() {
		return super.toString() + "\nRole: " + m_role + "\nID: " + m_id;
	}

}
