package common;

import java.util.ArrayList;

/**
 * Class represents a message been sent to/from Client-Server, includes {@link Action} type
 * and {@link ArrayList} of data sent from/to Client/Server and back
 */
public class Message implements java.io.Serializable{ 
	// Variables
	private static final long serialVersionUID = 1L;
    private Action action;
    private ArrayList<Object> data = new ArrayList<Object>();
   
    /**
     * Public constructor of Message class
     * @param action - type of action that has been requested
     * @param data - {@link ArrayList} of parameters to complete the requested action
     */
    public Message(Action action, ArrayList<Object> data) {
        super();
        this.action = action;
        this.data = data;
    }

    /**
     * Another public constructor, identical to the above
     * @param action - type of action that has been requested
     * @param inputs - dynamic number of Objects to complete the requested action
     */
    public Message(Action action, Object...inputs) {
        super();
        this.action = action;
        for (Object input : inputs) {
			data.add(input);
		}
    }

    /**
     * Get {@link Action} data member of class Member
     * @return {@link Action} - current action 
     */
    public Action getAction() {
        return action;
    }

    /**
     * Set current {@link Action} 
     * @param action - current action requested
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Get {@link ArrayList} of parameters of current Message for requested action
     * @return {@link ArrayList} of parameters of type {@link Object}
     */
    public ArrayList<Object> getData() {
        return data;
    }

    /**
     * Override of method toString of {@link Object}
     */
    @Override
    public String toString() {
        return "this is a message object. emum: " + this.action + " data: " +this.data;
    }
}