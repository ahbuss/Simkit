// FILE: Logging.java
package simkit.data;


/**
* Base interface for objects that support logging with
* simkit Log objects.
**/

public interface DataLogging
{
/**
Set this objects Log.  Log messages will be sent to this
Log object until setLog is called again.  This method
should register this object with the Log object by calling
registerLogger(this), and if necessary, unregisterLogger(this).
**/
   public void setLog(DataLog whichLog);
   
/**
Return the Log object to which Log messages are currently
being sent.  This method should not affect the state of
the log object.
**/
   public DataLog getLog();
} // interface Logging