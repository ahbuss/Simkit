package simkit;

/**
 * Base interface for simulation entities.
 *
 * This interface adds to the basic event scheduling
 * behavior of simulation entities in the simkit
 * structure to include the concepts of multiple event lists, and scheduler
 * control of multiple replications.
 * <p>
 * The reference implementations are BasicSimEntity and
 * SimEntityBase.
 * <p>
 * Objects implementing this interface are dependent upon a concrete
 * implementation ({@code EventList} which is specifically designed to 
 * provide support for multiple replications and thread safety.
 * 
 * @author Arnold Buss
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
