package simkit.viskit;

import simkit.viskit.mvc.mvcModelEvent;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 1:04:35 PM
 */

/**
 * This defines every event with which the application Model informs its listeners .. typically
 * the view.
 */

public class ModelEvent extends mvcModelEvent
{
  public static final int NEWMODEL = 0;

  public static final int PARAMETERADDED = 1;
  public static final int PARAMETERDELETED = 2;
  public static final int PARAMETERCHANGED = 3;

  public static final int STATEVARIABLEADDED = 4;
  public static final int STATEVARIABLEDELETED = 5;
  public static final int STATEVARIABLECHANGED = 6;

  public static final int EVENTADDED = 7;
  public static final int EVENTDELETED = 8;
  public static final int EVENTCHANGED = 9;

  public static final int EDGEADDED = 10;
  public static final int EDGEDELETED = 11;
  public static final int EDGECHANGED = 12;

  public static final int CANCELLINGEDGEADDED = 13;
  public static final int CANCELLINGEDGEDELETED = 14;
  public static final int CANCELLINGEDGECHANGED = 15;

  public ModelEvent(Object obj, int id, String message)
  {
    super(obj,id,message);
  }
}
