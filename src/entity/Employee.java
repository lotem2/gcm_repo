package entity;

import common.Permission;

public class Employee extends User implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Constructor */
	public Employee(String first, String last, String password, String username,
			String email, Permission permission, long id) {
		super(username, first, last, email, password, permission, id);
	}

	@Override
	public String toString() {
		return super.toString() + "\nRole: " + this.getPermission().toString();
	}

}
