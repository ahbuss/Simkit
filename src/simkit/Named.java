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

/**

* Interface for simkit Name attribute.<br>

* <br>

* Many classes in simkit hava a name.  The name

* property may be immutable, which is implied by

* implementing this interface.  Such objects will

* normally have a String argument in their constructor

* that sets the name.  Objects that have mutable

* names implement the Renameable interface.

*

* <P> name() changed to getName() in keeping with standard patterns - AB

* <br>

*

* @author Kirk A. Stork<br>

* @author Arnold Buss <BR>

* @version 1.1<br>

*

* @see simkit.Renameable

*/



package simkit;



public interface Named

{

/**

Returns the name of this object.

*/

   public String getName();

   

   public void   setName(String a_name);

}

