package simkit;

/**
  * Interface for a listener of SimEvents.  Uses the standard java beans
  * listener model.  The one method is invoked when a SimEvent is fired by
  * its source.
  *
  * @see SimEvent
  * @see SimEventSource
  *
  * @author Arnold Buss 
  *
**/
public interface SimEventListener {

/**
  * Process the fired SimEvent.  What is done is, of course, entirely up
  * to the listener. Listeners should not process other entity's doRun Events.
  * <I>Note:</I> Should this be changed to "handleSimEvent" in accord with Modkit
  * conventions??
  *
  * @param event The SimEvent to process.
**/
   public void processSimEvent(SimEvent event);
}
