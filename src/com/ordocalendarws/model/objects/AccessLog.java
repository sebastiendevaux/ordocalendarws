package com.ordocalendarws.model.objects;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AccessLog {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String path;

	@Persistent
	private Date date;
	
	@Persistent
	private String referer;
	
	@Persistent
	private String host;
	
	@Persistent
	private String ip;
	
	@Persistent
	private String agent;
	
	
	public Key getKey() {
		return key;
	}
	
	public String getPath() {
		return path;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getReferer() {
		return referer;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getAgent() {
		return agent;
	}
	
	/**
	 * Constructor
	 * @param path Resource Path
	 * @param referer Client referrer
	 * @param host Client host
	 * @param ip Client IP
	 * @param agent Client agent
	 */
	public AccessLog(String path, String referer, String host, String ip, String agent) {
		this.path = path;
		this.date = new Date();
		this.referer = referer;
		this.host = host;
		this.ip = ip;
		this.agent = agent;
	}
}
