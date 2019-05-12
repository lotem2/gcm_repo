package entity;

import common.*;

public class Client extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private final String m_userName;
	private final long m_telephone;
	private final int m_purchaseCount;
	
	/* Constructor */
	public Client(String first, String last, String password, String username,
			String email, Permission permission, long tel, int purchaseCount) {
		super(first, last, email, password, permission);
		m_userName = username;
		m_telephone = tel;
		m_purchaseCount = purchaseCount;
	}
	
	/* Getters */
	public String getUsername() { return m_userName; }
	public long getTelephone() { return m_telephone; }
	public int getPurchaseCount() { return m_purchaseCount; }

	@Override
	public String toString() {
		return super.toString() + "\nUser name: " + m_userName + "\nTelephone: " + m_telephone + 
				"\nPurchase count: " + m_purchaseCount;
	}
}
