package br.eti.claudiney.model.jca.outbound.def;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;

public interface IModelManagedConnectionAssociation {
	
	void setManagedConnectionFactorySignature(String signature);
	
	void removeAssociation() throws ModelResourceException;
	
	void associateManagedConnection(
			IModelManagedConnection managedConnection) throws ModelResourceException;
	
}
