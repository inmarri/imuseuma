package candileja.core;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Vector;


/**
 *
 * @author Mer
 */
public class ACLMessage{

    /** constant identifying the FIPA performative **/
	public static final int ACCEPT_PROPOSAL = 0;
	/** constant identifying the FIPA performative **/
	public static final int AGREE = 1;
	/** constant identifying the FIPA performative **/
	public static final int CANCEL = 2;
	/** constant identifying the FIPA performative **/
	public static final int CFP = 3;
	/** constant identifying the FIPA performative **/
	public static final int CONFIRM = 4;
	/** constant identifying the FIPA performative **/
	public static final int DISCONFIRM = 5;
	/** constant identifying the FIPA performative **/
	public static final int FAILURE = 6;
	/** constant identifying the FIPA performative **/
	public static final int INFORM = 7;
	/** constant identifying the FIPA performative **/
	public static final int INFORM_IF = 8;
	/** constant identifying the FIPA performative **/
	public static final int INFORM_REF = 9;
	/** constant identifying the FIPA performative **/
	public static final int NOT_UNDERSTOOD = 10;
	/** constant identifying the FIPA performative **/
	public static final int PROPOSE = 11;
	/** constant identifying the FIPA performative **/
	public static final int QUERY_IF = 12;
	/** constant identifying the FIPA performative **/
	public static final int QUERY_REF = 13;
	/** constant identifying the FIPA performative **/
	public static final int REFUSE = 14;
	/** constant identifying the FIPA performative **/
	public static final int REJECT_PROPOSAL = 15;
	/** constant identifying the FIPA performative **/
	public static final int REQUEST = 16;
	/** constant identifying the FIPA performative **/
	public static final int REQUEST_WHEN = 17;
	/** constant identifying the FIPA performative **/
	public static final int REQUEST_WHENEVER = 18;
	/** constant identifying the FIPA performative **/
	public static final int SUBSCRIBE = 19;
	/** constant identifying the FIPA performative **/
	public static final int PROXY = 20;
	/** constant identifying the FIPA performative **/
	public static final int PROPAGATE = 21;
	/** constant identifying an unknown performative **/
	public static final int UNKNOWN = -1;

        /** This array of Strings keeps the names of the performatives **/
	private static final String[] performatives = new String[22];
	static { // initialization of the Vector of performatives
		performatives[ACCEPT_PROPOSAL]="ACCEPT-PROPOSAL";
		performatives[AGREE]="AGREE";
		performatives[CANCEL]="CANCEL";
		performatives[CFP]="CFP";
		performatives[CONFIRM]="CONFIRM";
		performatives[DISCONFIRM]="DISCONFIRM";
		performatives[FAILURE]="FAILURE";
		performatives[INFORM]="INFORM";
		performatives[INFORM_IF]="INFORM-IF";
		performatives[INFORM_REF]="INFORM-REF";
		performatives[NOT_UNDERSTOOD]="NOT-UNDERSTOOD";
		performatives[PROPOSE]="PROPOSE";
		performatives[QUERY_IF]="QUERY-IF";
		performatives[QUERY_REF]="QUERY-REF";
		performatives[REFUSE]="REFUSE";
		performatives[REJECT_PROPOSAL]="REJECT-PROPOSAL";
		performatives[REQUEST]="REQUEST";
		performatives[REQUEST_WHEN]="REQUEST-WHEN";
		performatives[REQUEST_WHENEVER]="REQUEST-WHENEVER";
		performatives[SUBSCRIBE]="SUBSCRIBE";
		performatives[PROXY]="PROXY";
		performatives[PROPAGATE]="PROPAGATE";
	}
	




    private int _performative;
    private String _protocol;
    private String _conversationID;
    private Object _content;
    private Object _jadeRepresentation;
    private Vector<Object> _receivers;
    private Object _sender;
    private String _encoding;
    private String _language;
    private String _ontology;

    public ACLMessage() {
        //super(); ¿Esto qué es?
        _receivers = new Vector<Object>();
        // Doy valores pordefecto    
        _encoding="UTF";    
        _language="esperanto";

    }

    /**
     * @return the _performative
     */
    public String getPerformative() {
        return performatives[_performative];
    }

    /**
     * @param performative the _performative to set
     */
    public void setPerformative(String performative) {
        this._performative = getPerformativeCode(performative);
    }

    public void setPerformative(int perf) {
        this._performative =perf;
    }

    /**
     * @return the _protocol
     */
    public String getProtocol() {
        return _protocol;
    }

    /**
     * @param protocol the _protocol to set
     */
    public void setProtocol(String protocol) {
        this._protocol = protocol;
    }

    /**
     * @return the _conversationID
     */
    public String getConversationID() {
        return _conversationID;
    }

    /**
     * @param conversationID the _conversationID to set
     */
    public void setConversationID(String conversationID) {
        this._conversationID = conversationID;
    }

    /**
     * @return the _content
     */
    public Object getContent() {
        return _content;
    }

    /**
     * @param content the _content to set
     */
    public void setContent(Object content) {
        this._content = content;
    }

    /**
     * @return the _jadeRepresentation
     */
    public Object getJadeRepresentation() {
        return _jadeRepresentation;
    }

    /**
     * @param jadeRepresentation the _jadeRepresentation to set
     * �Esto no lo entiendo?
     */
    public void setJadeRepresentation(Object jadeRepresentation) {
        this._jadeRepresentation = jadeRepresentation;
    }

    public String getOntology() {
        return _ontology;
    }

    public void setOntology(String _ontology) {
        this._ontology = _ontology;
    }

    public void addReceiver(String toAgent) {
        if(_receivers!=null)
            _receivers.addElement(toAgent);
        else{
            _receivers = new Vector<Object>();
            _receivers.addElement(toAgent);
        }
    }

    /**
     * @return the _receiver
     */
    public Vector<Object> getReceivers() {
        return _receivers;
    }

    /**
     * @param receiver the _receiver to set
     */
    public void setReceivers(Vector<Object> receivers) {
        this._receivers = receivers;
    }

    public static String getPerformative(int perf){
		try {
			return performatives[perf];
		} catch (Exception e) {
			return performatives[NOT_UNDERSTOOD];
		}
	}


    public Object getSender() {
        return _sender;
    }

    public void setSender(Object _sender) {
        this._sender = _sender;
    }

    public int getPerformativeCode(String performative) {
        if (performative.equals("ACCEPT-PROPOSAL")) return ACCEPT_PROPOSAL;
	if (performative.equals("AGREE")) return AGREE;
        if (performative.equals("CANCEL")) return CANCEL;
	if (performative.equals("CFP")) return CFP;
	if (performative.equals("CONFIRM")) return CONFIRM;
	if (performative.equals("DISCONFIRM")) return DISCONFIRM;
	if (performative.equals("FAILURE")) return FAILURE;
	if (performative.equals("INFORM")) return INFORM;
	if (performative.equals("INFORM-IF")) return INFORM_IF;
	if (performative.equals("INFORM-REF")) return INFORM_REF;
        if (performative.equals("NOT-UNDERSTOOD")) return NOT_UNDERSTOOD;
	if (performative.equals("PROPOSE")) return PROPOSE;
	if (performative.equals("QUERY-IF")) return QUERY_IF;
	if (performative.equals("QUERY-REF")) return QUERY_REF;
	if (performative.equals("REFUSE")) return REFUSE;
	if (performative.equals("REJECT-PROPOSAL")) return REJECT_PROPOSAL;
	if (performative.equals("REQUEST")) return REQUEST;
	if (performative.equals("REQUEST-WHEN")) return REQUEST_WHEN;
	if (performative.equals("REQUEST-WHENEVER")) return REQUEST_WHENEVER;
	if (performative.equals("SUBSCRIBE")) return SUBSCRIBE;
	if (performative.equals("PROXY")) return PROXY;
	if (performative.equals("PROPAGATE")) return PROPAGATE;
        return UNKNOWN;
    }

    public String getEncoding() {
        return _encoding;
    }

    public void setEncoding(String _encoding) {
        this._encoding = _encoding;
    }

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String _language) {
        this._language = _language;
    }
    
    public ACLMessage createReply(){
        ACLMessage reply=new ACLMessage();
        reply.setContent("Fill yourself");
        reply.setConversationID(this.getConversationID());
        reply.setEncoding(this.getEncoding());
        reply.setLanguage(this.getLanguage());
        reply.setOntology(this.getOntology());
        reply.setPerformative(ACLMessage.AGREE);
        reply.setProtocol(this.getProtocol());
        Vector<Object> rec=new Vector<Object>();
        rec.addElement(this.getSender());
        reply.setReceivers(rec);
        return reply;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_content == null) ? 0 : _content.hashCode());
		result = prime * result
				+ ((_conversationID == null) ? 0 : _conversationID.hashCode());
		result = prime * result
				+ ((_encoding == null) ? 0 : _encoding.hashCode());
		result = prime
				* result
				+ ((_jadeRepresentation == null) ? 0 : _jadeRepresentation
						.hashCode());
		result = prime * result
				+ ((_language == null) ? 0 : _language.hashCode());
		result = prime * result
				+ ((_ontology == null) ? 0 : _ontology.hashCode());
		result = prime * result + _performative;
		result = prime * result
				+ ((_protocol == null) ? 0 : _protocol.hashCode());
		result = prime * result
				+ ((_receivers == null) ? 0 : _receivers.hashCode());
		result = prime * result + ((_sender == null) ? 0 : _sender.hashCode());
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
		ACLMessage other = (ACLMessage) obj;
		if (_content == null) {
			if (other._content != null)
				return false;
		} else if (!_content.equals(other._content))
			return false;
		if (_conversationID == null) {
			if (other._conversationID != null)
				return false;
		} else if (!_conversationID.equals(other._conversationID))
			return false;
		if (_encoding == null) {
			if (other._encoding != null)
				return false;
		} else if (!_encoding.equals(other._encoding))
			return false;
		if (_jadeRepresentation == null) {
			if (other._jadeRepresentation != null)
				return false;
		} else if (!_jadeRepresentation.equals(other._jadeRepresentation))
			return false;
		if (_language == null) {
			if (other._language != null)
				return false;
		} else if (!_language.equals(other._language))
			return false;
		if (_ontology == null) {
			if (other._ontology != null)
				return false;
		} else if (!_ontology.equals(other._ontology))
			return false;
		if (_performative != other._performative)
			return false;
		if (_protocol == null) {
			if (other._protocol != null)
				return false;
		} else if (!_protocol.equals(other._protocol))
			return false;
		if (_receivers == null) {
			if (other._receivers != null)
				return false;
		} else if (!_receivers.equals(other._receivers))
			return false;
		if (_sender == null) {
			if (other._sender != null)
				return false;
		} else if (!_sender.equals(other._sender))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ACLMessage [_performative=" + _performative + ", _protocol="
				+ _protocol + ", _conversationID=" + _conversationID
				+ ", _content=" + _content + ", _jadeRepresentation="
				+ _jadeRepresentation + ", _receivers=" + _receivers
				+ ", _sender=" + _sender + ", _encoding=" + _encoding
				+ ", _language=" + _language + ", _ontology=" + _ontology + "]";
	}
    
    
}
