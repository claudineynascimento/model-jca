package br.eti.claudiney.model.jca.outbound.def;

import java.io.Serializable;
import java.util.Map;

import javax.resource.cci.Connection;
import javax.resource.spi.ManagedConnection;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnection;
import br.eti.claudiney.model.jca.ra.def.IModelResourceAdapter;

public interface IModelManagedConnection extends ManagedConnection {
	
	void setResourceAdapter(IModelResourceAdapter resourceAdapter);
	
	IModelResourceAdapter getResourceAdapter();

	void onConnectionClosed(IModelConnection connection);
	
	void removeConnection(IModelConnection connection);
	
	void registerEvent(int eventType, Connection connection);
	
	Map<String, Serializable> delegateExecution(
			Map<String, Serializable> data) throws ModelResourceException;
	
}
