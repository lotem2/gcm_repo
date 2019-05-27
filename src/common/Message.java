package common;

import java.util.ArrayList;

public class Message implements java.io.Serializable{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   
    private Action action;
    private ArrayList<Object> data;
   
    public Message(Action action, ArrayList<Object> data) {
        super();
        this.action = action;
        this.data = data;
    }
    
//    public Message(Action action) {
//        super();
//        this.action = action;
//    }
 
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    public ArrayList<Object> getData() {
        return data;
    }
   
    @Override
    public String toString() {
        return "this is a message object. emum: " + this.action + " data: " +this.data;
    }
}