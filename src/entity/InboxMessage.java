package entity;

import java.time.LocalDate;

import common.Permission;
import common.Status;

public class InboxMessage {

	private int m_id;
	private String m_senderUsername;
	private Permission m_senderPermission;
	private String m_receiverUsername;
	private Permission m_receiverPermission;
	private String m_content;
	private Status m_status;
	private LocalDate m_receiveDate;

	public InboxMessage (int id, String senderUsername, Permission senderPermission, 
			String receiverUsername, Permission receiverPermission, String content, Status status, LocalDate receiveDate)
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
	public int getId() { return m_id; }
	public String getSenderUserName() { return m_senderUsername; }
	public Permission getSenderPermission() { return m_senderPermission; }
	public String getReceiverUserName() { return m_receiverUsername; }
	public Permission getReceiverPermission() { return m_receiverPermission; }
	public String getContent() { return m_content; }
	public Status getStatus() { return m_status; }
	public LocalDate getReceiveDate() { return m_receiveDate; }
}
