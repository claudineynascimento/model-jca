package br.eti.claudiney.model.jca.internal.factory;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.eti.claudiney.model.jca.internal.def.ILogger;

@SuppressWarnings("serial")
public class ModelLogger implements ILogger, Serializable {
	
	private transient Logger logger;
	
	private String className;
	private String classFullName;
	
	public ModelLogger() {
		logger = Logger.getGlobal();
	}
	
	public ModelLogger(Class<?> clazz) {
		logger = Logger.getGlobal();
		className = clazz.getSimpleName();
		classFullName = clazz.getCanonicalName();
	}

	@Override
	public void info(String message) {
		logger.info(String.format("\n### %s ### %s\n", className, message));
	}

	@Override
	public void warning(String message) {
		logger.warning(String.format("\n### %s ### %s\n", className, message));
	}

	@Override
	public void severe(String message) {
		logger.severe(String.format("\n### %s ### %s\n", className, message));
	}

	@Override
	public void severe(Throwable throwable) {
		logger.log(
			Level.SEVERE, 
			String.format("\n### %s ### %s\n", className, throwable.getMessage()),
			throwable);
	}

	@Override
	public void severe(String message, Throwable throwable) {
		
		logger.log(
				Level.SEVERE, 
				String.format("\n### %s ### %s\n", className, message),
				throwable);
		
	}
	
}
