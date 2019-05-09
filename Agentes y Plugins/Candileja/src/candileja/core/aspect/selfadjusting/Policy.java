package candileja.core.aspect.selfadjusting;

public class Policy {
	private SituationalPattern goalState;
	private Reconfiguration reconfiguration;
	
	public Policy(){
		goalState=null;
		reconfiguration=null;
	}
	
	public Policy(SituationalPattern gs,Reconfiguration r){
		goalState=gs;
		reconfiguration=r;
	}

	public SituationalPattern getGoalState() {
		return goalState;
	}

	public void setGoalState(SituationalPattern goalState) {
		this.goalState = goalState;
	}

	public Reconfiguration getReconfiguration() {
		return reconfiguration;
	}

	public void setReconfiguration(Reconfiguration reconfiguration) {
		this.reconfiguration = reconfiguration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((goalState == null) ? 0 : goalState.hashCode());
		result = prime * result
				+ ((reconfiguration == null) ? 0 : reconfiguration.hashCode());
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
		Policy other = (Policy) obj;
		if (goalState == null) {
			if (other.goalState != null)
				return false;
		} else if (!goalState.equals(other.goalState))
			return false;
		if (reconfiguration == null) {
			if (other.reconfiguration != null)
				return false;
		} else if (!reconfiguration.equals(other.reconfiguration))
			return false;
		return true;
	}
}
