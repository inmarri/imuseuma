/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

/**
 *
 * @author Inma
 */
public class AspectDescription {
    // Rol del aspecto
    private Role role;
    // [<condiciï¿½n>]
    private PatternInterface restriction;
    // Role instance
    private RoleInstance roleInstance;
    // NON_RELEVANT
    private boolean relevance;
    // Clase que implementa el ASPECTO
    private String aspect;
    // Scope
    private Scope scope;
    // Advice del aspecto que se ejecutará en la composición de aspectos.
    private String advice;

    public AspectDescription(){}

    public AspectDescription(String role, PatternInterface restriction, String instanceName, String aspectClass,boolean relevance,int sc,String adv)
    {
        this.role=new Role(role);
        this.restriction = restriction;
        this.roleInstance=new RoleInstance(instanceName);
        this.relevance=relevance;
        this.aspect=aspectClass;
        scope=new Scope(sc);
        advice=adv;
    }

    /**
     * @return the role
     */
   public String getRole() {
        return role.getRole();
    }

    /**
     * @param role the role to set
     */
   public void setRole(String role) {
        this.role.setRole(role);
    }

    /**
     * @return the restriction
     */
   public PatternInterface getRestriction() {
        return restriction;
    }

    /**
     * @param restriction the restriction to set
     */
  public void setRestriction(PatternInterface restriction) {
        this.restriction = restriction;
    }

    /**
     * @return the roleInstance
     */
  public String getRoleInstance() {
        return roleInstance.getRoleInstance();
    }

    /**
     * @param roleInstance the roleInstance to set
     */
	public void setRoleInstance(String roleInstance) {
        this.roleInstance.setRoleInstance(roleInstance);
    }

    /**
     * @return the relevance
     */
	public boolean isRelevant() {
        return relevance;
    }

    /**
     * @param relevance the relevance to set
     */
	public void setRelevance(boolean relevance) {
        this.relevance = relevance;
    }

    public String getAspectClass() {
        return aspect;
    }

    public void setAspectClass(String aspect) {
        this.aspect = aspect;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(int sc) {
        this.scope = new Scope(sc);
    }

    public void setScope(Scope sc){
        this.scope=sc;
    }

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((advice == null) ? 0 : advice.hashCode());
		result = prime * result + ((aspect == null) ? 0 : aspect.hashCode());
		result = prime * result + (relevance ? 1231 : 1237);
		result = prime * result
				+ ((restriction == null) ? 0 : restriction.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result
				+ ((roleInstance == null) ? 0 : roleInstance.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
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
		AspectDescription other = (AspectDescription) obj;
		if (advice == null) {
			if (other.advice != null)
				return false;
		} else if (!advice.equals(other.advice))
			return false;
		if (aspect == null) {
			if (other.aspect != null)
				return false;
		} else if (!aspect.equals(other.aspect))
			return false;
		if (relevance != other.relevance)
			return false;
		if (restriction == null) {
			if (other.restriction != null)
				return false;
		} else if (!restriction.equals(other.restriction))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (roleInstance == null) {
			if (other.roleInstance != null)
				return false;
		} else if (!roleInstance.equals(other.roleInstance))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}
}
