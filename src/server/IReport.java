package server;

import java.util.ArrayList;
import common.Message;


/**
 * Interface class which defines methods for creating reports.
 * ReportDB is the class which implements it's methods
 * This class can be expanded in the future so that adding a
 * new report type would be translated in practice into a new
 * method
 */
interface IReport {
	public Message InsertReport(String username, ArrayList<Object> params);
	public Message produceDailyReport();
	public Message produceActivityReport(ArrayList<Object> params);
}
