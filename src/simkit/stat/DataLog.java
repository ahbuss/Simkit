
package simkit.stat;

import java.io.IOException;

/**
* An interface for an Object that can log information in some manner.
* The information can be either text or numbers.
* @version $Id$
*/
public interface DataLog
{
/**
Logs the given String.
**/
   public void log(String msg);

/**
* Logs the given String at the given time.
**/
   public void log(double now, String msg);

/**
* Logs the given value at the given time.
**/
   public void log(double now, double val);
/**
Stop this Log.  This method is allowed to close files,
tear down network connections, and otherwise clean up
after itself.  After stop() is called, the Log should
be considered invalid, and never used again, that is,
stop() should probably call setLog(null) on all
registered Logging objects.
**/
   public void stop()
      throws IOException;
/**
Resume this log.  If the log has been stopped, this
method should have no affect, possibly printing a
warning.
**/
   public void resume();
/**
Temporarily stop logging messages sent to this Log.
This method should maintain the integrity of this Log
object so that resume() can be called.
**/
   public void suspend();
/**
Set the format of numbers used by this Log.  The
string should conform to the format strings supported
by class Format.

**/
   public void setNumberFormat(String f);
/**
Register a Logging object with this Log.  Log
objects should support the logging of messages from
multiple clients.
**/
   public void registerLogger(DataLogging who);
/**
Unregister a Logging object from this Log.
**/
   public void unregisterLogger(DataLogging who);
} // interface Log
