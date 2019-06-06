package server;

import java.util.ArrayList;

import common.Message;

interface IReport {
	public Message InsertReport(String username, ArrayList<Object> params);
	public Message produceDailyReport();
}
