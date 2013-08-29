package com.ordocalendarws.model.objects;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.ordocalendarws.model.PMF;


@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class OAuthToken {
	@PrimaryKey
    private String _userId;
	
	@Persistent
	private String _token;
	
	@Persistent
	private String _tokenSecret;
	
	public String getUser() {
		return _userId;
	}
	
	public String getToken() {
		return _token;
	}
	
	public String getTokenSecret() {
		return _tokenSecret;
	}
	
	public void setToken(String token) {
		_token = token;
	}
	
	public void setTokenSecret(String tokenSecret) {
		_tokenSecret = tokenSecret;
	}
	

	public OAuthToken(String userId, String token, String tokenSecret) {
		_userId = userId;
		_token = token;
		_tokenSecret = tokenSecret;
	}
	
	/**
	 * Store the token into datastore
	 */
	public void storeToken() {		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
	}
	

}
