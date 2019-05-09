package inma.guideagent.plan;

import inma.guideagent.GuideVocabulary;
import malacatiny.aspects.distribution.sol.SolAPInterface;
import candileja.core.Plan;

public class JoinGroupPlan extends Plan {

	@Override
	protected void execute() {
		// Se pone el timer en marcha
		/*Timer timer=new Timer(5000);
		Thread timerThread=new Thread(timer);
		getAgent().addComponent("timer", timer);
		timerThread.start();*/
		
		// Registro en el grupo
		Object[] groupArgs= new Object[1];
		groupArgs[0]=GuideVocabulary.GroupName;
		SolAPInterface apInterface=(SolAPInterface) getAgent().getComponent(GuideVocabulary.ap);
		apInterface.registerWithGroup(groupArgs);
		
	}

}
