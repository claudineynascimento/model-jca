package br.eti.claudiney.model.jca.ra.def;

import java.io.Serializable;
import java.util.Map;

import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.work.WorkListener;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.jca.outbound.beans.ModelServiceRequest;

public interface IModelResourceAdapter
extends ResourceAdapter, Serializable, WorkListener
// , ConnectionRequestInfo
{
	
	Map<String, Serializable> invokeService(
			ModelServiceRequest request) throws ModelResourceException;

}
