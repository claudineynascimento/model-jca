package br.eti.claudiney.model.jca.outbound.impl;

import javax.resource.ResourceException;

import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelLocalTransactionSPI;

public class ModelLocalTransactionSPI implements IModelLocalTransactionSPI {

	private ILogger logger = new ModelLogger(getClass());
	
	@Override
	public void begin() throws ResourceException {
		logger.info("begin()");
	}

	@Override
	public void commit() throws ResourceException {
		logger.info("commit()");
	}

	@Override
	public void rollback() throws ResourceException {
		logger.info("rollback()");
	}

}
