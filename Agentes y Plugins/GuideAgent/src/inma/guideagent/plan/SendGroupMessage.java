package inma.guideagent.plan;

import inma.guideagent.GuideVocabulary;

import java.util.Random;

import candileja.core.ACLMessage;
import candileja.core.Plan;

public class SendGroupMessage extends Plan {

	@Override
	protected void execute() {
		ACLMessage msg=new ACLMessage();
		msg.addReceiver(GuideVocabulary.GroupName);
		msg.setContent("Hola Juanola!!");
		msg.setConversationID(GuideVocabulary.GroupProtocol+new Random().nextInt());
		msg.setLanguage("SPANISH");
		msg.setOntology(GuideVocabulary.guideOntology);
		msg.setPerformative(ACLMessage.INFORM);
		msg.setProtocol(GuideVocabulary.GroupProtocol);
		Long time=System.currentTimeMillis();
		getAgent().addKnowledge(GuideVocabulary.sendTime, time);
		getAgent().sendMessage(msg);
	}

}
