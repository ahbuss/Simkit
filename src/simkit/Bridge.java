/*
 * Bridge.java
 *
 * Created on July 13, 2002, 11:46 AM
 */

package simkit;

/**
 *
 * @author  Arnold Buss
 */
public class Bridge extends simkit.BasicSimEntity {
    
    private String heardEvent;
    private String sentEvent;
    
    /** Creates a new instance of Bridge */
    public Bridge(String heard, String sent) {
        if (heard == null || sent == null) {
            throw new IllegalArgumentException("Heard or sent name is null: " +
                heard + " - " + sent);
        }
        heardEvent = heard;
        sentEvent = sent;
    }
    
    public void handleSimEvent(simkit.SimEvent simEvent) {
    }
    
    public void processSimEvent(simkit.SimEvent simEvent) {
        if (simEvent.getEventName().equals(heardEvent)) {
            waitDelay(sentEvent, 0.0, simEvent.getParameters(), simEvent.getEventPriority());
        }
    }
    
    public String paramString() {
        return "Bridge from " + heardEvent + " to " + sentEvent;
    }
    
}
