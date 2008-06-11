// FILE: Logging.java
package simkit.stat;


/**
* Base interface for objects that support logging with
* simkit DataLog objects.
* @version $Id: DataLogging.java 483 2003-12-11 01:05:37Z jlruck $
**/

public interface DataLogging
{
/**
Set this objects Log.  Log messages will be sent to this
DataLog object until setLog is called again.  This method
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
