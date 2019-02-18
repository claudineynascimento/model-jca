package br.eti.claudiney.model.jca.outbound.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnection;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.beans.ModelServiceRequest;
import br.eti.claudiney.model.jca.outbound.def.IModelLocalTransactionSPI;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnection;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnectionAssociation;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnectionFactory;
import br.eti.claudiney.model.jca.ra.def.IModelResourceAdapter;

public class ModelManagedConnectionImpl implements IModelManagedConnection {

	private ILogger logger = new ModelLogger(getClass());
	
	private IModelManagedConnectionFactory factory;
	
	private List<ConnectionEventListener> listeners = 
			new LinkedList<ConnectionEventListener>();
	
	private List<IModelConnection> connectionPool = 
			new LinkedList<IModelConnection>();
	
	private UUID uuid = UUID.randomUUID();
	
	public ModelManagedConnectionImpl() {
	}
	
	public ModelManagedConnectionImpl(
			IModelManagedConnectionFactory factory,
			Subject subject) throws ResourceException {
		
		this.factory = factory;
		
		if( subject == null ) {
//			throw new ModelResourceException("Credenciais n\u00E3o informadas");
			return;
		}
		
//		Set<java.security.Principal> principals = subject.getPrincipals();
//		Set<?> privateCredentials = subject.getPrivateCredentials();
//		Set<?> publicCredentials = subject.getPublicCredentials();
		
//		for( Object p: privateCredentials ) {
//			javax.resource.spi.security.PasswordCredential passwordCredential = 
//					(javax.resource.spi.security.PasswordCredential)p;
//			String username = passwordCredential.getUserName();
//			String password = new String(passwordCredential.getPassword());
//			break;
//		}
//		
//		for(Object p: publicCredentials) {
//			javax.resource.spi.security.PasswordCredential passwordCredential = 
//					(javax.resource.spi.security.PasswordCredential)p;
//			String password = new String(passwordCredential.getPassword());
//		}
		
	}

	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		listeners.add(listener);
	}

	/*
	 * The associateConnection method implementation for a ManagedConnection
	 * should dissociate the connection handle passed as a parameter from its currently
	 * associated ManagedConnection and associate the new connection handle with
	 * itself.
	 * 
	 * Note that the switching of connection associations must happen only for connection
	 * handles and ManagedConnection instances that correspond to the same
	 * ManagedConnectionFactory instance. The container should enforce this restriction
	 * in an implementation-specific manner. If a container cannot enforce the restriction,
	 * the container should not use the connection association mechanism.
	 * 
	 * @See javax.resource.spi.ManagedConnection#associateConnection(Object)
	 */
	@Override
	public void associateConnection(Object connection) throws ResourceException {
		
		IModelConnection mConn = (IModelConnection) connection;
		
		IModelManagedConnectionAssociation association = 
				(IModelManagedConnectionAssociation)connection;
		
		if( ! connectionPool.contains(mConn) ) {
			association.removeAssociation();
			association.associateManagedConnection(this);
			connectionPool.add(mConn);
		}
		
	}

	/*
	 * The cleanup must invalidate all connection handles created using the ManagedConnection instance.
	 * 
	 * Any attempt by an application component to use the associated connection handle after 
	 * cleanup of the underlying ManagedConnection should result in an exception.
	 * 
	 * @see javax.resource.spi.ManagedConnection#cleanup()
	 */
	@Override
	public void cleanup() throws ResourceException {
		logger.info("<<<"+uuid+">>> cleanup() >>> GO BACK HERE <<<");
	}

	/*
	 * An application server should explicitly call ManagedConnection.destroy
	 * to destroy a physical connection. An application server should destroy 
	 * a physical connection to manage the size of its connection pool 
	 * and to reclaim system resources.
	 * 
	 * A resource adapter should destroy all allocated system resources for this
	 * ManagedConnection instance when the method destroy is called.
	 * 
	 * @see javax.resource.spi.ManagedConnection#destroy()
	 */
	@Override
	public void destroy() throws ResourceException {
		factory.onManagedConnectionDestroyed(this);
	}

	@Override
//	public Object getConnection(
	public IModelConnection getConnection(
			Subject subject, 
			ConnectionRequestInfo info) throws ResourceException {
		
		/* Validate <Subject> if necessary */
		
		/* Validate <ConnectionRequestInfo> if necessary */
		
		IModelConnection connection = new ModelConnectionImpl(this);
		
		connectionPool.add(connection);
		
		return connection;
		
	}

	@Override
	public IModelLocalTransactionSPI getLocalTransaction() throws ResourceException {
		throw new ModelResourceException("Not Supported"); 
//		return new ModelLocalTransactionSPI();
	}

	@Override
	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		throw new ModelResourceException("Not Supported");
//		return null;
	}

	@Override
	public XAResource getXAResource() throws ResourceException {
		throw new ModelResourceException("Not Supported");
//		return null;
	}

	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		listeners.remove(listener);
	}

	private PrintWriter log;
	
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return log;
	}

	@Override
	public void setLogWriter(PrintWriter writer) throws ResourceException {
		log = writer;
	}
	
	public void registerEvent(int eventType, Connection connection) {
		
		ConnectionEvent event = new ConnectionEvent(this, eventType);
		event.setConnectionHandle(connection);
		
		switch(eventType) {
		case ConnectionEvent.CONNECTION_CLOSED:
			logger.info("<<<"+uuid+">>> registerEvent('CONNECTION_CLOSED')");
			for(ConnectionEventListener listener: listeners) {
				listener.connectionClosed(event);
			} break;
		case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
			logger.info("<<<"+uuid+">>> registerEvent('LOCAL_TRANSACTION_STARTED')");
			for(ConnectionEventListener listener: listeners) {
				listener.localTransactionStarted(event);
			} break;
		case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
			logger.info("<<<"+uuid+">>> registerEvent('LOCAL_TRANSACTION_COMMITTED')");
			for(ConnectionEventListener listener: listeners) {
				listener.localTransactionCommitted(event);
			} break;
		case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
			logger.info("<<<"+uuid+">>> registerEvent('LOCAL_TRANSACTION_ROLLEDBACK')");
			for(ConnectionEventListener listener: listeners) {
				listener.localTransactionRolledback(event);
			} break;
		}
		
	}
	
	/*
	 * This method is expected to be invoked by connection closing event
	 * or connection dissociation e re-association to another (can be the same)
	 * ManagedConnection context.
	 * 
	 * Only remove the passed Connection Handler from ManagedConnection context.
	 */
	@Override
	public void removeConnection(IModelConnection connection) {
		connectionPool.remove(connection);
	}
	
	@Override
	public void onConnectionClosed(IModelConnection connection) {
		removeConnection(connection);
		registerEvent(ConnectionEvent.CONNECTION_CLOSED, connection);
	}
	
	private IModelResourceAdapter resourceAdapter;
	
	@Override
	public void setResourceAdapter(IModelResourceAdapter resourceAdapter) {
		this.resourceAdapter = resourceAdapter;
	}
	
	@Override
	public IModelResourceAdapter getResourceAdapter() {
		return resourceAdapter;
	}
	
	@Override
	public Map<String, Serializable> delegateExecution(
			Map<String, Serializable> data) throws ModelResourceException {
		
		ModelServiceRequest request = new ModelServiceRequest();
		
		request.setRequestData(data);
		request.setServiceHost(factory.getServiceHost());
		request.setServicePort(factory.getServicePort());
		
		return resourceAdapter.invokeService(request);
		
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
		ModelManagedConnectionImpl other = (ModelManagedConnectionImpl) obj;
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
