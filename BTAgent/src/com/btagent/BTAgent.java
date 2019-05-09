package com.btagent;

import java.util.Random;

import malacatiny.aspects.distribution.sol.SolAPInterface;
import malacatiny.aspects.representation.aurora.AuroraRepresentation;
import sol.bluetooth.SBAPInterface;
import android.bluetooth.BluetoothAdapter;
import candileja.core.Agent;
import candileja.core.Goal;
import candileja.core.GoalType;
import candileja.core.PlanDescription;
import candileja.core.aspect.Role;
import candileja.core.aspect.Scope;
import candileja.core.aspect.selfadjusting.TruePattern;

import com.btagent.plan.JoinGroupPlan;
import com.btagent.plan.ReplyPlan;
import com.btagent.protocol.GroupProtocol;

public class BTAgent extends Agent implements BTVocabulary{
	
	private BTAgentActivity activity;
	@SuppressWarnings("unused")
	private BluetoothAdapter btAdapter;
	
	public BTAgent(BTAgentActivity act,BluetoothAdapter bt){
		activity=act;
		btAdapter=bt;
	}

	@Override
	public void setup() {
		this.setAID("visitorBluetoothAgent"+new Random().nextInt());
		// Registro en la plataforma
		/*SolAPInterface apInterface=new SolAPInterface();
		Object[] args=new Object[2];
		args[0]="172.16.51.91";//"150.214.108.46";// Trabajo//"192.168.1.33";//"192.168.1.38";//Piso//
		args[1]=8080;
		apInterface.startAP(args);
		this.addComponent(BTVocabulary.ap, apInterface);
		this.addDistributionAspect(BTVocabulary.ap, BTVocabulary.solAdaptor);*/
		
		
		// Se registra el agente en la plataforma usando bluetooth
		SBAPInterface bapInterface=new SBAPInterface();
		addComponent(BTVocabulary.ap, bapInterface);

		// Se inicia la plataforma
		Object[] startArgs={BTVocabulary.BluetoothAddress,BTVocabulary.UUID,BTVocabulary.mas,BTVocabulary.category};
 		bapInterface.startAP(startArgs);
		//this.addDistributionAspect(BTVocabulary.ap, BTVocabulary.blueAdaptor);
		
		// Se despliega el agente.
		Object[] deployArgs=new Object[3];
		deployArgs[0]=getAID();
		deployArgs[1]=btAdapter;
		deployArgs[2]=BTVocabulary.blueAdaptor;
		bapInterface.deployAgent(deployArgs);
		
		// Objetivos
		Goal replyGoal=new Goal("ReplyMessage",GoalType.APPLICATION);
		PlanDescription replyPlanPD=new PlanDescription(ReplyPlan.class.getName());
		replyPlanPD.setPrecondition(new TruePattern());
		replyPlanPD.addGoal(replyGoal);
		addPlan(replyPlanPD);
		
		Goal groupGoal=new Goal("RegisterWithGroup",GoalType.APPLICATION);
		PlanDescription joinGroupPlanPD=new PlanDescription(JoinGroupPlan.class.getName());
		joinGroupPlanPD.setPrecondition(new TruePattern());
		joinGroupPlanPD.addGoal(groupGoal);
		addPlan(joinGroupPlanPD);
		
		// Componentes
		addComponent(gui,activity);

	}

	@Override
	public void compositionRules() {
		addCompositionRule(SND_MSG, Role.REPRESENTATION, null, auroraAdaptor, AuroraRepresentation.class.getName(), true,Scope.AGENT_SCOPE, handleOutputMessage);
		addCompositionRule(SND_MSG, Role.DISTRIBUTION, null, blueAdaptor, null, true, Scope.AGENT_SCOPE, handleOutputMessage);
		
		addCompositionRule(RCV_MSG, Role.REPRESENTATION, null, auroraAdaptor, AuroraRepresentation.class.getName(), true, Scope.AGENT_SCOPE, handleInputMessage);
		addCompositionRule(RCV_MSG, Role.COORDINATION, null, GroupProtocol, GroupProtocol.class.getName(), true, Scope.AGENT_SCOPE, handleInputMessage);
		
		addCompositionRule(THRW_EVNT, Role.COORDINATION, null, GroupProtocol, GroupProtocol.class.getName(), true, Scope.AGENT_SCOPE, handleEvent);

	}
	
	protected void killAgent(){
		// Se desregistra de la plataforma.
		SBAPInterface bap=(SBAPInterface)getComponent(BTVocabulary.ap);
		bap.leaveGroup(BTVocabulary.GroupName);
		bap.killAgent(new Object[1]);
	}

}
