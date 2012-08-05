package com.undertree.showcase.jsf2.primefaces.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Extends the standard {@link HttpSessionListener} to maintain a current
 * active set of user sessions.
 * <p>
 * NOTE: Tomcat's default behavior is to persist sessions across server
 * restarts.  I currently do not have a solution to re-associate those
 * sessions to the active session set.
 * 
 * @author Shawn Sherwood
 *
 */
public class UserSessionListener implements HttpSessionListener {
	
	private static final int MAX_SESSION_SIZE = 1024 * 10; // 10 KB as the recommended size
	
	private static final Logger LOG = LoggerFactory.getLogger(UserSessionListener.class);
	
	static final Set<HttpSession> ACTIVE_SESSIONS_SET = Collections.synchronizedSet(new HashSet<HttpSession>());
	
	/*
	 * 
	 */
	public UserSessionListener() {
		LOG.trace("Creating UserSessionListener");
		
		// TODO find a way to re-associate persisted sessions due to a restart here
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		synchronized (ACTIVE_SESSIONS_SET) {
			ACTIVE_SESSIONS_SET.add(event.getSession());
		}
		
		LOG.debug("The HttpSession {} was created on {}.", event.getSession().getId(), new Date());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		synchronized (ACTIVE_SESSIONS_SET) {
			ACTIVE_SESSIONS_SET.remove(event.getSession());
		}
		
		checkFinalSessionSizeForBloat(event);
			
		LOG.debug("The HttpSession {} was destroyed on {} for user '{}'.", new Object[] { event.getSession().getId(), new Date(), getUserAssociatedWithCurrentSession() });
	}

	/** 
	 * 
	 * @return a copy of the active session set as a list.
	 */
	public static List<HttpSession> getActiveSessions() {
		return new ArrayList<HttpSession>(ACTIVE_SESSIONS_SET);
	}

	/*
	 * This check is a simplistic aid in managing session bloat.  Keeping the
	 * total session small can significantly help with scalability as
	 * techniques like session persistence or replication are more feasible. 
	 */
	private void checkFinalSessionSizeForBloat(HttpSessionEvent event) {
		long approximateSessionSizeInBytes = getApproximateSessionSize(event.getSession());
			
		LOG.trace("The HttpSession was approximately {} bytes when serialized.", approximateSessionSizeInBytes);
		
		if (approximateSessionSizeInBytes > MAX_SESSION_SIZE) {
			LOG.warn("The HttpSession is too damn high!");
		}
	}
		
	/*
	 * This is probably not the best way to determine the actual session size
	 * however, it does roughly measure the serialized size which is typically
	 * what we are interested in from a performance perspective (i.e. from a
	 * simplistic session replication scenario).
	 * 
	 * TODO: Move to a session "helper" class
	 */
	private long getApproximateSessionSize(HttpSession currentSession) {
		long totalBytes = 0L;
		
		for (Enumeration<String> sessionAttribute = currentSession.getAttributeNames(); sessionAttribute.hasMoreElements(); ) {
			String sessionAttributeKey = sessionAttribute.nextElement();
			
			try {
				Serializable sessionAttributeValue = (Serializable) currentSession.getAttribute(sessionAttributeKey);
				totalBytes += SerializationUtils.serialize(sessionAttributeKey).length;
				totalBytes += SerializationUtils.serialize(sessionAttributeValue).length;
				
				// TODO this trace should go somewhere else
				LOG.trace("Session attribute: '{}' = '{}'", sessionAttributeKey,sessionAttributeValue);
			}
			catch (ClassCastException ex) {
				// we should avoid having unserializable objects in the session
				LOG.warn("Found unserializable object in the HttpSession at '{}' as type {}.", sessionAttributeKey, currentSession.getAttribute(sessionAttributeKey).getClass());
			}
		}
		
		return totalBytes;
	}
	
	/*
	 * 
	 */
	private String getUserAssociatedWithCurrentSession() {
		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
		return userAuthentication == null ? "*Anonymous*" : userAuthentication.getName();
	}
}