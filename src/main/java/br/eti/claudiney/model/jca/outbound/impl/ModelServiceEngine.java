package br.eti.claudiney.model.jca.outbound.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.cci.ResourceWarning;

import org.apache.commons.io.IOUtils;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnection;
import br.eti.claudiney.model.api.ra.outbound.def.IModelService;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelInternalConnection;

public class ModelServiceEngine implements IModelService {

	private ILogger logger = new ModelLogger(getClass());
	
	private IModelInternalConnection connection;
	
	public ModelServiceEngine(IModelInternalConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public void clearWarnings() throws ModelResourceException {
		logger.info("clearWarnings()");
		throw new ModelResourceException("Unavailable");
	}

	@Override
	public void close() throws ModelResourceException {
		logger.info("close()");
		throw new ModelResourceException("Unavailable");
	}

	@Override
//	public Record execute(
	public MappedRecord execute(
			InteractionSpec spec,
			Record record) throws ModelResourceException {
		
		logger.info("<MappedRecord> execute(InteractionSpec, Record)");
		
//		throw new ModelResourceException("Unavailable");
		
		MappedRecord input = (MappedRecord) record; 
		
		Map<String, Serializable> data = new LinkedHashMap<>();
		
		Set<Map.Entry<String, Serializable>> entries = input.entrySet();
		for(Map.Entry<String, Serializable> entry: entries) {
			data.put(entry.getKey(), entry.getValue());
		}
		
		Map<String, Serializable> info = connection.delegateExecution(data);

		ModelServiceResponse response = new ModelServiceResponse();
		response.put("resourceData", info.get("resourceData"));
		
		return response;
		
	}

	@Override
	public boolean execute(
			InteractionSpec spec,
			Record input,
			Record output) throws ResourceException {
		
		logger.info("<boolean> execute(InteractionSpec, Record, Record)");
		
		return false;
		
	}

	@Override
	public ResourceWarning getWarnings() throws ResourceException {
		
		logger.info("<ResourceWarning> getWarnings()");
		
		throw new ModelResourceException("Unavailable");
		
//		return null;
		
	}

	@Override
	public IModelConnection getConnection() {
		logger.info("<Connection> getWarnings()");
		return connection;
	}

}
