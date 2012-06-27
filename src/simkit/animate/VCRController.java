package simkit.animate;

/**
 * All VCRController instances must be able to respond to these actions
 * @version $Id$
 * @author  Arnold Buss
 */
public interface VCRController {
    
    public void start();
    
    public void stop();
    
    public void pause();
    
    public void rewind();
    
    public void step(); // 22 May 2004 - kas - added
    
    public void fastForward();

}

