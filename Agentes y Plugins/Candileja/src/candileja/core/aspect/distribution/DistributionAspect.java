/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect.distribution;

import candileja.core.aspect.Aspect;

/**
 *
 * @author Mer
 */
public abstract class DistributionAspect extends Aspect{

   public Object handleInputMessage(Object msg){
        getMediator().receiveMessage(msg);
        return msg;
    }

    public abstract Object handleOutputMessage(Object message);

    public void destroyAspect() {
    }

    public abstract Object getAID();
    
    public abstract void stopPlugin();
}