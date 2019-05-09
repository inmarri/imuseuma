package candileja.core.aspect.selfadjusting;

import candileja.core.AgentConfigurationService;

public abstract class Reconfiguration implements SelfConfiguration{
	
	private AgentConfigurationService myAgent;

	public AgentConfigurationService getAgent(){
		return myAgent;
	}

	public void setAgent(AgentConfigurationService acs) {
		myAgent= acs;	
	}
	
	public abstract void update();

}
