package simkit.examples;
import simkit.*;
import java.beans.*;

/**
 * <p>This class is a simple illustration of a dispatcher class.  It keeps an
 * array of Servers and listens for an Arrival event.  Upon the Arrival
 * event, the index of the server with the fewest customers is determined
 * and the Arrival event is scheduled for that server.  The technique is
 * to directly invoke waitDelay() on the server instance, thereby making it
 * appear as if the server itself had scheduled the event.
 * <p>The class also illustrates the PropertyChangeNamespace class.  This
 * class listens for PropertyChangeEvents, prepends a namespace to the
 * property name, and re-fires the event.  
 * @author  Arnold Buss
 */
public class ShortestQueueDispatcher extends SimEntityBase {
    
    private Server[] server;
    private PropertyChangeNamespace[] nameSpace;
    
    /** Creates a new instance of ShortestQueueDispatcher */
    public ShortestQueueDispatcher(Server[] server) {
        property = new PropertyChangeNamespace(this, "facility");
        setServers(server);
    }
    
    /** Arrival is heard - schedule Arrival on server with the
     * smallest number.
     */    
    public void doArrival() {
        int leastBusyServer = getShortestQueue();
        server[leastBusyServer].waitDelay("Arrival", 0.0);
    }
    
    /**
     * @param serv Array of servers to be used.
     */    
    public void setServers(Server[] serv) {
        if (nameSpace != null) {
            for (int i = 0; i < nameSpace.length; ++i) {
                server[i].removePropertyChangeListener(nameSpace[i]);
                nameSpace[i].removePropertyChangeListener((PropertyChangeListener)property);
            }
        }
        this.server = (Server[]) serv.clone();
        nameSpace = new PropertyChangeNamespace[server.length];
        for (int i = 0; i < server.length; ++i) {
            nameSpace[i] = new PropertyChangeNamespace(server[i],
                "server-" + i);
            server[i].addPropertyChangeListener(nameSpace[i]);
            nameSpace[i].addPropertyChangeListener((PropertyChangeListener) property);
        }
    }
    
    /**
     * @return Server index with the smallest number of customers.
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
