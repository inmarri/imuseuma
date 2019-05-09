package candileja.core;

import java.util.ArrayList;
import java.util.List;

import candileja.core.aspect.selfadjusting.SituationalPattern;

public class Scheduler extends Thread {
	
	//private String TAG="SCHEDULER";
	//private Boolean D=false;
	
	private List<WorkPacket> workPackets;
	//private List<Plan> plans;
	private Boolean end;
	private long samplingTime;
	private Agent myAgent;
	
	public Scheduler(Agent ma){
		super();
		//plans =new ArrayList<Plan>();
		workPackets=new ArrayList<WorkPacket>();
		myAgent=ma;
		end=false;
		samplingTime=300;
	}
	
	public void addWorkPacket(WorkPacket wp){
		synchronized(workPackets){
			workPackets.add(wp);
		}
	}
	
	public WorkPacket getWorkPacket(){
		WorkPacket res=null;
		synchronized(workPackets){
			if(!workPackets.isEmpty()){
				res=workPackets.get(0);
				workPackets.remove(0);
			}
		}
		return res;
	}
	
	public void endExecution(){
		end=true;
	}
	
	public long getSamplingTime(){
		return samplingTime;
	}
	
	public void setSamplingTime(long st){
		samplingTime=st;
	}

	@Override
	public void run() {		
		WorkPacket packet;
		List<PlanDescription> candidates;
		PlanDescription description;
		Boolean executed;
		int i;
		SituationalPattern sp;
		
		while(!end){
			if(workPackets.isEmpty()){
				try{
					Thread.sleep(samplingTime);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				try{
					// Se coge el primer plan de la lista y se ejecuta
					packet=getWorkPacket();
					// 1. Se comprueba si ese objetivo está siendo buscando actualmente por el agente.
					if(myAgent.isActiveGoal(packet.getGoal())){
						executed=false;
						i=0;
						candidates=packet.getPlans();
						// 2. Buscamos que plan puede ser ejecutado
						while(i<candidates.size() && !executed){
							// 3. Cumple la precondición
							description=candidates.get(i);
							sp=description.getPrecondition();
							sp.setAgentContext(myAgent);
							if(sp.checkPattern()){
								// Se ejecuta el plan
								executePlan(description, packet.getInput());
								executed=true;
								// Se eliminan los objetivos del agente.
								myAgent.removeGoal(packet.getGoal());
							}
							i++;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void executePlan(PlanDescription pd,Object input){
		try {

			//if(D)Log.d(TAG, "Se inicia executePlan para el plan "+pd.getClassName());
			@SuppressWarnings("rawtypes")
			Class myClass = Class.forName(pd.getClassName());
			//if(D)Log.d(TAG, "1");
			Plan plan = (Plan) myClass.newInstance();
			//if(D)Log.d(TAG, "2");
			plan.setAgent(myAgent);
			plan.setInput(input);
			//if(D)Log.d(TAG, "3");
			plan.execute();
			//if(D)Log.d(TAG, "Fin: Si has llegado hasta aquí la ejecución ha sido un éxito");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Equals and hashcode
	 * */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + (int) (samplingTime ^ (samplingTime >>> 32));
		result = prime * result
				+ ((workPackets == null) ? 0 : workPackets.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scheduler other = (Scheduler) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (myAgent == null) {
			if (other.myAgent != null)
				return false;
		} else if (!myAgent.equals(other.myAgent))
			return false;
		if (samplingTime != other.samplingTime)
			return false;
		if (workPackets == null) {
			if (other.workPackets != null)
				return false;
		} else if (!workPackets.equals(other.workPackets))
			return false;
		return true;
	}
	
	
	
}
