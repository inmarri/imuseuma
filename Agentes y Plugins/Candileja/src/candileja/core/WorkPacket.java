package candileja.core;

import java.util.LinkedList;
import java.util.List;

public class WorkPacket {
	
	private Goal goal;
	private List<PlanDescription> plans;
	private Object input;
	
	public WorkPacket(){
		plans=new LinkedList<PlanDescription>();
	}
	
	public void addPlan(PlanDescription p){
		plans.add(p);
	}
	
	public void removePlan(PlanDescription p){
		plans.remove(p);
	}
	
	public List<PlanDescription> getPlans(){
		return plans;
	}
	
	public void setPlans(List<PlanDescription> p){
		plans=p;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public Object getInput() {
		return input;
	}

	public void setInput(Object input) {
		this.input = input;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((plans == null) ? 0 : plans.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkPacket other = (WorkPacket) obj;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (plans == null) {
			if (other.plans != null)
				return false;
		} else if (!plans.equals(other.plans))
			return false;
		return true;
	}
	
	

}
