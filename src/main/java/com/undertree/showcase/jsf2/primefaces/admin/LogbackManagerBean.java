package com.undertree.showcase.jsf2.primefaces.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@ManagedBean
@ViewScoped
public class LogbackManagerBean implements Serializable {

	private static final long serialVersionUID = 20120311L;
	static final Logger LOG = LoggerFactory.getLogger(LogbackManagerBean.class);
	
	private List<ch.qos.logback.classic.Logger> listOfLoggers = new ArrayList<ch.qos.logback.classic.Logger>();
	
	public LogbackManagerBean() {
		LOG.trace("ManagedBean created {}.", LogbackManagerBean.class);
	}
	
	@PostConstruct
	public void init() {
		LOG.trace("@PostConstruct {}", LogbackManagerBean.class);
		
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		listOfLoggers = lc.getLoggerList();
		
		for (ch.qos.logback.classic.Logger log : listOfLoggers) {
			//
		}
	}
	
	public List<String> getLevels() {
		List<String> listOfLevels = new ArrayList<String>();
		
		//listOfLevels.add(Level.DEBUG);
		listOfLevels.add("DEBUG");
		//listOfLevels.add(Level.INFO);
		//listOfLevels.add(Level.TRACE);
		
		return listOfLevels;
	}
	
	public List<ch.qos.logback.classic.Logger> getLoggerList() {
		LOG.trace("{} -> getLoggerList", LogbackManagerBean.class);
		return listOfLoggers;
	}
}
