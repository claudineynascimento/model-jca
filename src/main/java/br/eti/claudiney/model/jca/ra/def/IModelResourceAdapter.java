package br.eti.claudiney.model.jca.ra.def;

import java.io.Serializable;
import java.util.Map;

import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.work.WorkListener;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;

public interface IModelResourceAdapter
extends ResourceAdapter, Serializable, WorkListener
// , ConnectionRequestInfo
{
	
	Map<String, Serializable> invokeService(
			Map<String, Serializable> requestData) throws ModelResourceException;

}
