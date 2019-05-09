/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect.distribution;

/**
 *
 * @author Mer
 */
public interface FIPAAgentPlatform {

    //public void startAP();
    public void startAP(Object[] obj);
    //public void startProxy();
    public void stopAP();
    public boolean deployAgent(Object[] obj);
    public boolean killAgent(Object[] obj);
    public String getID();
    public void setID(String id);
    public void registerService(Object[] obj);
    public void unregisterService(Object[] obj);
    public Object getServiceProviders(Object[] obj);

}
