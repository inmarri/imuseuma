package candileja.core.aspect.selfadjusting;

import java.util.Enumeration;
import java.util.Hashtable;

import android.util.Log;

public class GoalState {
	
	// debugging
	private String tag="self";
	private Boolean d=true;
	
	private Hashtable<SituationalPattern,GoalTransition> transitions;
	private SelfAdjusting selfAdjusting;
	
	public GoalState(SelfAdjusting sa){
		selfAdjusting=sa;
		transitions=new Hashtable<SituationalPattern,GoalTransition>();
	}
	
	protected void addTransition(SituationalPattern sp,GoalTransition vt){
		transitions.put(sp, vt);
	}
	
	protected SelfAdjusting getSelfAdjusting(){
		return selfAdjusting; 
	}
	
	protected void setSelfAdjusting(SelfAdjusting sa){
		selfAdjusting=sa;
	}
	
	synchronized protected Object analyze(){
		Object res=null;
		Enumeration<SituationalPattern> patterns;
		SituationalPattern key;
		Boolean end=false;
		
		if(d)Log.d(tag, "Analyze de "+this.getClass().getName());
					
		patterns=transitions.keys();			
		while(!end && patterns.hasMoreElements()){				
			key=patterns.nextElement();	
			key.setAgentContext(selfAdjusting.getMediator());
			if(key.checkPattern()){					
				res=(GoalTransition)transitions.get(key);
				if(d){
					if(res==null){
						Log.d(tag,"La reconfiguración devuelve nulo y no debería");
					}else{
						Log.d(tag,"Se devuelve una transition");
					}
				}
				end=true;				
			}			
		}
		// Si no se transita a un nuevo estado devolvemos nulo.
		return res;
	}

}
