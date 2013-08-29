package com.ordocalendarws.model;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.ordocalendarws.model.objects.AccessLog;

public class AccessLogModel {
	/***
	 * Delete logs before the specified date
	 * @param date Date before which the logs are deleted
	 * @return The number of instances that were deleted
	 */
	public static long DeleteLogs(Date date) {
		Long nb_deleted = 0L;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(AccessLog.class);
		query.setFilter("date < minDate");
		query.declareParameters("java.util.Date minDate");
		//query..setTimeoutMillis(60000);
		
		try {
			nb_deleted = query.deletePersistentAll(date);
		} finally {
	        query.closeAll();
	        pm.close();
		}
		
		return nb_deleted;
	}
	
	/***
	 * Return the number of days between two days
	 * @param startDay First day in the interval 
	 * @param lastDay Last day in the interval
	 * @return Number of days
	 */
	public static float getNbDays(Date startDay, Date lastDay) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(AccessLog.class);
		query.setFilter("date > startDate && date <= endDate");
		query.declareParameters("java.util.Date startDate, java.util.Date endDate");
		query.setResult("count(date)");
		
		float nb;
		try {
			nb =  ((Long)query.execute(startDay, lastDay)).floatValue();
		} finally {
			query.closeAll();
			pm.close();
		}
		
		return nb;
	}
	
	/**
	 * Store the token into datastore
	 */
	public static void storeToken(AccessLog log) {		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(log);
	}
	
	/**
	 * Delete all objects from database
	 */
	public static void deleteAllLogs(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(AccessLog.class);
		try {
			if(query != null) query.deletePersistentAll();
		} finally {
	        if(query != null) query.closeAll();
		}
	}
}
