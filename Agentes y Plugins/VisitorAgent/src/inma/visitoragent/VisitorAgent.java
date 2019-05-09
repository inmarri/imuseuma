package inma.visitoragent;

import inma.visitoragent.plan.JoinGroupPlan;
import inma.visitoragent.plan.ReplyPlan;
import inma.visitoragent.protocol.GroupProtocol;

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

public class VisitorAgent extends Agent implements VisitorVocabulary{
	
	@SuppressWarnings("unused")
	private VisitorAgentActivity activity;
	@SuppressWarnings("unused")
	private BluetoothAdapter btAdpater;

	public VisitorAgent(VisitorAgentActivity visitorAgentActivity,BluetoothAdapter bt) {
		activity=visitorAgentActivity;
		btAdpater=bt;
		
	}

	@Override
	public void setup() {
		String aid="visitorAgent"+new Random().nextInt();
		setAID(aid);
		// Registro en la plataforma
		SolAPInterface apInterface=new SolAPInterface();
		Object[] args=new Object[4];
		args[0]="10.10.10.10";//"172.16.51.91";//"150.214.108.46";// Trabajo//"192.168.1.33";//"192.168.1.38";//Piso//
		args[1]=8080;
		args[2]=VisitorVocabulary.mas;
		args[3]=VisitorVocabulary.category;
		apInterface.startAP(args);
		this.addComponent(VisitorVocabulary.ap, apInterface);
		this.addDistributionAspect(VisitorVocabulary.ap, VisitorVocabulary.solAdaptor);
		
		// Se registra el agente en la plataforma usando bluetooth
		/*SBAPInterface ap=new SBAPInterface();
		// Se inicia la plataforma
		Object[] startArgs={VisitorVocabulary.BluetoothAddress,VisitorVocabulary.UUID};
		ap.startAP(startArgs);
		addComponent(VisitorVocabulary.ap, ap);
		
		// Se despliega el agente.
		Object[] deployArgs=new Object[3];
		deployArgs[0]=getAID();
		deployArgs[1]=btAdpater;
		deployArgs[2]=VisitorVocabulary.solAdaptor;
		ap.deployAgent(deployArgs);*/
		
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
		addCompositionRule(SND_MSG, Role.DISTRIBUTION, null, solAdaptor, SolPlugin.class.getName(), true, Scope.AGENT_SCOPE, handleOutputMessage);
		
		addCompositionRule(RCV_MSG, Role.REPRESENTATION, null, auroraAdaptor, AuroraRepresentation.class.getName(), true, Scope.AGENT_SCOPE, handleInputMessage);
		addCompositionRule(RCV_MSG, Role.COORDINATION, null, GroupProtocol, GroupProtocol.class.getName(), true, Scope.AGENT_SCOPE, handleInputMessage);
		
		addCompositionRule(THRW_EVNT, Role.COORDINATION, null, GroupProtocol, GroupProtocol.class.getName(), true, Scope.AGENT_SCOPE, handleEvent);

	}

	protected void killAgent(){
		// Se desregistra de la plataforma.
		SolAPInterface ap=(SolAPInterface)getComponent(VisitorVocabulary.ap);
		ap.killAgent(new Object[1]);
	}

}
