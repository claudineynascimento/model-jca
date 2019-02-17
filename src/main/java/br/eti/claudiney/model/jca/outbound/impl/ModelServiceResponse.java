package br.eti.claudiney.model.jca.outbound.impl;

import java.util.LinkedHashMap;

import javax.resource.cci.MappedRecord;

@SuppressWarnings({"serial","rawtypes"})
public class ModelServiceResponse
extends LinkedHashMap
implements MappedRecord {

	private String recordName;
	
	@Override
	public String getRecordName() {
		return recordName;
	}

	@Override
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	private String recordShortDescription;
	
	@Override
	public String getRecordShortDescription() {
		return recordShortDescription;
	}
	
	@Override
	public void setRecordShortDescription(String recordShortDescription) {
		this.recordShortDescription = recordShortDescription;
	}

}
