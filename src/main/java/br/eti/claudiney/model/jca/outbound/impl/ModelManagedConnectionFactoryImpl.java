package br.eti.claudiney.model.jca.outbound.impl;

import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.resource.ResourceException;
import javax.resource.spi.ConfigProperty;
import javax.resource.spi.ConnectionDefinition;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ResourceAdapter;
import javax.security.auth.Subject;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnection;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnectionFactory;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnection;
import br.eti.claudiney.model.jca.outbound.def.IModelManagedConnectionFactory;
import br.eti.claudiney.model.jca.ra.def.IModelResourceAdapter;

@ConnectionDefinition(
		connectionFactory = IModelConnectionFactory.class,
		connectionFactoryImpl = ModelConnectionFactoryImpl.class,
		connection = IModelConnection.class,
		connectionImpl = ModelConnectionImpl.class
	)
@SuppressWarnings({"serial","rawtypes"})
public class ModelManagedConnectionFactoryImpl implements IModelManagedConnectionFactory {

	private ILogger logger = new ModelLogger(getClass());
	
	private static AtomicInteger atomicInteger = new AtomicInteger();
	
	private final Integer FACTORY_ID = Integer.valueOf(atomicInteger.incrementAndGet());
	
	private List<IModelConnectionFactory> connectionFactoryPool = 
			new LinkedList<IModelConnectionFactory>();
	
	private List<IModelManagedConnection> managedConnectionPool = 
			new LinkedList<IModelManagedConnection>();
	
	private IModelResourceAdapter resourceAdapter;
	
	public ModelManagedConnectionFactoryImpl() {
		logger.info("_constructor_()");
	}
	
	@Override
	public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
		this.resourceAdapter = (IModelResourceAdapter) resourceAdapter;
	}
	
	@Override
	public IModelResourceAdapter getResourceAdapter() {
		return this.resourceAdapter;
	}
	
	@Override
//	public Object createConnectionFactory() throws ResourceException {
	public IModelConnectionFactory createConnectionFactory() throws ResourceException {
		throw new ModelResourceException("Unavailable");
	}

	@Override
//	public Object createConnectionFactory(
	public IModelConnectionFactory createConnectionFactory(
			ConnectionManager manager) throws ResourceException {

		IModelConnectionFactory factory = 
				new ModelConnectionFactoryImpl(this, manager);
		
		connectionFactoryPool.add(factory);
		
		return factory;
		
	}
	
	@Override
//	public ManagedConnection createManagedConnection(
	public IModelManagedConnection createManagedConnection(
			Subject subject,
			ConnectionRequestInfo info)
			throws ResourceException {

		// Validate <ConnectionRequestInfo> if necessary
		
		IModelManagedConnection managed = new ModelManagedConnectionImpl(
				this, subject);
		
		managed.setResourceAdapter(resourceAdapter);
		
		managedConnectionPool.add(managed);
		
		return managed;
		
	}
	
	@Override
//	public ManagedConnection matchManagedConnections(
	public IModelManagedConnection matchManagedConnections(
			Set set,
			Subject subject,
			ConnectionRequestInfo info)
			throws ModelResourceException {
		
		// Validate <Subject> if necessary
//		if( subject == null ) {
//			return null;
//		}
		
		// Validate <ConnectionRequestInfo> if necessary
		
		IModelManagedConnection c = null;
		
		if( set != null ) {
			for( Object mc: set ) {
				if( managedConnectionPool.contains(mc) ) {
					if( c == null ) {
						c = (IModelManagedConnection) mc; 
					}
				}
			}
		}

		return c;
		
	}
	
	@Override
	public void onManagedConnectionDestroyed(IModelManagedConnection managedConnection) {
		managedConnectionPool.remove(managedConnection);
	}
	
	@Override
//	public Set getInvalidConnections(
	public Set<IModelManagedConnection> getInvalidConnections(
			Set managedConnectionPool) throws ModelResourceException {
		
		Set<IModelManagedConnection> invalidConnectionPool = 
				new LinkedHashSet<>();
		
		for(Object connection: managedConnectionPool) {
			if( !this.managedConnectionPool.contains(connection) ) {
				invalidConnectionPool.add( (IModelManagedConnection)connection );
			}
		}
		
		return invalidConnectionPool;
		
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

	@Override
	// Required From JCA 1.6 Specification
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FACTORY_ID == null) ? 0 : FACTORY_ID.intValue());
		return result;
	}

	@Override
	// Required From JCA 1.6 Specification
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelManagedConnectionFactoryImpl other = (ModelManagedConnectionFactoryImpl) obj;
		if (FACTORY_ID == null) {
			if (other.FACTORY_ID != null)
				return false;
		} else if (!FACTORY_ID.equals(other.FACTORY_ID))
			return false;
		return true;
	}
	
	//------------ Bean Properties ----------
	
	private String serviceHost;
	private Integer servicePort;
	
	@ConfigProperty(defaultValue = "localhost")
	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}
	
	public String getServiceHost() {
		return serviceHost;
	}
	
	@ConfigProperty(defaultValue = "0")
	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}
	
	public Integer getServicePort() {
		return servicePort;
	}
	
}
