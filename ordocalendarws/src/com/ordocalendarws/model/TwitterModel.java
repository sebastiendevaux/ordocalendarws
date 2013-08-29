package com.ordocalendarws.model;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.ordocalendarws.model.objects.OAuthAccessToken;
import com.ordocalendarws.model.objects.OAuthRequestToken;

public class TwitterModel {
	
	/**
	 * Retreive the request token object from datastore
	 * @return The token
	 */
	public static OAuthRequestToken getRequestToken() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		OAuthRequestToken rt = null;
		try {
			rt = pm.getObjectById(OAuthRequestToken.class, "sanctoral");
		} catch(javax.jdo.JDOObjectNotFoundException ex) {
			rt = null;
		} finally {
			pm.close();
		}
		
		return rt;
	}
	
	/**
	 * Delete all objects in datastore
	 * @param c The type of objects to delete
	 */
	@SuppressWarnings("rawtypes")
	public static void deleteAllObjects(Class c) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(c);
		try {
			query.deletePersistentAll();
		} finally {
	        query.closeAll();
	        pm.close();
		}
	}
	
	/**
	 * Retreive all access token objects from datastore
	 * @return The token
	 */
	@SuppressWarnings("unchecked")
	public static List<OAuthAccessToken> getAllAccessToken() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(OAuthAccessToken.class);
		List<OAuthAccessToken> list = null;
				
		try {
			list = ((List<OAuthAccessToken>) query.execute());
			list.size();	// To remove "Object Manager has been closed" Exception. See http://www.atentia.net/2010/03/app-engine-and-jdo-object-manager-has-been-closed/
		} finally {
			query.closeAll();
			pm.close();
		}
		
		return list;
	}
}
