/*
 * Formato de mensajes para datagrama sobre UDP. Es muy similar al BlueMessage, pero no
 * tiene los campos de sender y receiver porque estos ya van incluidos en el datagram.
 */

package malacatiny.aspects.representation.aurora;

import java.util.StringTokenizer;
import java.util.Vector;

import android.util.Log;



/**
 *
 * @author Inma
 */
public class AuroraMessage {
	
	@SuppressWarnings("unused")
	private String tag="AURORA";
	@SuppressWarnings("unused")
	private Boolean d=true;

    private String content,converstionId,enconding,language,performative,protocol,sender,ontology;//,
    private Vector<String> receiver;

    public AuroraMessage(){
        receiver=new Vector<String>();
        content="";
    }

    // En el constructor se rellenan todos los campos del mensaje.
    public AuroraMessage(String message){
    	
    	try{
        receiver=new Vector<String>();
        
        StringTokenizer st=new StringTokenizer(message," ");
        String aux;
        // Aquí se adquiere (SL-MESSAGE
        aux=st.nextToken();
        // Se extraen los receiver, siempre tiene que haber por lo menos 1.
        //aux=(RECEIVER
        aux=st.nextToken();
        // El primer receiver
        aux=st.nextToken();
        while(!aux.equals("(LANGUAGUE")){
            this.receiver.addElement(aux);
            aux=st.nextToken();
        }
        // El último de los elementos tiene un ")" en el final que se puede quitar
        // facilmente
        aux=(String) this.receiver.elementAt(receiver.size()-1);
        aux=aux.substring(0, aux.length()-1);
        // Se sustituye el elemento
        this.receiver.removeElementAt(receiver.size()-1);
        this.receiver.addElement(aux);
        
        // Se obtiene el SENDER -> la cabecera "(SENDER" ya fue extraida
//        aux=st.nextToken();
//        this.sender=aux.substring(0, aux.length()-1);
        // Se obtiene el lenguaje
        // (LANGUAGUE
        //aux=st.nextToken();
        aux=st.nextToken();
        this.language=aux.substring(0, aux.length()-1);
        // Se obtiene el contenido -> es parecido a extraer a los receptores.
        // (CONTENT
        aux=st.nextToken();
        //System.out.println(aux);
        content=st.nextToken();
        aux=st.nextToken();
        //System.out.println(content);
        while(!aux.equals("(CONVERSATIONID")){
            content=content+" "+aux;
            aux=st.nextToken();
            //System.out.println(content);
        }
        //aux=st.nextToken();
        this.content=content.substring(0, content.length()-1);
        //System.out.println("El contenido es "+content);
        // Se obtiene el conversationID
        //aux=st.nextToken();
        aux=st.nextToken();
        this.converstionId=aux.substring(0, aux.length()-1);
        // Se obtiene el encoding
        aux=st.nextToken();
        aux=st.nextToken();
        this.enconding=aux.substring(0, aux.length()-1);
        // Se obtiene la performativa
        aux=st.nextToken();
        aux=st.nextToken();
        this.performative=aux.substring(0, aux.length()-1);
        // Se obtiene el protocolo, este tiene 2 paréntesis al final
        aux=st.nextToken();
        aux=st.nextToken();
        this.protocol=aux.substring(0, aux.length()-1);
        // Se obtiene la ontolog�a
        aux=st.nextToken();
        aux=st.nextToken();
        this.ontology=aux.substring(0, aux.length()-1);
        // Se obtiene el sender.
        aux=st.nextToken();
        aux=st.nextToken();
        this.sender=aux.substring(0, aux.length()-2);
    	}catch(Exception e){
    		Log.d("SOL","Error procesando el mensaje "+message);
    	}
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConverstionId() {
        return converstionId;
    }

    public void setConverstionId(String converstionId) {
        this.converstionId = converstionId;
    }

    public String getEnconding() {
        return enconding;
    }

    public void setEnconding(String enconding) {
        this.enconding = enconding;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPerformative() {
        return performative;
    }

    public void setPerformative(String performative) {
        this.performative = performative;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    
    public String getOntology(){
        return this.ontology;
    }
    
    public void setOntology(String ont){
        this.ontology=ont;
    }
    
    public Vector<String> getReceivers(){
        return this.receiver;
    }
    
    public void setReceivers(Vector<String> rec){
        this.receiver=rec;
    }
    
    public void addReceiver(String rec){
        this.receiver.addElement(rec);
    }
    
    public void removeReceiver(String rec){
        this.receiver.removeElement(rec);
    }
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
 // Un reply es un mensaje igual excepto en sender y el receiver.
    public AuroraMessage createReply(){
        AuroraMessage reply=new AuroraMessage();
        
        Vector<String> rec=new Vector<String>();
        rec.add(this.getSender());
        reply.setReceivers(rec);
        reply.setContent("TO-FILL");
        reply.setConverstionId(this.getConverstionId());
        reply.setEnconding(enconding);
        reply.setLanguage(language);
        reply.setOntology(this.getOntology());
        reply.setPerformative(AuroraMessage.ACCEPT_PROPOSAL);
        reply.setProtocol(protocol);
        
        return reply;
    }
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((converstionId == null) ? 0 : converstionId.hashCode());
		result = prime * result
				+ ((enconding == null) ? 0 : enconding.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result
				+ ((ontology == null) ? 0 : ontology.hashCode());
		result = prime * result
				+ ((performative == null) ? 0 : performative.hashCode());
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		AuroraMessage other = (AuroraMessage) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (converstionId == null) {
			if (other.converstionId != null)
				return false;
		} else if (!converstionId.equals(other.converstionId))
			return false;
		if (enconding == null) {
			if (other.enconding != null)
				return false;
		} else if (!enconding.equals(other.enconding))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (ontology == null) {
			if (other.ontology != null)
				return false;
		} else if (!ontology.equals(other.ontology))
			return false;
		if (performative == null) {
			if (other.performative != null)
				return false;
		} else if (!performative.equals(other.performative))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}

	   // Finalmente esto 
    public String toString(){
        String rec="";
        for(int i=0;i<this.receiver.size();i++){
            rec=rec+" "+this.receiver.elementAt(i);
        }     
        String res="(AURORA-MESSAGE  (RECEIVER"+rec+") (LANGUAGUE "+this.language
        +") (CONTENT "+this.content
        +") (CONVERSATIONID "+this.converstionId+") (ENCONDING "+this.enconding
        +") (PERFORMATIVE "+this.performative+") (PROTOCOL "+this.protocol
        +") (ONTOLOGY "+this.ontology+") (SENDER "+this.sender+"))";
        //Log.d(tag,"El tama�o del mensaje es: "+res.getBytes().length);
        return res;
    }
    
    /** constant identifying the FIPA performative **/
	public static final String ACCEPT_PROPOSAL = "ACCEPT_PROPOSAL";
	/** constant identifying the FIPA performative **/
	public static final String AGREE = "AGREE";
	/** constant identifying the FIPA performative **/
	public static final String CANCEL = "CANCEL";
	/** constant identifying the FIPA performative **/
	public static final String CFP = "CFP";
	/** constant identifying the FIPA performative **/
	public static final String CONFIRM = "CONFIRM";
	/** constant identifying the FIPA performative **/
	public static final String DISCONFIRM = "DISCONFIRM";
	/** constant identifying the FIPA performative **/
	public static final String FAILURE = "FAILURE";
	/** constant identifying the FIPA performative **/
	public static final String INFORM = "INFORM";
	/** constant identifying the FIPA performative **/
	public static final String INFORM_IF = "INFORM_IF";
	/** constant identifying the FIPA performative **/
	public static final String INFORM_REF = "INFORM_REF";
	/** constant identifying the FIPA performative **/
	public static final String NOT_UNDERSTOOD = "NOT_UNDERSTOOD";
	/** constant identifying the FIPA performative **/
	public static final String PROPOSE = "PROPOSE";
	/** constant identifying the FIPA performative **/
	public static final String QUERY_IF = "QUERY_IF";
	/** constant identifying the FIPA performative **/
	public static final String QUERY_REF = "QUERY_REF";
	/** constant identifying the FIPA performative **/
	public static final String REFUSE = "REFUSE";
	/** constant identifying the FIPA performative **/
	public static final String REJECT_PROPOSAL = "REJECT_PROPOSAL";
	/** constant identifying the FIPA performative **/
	public static final String REQUEST = "REQUEST";
	/** constant identifying the FIPA performative **/
	public static final String REQUEST_WHEN = "REQUEST_WHEN";
	/** constant identifying the FIPA performative **/
	public static final String REQUEST_WHENEVER = "REQUEST_WHENEVER";
	/** constant identifying the FIPA performative **/
	public static final String SUBSCRIBE = "SUBSCRIBE";
	/** constant identifying the FIPA performative **/
	public static final String PROXY = "PROXY";
	/** constant identifying the FIPA performative **/
	public static final String PROPAGATE = "PROPAGATE";
	/** constant identifying an unknown performative **/
	public static final String UNKNOWN = "UNKNOWN";
}
