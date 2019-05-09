/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

import android.util.Log;
import candileja.core.ACLMessage;



/**
 *
 * @author Inma
 */
public class MessagePattern implements PatternInterface{
	
	private Boolean d=false;
	private String TAG="PATTERN";

    private String performative, protocol, acl_representation, language,encoding, ontology;
	private BooleanConnector notPerf,notProt,notACL, notLang,notEncod,notOnto;


    public MessagePattern(){
        this.performative=null;
        this.acl_representation=null;
        this.encoding=null;
        this.language=null;
        this.protocol=null;
        this.ontology=null;
        this.notACL=null;
        this.notEncod=null;
        this.notLang=null;
        this.notOnto=null;
        this.notPerf=null;
        this.notProt=null;
    }

    public String getAcl_representation() {
        return acl_representation;
    }

    public void setAcl_representation(String acl_representation) {
        this.acl_representation = acl_representation;
    }

    public String getEncoding() {
        String res=this.encoding;
        if (this.notEncod!=null) {
            res=this.notEncod+res;
        }
        return res;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getLanguage() {
        String res=this.language;
        if (this.notLang!=null) {
            res=this.notLang+res;
        }
        return res;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    public void setLanguage(BooleanConnector bc,String language) {
        this.language = language;
        this.notLang=bc;
    }

    public String getOntology() {
        String res=this.ontology;
        if (this.notOnto!=null) {
            res=this.notOnto+res;
        }
        return res;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public void setOntology(BooleanConnector bc,String ontology) {
        this.notOnto=bc;
        this.ontology = ontology;
    }

    public String getPerformative() {
        String res=this.performative;
        if (this.notPerf!=null) {
            res=this.notPerf+res;
        }
        return res;
    }

    public void setPerformative(String performative) {
        this.performative = performative;
    }

    public void setPerformative(BooleanConnector bc,String performative) {
        this.performative = performative;
        this.notPerf=bc;
    }

    public String getProtocol() {
        String res=this.protocol;
        if (this.notProt!=null) {
            res=this.notProt+res;
        }
        return res;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setProtocol(BooleanConnector bc,String protocol) {
        this.protocol = protocol;
        this.notProt=bc;
    }

    public boolean matchPerformative(ACLMessage msg){
        boolean res=false;
        if(this.performative==null){
            res=true;
        }else{
            if(this.notPerf==null){
                if(this.performative.equals(msg.getPerformative())){
                    res=true;
                }else{
                    res=false;
                }
            }else{
                if(!this.performative.equals(msg.getPerformative())){
                    res=true;
                }else{
                    res=false;
                }
            }
        }
        if(res){
        	if(d)Log.d(TAG, "matchPerformativa");
        }else{
        	if(d)Log.d(TAG, "no!! matchPerformativa");
        }
        return res;
    }

    public boolean matchProtocol(ACLMessage msg){
        boolean res=false;
        if(this.protocol==null){
            res=true;
        }else{
            if(this.notProt==null){
                if(this.protocol.equals(msg.getProtocol())){
                    res=true;
                }else{
                    res=false;
                }
            }else{
                if(!this.protocol.equals(msg.getProtocol())){
                    res=true;
                }else{
                    res=false;
                }
            }
        }
        if(res){
        	if(d)Log.d(TAG, "matchProtocol");
        }else{
        	if(d)Log.d(TAG, "no!! matchProtocol");
        }
        return res;
    }
    
    public boolean matchOntology(ACLMessage msg){
        boolean res=false;
        if(this.ontology==null){
            res=true;
        }else{
            if(this.notOnto==null){
                if(this.ontology.equals(msg.getOntology())){
                    res=true;
                }else{
                    res=false;
                }
            }else{
                if(!this.ontology.equals(msg.getOntology())){
                    res=true;
                }else{
                    res=false;
                }
            }
        }
        if(res){
        	if(d)Log.d(TAG, "matchOntology");
        }else{
        	if(d)Log.d(TAG, "no!! matchOntology");
        }
        return res;
    }

    // Por ahora solo la performativa y el protocolo -> cuando la cosa se
    // complique podemos estudiar como hacen el mecanismo de plantillas los
    // ACLMessage de JADE
    public boolean matchPattern(Object obj){
        boolean res=false;
        if(obj instanceof ACLMessage){
            ACLMessage msg=(ACLMessage)obj;
            res=this.matchPerformative(msg) && this.matchProtocol(msg)&& this.matchOntology(msg);
        }
        return res;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((acl_representation == null) ? 0 : acl_representation
						.hashCode());
		result = prime * result
				+ ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((notACL == null) ? 0 : notACL.hashCode());
		result = prime * result
				+ ((notEncod == null) ? 0 : notEncod.hashCode());
		result = prime * result + ((notLang == null) ? 0 : notLang.hashCode());
		result = prime * result + ((notOnto == null) ? 0 : notOnto.hashCode());
		result = prime * result + ((notPerf == null) ? 0 : notPerf.hashCode());
		result = prime * result + ((notProt == null) ? 0 : notProt.hashCode());
		result = prime * result
				+ ((ontology == null) ? 0 : ontology.hashCode());
		result = prime * result
				+ ((performative == null) ? 0 : performative.hashCode());
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
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
		MessagePattern other = (MessagePattern) obj;
		if (acl_representation == null) {
			if (other.acl_representation != null)
				return false;
		} else if (!acl_representation.equals(other.acl_representation))
			return false;
		if (encoding == null) {
			if (other.encoding != null)
				return false;
		} else if (!encoding.equals(other.encoding))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (notACL == null) {
			if (other.notACL != null)
				return false;
		} else if (!notACL.equals(other.notACL))
			return false;
		if (notEncod == null) {
			if (other.notEncod != null)
				return false;
		} else if (!notEncod.equals(other.notEncod))
			return false;
		if (notLang == null) {
			if (other.notLang != null)
				return false;
		} else if (!notLang.equals(other.notLang))
			return false;
		if (notOnto == null) {
			if (other.notOnto != null)
				return false;
		} else if (!notOnto.equals(other.notOnto))
			return false;
		if (notPerf == null) {
			if (other.notPerf != null)
				return false;
		} else if (!notPerf.equals(other.notPerf))
			return false;
		if (notProt == null) {
			if (other.notProt != null)
				return false;
		} else if (!notProt.equals(other.notProt))
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
		return true;
	}
    
    

}
