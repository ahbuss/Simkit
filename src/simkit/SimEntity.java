package simkit;

/**
 * Base interface for simulation entities.
 *
 * This interface adds to the basic event scheduling
 * behavior of simulation entities in the simkit
 * structure to include the concepts of mulitple event lists, and scheduler
 * control of mulitple replications.
 * <p>
 * The reference implementations are BasicSimEntity and
 * SimEntityBase.
 * <p>
 * Objects implementing this interface are dependent upon a concrete
 * implementation ({@code EventList} which is specifically designed to 
 * provide support for multiple replications and thread safety.
 * 
 * @author Arnold Buss
 * @version $Id$
 *
**/
public interface SimEntity extends SimEventSource,
                                   SimEventListener,
                                   ReRunnable,
                                   PropertyChangeSource,
                                   Comparable<SimEntity>,
                                   SimEventScheduler
{
    int getEventListID();
    void setEventListID(int id);
}
