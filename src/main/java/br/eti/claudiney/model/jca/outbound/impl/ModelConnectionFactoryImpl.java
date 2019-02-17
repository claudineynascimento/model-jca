package br.eti.claudiney.model.jca.outbound.impl;

import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.RecordFactory;
import javax.resource.spi.ConnectionManager;

import br.eti.claudiney.model.api.ra.def.IModelResourceAdapterMetaData;
import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
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
	private ModelConnectionRequestInfo connectionRequestInfo; 
	
	public ModelConnectionFactoryImpl() {
		Logger.getGlobal().info("_constructor_()");
	}
	
	public ModelConnectionFactoryImpl(
			IModelManagedConnectionFactory managedConnectionFactory,
			ConnectionManager connectionManager,
			ModelConnectionRequestInfo connectionRequestInfo) {
		
		logger.info("_constructor_(ManagedConnectionFactory, ConnectionManager, ConnectionRequestInfo)");
		
		this.managedConnectionFactory = managedConnectionFactory;
		this.connectionManager = connectionManager;
		this.connectionRequestInfo = connectionRequestInfo;
		
	}
	
	@Override
	public IModelConnection getConnection() throws ModelResourceException {
		
		logger.info("getConnection()");
		
		IModelConnection connection = null;
		
		logger.info("getConnection()\n>>> delegate <<< ConnectionManager.allocateConnection(ManagedConnectionFactory, ConnectionRequestInfo)");
		try {
			
			connection = (IModelConnection) connectionManager.allocateConnection(
				managedConnectionFactory,
				connectionRequestInfo);
			
			logger.info("getConnection()\n>>> Connection Allocated >>> "+connection.toString());
			
		} catch(ResourceException e) {
			throw new ModelResourceException(e);
		}
		
		return connection;
		
	}

	@Override
	public IModelConnection getConnection(ConnectionSpec spec) throws ModelResourceException {
		logger.info("getConnection(ConnectionSpec)");
		throw new ModelResourceException("Not supported");
	}

	@Override
	public IModelResourceAdapterMetaData getMetaData() throws ModelResourceException {
		logger.info("getMetaData()");
		throw new ModelResourceException("Unavailable");
//		return null;
	}

	@Override
	public RecordFactory getRecordFactory() throws ResourceException {
		logger.info("getRecordFactory()");
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
		logger.info("getReference()");
		return this.reference;
	}

}
