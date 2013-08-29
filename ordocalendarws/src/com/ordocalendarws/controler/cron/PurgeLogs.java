package com.ordocalendarws.controler.cron;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.ordocalendarws.controler.rest.OrdoDayRessource;
import com.ordocalendarws.model.AccessLogModel;

public class PurgeLogs extends ServerResource {

	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@Get
	public Representation toXml() {
		Logger logger = Logger.getLogger("com.ordocalendarws.controler.cron");
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, -31);
		Long nb_deleted = AccessLogModel.DeleteLogs(cal.getTime());
		logger.info(String.format("%s logs deleted", Long.toString(nb_deleted)));
		
		return OrdoDayRessource.generateSuccessRepresentation(String.format("success: all logs before %s have been deleted", format.format(cal.getTime())));
	}
}
