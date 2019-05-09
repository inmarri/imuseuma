package inma.guideagent;

import inma.guideagent.plan.DoNothing;
import inma.guideagent.plan.JoinGroupPlan;
import inma.guideagent.plan.ReceiveMessage;
import inma.guideagent.plan.SendGroupMessage;
import inma.guideagent.protocol.RegisterGroupProtocol;

import java.util.Random;

import malacatiny.aspects.distribution.sol.SolAPInterface;
import malacatiny.aspects.distribution.sol.SolPlugin;
import malacatiny.aspects.representation.aurora.AuroraRepresentation;
import android.bluetooth.BluetoothAdapter;
import candileja.core.Agent;
import candileja.core.Goal;
import candileja.core.GoalType;
import candileja.core.PlanDescription;
import candileja.core.aspect.Role;
import candileja.core.aspect.Scope;
import candileja.core.aspect.selfadjusting.TruePattern;

public class GuideAgent extends Agent implements GuideVocabulary{
	
	private GuideAgentActivity actvity;
	@SuppressWarnings("unused")
	private BluetoothAdapter btAdapter;
	
	public GuideAgent(GuideAgentActivity act,BluetoothAdapter bt){
		actvity=act;
		btAdapter=bt;
	}

	@Override
	public void setup() {
		this.setAID("guideAgent"+new Random().nextInt());
		// Registro en la plataforma
		SolAPInterface apInterface=new SolAPInterface();
		Object[] args=new Object[4];
		args[0]="172.16.51.91";//"150.214.108.46";// Trabajo//"192.168.1.33";//"192.168.1.38";//Piso//
		args[1]=8080;
		args[2]=GuideVocabulary.mas;
		args[3]=GuideVocabulary.category;
		apInterface.startAP(args);
		this.addComponent(GuideVocabulary.ap, apInterface);
		this.addDistributionAspect(GuideVocabulary.ap, GuideVocabulary.solAdaptor);
		
		
		
		// Se registra el agente en la plataforma usando bluetooth
		/*SBAPInterface apInterface=new SBAPInterface();
		// Se inicia la plataforma
		Object[] startArgs={GuideVocabulary.BluetoothAddress,GuideVocabulary.UUID};
		apInterface.startAP(startArgs);
		addComponent(GuideVocabulary.ap, apInterface);
		
		// Se despliega el agente.
		Object[] deployArgs=new Object[3];
		deployArgs[0]=getAID();
		deployArgs[1]=btAdapter;
		deployArgs[2]=GuideVocabulary.solAdaptor;
		apInterface.deployAgent(deployArgs);*/
	
		
		addComponent(gui, actvity);
		
		// Planes
		Goal groupGoal=new Goal("RegisterWithGroup",GoalType.APPLICATION);
		PlanDescription joinGroupPlanPD=new PlanDescription(JoinGroupPlan.class.getName());
		joinGroupPlanPD.setPrecondition(new TruePattern());
		joinGroupPlanPD.addGoal(groupGoal);
		addPlan(joinGroupPlanPD);
		
		Goal doNothing=new Goal("DoNothing",GoalType.APPLICATION);
		PlanDescription doNothingPD=new PlanDescription(DoNothing.class.getName());
		doNothingPD.setPrecondition(new TruePattern());
		doNothingPD.addGoal(doNothing);
		addPlan(doNothingPD);
		
		Goal sendGoal=new Goal("SendMessage",GoalType.APPLICATION);
		PlanDescription sendMessagePlanPD=new PlanDescription(SendGroupMessage.class.getName());
		sendMessagePlanPD.setPrecondition(new TruePattern());
		sendMessagePlanPD.addGoal(sendGoal);
		addPlan(sendMessagePlanPD);
		
		Goal receiveMessage=new Goal("ReceiveMessage",GoalType.APPLICATION);
		PlanDescription receiveMessagePlanPD=new PlanDescription(ReceiveMessage.class.getName());
		receiveMessagePlanPD.setPrecondition(new TruePattern());
		receiveMessagePlanPD.addGoal(receiveMessage);
		addPlan(receiveMessagePlanPD);
	}

	@Override
	public void compositionRules() {
		addCompositionRule(SND_MSG, Role.REPRESENTATION, null, auroraAdaptor, AuroraRepresentation.class.getName(), true,Scope.AGENT_SCOPE, handleOutputMessage);
		addCompositionRule(SND_MSG, Role.DISTRIBUTION, null, solAdaptor, SolPlugin.class.getName(), true, Scope.AGENT_SCOPE, handleOutputMessage);
	
		addCompositionRule(RCV_MSG, Role.REPRESENTATION, null, auroraAdaptor, AuroraRepresentation.class.getName(), true, Scope.AGENT_SCOPE, handleInputMessage);
		addCompositionRule(RCV_MSG, Role.COORDINATION, null, GroupProtocol, RegisterGroupProtocol.class.getName(), true, Scope.AGENT_SCOPE, handleInputMessage);
		
		addCompositionRule(THRW_EVNT, "ContextAwareness", null, GroupProtocol, RegisterGroupProtocol.class.getName(), true, Scope.AGENT_SCOPE, handleEvent);

	}
	
	protected void killAgent(){
		SolAPInterface solAp=(SolAPInterface)getComponent(ap);
		solAp.killAgent(new Object[1]);
	}

}
