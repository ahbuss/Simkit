package simkit.viskit;

import actions.ActionIntrospector;
import org.jgraph.graph.DefaultGraphCell;
import simkit.viskit.mvc.mvcAbstractController;
import simkit.viskit.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 2, 2004
 * Time: 12:52:59 PM
 */

/**
 * This is the MVC controller for the Viskit app.  All user inputs come here, and this
 * code decides what to do about it.  To add new events:
 * 1 add a new public Action BLAH field
 * 2 instantiate it in the constructor, mapping it to a handler (name)
 * 3 write the handler
 */

public class Controller extends mvcAbstractController implements ViskitController
/***************************************************************************/
{
  public Controller()
  //=================
  {
  }

  public void quit()
  //----------------
  {
    // todo implement
    System.exit(0);
  }

  File lastFile;
  public void open()
  //----------------
  {
    if (((Model)getModel()).isDirty()) {
      // Ask to save
      int yn = (((ViskitView) getView()).genericAsk("Question", "Save current graph?"));

      switch (yn) {
        case JOptionPane.YES_OPTION:
          ((Model)getModel()).saveModel(null);       // null means use original file
          break;
        case JOptionPane.NO_OPTION:
          break;
        case JOptionPane.CANCEL_OPTION:
        default:
          return;
      }
    }

    lastFile = ((ViskitView) getView()).openFileAsk();
    if (lastFile != null) {
      ((ViskitModel) getModel()).newModel(lastFile);
      ((ViskitView) getView()).fileName(lastFile.getName());
    }
  }

  public void save()
  //----------------
  {
    if(lastFile == null)
      saveAs();
    else
      ((ViskitModel)getModel()).saveModel(lastFile);
  }

  public void saveAs()
  //------------------
  {
    lastFile = ((ViskitView)getView()).saveFileAsk();
    if(lastFile != null)
      ((ViskitModel)getModel()).saveModel(lastFile);

    ((ViskitView)getView()).fileName(lastFile.getName());
  }

  public void newSimParameter()
  //------------------------
  {
    ((ViskitView) getView()).addParameterDialog();

  }
  public void newSimParameter(String name, String type, String initVal, String comment)
  {
    ((ViskitModel)getModel()).newSimParameter(name, type, initVal, comment);
  }

  public void simParameterEdit(vParameter param)
  {
    boolean modified = ((ViskitView) getView()).doEditParameter(param);
    if (modified) {
      ((simkit.viskit.model.ViskitModel) getModel()).changeSimParameter(param);
    }
  }
  public void stateVariableEdit(vStateVariable var)
  {
    boolean modified = ((ViskitView) getView()).doEditStateVariable(var);
    if (modified) {
      ((simkit.viskit.model.ViskitModel) getModel()).changeStateVariable(var);
    }

  }
  public void newStateVariable()
  {
    ((ViskitView) getView()).addStateVariableDialog();
  }
  public void newStateVariable(String name, String type, String initVal, String comment)
  //----------------------------
  {
    ((simkit.viskit.model.ViskitModel)getModel()).newStateVariable(name,type,initVal,comment);
  }

  private Vector selectionVector = new Vector();

  public void selectNodeOrEdge(Vector v)
  //------------------------------------
  {
    selectionVector = v;
    boolean ccbool = (selectionVector.size() > 0 ? true : false);
    ActionIntrospector.getAction(this, "copy").setEnabled(ccbool);
    ActionIntrospector.getAction(this, "cut").setEnabled(ccbool);
  }

  private Vector copyVector = new Vector();

  public void copy()
  //----------------
  {
    if (selectionVector.size() <= 0)
      return;
    copyVector = (Vector) selectionVector.clone();
    ActionIntrospector.getAction(this,"paste").setEnabled(true);
  }

  public void paste()
  //-----------------
  {
    if (copyVector.size() <= 0)
      return;
    int x=100,y=100; int n=0;
    // We only paste un-attached nodes (at first)
    for(Iterator itr = copyVector.iterator(); itr.hasNext();) {
      Object o = itr.next();
      if(o instanceof Edge)
        continue;
      String nm = ((simkit.viskit.model.EventNode)o).getName();
      ((simkit.viskit.model.ViskitModel) getModel()).newEvent(nm+"-copy", new Point(x+(20*n),y+(20*n)));
      n++;
    }
  }

  public void cut()
  //---------------
  {
    if (selectionVector != null && selectionVector.size() > 0) {
      // first ask:
      String msg = "";
      for (Iterator itr = selectionVector.iterator(); itr.hasNext();) {
        Object o = itr.next();
        String s = o.toString();
        s = s.replace('\n', ' ');
        msg += ", \n" + s;
      }
      if (((ViskitView) getView()).genericAsk("Delete element(s)?", "Confirm remove" + msg + "?" +
          "\n(All unselected but attached edges will also be deleted.)") == JOptionPane.YES_OPTION) {
        // do edges first?
        Vector localV = (Vector) selectionVector.clone();   // avoid concurrent update
        for (Iterator itr = localV.iterator(); itr.hasNext();) {
          Object elem = itr.next();
          if(elem instanceof Edge) {
            killEdge((Edge)elem);
          }
          else if(elem instanceof EventNode) {
            EventNode en = (EventNode)elem;
            for (Iterator it2 = en.getConnections().iterator(); it2.hasNext();) {
              Edge ed = (Edge) it2.next();
              killEdge(ed);
            }
            ((ViskitModel) getModel()).deleteEvent(en);
          }
        }
      }
    }
  }

  private void killEdge(Edge e)
  {
    if (e instanceof SchedulingEdge)
      ((ViskitModel) getModel()).deleteEdge((SchedulingEdge) e);
    else
      ((ViskitModel) getModel()).deleteCancelEdge((CancellingEdge) e);
  }

  public void xdeleteSimParameter()
  //---------------------------
  {
    String ret = ((ViskitView) getView()).promptForStringOrCancel("Delete vParameter", "Enter parameter name:", "");

    if (ret == null || ret.trim().length() <= 0)
      return;
    ((ViskitModel) getModel()).deleteSimParameter(ret.trim());
  }
  public void deleteSimParameter(vParameter p)
  {
    ((ViskitModel) getModel()).deleteSimParameter(p);    
  }

  public void xdeleteStateVariable()
  //-------------------------------
  {
    String ret = ((ViskitView) getView()).promptForStringOrCancel("Delete State Variable", "Enter variable name:", "");

    if (ret == null || ret.trim().length() <= 0)
      return;

    ((ViskitModel) getModel()).deleteStateVariable(ret.trim());
  }
  public void deleteStateVariable(vStateVariable var)
  {
    ((ViskitModel)getModel()).deleteStateVariable(var);
  }

  public void generateJavaClass()
  //-----------------------------
  {
    // todo implement
    System.out.println("generateJavaClass in " + this);
  }

  public void newAssembly()
  {
    // todo implement
    System.out.println("newAssembly in " + this);
  }

  public void newEventGraph()
  {
    // todo implement
    System.out.println("newEventGraph in " + this);
  }

  public void eventList()
  {
    // todo implement
    System.out.println("EventListAction in " + this);
  }

  private int nodeCount = 0;
  public void newNode()
  {
    newNode(new Point(100,100));
  }
  public void newNode(Point p)
  //--------------------------
  {
    String fauxName = "evnt " + nodeCount++;
    ((simkit.viskit.model.ViskitModel) getModel()).newEvent(fauxName, p);
  }

  public void newArc(Object[] nodes)
  //--------------------------------
  {
    // My node view objects hold node model objects and vice versa
    EventNode src = (EventNode) ((DefaultGraphCell) nodes[0]).getUserObject();
    EventNode tar = (EventNode) ((DefaultGraphCell) nodes[1]).getUserObject();
    ((ViskitModel) getModel()).newEdge(src, tar);
  }

  public void newCancelArc(Object[] nodes)
  //--------------------------------------
  {
    // My node view objects hold node model objects and vice versa
    EventNode src = (EventNode) ((DefaultGraphCell) nodes[0]).getUserObject();
    EventNode tar = (EventNode) ((DefaultGraphCell) nodes[1]).getUserObject();
    ((ViskitModel) getModel()).newCancelEdge(src, tar);
  }

  public void nodeEdit(simkit.viskit.model.EventNode node)      // shouldn't be required
  //----------------------------------
  {
    boolean modified = ((ViskitView) getView()).doEditNode(node);
    if (modified) {
      ((simkit.viskit.model.ViskitModel) getModel()).changeEvent(node);
    }
  }

  public void arcEdit(simkit.viskit.model.SchedulingEdge ed)
  //------------------------------------
  {
    boolean modified = ((ViskitView) getView()).doEditEdge(ed);
    if (modified) {
      ((simkit.viskit.model.ViskitModel) getModel()).changeEdge(ed);
    }
  }

  public void canArcEdit(simkit.viskit.model.CancellingEdge ed)
  //---------------------------------------
  {
    boolean modified = ((ViskitView) getView()).doEditCancelEdge(ed);
    if (modified) {
      ((simkit.viskit.model.ViskitModel) getModel()).changeCancelEdge(ed);
    }
  }

}
