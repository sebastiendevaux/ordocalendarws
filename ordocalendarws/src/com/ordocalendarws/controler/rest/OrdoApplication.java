package com.ordocalendarws.controler.rest;

import com.ordocalendarws.controler.cron.CreateYear;
import com.ordocalendarws.controler.cron.PurgeLogs;
import com.ordocalendarws.controler.cron.PurgeOrdoDayTable;
import com.ordocalendarws.controler.cron.TwitterMsg;
import com.ordocalendarws.controler.cron.TwitterOAuthAuthorization;
import com.ordocalendarws.view.RSSFeed;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public class OrdoApplication extends Application {
	public OrdoApplication() {
		super();
	}

	/** 
	* Creates a root Restlet that will receive all incoming calls. 
	*/  
	@Override
	public synchronized Restlet createInboundRoot() {   
		// Create a router Restlet that routes each call to a  
		// new instance of HelloWorldResource.  
	    Router router = new Router(getContext());
	 
	
	    // Defines routes
	    router.attach("/ordo/", OrdoDayRessource.class);
	    router.attach("/ordo/{date}", OrdoDayRessource.class);
	    router.attach("/stats/",Stats.class);
	    router.attach("/rss/", RSSFeed.class);
	    router.attach("/cron/update/{year}", CreateYear.class);
	    router.attach("/cron/twitter",TwitterMsg.class);
	    router.attach("/cron/twitter/auth/", TwitterOAuthAuthorization.class);
	    router.attach("/cron/twitter/auth/{pin}", TwitterOAuthAuthorization.class);
	    router.attach("/cron/purgelogs", PurgeLogs.class);
	    router.attach("/cron/purgedays", PurgeOrdoDayTable.class);
	    
	    return router;
	}
}
