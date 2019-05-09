/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

/**
 *
 * @author Inma
 */
public class RoleInstance {
    //public static final String PROTOCOL_BASED="PROTOCOL_BASED";
    public static final String CONVERSATION_BASED="CONVERSATION_BASED";
    private String instance;

    public RoleInstance(){
        instance=RoleInstance.CONVERSATION_BASED;
    }

    public RoleInstance(String ins){
        this.instance=ins;
    }

    public void setRoleInstance(String ins){
        this.instance=ins;
    }

    public String getRoleInstance(){
        return this.instance;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instance == null) ? 0 : instance.hashCode());
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
		RoleInstance other = (RoleInstance) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		return true;
	}
}
