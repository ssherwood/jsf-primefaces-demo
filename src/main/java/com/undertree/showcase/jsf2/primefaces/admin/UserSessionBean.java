package com.undertree.showcase.jsf2.primefaces.admin;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@ManagedBean
@ApplicationScoped
public class UserSessionBean {

	static final Logger LOG = LoggerFactory.getLogger(UserSessionBean.class);
	List<UserSession> currentSessions = new LinkedList<UserSession>();
	
	
	
	public List<UserSession> getCurrentUserSessions() {
		LOG.debug("called getCurrentUseSessions");
		
		currentSessions.clear();
		
		for (HttpSession activeSession : UserSessionListener.getActiveSessions()) {
			LOG.debug("Active Session {}", activeSession);
			UserSession userSession = new UserSession();
			
			userSession.setSessionId(activeSession.getId());
			userSession.setCreationTime(new Date(activeSession.getCreationTime()));
			userSession.setLastAccessTime(new Date(activeSession.getLastAccessedTime()));
			userSession.setMaxInterval(activeSession.getMaxInactiveInterval());
			
			SecurityContext sc = (SecurityContext)activeSession.getAttribute("SPRING_SECURITY_CONTEXT");
			
			if (sc == null) {
				userSession.setUserName("*anonymous*");
			}
			else {
				userSession.setUserName(sc.getAuthentication().getName());
				
				WebAuthenticationDetails wd = (WebAuthenticationDetails) sc.getAuthentication().getDetails();
				userSession.setRemoteAddress(wd.getRemoteAddress());
			}
			
			currentSessions.add(userSession);
		}
		
		return currentSessions;
	}
}
