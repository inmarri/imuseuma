package candileja.core;

import java.util.List;

public interface AgentContextInterface {
	public void addKnowledge(String id,Object obj);
	public Object getKnowledge(String id);
	public Context getContext(String id);
	public Effector getEffector(String id);
	public List<Goal> getGoals();
	public Object getComponent(String id);
	public int hashCode();
	public boolean equals(Object obj);

}
