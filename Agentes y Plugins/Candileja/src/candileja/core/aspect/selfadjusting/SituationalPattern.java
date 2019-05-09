package candileja.core.aspect.selfadjusting;

import java.util.Random;

import candileja.core.AgentContextInterface;

public abstract class SituationalPattern {
	private AgentContextInterface agentContext;
	private String code;
	
	public SituationalPattern(){
		code=getCode();
	}
	
	public SituationalPattern(AgentContextInterface aci){
		this();
		agentContext=aci;
	}
	
	public void setAgentContext(AgentContextInterface aci){
		agentContext=aci;
	}
	
	protected AgentContextInterface getAgentContext() {
		return agentContext;
	}
	
	private String getCode(){
        Random generator = new Random(1000000);
        int f = generator.nextInt();
        String id=""+f;
        return id;
    }
	
	public abstract Boolean checkPattern();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		SituationalPattern other = (SituationalPattern) obj;
		if (agentContext == null) {
			if (other.agentContext != null)
				return false;
		} else if (!agentContext.equals(other.agentContext))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
}
