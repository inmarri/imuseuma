package candileja.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import candileja.core.aspect.Aspect;
import candileja.core.aspect.AspectDescription;
import candileja.core.aspect.PatternInterface;
import candileja.core.aspect.Role;
import candileja.core.aspect.Scope;
import candileja.core.aspect.coordination.ProtocolConnector;
import candileja.core.aspect.distribution.DistributionAspect;
import candileja.core.aspect.distribution.FIPAAgentPlatform;
import candileja.core.aspect.monitoring.Monitoring;
import candileja.core.aspect.selfadjusting.SelfAdjusting;

public abstract class Agent implements AspectConfigurationService,AgentContextInterface,AgentConfigurationService{
	private Boolean d=false;
	private String TAG="CANDILEJA";
	
	// Application specific information
	private Hashtable<String,Object> knowledgeBase;
	// Environment information -> Physical, Computational
	private Hashtable<String,Context> context;
	// Mecanismos de comunicación con el mundo externo al agente
	private Hashtable<String,Facet> facets;
	// Objetivos que el agente tiene que alcanzar actualmente
	private List<Goal> goals;
	// Planes que el agente puede ejecutar para alcanzar esos objetivos.
	private PlanLibrary planLibrary;
	//private List<PlanDescription> plans;
	// Componentes internos del agente -> No sé si vamos a tener componentes o no.
	private Hashtable<String,Object> components;
	// Ejecutor de planes
	private Scheduler scheduler;
	// Ejecutor de configuraciones.
	//private ReconfigurationExecutor reconfigurationExecutor;
	
	// Puntos de intercepción por defecto
	public static String RCV_MSG="RCV_MSG";
    public static String SND_MSG="SND_MSG";
    public static String THRW_EVNT="THRW_EVNT";   
    public static Agent myAgent;
    
    // Identificador del agent
    private String aid;
    
    // Aspectos y reglas de composición.
    private Hashtable<String,List<AspectDescription>> compositionRules;
    private Hashtable<String,Aspect> activeAspects;
	
	public Agent(){
		myAgent=this;
		knowledgeBase =new Hashtable<String,Object>();
		context=new Hashtable<String,Context>();
		facets=new Hashtable<String,Facet>();
		goals=new ArrayList<Goal>();
		//plans=new ArrayList<PlanDescription>();
		components=new Hashtable<String,Object>();
		scheduler=new Scheduler(this);
		//reconfigurationExecutor=new ReconfigurationExecutor(this);
		compositionRules=new Hashtable<String,List<AspectDescription>>();
		activeAspects=new Hashtable<String,Aspect>();
		planLibrary=new PlanLibrary();
	}
	
	/**
	 * Métodos para la configuración del agente.
	 * */
	public abstract void setup();
	public abstract void compositionRules();
	
	/**
	 * Métodos para el conocimiento.
	 * */
	public void addKnowledge(String id,Object obj){
		knowledgeBase.put(id, obj);
		this.throwEvent(new KnowledgeEvent(id,obj));
	}
	
	public Object getKnowledge(String id){
		return knowledgeBase.get(id);
	}
	
	/**
	 * Métodos dedicados al contexto
	 * */
	public void addContext(String id,Context ctx){
		context.put(id,ctx);
	}
	
	public Context getContext(String id){
		return context.get(id);
	}
	
	public void updateContext(String id,Object obj){
		 context.get(id).setData(obj);
		 context.get(id).setTime(System.currentTimeMillis());
		 this.throwEvent(new ContextEvent(id,obj));
	}
	
	public void removeContext(String id){
		context.remove(id);
	}
	
	/**
	 * Métodos dedicados a las facetas
	 * */
	public void addFacet(String id,Facet f){
		f.setAgent(this);
		facets.put(id,f);
	}
	
	public Facet getFacet(String id){
		//if(d)Log.d(TAG,"Se ejecuta getFacet");
		return facets.get(id);
	}
	
	public Effector getEffector(String id){
		return (Effector)facets.get(id);
	}
	
	public void removeFacet(String id){
		facets.remove(id);
	}
	
	/**
	 * Métodos dedicados a los objetivos
	 * */
	public List<Goal> getGoals(){
		List<Goal> res=null;
		synchronized(goals){
			res=goals;
		}
		return res;
	}
	
	public void addGoal(Goal g,Object input){
		WorkPacket wp=new WorkPacket();
		
		synchronized(goals){
			//if(true)Log.d(TAG, "Se añade al agente el objetivo "+g.getGoal());
			// Nuevo objetivo a cumplir.
			wp.setGoal(g);
			wp.setInput(input);
			goals.add(g);
			// Se busca en la plan library los planes que pueden resolver este objetivo.
			List<PlanDescription> plans=planLibrary.getPlanDescription(g);
			wp.setPlans(plans);
			// Se manda el work packet al scheduler
			scheduler.addWorkPacket(wp);
		}
	}
	
	public void removeGoals(List<Goal> g){
		synchronized(goals){
			for(int i =0;i<g.size();i++){
				goals.remove(g.get(i));
			}
		}
	}
	
	public void removeGoal(Goal g){
		synchronized(goals){
			goals.remove(g);
		}
	}
	
	public Boolean isActiveGoal(Goal g){
		return goals.contains(g);
	}
	
	/**
	 * Métodos dedicados a los planes.
	 * */
	public void addPlan(PlanDescription p){
		planLibrary.addPlanDescription(p);
		
	}
	
	public void removePlan(PlanDescription p){
		planLibrary.removePlanDescription(p);
	}
	
	/**
	 * Métodos dedicados a las componentes.
	 * */
	public void addComponent(String id,Object comp){
		synchronized(components){
			if(comp instanceof EventCommunication){
				((EventCommunication) comp).setMediator(this);
			}
			components.put(id,comp);
		}
	}
	
	public Object getComponent(String id){
		Object res=null;
		synchronized(components){
			res=components.get(id);
		}
		//if(d)Log.d(TAG,"getcomponent");
		return res;
	}
	
	public void removeComponent(String id){
		synchronized(components){
			this.components.remove(id);
		}
	}
	
	/**
	 * Métodos dedicados a las reglas de composición.
	 * */
	public void addCompositionRule(String t,String role, PatternInterface restriction, String instanceName, String aspectClass, boolean relevance,int scope,String ad){		
		//if(d)Log.d(TAG,"Se añade regla de composicion para el tag "+t);
		List<AspectDescription> aspects;
		AspectDescription aspDesc;		
		//  AspectDescription(String role, PatternInterface restriction, String instanceName, String aspectClass,boolean relevance,int sc)
		// hay reglas para este punto de interecepción
		synchronized(compositionRules){
			if(compositionRules.containsKey(t)){
				aspects=compositionRules.get(t);
			}else{
				aspects=new ArrayList<AspectDescription>();
			}
			aspDesc=new AspectDescription(role,restriction,instanceName,aspectClass,relevance, scope,ad);
			aspects.add(aspDesc);
			//if(d)Log.d(TAG, "Hay "+aspects.size()+" entradas en la lista.");
			compositionRules.put(t, aspects);
		}
		//if(d)Log.d(TAG, "Hay "+compositionRules.size()+" elementos en la tabla.");
	}
	
	// Eliminar todas las reglas asociadas con un punto de intercepción.
	public void removeCompositionRuleByIP(String ip){
		synchronized(compositionRules){
			compositionRules.remove(ip);
		}
		//if(d)Log.d(TAG, "Hay "+compositionRules.size()+" elementos en la tabla.");
	}
	
	// Eliminar todas las reglas asociadas a un aspecto.
	public void removeCompositionRuleByAspect(String aspectClass){
		List<AspectDescription> aspects;
		String ip;
		
		synchronized(compositionRules){
			// 1. Se obtienen todos los puntos de intercepción.
			Enumeration<String> enume=compositionRules.keys();
			// 2. Para cada punto de intercepción se inspecciona si existen aspectos que tenga la misma clase.
			while(enume.hasMoreElements()){
				ip=enume.nextElement();
				aspects=compositionRules.get(ip);
				for(int i=0;i<aspects.size();i++){
					if(aspects.get(i).getAspectClass().equals(aspectClass)){
						aspects.remove(i);
						//if(d)Log.d(TAG, "Hay "+aspects.size()+" entradas en la lista.");
					}
				}
				// 3. Se actualiza la lista de aspectos asociados a este punto de intercepción.
				compositionRules.put(ip, aspects);
			}
		}
		// Se eliminan todas las instancias del aspecto eliminado.
		removeActiveAspectByClass(aspectClass);
		//if(d)Log.d(TAG, "Hay "+compositionRules.size()+" elementos en la tabla.");
	}
	
	// Eliminar por regla de composición en un punto de intercepción en concreto.
	public void removeCompositionRuleByRule(String ip,AspectDescription ad){
		List<AspectDescription> aspects;
		
		synchronized(compositionRules){
			// 1. Se obtiene la lista de aspectos asociados al punto de intercepción ip.
			aspects=compositionRules.get(ip);
			// 2. Se borra el AspectDescription que se pasa como argumento.
			aspects.remove(ad);
			//if(d)Log.d(TAG, "Hay "+aspects.size()+" entradas en la lista.");
			compositionRules.put(ip, aspects);
		}
		//if(d)Log.d(TAG, "Hay "+compositionRules.size()+" elementos en la tabla.");
	}
	
	/**
	 * Métods dedicados a los aspectos.
	 * */
	public void addActiveAspect(String ri,Aspect aspect){
		synchronized(activeAspects){
			//if(d)Log.d(TAG,"Se añade el aspecto activo con id "+ri);
			aspect.setMediator(this);
			activeAspects.put(ri, aspect);
			if(aspect instanceof ProtocolConnector){
				((ProtocolConnector)aspect).runProtocol();
			}
			if(aspect instanceof Monitoring){
				Monitoring mon=(Monitoring)aspect;
				Thread monitorThread=new Thread(mon);
				monitorThread.start();
			}
			if(aspect instanceof SelfAdjusting){
				((SelfAdjusting)aspect).setup();
			}
		}
	}
	
	public void removeActiveAspect(String ri){
		synchronized(activeAspects){
			Aspect asp=activeAspects.get(ri);
			if(asp instanceof Monitoring){
				Monitoring mon=(Monitoring)asp;
				mon.end();
			}
			activeAspects.remove(ri);
		}
	}
	
	// Elimina todas las instancias de un aspecto de una determinada clase en la lista de
	// aspectos activos.
	public void removeActiveAspectByClass(String className){
		String key;
		Aspect cand;
		
		synchronized(activeAspects){
			Enumeration<String> keysEnum=activeAspects.keys();
			while(keysEnum.hasMoreElements()){
				key=keysEnum.nextElement();
				cand=activeAspects.get(key);
				if(cand.getClass().getName().equals(className)){
					activeAspects.remove(key);
				}
			}
		}
	}
	
	public Boolean isActiveAspect(String ri){
		return activeAspects.containsKey(ri);
	}
	
	public Aspect getActiveAspect(String ri){
		return activeAspects.get(ri);
	}
	
	/**
	 * Métodos dedicados al tejido de aspectos.
	 **/
	// Dado un string genera un identificador unico
    private String generateConversationId(String protName){
        Random generator = new Random(1000000);
        int f = generator.nextInt();
        String id=protName+f;
        return id;
    }
    
    // Instancia un aspecto con el identificador dado y lo registra en la estructura active aspects.
    public Aspect instantiateAspect(String id, String className, boolean conversationScope){
        Aspect asp=null;
        try{
            @SuppressWarnings("rawtypes")
			Class myClass=Class.forName(className);
            asp=(Aspect)myClass.newInstance();
            //if(d)Log.d(TAG,"Se asigna el mediador para el aspecto "+asp.getClass().getName());
            asp.setMediator(this);          
            if(asp instanceof ProtocolConnector){
                if(conversationScope){
                    // El id de la conversaciÃ³n es el mismo que se ha pasado como argumento
                    ((ProtocolConnector)asp).setConversationId(id);
                }else{
                    // Debe de generarse un nuevo id para la conversaciÃ³n.
                    ((ProtocolConnector)asp).setConversationId(this.generateConversationId(id));
                }           
            } 
            // se guarda en la lista de aspectos instanciados
            this.addActiveAspect(id, asp);		         
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return asp;
    }
    
    private Object executeMethod(Aspect asp,String met,Object input){
    	  	
    	Method method = null;
    	Object res=null;
    	
    	try {
    		//if(d)Log.d(TAG, "Ejecutando método para "+asp.getClass().getName()+" para el método "+met);
			method=asp.getClass().getMethod(met,Object.class);
			//if(d)Log.d(TAG, "Después de la obtención del método");
			res=method.invoke(asp, input);
			//if(d)Log.d(TAG, "Después de la ejecutar el método");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return res;
    }
    
	// Obtiene un aspecto dada su descripción y la entrada del pointcut.
    private Aspect getRequestAspectForMessage(AspectDescription desc, Object input){
        String roleInstance="no set";
        Aspect asp;

        // Se determina el scope del mensaje
        if(desc.getScope().equals(Scope.CONVERSATION_SCOPE)){
            roleInstance=((ACLMessage)input).getConversationID();
        }else if(desc.getScope().equals(Scope.PROTOCOL_SCOPE)){
            roleInstance=((ACLMessage)input).getProtocol();
        }else{
            roleInstance=desc.getRoleInstance();
        }
        // comprobamos si ya estÃ¡ instanciado,si no lo estÃ¡ se le instancia
        if(this.isActiveAspect(roleInstance)){
            asp=this.getActiveAspect(roleInstance);
        }else{
            if(desc.getScope().equals(Scope.CONVERSATION_SCOPE)){
                asp=this.instantiateAspect(roleInstance, desc.getAspectClass(),true);
            }else{
                asp=this.instantiateAspect(roleInstance, desc.getAspectClass(),false);
            }
        }
        return asp;
    }
    
    // Dado un evento genera un aspecto para compuesto en el lanzamiento de dicho aspecto.
    private Aspect getRequestAspectForEvent(AspectDescription desc,Object event){
        String convId;
        Aspect evSer;

        if(desc.getScope().equals(Scope.CONVERSATION_SCOPE)){
            // Se genera el identificador de la conversacion
            convId=this.generateConversationId("eventService");
            // Se instancia y registra en la arquitectura
            evSer=(Aspect) this.instantiateAspect(convId, desc.getAspectClass(),true);
        }else{
            convId=desc.getRoleInstance();
            //if(convId.equals("MeasureTime") || convId.equals("FeedReader"))Log.d("MEDIATOR","La roleInstance buscada es "+convId);
            // ¿Se encuentra el protocolo instanciado?
            if(this.isActiveAspect(convId)){
                evSer=(Aspect) this.getActiveAspect(convId);
            }else{
                evSer=(Aspect) this.instantiateAspect(convId, desc.getAspectClass(),false);
            }
        }
        return evSer;
    }
    /**
     * Puntos de intercepción.
     * **/
	public void sendMessage(Object obj){
		weaveAspect(SND_MSG, obj);
	}
	
	public void receiveMessage(Object obj){
		weaveAspect(RCV_MSG, obj);
	}
	
	public void throwEvent(Object obj){
		weaveAspect(THRW_EVNT, obj);
	}
	
	// Dada una etiqueta que identifica un punto de intercepción y un objeto, realiza el proceso de composición de aspectos.
		protected Object weaveAspect(String ip,Object obj){
			
			PatternInterface pi;
			Aspect aspect;
			
			synchronized(compositionRules){
				// 1. Obtener los aspectos que se corresponden con ese punto de intercepción.
				List<AspectDescription> aspects=compositionRules.get(ip);
				
				if(aspects!=null){
					for(int i=0;i<aspects.size();i++){
						// 2. Se obtiene el patrón que habilita la composición de aspectos.
						pi=aspects.get(i).getRestriction();
						// 3.1. Si no tiene patrón que cumplir se obtiene el aspecto y le pasamos la entrada.
						if(pi==null) {
							if(aspects.get(i).getRole()==Role.COORDINATION){
								aspect=getRequestAspectForMessage(aspects.get(i), obj);
							}else{
								aspect=getRequestAspectForEvent(aspects.get(i), obj);
							}
							//obj=aspect.weaveAspectAtIP(ip, obj);
							obj=executeMethod(aspect, aspects.get(i).getAdvice(), obj);
							//if(d)Log.d(TAG,"dentro del weaveaspect y después de executeMethod");
						}else{
							// 3.2. Si tiene restricción que cumplir se comprueba que la entrada cumple 
							// la restricción y se procede igual que antes.
							if(pi.matchPattern(obj)){
								if(aspects.get(i).getRole()==Role.COORDINATION && obj instanceof ACLMessage){
									aspect=getRequestAspectForMessage(aspects.get(i), obj);
								}else{
									aspect=getRequestAspectForEvent(aspects.get(i), obj);
								}
								//obj=aspect.weaveAspectAtIP(ip, obj);
								obj=executeMethod(aspect, aspects.get(i).getAdvice(), obj);
							}
						}
					}
				}
			}
			return obj;
		}

	
	/**
	 * Poner al agente en ejecución
	 * */
	public void runAgent(){
		// Puede ser necesario en el futuro que esto componentes puedan ser accedidos como componentes.
		scheduler.start();
		//reconfigurationExecutor.start();
		compositionRules();
		setup();
	}

	/**
	 * Equals and hashcode
	 * */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((TAG == null) ? 0 : TAG.hashCode());
		result = prime * result
				+ ((activeAspects == null) ? 0 : activeAspects.hashCode());
		result = prime * result
				+ ((components == null) ? 0 : components.hashCode());
		result = prime
				* result
				+ ((compositionRules == null) ? 0 : compositionRules.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		result = prime * result + ((facets == null) ? 0 : facets.hashCode());
		result = prime * result + ((goals == null) ? 0 : goals.hashCode());
		result = prime * result
				+ ((knowledgeBase == null) ? 0 : knowledgeBase.hashCode());
		result = prime * result
				+ ((planLibrary == null) ? 0 : planLibrary.hashCode());
		result = prime * result
				+ ((scheduler == null) ? 0 : scheduler.hashCode());
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
		Agent other = (Agent) obj;
		if (TAG == null) {
			if (other.TAG != null)
				return false;
		} else if (!TAG.equals(other.TAG))
			return false;
		if (activeAspects == null) {
			if (other.activeAspects != null)
				return false;
		} else if (!activeAspects.equals(other.activeAspects))
			return false;
		if (components == null) {
			if (other.components != null)
				return false;
		} else if (!components.equals(other.components))
			return false;
		if (compositionRules == null) {
			if (other.compositionRules != null)
				return false;
		} else if (!compositionRules.equals(other.compositionRules))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		if (facets == null) {
			if (other.facets != null)
				return false;
		} else if (!facets.equals(other.facets))
			return false;
		if (goals == null) {
			if (other.goals != null)
				return false;
		} else if (!goals.equals(other.goals))
			return false;
		if (knowledgeBase == null) {
			if (other.knowledgeBase != null)
				return false;
		} else if (!knowledgeBase.equals(other.knowledgeBase))
			return false;
		if (planLibrary == null) {
			if (other.planLibrary != null)
				return false;
		} else if (!planLibrary.equals(other.planLibrary))
			return false;
		if (scheduler == null) {
			if (other.scheduler != null)
				return false;
		} else if (!scheduler.equals(other.scheduler))
			return false;
		return true;
	}
	
	/**Añadir un agente a una plataforma -> Revisar!!*/
	public DistributionAspect addDistributionAspect(String apId, String name){
		//if(d)Log.d(TAG,"Se ejecuta addDistribution aspect.");
        String[] args=new String[2];
        args[0]=this.getAID();
        args[1]=name;
        FIPAAgentPlatform ap=(FIPAAgentPlatform) this.getComponent(apId);
        //if(d)Log.d(TAG,"Antes de deploy agent");
        ap.deployAgent(args);
        //if(d)Log.d(TAG,"Despues de deploy agent");
        //while(this._mediator.getActiveAspect(name)==null);
        return (DistributionAspect) this.getActiveAspect(name);
    }
	
	public void setAID(String a){
		aid=a;
	}
	
	public String getAID(){
		return aid;
	}
	
	
	
}
