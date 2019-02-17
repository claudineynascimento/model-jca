package br.eti.claudiney.model.jca.work.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.resource.ResourceException;
import javax.resource.spi.ResourceAdapter;

import org.apache.commons.io.IOUtils;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelWork;

public class ModelWork implements IModelWork {

	private ILogger logger = new ModelLogger(getClass());
	
	private String uuid = UUID.randomUUID().toString();
	
	private ResourceAdapter resourceAdapter;
	
	private Map<String, Serializable> requestData = 
			new LinkedHashMap<>();
	
	private Map<String, Serializable> responseData =
			new LinkedHashMap<>();
	
	public ModelWork() {
		
	}
	
	public ModelWork(Map<String, Serializable> requestData) {
		logger.info(">>> "+uuid+" constructor initialisation <<< ");
		this.requestData.putAll(requestData);
	}
	
	@Override
	// From ResourceAdapterAssociation
	public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
		logger.info("setResourceAdapter(ResourceAdapter)");
		this.resourceAdapter = resourceAdapter;
	}
	
	@Override
	// From ResourceAdapterAssociation
	public ResourceAdapter getResourceAdapter() {
		logger.info("getResourceAdapter()");
		return this.resourceAdapter;
	}
	
	private boolean isJobCompleted = false;
	
	@Override
	public boolean jobCompleted() {
		return isJobCompleted;
	}
	
	@Override
	public Map<String, Serializable> getData() {
		return Collections.unmodifiableMap(responseData);
	}
	
	private Throwable failure;
	
	@Override
	public Throwable getFailure() {
		return failure;
	}
	
	@Override
	public void run() {
		logger.info("<<< "+uuid+" | running >>>");
		if(isJobCompleted) {
			throw new IllegalStateException("<<Work>> has already completed task and cannot be reused");
		}
		loadData();
		synchronized(this) {
			this.isJobCompleted = true;
			this.notifyAll();
		}
	}

	@Override
	public void release() {
		logger.info("<<< "+uuid+" | releasing >>>");
		isJobCompleted = false;
		requestData.clear();
		responseData.clear();
	}
	
	@Override
	public void finalize() throws Throwable {
		logger.info("<<< "+uuid+" | java Object >>>: finalize()");
	}
	
	private void loadData() {
		
		URL url = null;
		try {
			url = new URL( (String) requestData.get("url") );
		} catch(Exception e) {
			failure = new ModelResourceException("Requested Resource Unavailable (001)");
			return;
		}
		
		ByteArrayOutputStream cache = new ByteArrayOutputStream();
		
		try {
			IOUtils.copy(url.openStream(), cache);
		} catch(IOException e) {
			failure = new ModelResourceException("Requested Resource Unavailable (002)");
			return;
		}
		
		responseData.clear();
		responseData.put("resourceData", cache.toByteArray());
		
	}

}
