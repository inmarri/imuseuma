package candileja.core.aspect.selfadjusting;

import candileja.core.AgentConfigurationService;

public interface SelfConfiguration {
	public void update();
	public void setAgent(AgentConfigurationService acs);
}
