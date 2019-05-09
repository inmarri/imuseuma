package candileja.core.aspect.monitoring;

import candileja.core.aspect.Aspect;

public abstract class Monitoring extends Aspect implements Runnable{
	
	// Debuging
	@SuppressWarnings("unused")
	private Boolean d=true;
	@SuppressWarnings("unused")
	private String tag="MONITOR";
	
	private long samplingTime;
	private Boolean end;
	private Integer token;
	
	public Monitoring(){
		super();
		token=0;
		samplingTime=1000;
		end=false;
	}
	
	public abstract void updateContext(Object v);
	
	public abstract Object getValue();
	/*{
		Object probe=null;		
		Sensor sens=(Sensor)this.getMediator().getFacet(getSensorId());
		probe=sens.getValue();
		return probe;
	}*/

	public long getSamplingTime() {
		long res;
		synchronized(token){
			res=samplingTime;
		}
		return res;
	}

	public void end(){
		end=true;
	}
	
	public void restart(){
		end=false;
	}

	public void setSamplingTime(long samplingTime) {
		synchronized(token){
			this.samplingTime = samplingTime;
		}
	}

	public void run() {
		
		long st;
		
		while(!end){
			Object probe=this.getValue();
			this.updateContext(probe);
			
			try{
				st=this.getSamplingTime();
				Thread.sleep(st);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + (int) (samplingTime ^ (samplingTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Monitoring other = (Monitoring) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (samplingTime != other.samplingTime)
			return false;
		return true;
	}
}
