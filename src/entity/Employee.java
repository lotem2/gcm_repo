package entity;

import common.Permission;


/**
 * Entity that extends the User user abstract class in the GCM system
 * and represents an employee on the GCM system.
 */
public class Employee extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that builds the User entity
	 * @param first - The user's first name
	 * @param last - The user's last name
	 * @param password - The user's password
	 * @param username - Username of a client as it appears in database
	 * @param email - The user's email
	 * @param permission - The user's permission
	 * @param id - The user's id
	 */
	/* Constructor */
	public Employee(String first, String last, String password, String username,
			String email, Permission permission, long id) {
		super(username, first, last, email, password, permission, id);
	}

	/**
	 * Override of {@link Object}'s toString method
	 */
	@Override
	public String toString() {
		return super.toString() + "\nRole: " + this.getPermission().toString();
	}

}