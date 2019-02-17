package br.eti.claudiney.model.jca.outbound.def;

import java.io.Serializable;
import java.util.Map;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.api.ra.outbound.def.IModelConnection;

public interface IModelInternalConnection extends IModelConnection {

	Map<String, Serializable> delegateExecution(
			Map<String, Serializable> data) throws ModelResourceException ;
	
}
