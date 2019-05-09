package inma.visitoragent.plan;

import inma.visitoragent.VisitorVocabulary;
import malacatiny.aspects.distribution.sol.SolAPInterface;
import android.util.Log;
import candileja.core.Plan;

public class JoinGroupPlan extends Plan {
	// debuging
	private Boolean d=true;
	private String tag="joingroup";

	@Override
	protected void execute() {
		// Se pone el timer en marcha
		/*Timer timer=new Timer(5000);
		Thread timerThread=new Thread(timer);
		getAgent().addComponent("timer", timer);
		timerThread.start();*/
		
		// Registro en el grupo
		if(d)Log.d(tag,"Se ejecuta joingroupplan");
		Object[] groupArgs= new Object[1];
		groupArgs[0]=VisitorVocabulary.GroupName;
		SolAPInterface apInterface=(SolAPInterface) getAgent().getComponent(VisitorVocabulary.ap);
		apInterface.registerWithGroup(groupArgs);
		
	}

}
