package inma.visitoragent.plan;

import inma.visitoragent.VisitorAgentActivity;
import inma.visitoragent.VisitorVocabulary;
import candileja.core.ACLMessage;
import candileja.core.Plan;

public class ReplyPlan extends Plan {

	@Override
	protected void execute() {
		ACLMessage msg=(ACLMessage)getInput();
		VisitorAgentActivity act=(VisitorAgentActivity)getAgent().getComponent(VisitorVocabulary.gui);
		
		act.addMessage(msg.getContent().toString());
		
		//act.addStatus(msg.getLanguage().toString()); // Estamos poniendo el estado en el campo 'language'. 
		//Tenemos que mirar si podemos crear un campo nuevo en AuroraMessage o ponerlo en 'content' con un separador
		
		ACLMessage reply=msg.createReply();
		reply.setContent("Received!");
		getAgent().sendMessage(reply);
	}

}
