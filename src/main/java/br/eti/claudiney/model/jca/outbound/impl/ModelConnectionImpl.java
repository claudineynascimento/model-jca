package br.eti.claudiney.model.jca.outbound.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.Subject;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnectionMetaData;
import br.eti.claudiney.model.api.ra.outbound.def.IModelLocalTransaction;
import br.eti.claudiney.model.api.ra.outbound.def.IModelResultSetInfo;
import br.eti.claudiney.model.api.ra.outbound.def.IModelService;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelInternalConnection;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnection;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnectionAssociation;
import br.eti.claudiney.model.jca.outbound.exception.ModelConnectionAssociationException;

public class ModelConnectionImpl
implements IModelInternalConnection, IModelManagedConnectionAssociation {

	private ILogger logger = new ModelLogger(getClass());
	
	private IModelManagedConnection managedConnection;
	
	private ModelConnectionRequestInfo connectionRequestInfo;
	
	private UUID uuid = UUID.randomUUID();
	
	private boolean valid;
	
	public ModelConnectionImpl() {
		logger.info("_constructor_()");
	}
	
	public ModelConnectionImpl(
			IModelManagedConnection managedConnection,
			Subject subject, 
			ModelConnectionRequestInfo info) {
		
		logger.info("_constructor_(Subject, ConnectionRequestInfo)");
		
		this.managedConnection = managedConnection;
		
		this.connectionRequestInfo = info;
		
		valid = true;
		
	}
	
	private void validate() throws ModelResourceException {
		if(!valid) {
			throw new ModelResourceException("Invalid Connection State");
		}
	}
	
	@Override
	public void close() throws ModelResourceException {
		logger.info("close()");
		validate();
		valid = false;
		managedConnection.onConnectionClosed(this);
		managedConnection = null;
	}

	@Override
	public IModelService createInteraction() throws ModelResourceException {
		logger.info("createInteraction()");
		validate();
		return new ModelServiceEngine(this);
	}
	
	@Override
	public IModelService createService() throws ModelResourceException {
		logger.info("createService()");
		validate();
		return new ModelServiceEngine(this);
	}

	@Override
	public IModelLocalTransaction getLocalTransaction() throws ModelResourceException {
		logger.info("getLocalTransaction()");
		throw new ModelResourceException("Not Supported");
//		return null;
	}

	@Override
	public IModelConnectionMetaData getMetaData() throws ModelResourceException {
		logger.info("getLocalTransaction()");
		ModelConnectionMetaDataImpl data = new ModelConnectionMetaDataImpl();
		data.setProductName("Model");
		data.setProductVersion("1.0.0");
		data.setUsername("Anonymous");
		return data;
	}

	@Override
	public IModelResultSetInfo getResultSetInfo() throws ModelResourceException {
		logger.info("getResultSetInfo()");
		throw new ModelResourceException("Not Supported");
//		return null;
	}
	
	@Override
	public void removeAssociation() throws ModelResourceException {
		logger.info("removeAssociation()");
		managedConnection.removeConnection(this);
		managedConnection = null;
	}
	
	@Override
	public void associateManagedConnection(IModelManagedConnection managedConnection)
			throws ModelResourceException {
		
		logger.info("associateManagedConnection(IModelManagedConnection)");
		
		if( this.managedConnection != null ) {
			throw new ModelConnectionAssociationException(
					"Connection handler is already associated to another ManagedConnection instance"); 
		}
		
		this.managedConnection = managedConnection;
		
	}
	
	private String managedConnectionSignature;
	
	@Override
	public void setManagedConnectionFactorySignature(String signature) {
		this.managedConnectionSignature = signature;
	}
	
	@Override
	public Map<String, Serializable> delegateExecution(
			Map<String, Serializable> data) throws ModelResourceException {
		return managedConnection.delegateExecution(data);
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
		ModelConnectionImpl other = (ModelConnectionImpl) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName()+"("+uuid.toString()+")";
	}
	
}
