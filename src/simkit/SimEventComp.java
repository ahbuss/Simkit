package simkit;

import java.util.Comparator;

/**
 * Binary predicate (comparator) for simkit SimEvents.
 *
 * This functor is used to prioritize events on the
 * Schedule, and in the individual SimEntities active
 * agendas.  This comparator should prevent ties from
 * arising.
 *
 * The order of importance regarding scheduling is
 * as follows:
 *
 * <br>
 *
 * 1. Scheduled time of the events (Earlier time is a higher priority.) <br>
 * 2. Priority of the SimEvents. (A larger number is a higher priority.) <BR>
 * 3. Priorities of the SimEntities that own the SimEvents (A larger number
 * is a higher priority.) <br>
 * 4. The SimTime the SimEvents were instantiated. (An earlier creation time
 * is a higher priority.) <br>
 * 5. The ID number of the event. (A lower ID number is a higher priority.) <BR/>
 *
 * SimEvents scheduled within the precision (default 0.0) are treated as 
 * simultaneous.
 *
 * @author K. A. Stork
 * @author Arnold H. Buss
 * @version $Id$
 *
 **/
public class SimEventComp implements Comparator {
    
    private double precision;
    /**
     * Construct a new SimEventComp.
     **/
    public SimEventComp() {
        this(0.0);
    }
    
    public SimEventComp(double precision) {
        setPrecision(precision);
    }

    /**
     * Compare the priorities of 2 SimEvents.
     * @return -1 if fst is higher priority than snd,
     * 0 if the priorities are equal, 1 if snd is higher
     * priority than fst.
     * @throws ClassCastException if either argument is not a SimEntity.
     */
    public int compare( Object fst, Object snd ) {
        if (fst.equals(snd)) {return 0;}
        if (snd.equals(fst)) {return 0;}
        
        SimEvent a = (SimEvent)fst;
        SimEvent b = (SimEvent)snd;
        
        if ( a.getScheduledTime() > b.getScheduledTime() + precision ) return 1;
        if ( a.getScheduledTime()+ precision < b.getScheduledTime() ) return -1;
        if ( a.getEventPriority() < b.getEventPriority() ) return 1;
        if ( a.getEventPriority() > b.getEventPriority() ) return -1;
        if ( a.getOwnerPriority() > b.getOwnerPriority() ) return -1;
        if ( a.getOwnerPriority() < b.getOwnerPriority() ) return 1;
        if ( a.getCreationTime()  > b.getCreationTime() )  return 1;
        if ( a.getCreationTime()  < b.getCreationTime() )  return -1;
        if ( a.getID()        > b.getID() )        return 1;
        if ( a.getID()        < b.getID() )        return -1;
        return 0;
    }
    
    public void setPrecision(double d) {
        if (d >= 0.0) {
            precision = d;
        } else {
            throw new IllegalArgumentException("Precision must be >= 0.0:" + d );
        }
    }
    
    public double getPrecision() { return precision; }
    
}
