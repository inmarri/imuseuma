package candileja.core;


// Componente con conexión al mediador, están pensados para modelar la interfaz con el mundo externo.

public class Facet {
	
	private Agent myAgent;
	//private String context;
	
	protected void setAgent(Agent ag){
		myAgent=ag;
	}
	
	protected Agent getAgent(){
		return myAgent;
	}
	
	public void throwEvent(Object obj){
		myAgent.throwEvent(obj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myAgent == null) ? 0 : myAgent.hashCode());
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
		Facet other = (Facet) obj;
		if (myAgent == null) {
			if (other.myAgent != null)
				return false;
		} else if (!myAgent.equals(other.myAgent))
			return false;
		return true;
	}
}
