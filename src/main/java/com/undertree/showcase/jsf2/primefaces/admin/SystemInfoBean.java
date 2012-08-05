package com.undertree.showcase.jsf2.primefaces.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@RequestScoped
public class SystemInfoBean implements Serializable {

	private static final long serialVersionUID = 20120302L;
	static final Logger LOG = LoggerFactory.getLogger(SystemInfoBean.class);
	
	private List<SystemProperty> systemEnvironmentVariables = new ArrayList<SystemProperty>();
	private List<SystemProperty> jvmSystemProperties = new ArrayList<SystemProperty>();
	
	private Date jvmStartTime;
	private String jvmUptime;

	public SystemInfoBean() {
		LOG.trace("Creating SystemInfoBean");
	}
	
	@PostConstruct
	public void initInfo() {
		LOG.trace("@PostConstruct SystemInfoBean.initInfo");
		
		jvmStartTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
		jvmUptime = DurationFormatUtils.formatDurationHMS(ManagementFactory.getRuntimeMXBean().getUptime());
		
		
		for (Map.Entry<String, String> property : System.getenv().entrySet()) {
			systemEnvironmentVariables.add(new SystemProperty(property.getKey(), property.getValue()));
		}
		
		Collections.sort(systemEnvironmentVariables);
		
		for (Entry<Object, Object> p : System.getProperties().entrySet()) {
			jvmSystemProperties.add(new SystemProperty(p.getKey().toString(), p.getValue().toString()));
		}
		
		Collections.sort(jvmSystemProperties);
	}

	public int getAvailableProcessors() {
		LOG.trace("Calling SystemInfoBean.getAvailableProcessors");
		return Runtime.getRuntime().availableProcessors();
	}
	
	public long getFreeMemory() {
		LOG.trace("Calling SystemInfoBean.getFreeMemory");
		return Runtime.getRuntime().freeMemory() / 1048576;
	}
	
	public long getMaxMemory() {
		return Runtime.getRuntime().maxMemory() / 1048576;
	}
	
	public long getTotalMemory() {
		return Runtime.getRuntime().totalMemory() / 1048576;
	}
	
	public long getUsedMemory() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
	}
	
	
	public Date getJVMStartTime() {
		return jvmStartTime;
	}
	
	public String getJVMUptime() {
		return jvmUptime;
	}
	
	// not sure where this name comes from
	public String getName() {
		return ManagementFactory.getRuntimeMXBean().getName();
	}
	
	public String getVmName() {
		return SystemUtils.JAVA_VM_NAME;
	}
	
	public String getVmVendor() {
		return SystemUtils.JAVA_VM_VENDOR;
	}
	
	public String getVmVersion() {
		return SystemUtils.JAVA_VM_VERSION;
	}
	
	public String getJavaVersion() {
		return SystemUtils.JAVA_VERSION;
	}

	public List<SystemProperty> getSystemEnv() {
		LOG.trace("Calling SystemInfoBean.getSystemEnv");
		return systemEnvironmentVariables;
	}
	
	public List<SystemProperty> getSystemProps() {
		LOG.trace("Calling SystemInfoBean.getSystemProps");
		return jvmSystemProperties;
	}
}
