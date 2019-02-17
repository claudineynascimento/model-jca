package br.eti.claudiney.model.jca.outbound.exception;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;

@SuppressWarnings("serial")
public class ModelConnectionAssociationException extends ModelResourceException {

	public ModelConnectionAssociationException() {
		
	}
	
	public ModelConnectionAssociationException(String message) {
		super(message);
	}
	
}
