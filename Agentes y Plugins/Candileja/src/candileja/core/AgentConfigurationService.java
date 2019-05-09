package candileja.core;


public interface AgentConfigurationService extends AspectConfigurationService {
	public void addKnowledge(String id,Object obj);
	public Object getKnowledge(String id);
	public void addContext(String id,Context ctx);
	public void updateContext(String id,Object obj);
	public void addFacet(String id,Facet f);
	public Facet getFacet(String id);
	public void addPlan(PlanDescription p);
	public void addComponent(String id,Object comp);
	public Object getComponent(String id);
	
}
