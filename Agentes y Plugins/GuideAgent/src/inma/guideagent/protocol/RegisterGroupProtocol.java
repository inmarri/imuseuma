package inma.guideagent.protocol;

import inma.guideagent.ButtonEvent;
import inma.guideagent.GuideVocabulary;
import malacatiny.aspects.distribution.sol.SolPluginRunningEvent;
import candileja.core.Goal;
import candileja.core.GoalType;
import candileja.core.aspect.InstancePattern;
import candileja.core.aspect.MessagePattern;
import candileja.core.aspect.coordination.ProtocolConnector;
import candileja.core.aspect.coordination.ProtocolState;

public class RegisterGroupProtocol extends ProtocolConnector {

	@Override
	public String getProtocolName() {
		return GuideVocabulary.GroupProtocol;
	}

	@Override
	protected void setup() {
		InstancePattern registerPattern=new InstancePattern(new SolPluginRunningEvent());
		ProtocolState initial=new ProtocolState(this,"initial");
		Goal doNothing=new Goal("DoNothing",GoalType.APPLICATION);
		//Goal joinGoal=new Goal("RegisterWithGroup",GoalType.APPLICATION);
		
		InstancePattern buttonPattern=new InstancePattern(new ButtonEvent());
		ProtocolState sendMessage=new ProtocolState(this,"sendmessage");
		Goal sendGoal=new Goal("SendMessage",GoalType.APPLICATION);
		
		MessagePattern groupProtocolPattern=new MessagePattern();
		groupProtocolPattern.setProtocol(getProtocolName());
		ProtocolState waitingMessage=new ProtocolState(this,"waitingmessage");
		Goal receiveMessage=new Goal("ReceiveMessage",GoalType.APPLICATION);
		
		registerTransition(registerPattern, initial, sendMessage, doNothing);
		registerTransition(buttonPattern, sendMessage, waitingMessage, sendGoal);
		registerTransition(groupProtocolPattern, waitingMessage, sendMessage, receiveMessage);
		
		
		setCurrent_State(initial);
		setInitial_state(initial);
	}

}
