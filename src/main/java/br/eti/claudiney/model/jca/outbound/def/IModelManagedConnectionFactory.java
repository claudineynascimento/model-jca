package br.eti.claudiney.model.jca.outbound.def;

import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.ValidatingManagedConnectionFactory;

public interface IModelManagedConnectionFactory
extends
ManagedConnectionFactory,
ResourceAdapterAssociation,
ValidatingManagedConnectionFactory {
	
	void onManagedConnectionDestroyed(IModelManagedConnection managedConnection);
	
}
