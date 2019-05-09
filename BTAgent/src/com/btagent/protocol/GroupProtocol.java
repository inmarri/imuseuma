package com.btagent.protocol;

import com.btagent.BTVocabulary;

import malacatiny.aspects.distribution.sol.SolPluginRunningEvent;
import candileja.core.Goal;
import candileja.core.GoalType;
import candileja.core.aspect.InstancePattern;
import candileja.core.aspect.MessagePattern;
import candileja.core.aspect.coordination.ProtocolConnector;
import candileja.core.aspect.coordination.ProtocolState;

public class GroupProtocol extends ProtocolConnector {

	@Override
	public String getProtocolName() {
		return BTVocabulary.GroupProtocol;
	}

	@Override
	protected void setup() {
		// Estado inicial de registro en la plataforma
		ProtocolState initial=new ProtocolState(this,"initial");
		InstancePattern registerPattern=new InstancePattern(new SolPluginRunningEvent());
		Goal groupGoal=new Goal("RegisterWithGroup",GoalType.APPLICATION);
		
		Goal replyGoal=new Goal("ReplyMessage",GoalType.APPLICATION);
		MessagePattern groupProtocolPattern=new MessagePattern();
		groupProtocolPattern.setProtocol(getProtocolName());
		ProtocolState reply=new ProtocolState(this,"reply");
		
		registerTransition(registerPattern, initial, reply, groupGoal);
		registerTransition(groupProtocolPattern, reply, reply, replyGoal);
		
		setInitial_state(initial);
		setCurrent_State(initial);
	}

}
