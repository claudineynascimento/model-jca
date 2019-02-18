package br.eti.claudiney.model.jca.outbound.beans;

import java.io.Serializable;
import java.util.Map;

public class ModelServiceRequest {
	
	private Map<String, Serializable> requestData;
	
	public void setRequestData(Map<String, Serializable> requestData) {
		this.requestData = requestData;
	}
	
	public Map<String, Serializable> getRequestData() {
		return requestData;
	}
	
	private String serviceHost;
	
	public String getServiceHost() {
		return serviceHost;
	}
	
	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}
	
	private Integer servicePort;
	
	public Integer getServicePort() {
		return servicePort;
	}
	
	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}

}
