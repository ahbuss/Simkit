package simkit.viskit;

import simkit.viskit.mvc.mvcAbstractModel;
import java.io.File;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 1:09:38 PM
 */

/**
 * This is the "master" model of an event graph.  It should hold the node, edge and assembly
 * information.  What hasn't been done is to put in accessor methods for the view to
 * read pieces that it needs, say after it receives a "new model" event.
 * 
 */
public class Model extends mvcAbstractModel
{
  private boolean modelDirty = false;

  /**
   * Boolean to signify whether the model has been changed since last disk save.
   * @return true means changes have been made and it needs to be flushed.
   */
  public boolean isDirty()
  {
    return modelDirty;
  }

  public void newModel(File f)
  {
    // put code to do it here

    this.newModel(new EventGraphXMLReader(/*f*/),true);   // something like
  }

  public void newModel(EventGraphXMLReader reader, boolean pIsEditable)
  {
    // put code to do it here

    modelDirty = false;
    this.notifyChanged(new ModelEvent(this,ModelEvent.NEWMODEL,"New model loaded from file"));
  }

  // parameter mods
  // --------------
  public void newParameter(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.PARAMETERADDED,"Parameter added"));
  }
  public void deleteParameter(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.PARAMETERDELETED,"Parameter deleted"));
  }
  public void changeParameter(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.PARAMETERCHANGED,"Parameter changed"));
  }

  // State variable mods
  // -------------------
  public void newStateVariable(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.STATEVARIABLEADDED,"State variable added"));
  }
  public void deleteStateVariable(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.STATEVARIABLEDELETED,"State variable deleted"));
  }
  public void changeStateVariable(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.STATEVARIABLEDELETED,"State variable changed"));
  }

  // Event (node) mods
  // -----------------
  public void newEvent(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.EVENTADDED,"Event added"));
  }
  public void deleteEvent(String name)
  {
    // todo insert code to do it

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.EVENTDELETED,"Event deleted"));
  }
  public void changeEvent(String name)
  {
    // todo insert code to do it
    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.EVENTCHANGED,"Event changed"));
  }

  // Edge mods
  // ---------
  public void newEdge(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.EDGEADDED,"Edge added"));
  }
  public void deleteEdge(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.EDGEDELETED,"Edge deleted"));
  }
  public void changeEdge(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.EDGECHANGED,"Edge changed"));
  }

  // Cancelling Edge mods
  // --------------------
  public void newCancelEdge(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.CANCELLINGEDGEADDED,"Cancelling edge added"));
  }
  public void deleteCancelEdge(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.CANCELLINGEDGEDELETED,"Cancelling edge deleted"));
  }
  public void changeCancelEdge(String name)
  {
    // put code to do it here

    modelDirty = true;
    this.notifyChanged(new ModelEvent(name,ModelEvent.CANCELLINGEDGECHANGED,"Cancelling edge changed"));
  }


  public void saveModel(File f)
  {
    // if f == null, use same name
    // else, this is a "save as"
    System.out.println("Model.saveModel("+f.getName()+")");
    modelDirty = false;
  }
}
