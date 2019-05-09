package candileja.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Plan {
	
	private Agent scheduler;
	private List<Goal> goals;
	private Object input=null;
	
	public Plan (){
		goals=new ArrayList<Goal>();
	}
	
	protected void setAgent(Agent sc){
		scheduler=sc;
	}
	
	protected Agent getAgent(){
		return scheduler;
	}
	
	protected void setInput(Object i){
		input=i;
	}
	
	protected Object getInput(){
		return input;
	}
	
	protected void addGoal(Goal g){
		goals.add(g);
	}
	
	protected List<Goal> getGoals(){
		return goals;
	}
	
	protected abstract void execute();

}
