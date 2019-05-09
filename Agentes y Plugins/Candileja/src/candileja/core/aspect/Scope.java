/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

/**
 *
 * @author Inma
 */
public class Scope {
    public static final int AGENT_SCOPE=0;
    public static final int PROTOCOL_SCOPE=1;
    public static final int CONVERSATION_SCOPE=2;

    private static final String[] scopes = new String[3];
	static { // initialization of the Vector of performatives
		scopes[AGENT_SCOPE]="AGENT_SCOPE";
		scopes[PROTOCOL_SCOPE]="PROTOCOL_SCOPE";
		scopes[CONVERSATION_SCOPE]="CONVERSATION_SCOPE";
	}


    private int scope;

    public Scope(){
        this.scope=0;
    }

    public Scope(int sc){
        this.scope = sc;
    }

    public String getScope() {
        return scopes[scope];
    }

    public void setScope(int sc) {
        this.scope = sc;
    }

    public boolean equals(int sc){
        return scope==sc;
    }

    public boolean equals(String sc){
        return Scope.scopes[scope].equals(sc);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + scope;
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
		Scope other = (Scope) obj;
		if (scope != other.scope)
			return false;
		return true;
	}
}
