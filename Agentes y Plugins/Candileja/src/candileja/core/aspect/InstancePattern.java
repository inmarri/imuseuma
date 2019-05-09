/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

/**
 *
 * @author Inma
 */
public class InstancePattern implements PatternInterface{

    private Object _event_class;

    public InstancePattern(Object c){
        this._event_class=c;
    }

    public Object getEvent_class() {
        return _event_class;
    }

    public void setEvent_class(Object _event_class) {
        this._event_class = _event_class;
    }

    public boolean matchPattern(Object obj){
        String myClass,objClass;
        myClass=this.getEvent_class().getClass().getName();
        objClass=obj.getClass().getName();
        //System.out.println("Se ejecuta match pattern");
        //System.out.println("Nuestra clase es "+myClass+" y la clase que se ha recibido es "+objClass);
        boolean res=false;
        if(myClass.equals(objClass)){
            res=true;
        }
        return res;
    }
    
    public String toString(){
    	return _event_class.getClass().toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_event_class == null) ? 0 : _event_class.hashCode());
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
		InstancePattern other = (InstancePattern) obj;
		if (_event_class == null) {
			if (other._event_class != null)
				return false;
		} else if (!_event_class.equals(other._event_class))
			return false;
		return true;
	}
}
