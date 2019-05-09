package inma.guideagent.plan;

import inma.guideagent.GuideAgentActivity;
import inma.guideagent.GuideVocabulary;
import candileja.core.ACLMessage;
import candileja.core.Plan;

public class ReceiveMessage extends Plan {

	@Override
	protected void execute() {
		@SuppressWarnings("unused")
		ACLMessage msg=(ACLMessage)getInput();
		Long recTime=System.currentTimeMillis();
		Long sendTime=(Long)getAgent().getKnowledge(GuideVocabulary.sendTime);
		Long result=recTime-sendTime;
		GuideAgentActivity act=(GuideAgentActivity)getAgent().getComponent(GuideVocabulary.gui);
		act.addTime(result);
	}

}
