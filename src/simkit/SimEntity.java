package simkit;

/**
 * Base interface for simulation entities.
 *
 * This interface defines the basic event scheduling
 * behavior of simulation entities in the simkit
 * structure.  The reference implementations are BasicSimEntity and
 * SimEntityBase.
 *
 * @author K. A. Stork
 * @author Arnold Buss
 * @version $Id: SimEntity.java 1000 2007-02-15 19:43:11Z ahbuss $
 *
**/
public interface SimEntity extends SimEventSource,
                                   SimEventListener,
                                   SimEventScheduler,
                                   PropertyChangeSource,
                                   Comparable<SimEntity>
{
}
