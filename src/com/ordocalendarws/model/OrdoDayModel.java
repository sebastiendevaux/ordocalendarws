package com.ordocalendarws.model;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import com.ordocalendarws.model.objects.OrdoDayObject;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class OrdoDayModel {
	
	public static OrdoDayObject getDay(String date) {
		OrdoDayObject od = null;
		Logger logger = Logger.getLogger("com.ordocalendarws.model");
		
		// Using memcache 
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    od = (OrdoDayObject)syncCache.get(date);
	    
	    if(od == null) {
	    	// If not in memcache:
	    	// 1. Retrieve from db
	    	PersistenceManager pm = PMF.get().getPersistenceManager();
	   
			try {
				od = pm.getObjectById(OrdoDayObject.class, date);
				// 2. Put in memcache
				Date d = getNextMidnight();
				syncCache.put(date, od, Expiration.onDate(d));
				logger.info(String.format("Put object in memcache with expiration date: %s", d.toString()));
			} catch(javax.jdo.JDOObjectNotFoundException ex) {
				od = null;
			} catch(Exception ex) {
				ex.printStackTrace();
				od = null;
			} finally {
				pm.close();
			}
	    }
	    
		return od;
	}
	
	/***
	 * Return the date of the next midnight
	 * @return Date of next midnight
	 */
	private static Date getNextMidnight() {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		date.add(Calendar.DAY_OF_MONTH, 1);
		return date.getTime();
	}

	public static void deleteAllDay() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(OrdoDayObject.class);
		try {
			query.deletePersistentAll();
		} finally {
	        query.closeAll();
	        pm.close();
		}
	}
	

	
	/***
	 * Insert days into the db
	 * @param pd List of days to instert
	 */
	public static void insertDays(ArrayList<OrdoDayObject> pd) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	pm.makePersistentAll(pd);
        } finally {
            pm.close();
        }
	}
	
	/***
	 * Delete all days in the specified year (1st of December N-1 to 30th of November)
	 * @param year Year to delete from db
	 */
	public static void deleteAllDayInYear(int year) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(OrdoDayModel.class);
		query.setFilter("date >= minDate");
		query.setFilter("date <= maxDate ");
		query.declareParameters("java.util.Date minDate, java.util.Date maxDate");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date_start, date_end;
		try {
			date_start = formatter.parse(String.format("%i-12-01", year-1));
			date_end   = formatter.parse(String.format("%i-11-30", year));
			if(date_start != null && date_end != null)
				query.deletePersistentAll(date_start, date_end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	       query.closeAll();
	       pm.close();
		}
	}
}
