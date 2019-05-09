package candileja.core;

import java.util.ArrayList;
import java.util.List;

public class PlanLibrary {
	
	private List<PlanDescription> plans;
	
	public PlanLibrary(){
		plans=new ArrayList<PlanDescription>();
	}
	
	public void addPlanDescription(PlanDescription pd){
		plans.add(pd);
	}
	
	public void removePlanDescription(PlanDescription pd){
		plans.remove(pd);
	}
	
	public List<PlanDescription> getPlanDescription(Goal g){
		List<PlanDescription> pd=new ArrayList<PlanDescription>();
		List<Goal> aux;
		
		for(int i=0;i<plans.size();i++){
			aux=plans.get(i).getGoals();
			if(aux.contains(g)){
				pd.add(plans.get(i));
			}
		}		
		return pd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plans == null) ? 0 : plans.hashCode());
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
		PlanLibrary other = (PlanLibrary) obj;
		if (plans == null) {
			if (other.plans != null)
				return false;
		} else if (!plans.equals(other.plans))
			return false;
		return true;
	}
	
	

}
