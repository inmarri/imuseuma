package candileja.core.aspect.selfadjusting;

import candileja.core.Goal;

// Plan ejecutado en el cambio de estado de Venom, que afecta a los aspectos de monitorización del agente.
public class GoalTransition {
	private Goal reconfigurationGoal;
	private GoalState nextState;
	
	public void setNextState(GoalState ns, Goal g){
		nextState=ns;
		reconfigurationGoal=g;
	}
	
	public GoalState getNextState(){
		return nextState;
	}
	
	protected Goal getReconfigurationGoal(){
		return reconfigurationGoal;
	}
	
	public void setReconfigurationGoal(Goal g){
		reconfigurationGoal=g;
	}
}
