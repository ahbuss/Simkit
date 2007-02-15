package simkit.nss;

import simkit.BasicSimEntity;
import simkit.Priority;
import simkit.SimEvent;

/**
 * <p>An attmept to use NSS Objects driven by the Simkit engine.
 * The idea is to use native methods to do the callbacks for 
 * processSimEvent() and handleSimEvent().  Since the state will typically
 * be maintained outside Simkit, a native run() method is called for
 * in order to perform whatever initializations are required.
 *
 * <p>The header file simkit_nss_NativeBasicSimEntity.h can be generated
 * by the call
 * <pre>
 *    > javah simkit.nss.NativeBasicSimEntity
 * </pre>
 * from the command line, where the current working directory is one level 
 * above the simkit folder.
 *
 * @author  Arnold Buss
 */
public class NativeBasicSimEntity extends BasicSimEntity {
    
    public NativeBasicSimEntity() {
        super();
    }
    
    /**
     * @param name The name of the NativeBasicSimEntity created
     */    
    public NativeBasicSimEntity(String name) {
        super(name);
    }
    
    /**
     * @param priority The priority of the NativeBasicSimEntity instance
     */    
    public NativeBasicSimEntity(Priority priority) {
        super(priority);
    }
    
    /**
     * @param name The name of the NativeBasicSimEntity
     * @param priority The priority of the NativeBasicSimEntity
     */    
    public NativeBasicSimEntity(String name, Priority priority) {
        super(name, priority);
    }
    
    /** Perform whatever initializations are required. */    
    public native void reset();
    
    /** This method is called from Schedule and will only
     * be passed SimEvents it had originally scheduled.
     * @param simEvent The SimEvent instance.
     */    
    public native void handleSimEvent(SimEvent simEvent);
    
    /** This callback is received by listening to another SimEntity
     * @param simEvent The SimEvent being passed
     */    
    public native void processSimEvent(SimEvent simEvent);
}
