package candileja.core;

import java.util.ArrayList;
import java.util.List;

import candileja.core.aspect.selfadjusting.SituationalPattern;

public class PlanDescription {
	private String className;
	private List<Goal> goals;
	private SituationalPattern precondition;
	
	public PlanDescription(String c){
		className=c;
		goals=new ArrayList<Goal>();
	}
	
	public String getClassName(){
		return className;
	}
	
	public void setClassName(String cn){
		className=cn;
	}
	
	public void addGoal(Goal g){
		goals.add(g);
	}
	
	public List<Goal> getGoals(){
		return goals;
	}

	public SituationalPattern getPrecondition() {
		return precondition;
	}

	public void setPrecondition(SituationalPattern precondition) {
		this.precondition = precondition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((goals == null) ? 0 : goals.hashCode());
		result = prime * result
				+ ((precondition == null) ? 0 : precondition.hashCode());
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
		PlanDescription other = (PlanDescription) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (goals == null) {
			if (other.goals != null)
				return false;
		} else if (!goals.equals(other.goals))
			return false;
		if (precondition == null) {
			if (other.precondition != null)
				return false;
		} else if (!precondition.equals(other.precondition))
			return false;
		return true;
	}
}
