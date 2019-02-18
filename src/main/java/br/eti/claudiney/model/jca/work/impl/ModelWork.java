package br.eti.claudiney.model.jca.work.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.resource.ResourceException;
import javax.resource.spi.ResourceAdapter;

import br.eti.claudiney.icap.client.ICAPClient;
import br.eti.claudiney.icap.client.ICAPException;
import br.eti.claudiney.icap.client.ICAPResponse;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.beans.ModelServiceRequest;
import br.eti.claudiney.model.jca.outbound.def.IModelWork;

public class ModelWork implements IModelWork {

	private ILogger logger = new ModelLogger(getClass());
	
	private String uuid = UUID.randomUUID().toString();
	
	private ResourceAdapter resourceAdapter;
	
	private Map<String, Serializable> responseData =
			new LinkedHashMap<>();
	
	public ModelWork() {
		
	}
	
	private ModelServiceRequest request;
	
	public ModelWork(ModelServiceRequest request) {
		this.request = request;
	}
	
	@Override
	// From ResourceAdapterAssociation
	public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
		this.resourceAdapter = resourceAdapter;
	}
	
	@Override
	// From ResourceAdapterAssociation
	public ResourceAdapter getResourceAdapter() {
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
		}
	}

	@Override
	public void release() {
		logger.info("<<< "+uuid+" | releasing >>>");
//		isJobCompleted = false;
//		requestData.clear();
//		responseData.clear();
	}
	
	@Override
	public void finalize() throws Throwable {
		logger.info("<<< "+uuid+" | java Object >>>: finalize()");
	}
	
	private void loadData() {
		
		ICAPClient client = new ICAPClient(
				request.getServiceHost(), request.getServicePort());
		
		File file = new File("c:\\temp\\eicar.com.txt");
		
		ICAPResponse response = null;
		try {
			response = client.virus_scan(file);
//			response = client.info();
			responseData.clear();
			if( response.getBody() != null ) {
				responseData.put("content", response.getBody());
			} else {
				responseData.put("content", "<EMPTY>".getBytes());
			}
		} catch(ICAPException e) {
			failure = e;
		}
		
	}

}
