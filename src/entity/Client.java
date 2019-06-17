package entity;

import common.*;
import java.time.LocalDate;

/**
 * Entity that extends the User user abstract class in the GCM system
 * and represents a client on the GCM system.
 */
public class Client extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Variables
	private long m_telephone;
	private long m_cardnumber;
	private LocalDate m_expirydate;
	private final byte[] m_salt;

	/**
	 * Constructor that builds the User entity
	 * @param first - The user's first name
	 * @param last - The user's last name
	 * @param password - The user's password
	 * @param salt - The user's salt
	 * @param username - Username of a client as it appears in database
	 * @param email - The user's email
	 * @param permission - The user's permission
	 * @param tel - The user's telephone number
	 * @param cardnumber - The user's card number
	 * @param id - The user's id
	 * @param expirydate - The user's expiry date of credit card
	 */
	/* Constructor */
	public Client(String first, String last, String password, byte[] salt, String username,
				  String email, Permission permission, long tel, long cardnumber, long id, LocalDate expirydate) {
		super(username, first, last, email, password, permission, id);
		m_telephone = tel;
		m_salt = salt;
		m_cardnumber = cardnumber;
		m_expirydate = expirydate;
}

	/* Getters */


	/**
	 * Get User's unique salt
	 * @return byte[]
	 */
	public byte[] getSalt() { return m_salt; }

	/**
	 * Get User's telephone number
	 * @return long
	 */
	public long getTelephone() { return m_telephone; }

	/**
	 * Get User's credit card number
	 * @return long
	 */
	public long getCardNumber() { return m_cardnumber; }

	/**
	 * Get User's expiry date of credit card
	 * @return LocalDate
	 */
	public LocalDate getExpiryDate() { return m_expirydate; }

	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
			return super.toString() + "\nID: " + this.getID() + "\nCard number: " + m_cardnumber
			+ "\nExpiry date: " + m_expirydate + "\nTelephone: " + m_telephone;

	}
}
