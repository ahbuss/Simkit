package simkit.viskit.model;

import simkit.xsd.bindings.Event;

import java.util.Vector;
import java.util.ArrayList;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 9:08:08 AM
 */

/**
 * An event as seen by the model (not the view)
 */
public class EventNode extends ViskitElement
{
  private String name;

  private Vector    connections = new Vector();
  private ArrayList comments = new ArrayList();
  private ArrayList transitions = new ArrayList();
  private Vector    localVariables = new Vector();
  private ArrayList arguments = new ArrayList();

  EventNode(String name)      // package access on constructor
  {
    this.name = name;
  }
  public String toString()
  {
    return name;
  }
  public EventNode shallowCopy()
  {
    EventNode en   = (EventNode)super.shallowCopy(new EventNode(name+"-copy"));
    en.connections = connections;
    en.comments    = comments;
    en.transitions = transitions;
    en.localVariables = localVariables;
    en.arguments   = arguments;
    en.connections = connections;
    return en;
  }
  public String getName()
  {
    /*
    if(this.opaqueModelObject != null)
      return ((Event)opaqueModelObject).getName();
    else
    */
      return name;
  }
  public void setName(String s)
  {
    if(this.opaqueModelObject != null)
      ((Event)opaqueModelObject).setName(s);

    this.name = s;
  }
  public ArrayList getArguments()
  {
    return arguments;
  }

  public void setArguments(ArrayList arguments)
  {
    this.arguments = arguments;
  }

  public ArrayList getComments()
  {
    return comments;
  }

  public void setComments(ArrayList comments)
  {
    this.comments = comments;
  }

  public Vector getConnections()
  {
    return connections;
  }

  public void setConnections(Vector connections)
  {
    this.connections = connections;
  }

  public Vector getLocalVariables()
  {
    return localVariables;
  }

  public void setLocalVariables(Vector localVariables)
  {
    this.localVariables = localVariables;
  }

  public ArrayList getTransitions()
  {
    return transitions;
  }

  public void setTransitions(ArrayList transitions)
  {
    this.transitions = transitions;
  }


}
