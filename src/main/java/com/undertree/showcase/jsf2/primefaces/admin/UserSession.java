package com.undertree.showcase.jsf2.primefaces.admin;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class UserSession implements Serializable {

	private static final long serialVersionUID = 20110407L;
	
	private String userName;
	private String sessionId;
	private Date creationTime;
	private Date lastAccessTime;
	private int maxInterval;
	private String remoteAddress;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public int getMaxInterval() {
		return maxInterval;
	}

	public void setMaxInterval(int maxInterval) {
		this.maxInterval = maxInterval;
	}
	
	public String getRemoteAddress() {
		return remoteAddress;
	}
	
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public long getUsedTime() {
		return lastAccessTime.getTime() - creationTime.getTime();
	}
	
	public long getInactiveTime() {
		return System.currentTimeMillis() - lastAccessTime.getTime();
	}
	
	public long getTimeToLive() {
		long timeToLive = DateUtils.addSeconds(lastAccessTime, maxInterval).getTime() - System.currentTimeMillis();
		return timeToLive > 0 ? timeToLive : 0;
	}
}
