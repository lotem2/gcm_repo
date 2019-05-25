package entity;

import common.*;
import java.time.LocalDate;

public class Client extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private long m_telephone;
	private long m_cardnumber;
	private long m_id;
	private LocalDate m_expirydate;

	
	/* Constructor */
	public Client(String first, String last, String password, byte[] salt, String username,
				  String email, Permission permission, long tel, long cardnumber, long id, LocalDate expirydate) {
		super(username, first, last, email, password, salt, permission);
		m_telephone = tel;
		m_cardnumber = cardnumber;
		m_id = id;
		m_expirydate = expirydate;
}

	/* Getters */
	public long getTelephone() { return m_telephone; }
	public long getCardNumber() { return m_cardnumber; }
	public long getID() { return m_id; }
	public LocalDate getExpiryDate() { return m_expirydate; }

	@Override
	public String toString() {
			return super.toString() + "\nID: " + m_id + "\nCard number: " + m_cardnumber
			+ "\nExpiry date: " + m_expirydate + "\nTelephone: " + m_telephone;

	}
}
