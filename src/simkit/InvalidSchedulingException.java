// FILE InvalidSchedulingException.java
package simkit;

/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

/**
* Exception thrown by the simkit Schedule class when
* a SimEvent cannot be scheduled as requested.  For instance
* if a SimEvent is scheduled in simulated history, an instance
* of this class will be thrown.
*
* @author K. A. Stork
* @version $Id$
*
**/

public class InvalidSchedulingException extends RuntimeException
{

/**
  * Create an InvalidSchedulingxception with detailed message s.
  * @param String s
**/
   public InvalidSchedulingException( String s) {
      super(s);
   }
   
/**
  * Create an InvalidSchedulingxception no detailed message.
**/
   public InvalidSchedulingException() {
      super();
   }
}
