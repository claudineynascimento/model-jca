package br.eti.claudiney.model.jca.outbound.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.resource.spi.ConnectionRequestInfo;

@SuppressWarnings({"serial","unchecked"})
public class ModelConnectionRequestInfo
implements Serializable, ConnectionRequestInfo{

	private Map<String, Serializable> cache = 
			new LinkedHashMap<String, Serializable>();
	
	public void setAttribute(String key, Serializable value) {
		cache.put(key, value);
	}
	
	public <T extends Serializable> T getAttribute(String key) {
		return (T) cache.get(key);
	}
	
	public Set<String> attributeNames() {
		return Collections.unmodifiableSet(cache.keySet());
	}
	
}
