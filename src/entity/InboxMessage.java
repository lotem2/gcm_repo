package entity;

import java.time.LocalDate;

import common.Permission;
import common.Status;

/**
 * Entity that represents a message between employees and clients
 *
 */

public class InboxMessage implements java.io.Serializable {

	private int m_id;
	private String m_senderUsername;
	private Permission m_senderPermission;
	private String m_receiverUsername;
	private Permission m_receiverPermission;
	private String m_content;
	private Status m_status;
	private LocalDate m_receiveDate;

	/**
	 * Constructor that builds the User entity
	 * @param id - The message identifier
	 * @param senderUsername - The sender's user name
	 * @param senderPermission - The sender's {@link Permission}
	 * @param receiverUsername - The receiver's user name
	 * @param receiverPermission - The reciever's {@link Permission}
	 * @param content - The content of the message
	 * @param status - The {@link Status} of the message
	 * @param receiveDate - The {@link LocalDate} of the message
	 */
	public InboxMessage (int id, String senderUsername, Permission senderPermission, String receiverUsername,
			Permission receiverPermission, String content, Status status, LocalDate receiveDate)
	{
		m_id = id;
		m_senderUsername = senderUsername;
		m_senderPermission = senderPermission;
		m_receiverUsername = receiverUsername;
		m_receiverPermission = receiverPermission;
		m_content = content;
		m_status = status;
		m_receiveDate = receiveDate;
	}

	/* Getters */

	/**
	 * Get the the message's identifier
	 * @return Integer
	 */
	public int getID() { return m_id; }

	/**
	 * Get the sender's user name
	 * @return String
	 */
	public String getSenderUserName() { return m_senderUsername; }

	/**
	 * Get sender's classification
	 * @return {@link  common.Classification}
	 */
	public Permission getSenderPermission() { return m_senderPermission; }

	/**
	 * Get the receiver's user name
	 * @return String
	 */
	public String getReceiverUserName() { return m_receiverUsername; }

	/**
	 * Get receiver's classification
	 * @return {@link common.Classification}
	 */
	public Permission getReceiverPermission() { return m_receiverPermission; }

	/**
	 * Get the message's content
	 * @return String
	 */
	public String getContent() { return m_content; }

	/**
	 * Get the message's {@link Status}
	 * @return {@link Status}
	 */
	public Status getStatus() { return m_status; }

	/**
	 * Get the message's {@link LocalDate}
	 * @return {@link LocalDate}
	 */
	public LocalDate getReceiveDate() { return m_receiveDate; }

}
