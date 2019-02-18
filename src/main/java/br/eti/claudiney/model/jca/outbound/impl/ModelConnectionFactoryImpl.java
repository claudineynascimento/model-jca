package br.eti.claudiney.model.jca.outbound.impl;

import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.RecordFactory;
import javax.resource.spi.ConnectionManager;

import br.eti.claudiney.model.api.ra.def.IModelResourceAdapterMetaData;
import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.beans.ModelConnectionSpec;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnection;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnectionFactory;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnectionFactory;

@SuppressWarnings("serial")
public class ModelConnectionFactoryImpl implements IModelConnectionFactory {

	private ILogger logger = new ModelLogger(getClass());
	
	private IModelManagedConnectionFactory managedConnectionFactory;
	private ConnectionManager connectionManager;
	
	public ModelConnectionFactoryImpl() {
	}
	
	public ModelConnectionFactoryImpl(
			IModelManagedConnectionFactory managedConnectionFactory,
			ConnectionManager connectionManager) {
		
		this.managedConnectionFactory = managedConnectionFactory;
		this.connectionManager = connectionManager;
		
	}
	
	@Override
	public IModelConnection getConnection() throws ModelResourceException {
		
		IModelConnection connection = null;
		
		/* It all starts here */
		ModelConnectionRequestInfo info = new ModelConnectionRequestInfo();
		
		try {
			
			connection = (IModelConnection) connectionManager.allocateConnection(
				managedConnectionFactory,
				info);
			
		} catch(ResourceException e) {
			throw new ModelResourceException(e);
		}
		
		return connection;
		
	}

	@Override
	public IModelConnection getConnection(ConnectionSpec spec) throws ModelResourceException {
		
		if( spec == null ) {
			throw new ModelResourceException("<spec> argument cannot be null");
		}
		
		if( ! (spec instanceof ModelConnectionSpec) ) {
			throw new ModelResourceException("<spec> argument must be an instance of class <" + ModelConnectionSpec.class.getCanonicalName() +">");
		}
		
		
		IModelConnection connection = null;
		
		ModelConnectionSpec modelSpec = (ModelConnectionSpec)spec;
		
		/* It all starts also here */
		ModelConnectionRequestInfo info = new ModelConnectionRequestInfo();
		
		Set<String> specAttrs = modelSpec.attributes();
		
		for( String attribute: specAttrs ) {
			info.setAttribute(attribute, modelSpec.getAttribute(attribute));
		}
		
		try {
			
			connection = (IModelConnection) connectionManager.allocateConnection(
				managedConnectionFactory,
				info);
			
		} catch(ResourceException e) {
			throw new ModelResourceException(e);
		}
		
		return connection;
		
	}

	@Override
	public IModelResourceAdapterMetaData getMetaData() throws ModelResourceException {
		throw new ModelResourceException("Unavailable");
//		return null;
	}

	@Override
	public RecordFactory getRecordFactory() throws ResourceException {
		throw new ModelResourceException("Not supported");
//		return null;
	}

	private Reference reference;
	
	@Override
	public void setReference(Reference reference) {
		logger.info("setReference("+reference.getClass().getCanonicalName()+")");
		this.reference = reference;
	}

	@Override
	public Reference getReference() throws NamingException {
		return this.reference;
	}

}
