package entity;

import common.*;

public class Client extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private final long m_telephone;
	
	/* Constructor */
	public Client(String first, String last, String password, String username,
			String email, Permission permission, long tel) {
		super(username, first, last, email, password, permission);
		m_telephone = tel;
	}
	
	/* Getters */
	public long getTelephone() { return m_telephone; }

	@Override
	public String toString() {
		return super.toString() + "\nTelephone: " + m_telephone;
	}
}
