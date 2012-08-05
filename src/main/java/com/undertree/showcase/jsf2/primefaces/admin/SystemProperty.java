package com.undertree.showcase.jsf2.primefaces.admin;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class SystemProperty implements Comparable<SystemProperty> {

	private String key;
	private String value;
	
	public SystemProperty() {
	}
	
	public SystemProperty(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public int compareTo(SystemProperty systemProperty) {
		return new CompareToBuilder()
			.append(key, systemProperty.key)
			.toComparison();
	}
}
