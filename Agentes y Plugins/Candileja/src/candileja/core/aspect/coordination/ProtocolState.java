/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect.coordination;

import java.util.Vector;

import candileja.core.Goal;
import candileja.core.aspect.PatternInterface;

/**
 *
 * @author Mer
 */
public class ProtocolState {
    String _state_name;
    ProtocolConnector _connector;
    private Vector<Transition> _transtitions;
    
    // Debugin
    //private String TAG="CANDILEJA";
    //private Boolean D=false;
    
    public ProtocolState(ProtocolConnector connector, String name)
    {
        this._transtitions=new Vector<Transition>();
        _connector=connector;
        _state_name = name;
    }

    // Dado un mensaje/evento genera un objetivo y devuelve el siguiente estado.
    // Por ahora solo mensajes.
    public ProtocolState handleInput(Object input){

        ProtocolState res=this;
        Transition trans=this.getTransition(/*(ACLMessage)*/ input);
        
        synchronized(this){
            if(trans!=null){
            	// Enviamos el plan a ejecución
            	//if(D) Log.d(TAG, "se añade un objetivo");
            	_connector.addGoal(trans.getGoal(), input);
            	// Se hace el cambio de estado.
            	res=trans.getState();
            }
        }
        return res;
    }

     // Registra una trancisiï¿½n en la FSM del estado. Por ahora solo con mensajes
     protected void registerTransition(PatternInterface msgPat,ProtocolState state, Goal goal){
        this._transtitions.addElement(new Transition(msgPat,state,goal));
     }
     
     protected Vector<Transition> getTransitions(){
    	 return this._transtitions;
     }

     public Transition getTransition(Object obj){

        boolean fin=false;
        int i=0;
        Transition res=null;
        Transition check;

        try{
            while (i<this._transtitions.size() && !fin){
                check=(Transition) this._transtitions.elementAt(i);
                if(check.getPattern().matchPattern(obj)){
                    res=check;
                    fin=true;
                }
                i++;
            }
         }catch(Exception e){
            e.printStackTrace();
         }
        return res;
     }

     public void setProtocolConnector(ProtocolConnector pc){
        this._connector=pc;
     }

     public ProtocolConnector getProtocolConnector(){
        return this._connector;
     }

     /**
      * Getter an setter 
      **/
    public String getState_name() {
        return _state_name;
    }

    public void setState_name(String _state_name) {
        this._state_name = _state_name;
    }

    protected Object getVariable(String id){
        return this._connector.getVariable(id);
    }

    protected void setVariable(String id,Object obj){
        this._connector.setVariable(id, obj);
    }

    protected void removeVariable(String id){
        this._connector.removeVariable(id);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_connector == null) ? 0 : _connector.hashCode());
		result = prime * result
				+ ((_state_name == null) ? 0 : _state_name.hashCode());
		result = prime * result
				+ ((_transtitions == null) ? 0 : _transtitions.hashCode());
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
		ProtocolState other = (ProtocolState) obj;
		if (_connector == null) {
			if (other._connector != null)
				return false;
		} else if (!_connector.equals(other._connector))
			return false;
		if (_state_name == null) {
			if (other._state_name != null)
				return false;
		} else if (!_state_name.equals(other._state_name))
			return false;
		if (_transtitions == null) {
			if (other._transtitions != null)
				return false;
		} else if (!_transtitions.equals(other._transtitions))
			return false;
		return true;
	}
}
