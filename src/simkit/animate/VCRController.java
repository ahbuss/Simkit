package simkit.animate;

import simkit.SimEventSource;

/**
 * All VCRController instances must be able to respond to these actions
 * @version $Id$
 * @author  Arnold Buss
 */
public interface VCRController extends SimEventSource {
    
    public void start();
    
    public void stop();
    
    public void pause();
    
    public void rewind();
    
    public void step(); // 22 May 2004 - kas - added
    
    public void fastForward();
    
    public void setDeltaT(double deltaT);
    
    public void setMillisPerSimtime(double millisPerSimTime);

}

