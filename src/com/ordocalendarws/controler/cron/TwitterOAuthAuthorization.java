package com.ordocalendarws.controler.cron;


import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.ordocalendarws.model.TwitterModel;
import com.ordocalendarws.model.objects.OAuthAccessToken;
import com.ordocalendarws.model.objects.OAuthRequestToken;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterOAuthAuthorization extends ServerResource {

	@Get("html")
	public Representation getAuthorization() {
		StringBuilder r = new StringBuilder();
		r.append("<html>\n");
		r.append("  <head><title>To give access to your account</title></head>\n");
		r.append("  <body>\n");
		
		String pin = (String) getRequest().getAttributes().get("pin");

		if(pin != null) {
			r.append(getAcessToken(pin));
		} else {
			r.append(getPin());
		}
		
		r.append("  </body>\n");
		r.append("</html>");
		
		return new StringRepresentation(r, MediaType.TEXT_HTML);
	}
	
	/**
	 * First step in order to be authorized : get the pin number
	 * @return Result
	 */
	private String getPin() {
		String r = "";
		RequestToken requestToken = null;
		
		
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer("94Bsx28YkfgmEjMlTFqtZA", "EeYZHOZNGDbxVwm8NnFSwpagem810ZnRvVTi8XBBsY");
			requestToken = twitter.getOAuthRequestToken();
			
			r += "    <h1>To give access to your account:</h1>\n";
			r += "    <ol>\n";
			r += "      <li>Open this <a href=\"" + requestToken.getAuthorizationURL() + "\">URL</a> and grant access to your account</li>\n";
			r += "      <li>Note the PIN number and use it as a parameter here : http://ordocalendarws.appspot.com/cron/twitter/auth/[pin]</li>\n";
			r += "    </ol>\n";
			
			// Store Request token into datastore
			TwitterModel.deleteAllObjects(OAuthRequestToken.class);
			
			OAuthRequestToken rt = new OAuthRequestToken(requestToken);
			rt.storeToken();
		} catch (TwitterException e) {
			e.printStackTrace();
		}	
		
		return r;
	}
	
	/**
	 * Second step : get access token
	 * @param pin The pin received from twitter in the first step
	 * @return Result
	 */
	private String getAcessToken(String pin) {
		String r = "";
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("94Bsx28YkfgmEjMlTFqtZA", "EeYZHOZNGDbxVwm8NnFSwpagem810ZnRvVTi8XBBsY");
		
		try {
			// Get Request token from datastore
			OAuthRequestToken rt = TwitterModel.getRequestToken();
			AccessToken accessToken = twitter.getOAuthAccessToken(rt.getToken(), pin);
			twitter.setOAuthAccessToken(accessToken);
			
			// Store Access token into datastores
			OAuthAccessToken at = new OAuthAccessToken(String.valueOf(twitter.verifyCredentials().getId()), accessToken);
			at.storeToken();
			
			// Try to update status
			Status status = twitter.updateStatus("Mon compte publie maintenant le calendrier catholique traditionnel : http://ordocalendarws.appspot.com/");
			r += "Successfully updated the status to [" + status.getText() + "].\n";
			
			// delete request token
			TwitterModel.deleteAllObjects(OAuthRequestToken.class);
			
		} catch (TwitterException te) {
			if(401 == te.getStatusCode()){
				r += "Unable to get the access token.\n";
			}else{
				te.printStackTrace();
			}
		}

		return r;
	}

	
	
}
