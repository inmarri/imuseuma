/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect.coordination;

import candileja.core.Goal;
import candileja.core.aspect.PatternInterface;

/**
 *
 * @author Inma
 */
public class Transition {
	
	// Debuging
    @SuppressWarnings("unused")
	private String TAG="CANDILEJA";
    @SuppressWarnings("unused")
	private Boolean D=false;

    private PatternInterface pattern;
    private ProtocolState state;
    private Goal goal;

    public Transition(){}

    public Transition(PatternInterface p,ProtocolState s, Goal g){
        this.pattern=p;
        this.state=s;
        this.goal=g;
    }

    public PatternInterface getPattern() {
        return pattern;
    }

    public void setPattern(PatternInterface pattern) {
        this.pattern = pattern;
    }

    public ProtocolState getState() {
        return state;
    }

    public void setState(ProtocolState state) {
        this.state = state;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Transition other = (Transition) obj;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transition [pattern=" + pattern + ", state=" + state
				+ ", goal=" + goal + "]";
	}

	
    
    
}
