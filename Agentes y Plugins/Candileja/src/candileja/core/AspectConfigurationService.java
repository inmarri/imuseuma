package candileja.core;

import candileja.core.aspect.AspectDescription;
import candileja.core.aspect.PatternInterface;


/**
 * Modify composition rule.
 * */

public interface AspectConfigurationService {
	public void addCompositionRule(String t,String role, PatternInterface restriction, String instanceName, String aspectClass, boolean relevance,int scope,String ad);
	public void removeCompositionRuleByIP(String ip);
	public void removeCompositionRuleByAspect(String aspectClass);
	public void removeCompositionRuleByRule(String ip,AspectDescription ad);
}
