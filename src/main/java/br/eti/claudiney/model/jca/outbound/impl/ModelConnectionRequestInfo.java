package br.eti.claudiney.model.jca.outbound.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.resource.spi.ConnectionRequestInfo;

@SuppressWarnings({"serial","unchecked"})
public class ModelConnectionRequestInfo
implements Serializable, ConnectionRequestInfo{

	private String uuid = UUID.randomUUID().toString();
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelConnectionRequestInfo other = (ModelConnectionRequestInfo) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
}
