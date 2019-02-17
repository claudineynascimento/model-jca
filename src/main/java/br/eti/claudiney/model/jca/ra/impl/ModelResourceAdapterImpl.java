package br.eti.claudiney.model.jca.ra.impl;

import java.io.Serializable;
import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.AuthenticationMechanism;
import javax.resource.spi.AuthenticationMechanism.CredentialInterface;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkEvent;
import javax.resource.spi.work.WorkException;
import javax.transaction.xa.XAResource;

import br.eti.claudiney.model.api.ra.exceptions.ModelResourceException;
import br.eti.claudiney.model.jca.internal.def.ILogger;
import br.eti.claudiney.model.jca.internal.factory.ModelLogger;
import br.eti.claudiney.model.jca.outbound.def.IModelWork;
import br.eti.claudiney.model.jca.ra.def.IModelResourceAdapter;
import br.eti.claudiney.model.jca.work.impl.ModelWork;

@Connector(
	transactionSupport = TransactionSupportLevel.NoTransaction,
//	 transactionSupport = TransactionSupportLevel.LocalTransaction,
	// transactionSupport = TransactionSupportLevel.XATransaction,
	authMechanisms = {
		@AuthenticationMechanism(
				authMechanism = "BasicPassword",
				credentialInterface = CredentialInterface.GenericCredential), 
	},
	reauthenticationSupport = false
)
@SuppressWarnings("serial")
public class ModelResourceAdapterImpl implements IModelResourceAdapter {

	private ILogger logger = new ModelLogger(getClass());
	
	@Override
	public void endpointActivation(
			MessageEndpointFactory factory,
			ActivationSpec spec) throws ResourceException {

		logger.info("endpointActivation(MessageEndpointFactory, ActivationSpec)");
		
	}

	@Override
	public void endpointDeactivation(
			MessageEndpointFactory factory,
			ActivationSpec spec) {

		logger.info("endpointDeactivation(MessageEndpointFactory, ActivationSpec)");
		
	}

	@Override
	public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
		logger.info("getXAResources(ActivationSpec[])");
		return null;
	}

	private BootstrapContext context;
	@Override
	public void start(BootstrapContext context) throws ResourceAdapterInternalException {
		logger.info("start(BootstrapContext) (can be used to start a Work)");
		this.context = context;
	}
	
	//--------------------------------------------------------
	
	@Override
	public Map<String, Serializable> invokeService(Map<String, Serializable> requestData)
			throws ModelResourceException {
		
		logger.info("invokeService(data)");
		
		IModelWork work = new ModelWork(requestData);
		
		try {
			context.getWorkManager().scheduleWork(work, 0, new ExecutionContext(), this);
			synchronized(work) {
				if(!work.jobCompleted()) {
					logger.info("invokeService(): ### WAITING ###");
					work.wait(5000);
				}
			}
		} catch(WorkException | InterruptedException e) {
			logger.severe(e);
		}
		
		logger.info("invokeService(): ### COMPLETED ###");
		
		if( ! work.jobCompleted() ) {
			throw new ModelResourceException("Work Timeout");
		}
		
		return work.getData();
		
	}

	//---------------------- WorkListener --------------------
	
	@Override
	public void stop() {
		logger.info("stop()");
	}

	@Override
	public void workAccepted(WorkEvent event) {
		logger.info("workAccepted(WorkEvent)");
	}

	@Override
	public void workCompleted(WorkEvent event) {
		logger.info("workCompleted(WorkEvent)");
	}

	@Override
	public void workRejected(WorkEvent event) {
		logger.info("workRejected(WorkEvent)");
	}

	@Override
	public void workStarted(WorkEvent event) {
		logger.info("workStarted(WorkEvent)");
	}
	
//	//------------- Bean Properties -------------------
//	
//	private String configData;
//	
////	@ConfigProperty(defaultValue = "???")
//	@ConfigProperty
//	public void setConfigData(String configData) {
//		logger.info("### ResourceAdapterImpl ### PROPERTY: setConfigData(???)");
//		this.configData = configData;
//	}
//	
//	public String getConfigData() {
//		logger.info("### ResourceAdapterImpl ### PROPERTY: getConfigData()");		
//		return configData;
//	}

}
