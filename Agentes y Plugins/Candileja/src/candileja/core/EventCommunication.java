package candileja.core;

public class EventCommunication {
	
	private Agent mediator;
	
	protected void setMediator(Agent med){
		mediator=med;
	}
	
	public void throwEvent(Object obj){
		mediator.throwEvent(obj);
	}

}
