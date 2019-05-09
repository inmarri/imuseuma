/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package malacatiny.aspects.representation.aurora;

import java.util.Vector;

import candileja.core.ACLMessage;
import candileja.core.aspect.representation.RepresentationAspect;

/**
 *
 * @author Inma
 */
public class AuroraRepresentation extends RepresentationAspect{
	
	// debuging
	//private Boolean d=false;
	//private String tag="aurora";

    // Transforma un mensaje ACL de malasia al formato Aurora.
	public Object handleOutputMessage(Object msg){
        ACLMessage input=(ACLMessage)msg;
        /**
         * private int _performative;
            private String _protocol;
            private String _conversationID;
            private Object _content;
            private Vector _receivers;
            private String _sender;
            private String _encoding;
            private String _language;
         */
        //if(d)Log.d(tag,"handleOutputMessage");
        //Log.d("AURORA","Se handleOutput en aurora");
        AuroraMessage output=new AuroraMessage();
        output.setProtocol(input.getProtocol());
        output.setConverstionId(input.getConversationID());
        // Si algÃºn dÃ­a deja de ser String tendremos que cambiar esto
        output.setContent((String) input.getContent());
        output.setEnconding(input.getEncoding());
        output.setLanguage(input.getLanguage());
        output.setPerformative(input.getPerformative());
        output.setOntology(input.getOntology());
        output.setReceivers(this.transformVectorObject(input.getReceivers()));
        output.setSender((String)input.getSender());
        // No podemos crear un datagrama sin tener la conexión
        // Se le envía el recpetor del mensaje al aspecto de distribución
        // usando un array.
//        Object[] res=new Object[2];
//        // Se enviará un mensaje por cada uno de los receptores.
//        res[0]=input.getReceivers();
//        res[1]=output;
       // byte[] bytes=output.toString().getBytes();
        //Log.d("AURORA", "La longitud del mensaje es: "+bytes.length);

        return output;
    }
    
    private Vector<String> transformVectorObject(Vector<Object> input){
    	Vector<String> output=new Vector<String>();
    	
    	for(int i=0;i<input.size();i++){
    		output.addElement(input.elementAt(i).toString());
    	}
    	
    	return output;
    }
    
    private Vector<Object> transformVectorString(Vector<String> input){
    	Vector<Object> output=new Vector<Object>();
    	
    	for(int i=0;i<input.size();i++){
    		output.addElement((Object)input.elementAt(i));
    	}
    	return output;
    }

    // El aspecto de distribución va a generar mensajes Aurora.
    public Object handleInputMessage(Object msg){
        AuroraMessage input=(AuroraMessage)msg;
        ACLMessage res=null;
        try {
            /**
             * private int _performative;
                private String _protocol;
                private String _conversationID;
                private Object _content;
                private Vector _receivers;
                private String _sender;
                private String _encoding;
                private String _language;
             */
        	//if(d)Log.d(tag,"handleInputMessage");
            //content = new AuroraMessage(input.readUTF());
            res=new ACLMessage();
            res.setPerformative(input.getPerformative());
            res.setProtocol(input.getProtocol());
            res.setConversationID(input.getConverstionId());
            res.setContent(input.getContent());
            res.setReceivers(this.transformVectorString(input.getReceivers()));
            res.setEncoding(input.getEnconding());
            res.setLanguage(input.getLanguage());
            res.setOntology(input.getOntology());
            // El sender está en el datagram.
            res.setSender(input.getSender());
        } catch (Exception ex) {
            ex.printStackTrace();
        }      
        return res;
    }
    
    @SuppressWarnings("unused")
	private Byte[] transformByte(byte[] input){
        Byte[] res=new Byte[input.length];
        
        for (int i=0;i<input.length;i++){
            res[i]=new Byte(input[i]);
        }
        
        return res;
    }
}
