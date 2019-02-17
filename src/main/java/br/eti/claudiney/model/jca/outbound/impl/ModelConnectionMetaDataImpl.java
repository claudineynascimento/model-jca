package br.eti.claudiney.model.jca.outbound.impl;

import javax.resource.ResourceException;

import br.eti.claudiney.model.api.ra.outbound.def.IModelConnectionMetaData;

public class ModelConnectionMetaDataImpl implements IModelConnectionMetaData {

	private String productName;
	private String productVersion;
	private String username;
	
	void setProductName(String productName) {
		this.productName = productName;
	}
	
	void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	
	void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getEISProductName() throws ResourceException {
		return productName;
	}

	@Override
	public String getEISProductVersion() throws ResourceException {
		return productVersion;
	}

	@Override
	public String getUserName() throws ResourceException {
		return username;
	}
	
}
