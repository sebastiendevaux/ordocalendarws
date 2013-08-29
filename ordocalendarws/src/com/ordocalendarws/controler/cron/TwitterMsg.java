package com.ordocalendarws.controler.cron;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.ordocalendarws.controler.rest.OrdoDayRessource;
import com.ordocalendarws.model.OrdoDayModel;
import com.ordocalendarws.model.TwitterModel;
import com.ordocalendarws.model.objects.OAuthAccessToken;
import com.ordocalendarws.model.objects.OrdoDayObject;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;


public class TwitterMsg extends ServerResource {

	@Get
	public Representation toXml() {
		String s = post();
		return OrdoDayRessource.generateSuccessRepresentation(s);
	}
	
	private String post() {
		String ret = "";
		String today = OrdoDayRessource.getCurrentDate();
		OrdoDayObject od = OrdoDayModel.getDay(today);
		
		if(od != null) {
			try {
				String date_fr = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE).format(od.getDate());

				// Update status for each user
				List<OAuthAccessToken> list = TwitterModel.getAllAccessToken();
				if(!list.isEmpty()) { 
					TwitterFactory factory = new TwitterFactory();
					for(OAuthAccessToken token : list) {
						AccessToken accessToken = loadAccessToken(token);
						Twitter twitter = factory.getInstance();
						twitter.setOAuthConsumer("94Bsx28YkfgmEjMlTFqtZA", "EeYZHOZNGDbxVwm8NnFSwpagem810ZnRvVTi8XBBsY");
						twitter.setOAuthAccessToken(accessToken);
						Status status = twitter.updateStatus("Aujourd'hui " + date_fr + ": " + od.getFeast() + " - " + od.getFeastClass() + ", " + od.getColor());
						ret = "Successfully updated the status to [" + status.getText() + "] for " + twitter.getScreenName();
					}
				} else {
					ret = "No Access Token found!";
				}
				
			} catch (TwitterException e) {
				ret = "twitter exception:" + e.getMessage();
			}
		} else
			ret = String.format("Error: today (%s) not found!", today);

		return ret;
	}
	
	private AccessToken loadAccessToken(OAuthAccessToken a) {
	    return new AccessToken(a.getToken(), a.getTokenSecret());
	}
}
