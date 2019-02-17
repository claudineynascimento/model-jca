package br.eti.claudiney.model.jca.internal.factory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import br.eti.claudiney.model.jca.internal.def.ILogger;

public class LoggerFactory {

	@Produces
	public ILogger getInstance(InjectionPoint point) {
		return new ModelLogger(point.getBean().getBeanClass());
	}
	
}
