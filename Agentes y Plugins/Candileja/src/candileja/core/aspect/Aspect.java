/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

import android.util.Log;
import candileja.core.Agent;


/**
 *
 * @author Inma
 */
public class Aspect{

	private String TAG="CANDILEJA";
	private Boolean d=false;
    private Agent mediator;
    // Identificador con el que se ha guardado el aspecto en la lista de active aspects del mediador.
    private String _id;
 

    public Object handleEvent(Object msg){
    	if(d)Log.d(TAG,"handleEvent dentro de Aspect");
        return msg;
    }

    public Object handleOutputMessage(Object msg){
        return msg;
    }

    public Object handleInputMessage(Object msg){
        return msg;
    }

    public Agent getMediator(){
        return this.mediator;
    }

    public void setMediator(Agent med){
        this.mediator=med;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
		Aspect other = (Aspect) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (mediator == null) {
			if (other.mediator != null)
				return false;
		} else if (!mediator.equals(other.mediator))
			return false;
		return true;
	}
}
