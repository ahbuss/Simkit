package simkit.examples;

import java.beans.PropertyChangeListener;

import simkit.PropertyChangeNamespace;
import simkit.SimEntityBase;

/**
 * Implementation of a dispatcher that directs Arrival events to the
 * SimpleServer with the shortest queue.
 * <p>
 * This class is a simple illustration of a dispatcher class. It keeps an array
 * of Servers and listens for an Arrival event. Upon the Arrival event, the
 * index of the server with the fewest customers is determined and the Arrival
 * event is scheduled for that server. The technique is to directly invoke
 * waitDelay() on the server instance, thereby making it appear as if the server
 * itself had scheduled the event.
 * <p>
 * The class also illustrates the PropertyChangeNamespace class. This class
 * listens for PropertyChangeEvents, prepends a namespace to the property name,
 * and re-fires the event.
 *
 * @author Arnold Buss
 * @version $Id: ShortestQueueDispatcher.java 1083 2008-06-11 20:41:21Z kastork
 * $
 */
public class ShortestQueueDispatcher extends SimEntityBase {

    /**
     * The Servers that this dispatcher can direct arrivals to.
     */
    private SimpleServer[] server;
    private PropertyChangeNamespace[] nameSpace;

    public ShortestQueueDispatcher() {
    }

    /**
     * Creates a new dispatcher that will direct arrivals to the given Servers.
     * Each SimpleServer will have a corresponding
     * {@link PropertyChangeNamespace} that listens for its
 PropertyChangeEvents, prepends the property name with the servers number
 and then re-fires the event. The dispatcher has a PropertyChangeNamespace
 that listens to all of the SimpleServer's PropertyChangeNamespaces and
 re-fires the events. Therefore, any PropertyChangeListener need only
 listen to this dispatcher's events to be informed of all property changes
 on Servers in the system.
     *
     * @param servers
     */
    public ShortestQueueDispatcher(SimpleServer[] servers) {
        property = new PropertyChangeNamespace(this, "facility");
        setServers(servers);
    }

    /**
     * Schedules Arrival on server with the shortest queue.
     */
    public void doArrival() {
        int leastBusyServer = getShortestQueue();
        server[leastBusyServer].waitDelay("Arrival", 0.0);
    }

    /**
     * Sets the Servers available to this dispatcher. Each SimpleServer will
     * have a corresponding {@link PropertyChangeNamespace} that listens for its
     * PropertyChangeEvents, prepends the property name with the server number
     * and then re-fires the event. The dispatcher has a PropertyChangeNamespace
     * that listens to all of the SimpleServer's PropertyChangeNamespaces and
     * re-fires the events. Therefore, any PropertyChangeListener need only
     * listen to this dispatcher's events to be informed of all property changes
     * on Servers in the system.
     *
     * @param serv Array of servers to be used.
     */
    public void setServers(SimpleServer[] serv) {
        if (nameSpace != null) {
            for (int i = 0; i < nameSpace.length; ++i) {
                server[i].removePropertyChangeListener(nameSpace[i]);
                nameSpace[i].removePropertyChangeListener((PropertyChangeListener) property);
            }
        }
        this.server = (SimpleServer[]) serv.clone();
        nameSpace = new PropertyChangeNamespace[server.length];
        for (int i = 0; i < server.length; ++i) {
            nameSpace[i] = new PropertyChangeNamespace(server[i],
                    "server-" + i);
            server[i].addPropertyChangeListener(nameSpace[i]);
            nameSpace[i].addPropertyChangeListener((PropertyChangeListener) property);
        }
    }

    /**
     * Return the index of the SimpleServer with the shortest queue.
     *
     * @return SimpleServer index with the smallest number of customers.
     */
    protected int getShortestQueue() {
        int leastBusyServer = Integer.MIN_VALUE;
        int smallestNumber = Integer.MAX_VALUE;
        for (int i = 0; i < server.length; ++i) {
            int thisNumber = server[i].getNumberInSystem();
            if (thisNumber < smallestNumber) {
                smallestNumber = thisNumber;
                leastBusyServer = i;
            }
        }
        return leastBusyServer;
    }
}
