// FILE: Named.java

/////////////////////////// Copyright Notice //////////////////////////

//                                                                   //

// This simkit package or sub-package and this file is Copyright (c) //

// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //

//                                                                   //

// Please forward any changes, comments or suggestions to:           //

//   abuss@nps.navy.mil                                              //

//                                                                   //

///////////////////////////////////////////////////////////////////////

package simkit;

/**
* Interface for Objects in simkit that have a Name attribute.<br>
* <br>
* Many classes in simkit hava a name.  The name
* property may be immutable. Such objects will
* normally have a String argument in their constructor
* that sets the name.
*
* <P> name() changed to getName() in keeping with standard patterns - AB
* <br>
*
* @author Kirk A. Stork<br>
* @author Arnold Buss <BR>
* @version 1.1<br>
*/
public interface Named
{

/**
* Returns the name of this object.
*/
   public String getName();

   
/**
* Set the name of the Object.
* @param a_name The new name of the Object.
**/
   public void   setName(String a_name);

}

