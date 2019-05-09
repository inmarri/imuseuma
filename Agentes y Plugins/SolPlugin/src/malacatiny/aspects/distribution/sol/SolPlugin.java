package malacatiny.aspects.distribution.sol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import malacatiny.aspects.representation.aurora.AuroraMessage;
import android.annotation.SuppressLint;
import candileja.core.Agent;
import candileja.core.aspect.distribution.DistributionAspect;

public class SolPlugin extends DistributionAspect implements Runnable{
	
	// Debuging
	//private String tag="SOL";
	//private Boolean d=true;
	
	private Boolean _end;
	private String _HOST;
	private int _PORT;
	private Socket _socket;
    private PrintStream             _printer;
    private BufferedReader    _reader;
    //private SolAPInterface _ap;
    private String aid;
    private String mas;
    private String category;
    private String idaspect;
	
	public SolPlugin(String i,int p,String a, String mas, String cat, String idaspect){
		//if(d)Log.d(tag,"Se ha creado el SolPlugin.");
		this._HOST=i;
		this._PORT=p;
		this._end=false;		
		this.aid=a;
		this.mas=mas;
		this.category=cat;
		this.idaspect=idaspect;
	}

	@Override
	synchronized public Object handleOutputMessage(Object message) {
		//if(d)Log.d(tag,"Se envía un mensaje.");
		AuroraMessage output=(AuroraMessage)message;
		output.setSender((String)this.getAID());
		this._printer.println(message.toString());
		return output;
	}

	@Override
	public Object getAID() {
		// TODO Auto-generated method stub
		return this.aid;
	}

	public void stopPlugin() {
		this._end=true;
	}

	public void run() {
		//if(d)Log.d(tag,"Se ejecuta el run");
		String line;
		AuroraMessage input;
		
		try {			
			_socket=new Socket(this._HOST,this._PORT);
			this._printer = new PrintStream(this._socket.getOutputStream());
			this._reader=new BufferedReader ( new InputStreamReader ( this._socket.getInputStream() ) );
			this.registerAgent();
			while(!this._end){
				line=this._reader.readLine();
				// Se procesa el mensaje leido.
				if(line!=null){
					input=new AuroraMessage(line);
					//if(d)Log.d(tag,this.getAID().toString()+" ha recibido un mensaje de "+input.toString());
					// Se propaga el mensaje.
					this.handleInputMessage(input);
					// Se ha recibido una respuesta del servicio de registro de agentes.
					if(input.getOntology().equals("REG_ONTOLOGY")){
						if(input.getProtocol().equals("RegisterAgent")){
							//if(d)Log.d(tag,"El agente ha sido registrado correcamente.");
							// Lanzar evento de que está funcionando.
							this.getMediator().throwEvent(new SolPluginRunningEvent());
						}else if(input.getProtocol().equals("RegisterGroup")){
							//if(d)Log.d(tag,"El agente ha sido dado de alta en el grupo.");
							// Se inicia el plugin de grupos.
							this.createMulticastPlugin(input.getContent());
							StringTokenizer tokenizer=new StringTokenizer(input.getContent(),":");
							String group=tokenizer.nextToken();
							this.getMediator().throwEvent(new RegisterGroupEvent(group));
						}else if(input.getProtocol().equals("UnregisterAgent")){ //&& input.getPerformative().equals(AuroraMessage.CONFIRM)){
							//if(d)Log.d(tag,"El agente ha sido desregistrado correcamente.");
							this.stopPlugin();
							// Lanzar evento de que está funcionando.
							this.getMediator().throwEvent(new SolPluginUnregisterEvent());
							this.getMediator().removeActiveAspectByClass(idaspect);
							this.getMediator().removeCompositionRuleByAspect(idaspect);
						}else if(input.getProtocol().equals("LeaveGroup")){
							if(input.getPerformative().equals(AuroraMessage.FAILURE)){
								//if(d)Log.d(tag,"El desregistro del grupo ha fallado.");
							}else{
								//if(d)Log.d(tag,"El desregistro del grupo ha sido un éxito.");
								removeMulticastPlugin(input.getContent());
							}
						}
					// Se ha recibido una respuesta del registro de servicios
					}else if(input.getOntology().equals("DF_ONTOLOGY")){
						if(input.getProtocol().equals("RegisterService")){
							//if(d)Log.d(tag,"El servicio ha sido registrado correctamente.");
						}else if(input.getProtocol().equals("QueryService")){
							//if(d)Log.d(tag,"Se ha recibido una respuesta de la consulta de servicios.");
							//if(d)Log.d(tag,input.getContent());							
							//this._ap.addAnswer(input.getContent());
						}if(input.getProtocol().equals("UnregisterService")){
							if(input.getPerformative().equals(AuroraMessage.CONFIRM)){
								//if(d)Log.d(tag,"El servicio ha sido desregistrado correctamente.");
							}else if(input.getPerformative().equals(AuroraMessage.FAILURE)){
								//if(d)Log.d(tag,"El servicio NO ha sido desregistrado correctamente.");
							}
						}
					}
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void registerAgent(){		
		AuroraMessage msg=new AuroraMessage();
		msg.setContent((String)this.getAID()+"##"+mas+"##"+category);
		msg.setConverstionId(System.currentTimeMillis()+"");
		Vector<String> rec=new Vector<String>();
		rec.add("HOST");
		msg.setReceivers(rec);
		msg.setSender((String)this.getAID());
		msg.setLanguage("FIPA-ACL");
		msg.setOntology("REG_ONTOLOGY");
		msg.setPerformative(AuroraMessage.REQUEST);
		msg.setProtocol("RegisterAgent");
		msg.setEnconding("UTF");
		// Se envía el mensaje de registro.
		this.handleOutputMessage(msg);
	}
	
	/*protected void registerService(String type,String name){
		// Se compone el mensaje de registro.
		AuroraMessage msg=new AuroraMessage();
		msg.setContent("(SERVICE (NOMBRE "+name+") (TIPO "+type+"))");
		msg.setConverstionId(System.currentTimeMillis()+"");
		msg.setEnconding("UTF");
		msg.setLanguage("FIPA-ACL");
		msg.setOntology("DF_ONTOLOGY");
		msg.setPerformative(AuroraMessage.REQUEST);
		msg.setProtocol("RegisterService");
		Vector<String> rec=new Vector<String>();
		rec.add("HOST");
		msg.setReceivers(rec);
		this.handleOutputMessage(msg);
	}
	
	protected void unregisterService(String type,String name){
		// Se compone el mensaje de registro.
		AuroraMessage msg=new AuroraMessage();
		msg.setContent("(SERVICE (NOMBRE "+name+") (TIPO "+type+"))");
		msg.setConverstionId(System.currentTimeMillis()+"");
		msg.setEnconding("UTF");
		msg.setLanguage("FIPA-ACL");
		msg.setOntology("DF_ONTOLOGY");
		msg.setPerformative(AuroraMessage.REQUEST);
		msg.setProtocol("UnregisterService");
		Vector<String> rec=new Vector<String>();
		rec.add("HOST");
		msg.setReceivers(rec);
		this.handleOutputMessage(msg);
	}*/
	
	// data = ip:puerto
	@SuppressLint("UseValueOf")
	protected void createMulticastPlugin(String data){
		StringTokenizer tokenizer=new StringTokenizer(data,":");
		String groupName=tokenizer.nextToken();
        String ip=tokenizer.nextToken();
        Integer portInteger=new Integer(tokenizer.nextToken());
        int port=portInteger.intValue();
        MulticastPlugin ma=new MulticastPlugin(groupName,ip,port);
        Thread ta=new Thread(ma);
		// Se añade en la arquitectura
		Agent.myAgent.addActiveAspect(groupName, ma);
		// Se inicia la hebra para la escucha de mensajes.
		ta.start();
		
	}
	
	private Boolean removeMulticastPlugin(String id){
		Boolean res=false;
		if(Agent.myAgent.isActiveAspect(id)){
			Agent.myAgent.removeActiveAspect(id);
			res=true;
		}
		return res;
	}
}
