package com.ordocalendarws.model.objects;

import javax.jdo.annotations.PersistenceCapable;


import twitter4j.auth.AccessToken;

@PersistenceCapable
public class OAuthAccessToken  extends OAuthToken {
	
	public OAuthAccessToken(String user_id, AccessToken accessToken) {
		super(user_id, accessToken.getToken(), accessToken.getTokenSecret());
	}

}
