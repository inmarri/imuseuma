/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect.coordination;

import java.util.Hashtable;
import java.util.Vector;

import candileja.core.ACLMessage;
import candileja.core.Goal;
import candileja.core.aspect.Aspect;
import candileja.core.aspect.PatternInterface;


/**
 *
 * @author Mer
 */
public abstract class ProtocolConnector extends Aspect{
	// Depuración
	//private Boolean d=false;
	//private String TAG="CANDILEJA";
	
	
    private ProtocolState current_state;
    private ProtocolState initial_state;
    //private Agent _mediator;
    private Hashtable<String, Object> _execution_context;
    private String _conversatin_id;
    @SuppressWarnings("rawtypes")
	private Vector _state_lock,_current_lock;
    
    // Nombre del protocolo para el que implementa el rol
    //private String protocolName;

    @SuppressWarnings("rawtypes")
	public ProtocolConnector(){
        //System.out.println("Se inicia el protocolo: "+this.getClass().toString());
        this._execution_context=new Hashtable<String, Object>();
        this._state_lock=new Vector();
        this._current_lock=new Vector();
       //this.current_state=new ProtocolState();
    }

    // Acciones que se ejecutan antes de que empiece la ejecuciï¿½n del agente.
    public void initialTransition(){}

    public Object handleInputMessage(Object msg){
    	//if(d)Log.d(TAG,"handleInputMessage en ProtocolConnector para "+this.getProtocolName());
    	synchronized(this._state_lock){
	        ProtocolState next_state = getCurrent_state().handleInput(msg);
	        setCurrent_State(next_state);
    	}
        return msg;
    }

    // Envï¿½o de mensajes a travï¿½s del mediador
    public Object handleOutputMessage(Object message){
    	//if(d)Log.d(TAG,"handleOutputMessage en ProtocolConnector para "+this.getProtocolName());
    	synchronized(this._state_lock){
    		this.getMediator().sendMessage((ACLMessage)message);
    	}
        return message;
    }
    
    public Object handleEvent(Object event){
    	//if(d)Log.d(TAG,"handleEvent en ProtocolConnector para "+this.getProtocolName());
    	synchronized(this._state_lock){
    		/*if(getCurrent_state()==null){
    			//Log.d("INITIATOR","el estado actual es nulo para "+event.getClass().getName());
	        }*/
	        ProtocolState next_state=getCurrent_state().handleInput(event);	        
	        //Log.d("CONNECTOR", "Desde ProtocolConnector (handleEvent) pasamos al estado "+this.getCurrent_state().getState_name());
	        setCurrent_State(next_state);       
    	}
        return event;
    }
    
    /**
     * @return the current_state
     */
    public ProtocolState getCurrent_state() {
    	ProtocolState res;
    	synchronized(this._current_lock){
    		res=current_state;
    	}
        return res;
        //return this.current_state;
    }

    /**
     * @param current_state the current_state to set
     */
    synchronized protected void setCurrent_State(ProtocolState cs) { 
    	synchronized(this._current_lock){
    		//Log.d("CONNECTOR","El estado actual es "+cs.getState_name());
    		this.current_state = cs;
    	}
    }

    /**
     * @return the initial_state
     */
    public ProtocolState getInitial_State() {
        return initial_state;
    }

    /**
     * @param initial_state the initial_state to set
     */
    protected void setInitial_state(ProtocolState initial_state) {
        //System.out.println("SetInitialstate");
        this.initial_state = initial_state;
    }

    // Mï¿½todos para manipular el contexto de ejecuciï¿½n
    public Object getVariable(String id){
        return this._execution_context.get(id);
    }

    public void setVariable(String id,Object obj){
        synchronized(this._execution_context){
            this._execution_context.put(id, obj);
        }
    }

    public void removeVariable(String id){
        synchronized(this._execution_context){
            this._execution_context.remove(id);
        }
    }

    public void registerTransition(PatternInterface mp,ProtocolState ini,ProtocolState nex, Goal goal){
        ini.registerTransition(mp, nex, goal);
    }

    // Para que sea obligatorio asignar un nombre al protocolo
    abstract public String getProtocolName();

    // Aquí van todas las inicializaciones
    abstract protected void setup();

    synchronized public void runProtocol(){
        this.initialTransition();
        this.setup();
    }

    public void setConversationId(String id){
        this._conversatin_id=id;
    }

    public String getConversationId(){
        return this._conversatin_id;
    }

    /**
     * Obtener una referencia a un componente
     */
    public Object getComponent(String id){
        return this.getMediator().getComponent(id);
    }
    
    /**
     * Se añade un objetivo para que sea ejecutado por el agente.
     * */
    public void addGoal(Goal g, Object input){
    	//if(d)Log.d(TAG, "se añade un objetivo en protocol connector");
    	getMediator().addGoal(g,input);
    }
}
