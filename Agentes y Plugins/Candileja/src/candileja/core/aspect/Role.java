/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package candileja.core.aspect;

/**
 *
 * @author Inma
 */
public class Role {
 
    public static final String COORDINATION="COORDINATION";
    public static final String REPRESENTATION="REPRESENTATION";
    public static final String DISTRIBUTION="DISTRIBUTION";

    private String role;

    public Role(){
        this.role=Role.COORDINATION;
    }

    public Role(String r){
        this.role=r;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		Role other = (Role) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}
}
