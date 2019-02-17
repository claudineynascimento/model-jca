package br.eti.claudiney.model.jca.internal.def;

public interface ILogger {

	void info(String message);
	
	void warning(String message);
	
	void severe(String message);
	
	void severe(Throwable throwable);
	
	void severe(String message, Throwable throwable);
	
}
