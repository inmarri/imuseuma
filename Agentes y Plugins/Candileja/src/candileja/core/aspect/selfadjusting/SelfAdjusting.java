package candileja.core.aspect.selfadjusting;

import candileja.core.aspect.Aspect;

public abstract class SelfAdjusting extends Aspect{
	// Debuging
	//private Boolean d=false;
	//private String tag="self";
	
	private GoalState currentState;

	// El lanzamiento de un evento de este tipo implica que ha habido una actualización
	// de alguno de los elementos que componen la base de conocimiento del agente.
	@Override
	public Object handleEvent(Object msg) {
		//if(d)Log.d(tag,"Se ha recibido un input en SelfAdjusting");
		synchronized(currentState){
		
				Object conclussion=currentState.analyze();	
				if(conclussion==null){
					// No hacemos nada
				}else if(conclussion instanceof GoalTransition){
					try{
						GoalTransition gt=(GoalTransition)conclussion;
						getMediator().addGoal(gt.getReconfigurationGoal(), msg);
						currentState=gt.getNextState();
						//if(d)Log.d(tag,"Se ha generado el goal "+gt.getReconfigurationGoal().getGoal()+" y transita al estado "+currentState.getClass().getName());
					}catch(Exception e){
						e.printStackTrace();
					}		
				}
		}
		return msg;
	}
	
	protected void setCurrentState(GoalState vs){
		currentState=vs;
	}
	
	// tiene que haber un método para registrar trancisiones.
	public void registerTransition(GoalState initial,SituationalPattern sp,GoalTransition trans){
		initial.setSelfAdjusting(this);
		initial.addTransition(sp, trans);
		sp.setAgentContext(getMediator());
	}
	
	public void setInitialState(GoalState vs){
		synchronized(currentState){
			currentState=vs;
		}
	}
	
	public abstract void setup();

}
