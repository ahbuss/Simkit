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

import java.beans.*;
/**
 *  <UL>
 *  <LI> Started 14 November 1998
 *  </UL>
 *
 *  <P>Basic interface for property sources, since java.beans does not do
 *  it.  The usual stuff, inspired by Arent Arntzen's Modkit ModPropertySource.
 *  For Simkit's purposes, however, we can probably reuse the rest
 *  from java.beans -- that is, fire PropertyChangeEvents on PropertyChangeListeners
 *
 *  <P>The listeners will be java.beans.PropertyChangeListeners, who will have
 *  their propertyChanged(PropertyChangeEvent) method invoked (not enforced).
 *
 *  @author Arnold Buss
**/

public interface PropertyChangeSource {

/**
 *  @param name The name of the property to be set
 *  @param value The new value of the property 
**/
  public void setProperty(String name, Object value);

/**
 *  @param name The name of the property to be retrieved
 *  @return The value of the property
**/
  public Object getProperty(String name);

/**
 *  @param name The name of the property to be retrieved.
 *  @param defaultValue The default value -- returned if property's value cannot
 *         be returned or is null. 
 *  @return The value of the property.
**/
  public Object getProperty(String name, Object defaultValue);

/**
 *  Notify all PropertyChangeListeners of the PropertyChangeEvent.
 *  @param e The event with all the information about what property has changed
 *  and to what value.
**/
  public void notifyPropertyChangeListeners(PropertyChangeEvent e);

/**
 *  @param listener The new listener to all my property changes.
**/
  public void addPropertyChangeListener(PropertyChangeListener listener);

/**
 *  @param listener The listener that will stop listening to my property changes.
**/
  public void removePropertyChangeListener(PropertyChangeListener listener);
}
