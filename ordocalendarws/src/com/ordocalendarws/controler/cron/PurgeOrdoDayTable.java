package com.ordocalendarws.controler.cron;

import java.util.logging.Logger;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.ordocalendarws.controler.rest.OrdoDayRessource;
import com.ordocalendarws.model.OrdoDayModel;

public class PurgeOrdoDayTable extends ServerResource {
	
	@Get
	public Representation toXml() {
		Logger logger = Logger.getLogger("com.ordocalendarws.controler.cron");
		OrdoDayModel.deleteAllDay();
		logger.info("All fays have been deleted");
		
		return OrdoDayRessource.generateSuccessRepresentation("success: all day have been deleted");
	}

}
