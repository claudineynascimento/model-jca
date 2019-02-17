package br.eti.claudiney.model.jca.outbound.def;

import java.io.Serializable;
import java.util.Map;

import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.work.Work;

public interface IModelWork extends Work, ResourceAdapterAssociation {

	boolean jobCompleted();
	
	Map<String, Serializable> getData();
	
	Throwable getFailure();
	
}
