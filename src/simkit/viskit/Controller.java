package simkit.viskit;

import simkit.viskit.mvc.mvcAbstractController;
import simkit.viskit.mvc.mvcModelEvent;

import javax.swing.*;
import java.io.File;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

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
 *  1 add a new public Action BLAH field
 *  2 instantiate it in the constructor, mapping it to a handler (name)
 *  3 write the handler
 */

public class Controller extends mvcAbstractController
{
  public Action OPEN;
  public Action ADDPARAMETER;
  public Action ADDSTATEVARIABLE;
  public Action COPY;
  public Action CUT;
  public Action DELETEPARAMETER;
  public Action DELETESTATEVARIABLE;
  public Action GENERATEJAVACLASS;
  public Action NEWASSEMBLY;
  public Action NEWEVENTGRAPH;
  public Action PASTE;
  public Action QUIT;
  public Action SAVE;
  public Action SAVEAS;
  public Action EVENTLIST;

  public Action SELECTMODE;
  public Action NEWNODEMODE;
  public Action ARCMODE;
  public Action CANCELARCMODE;


  public Controller()
  {
    ADDPARAMETER        = new VAction("AddParameterAction");
    ADDSTATEVARIABLE    = new VAction("AddStateVariableAction");
    COPY                = new VAction("CopyAction");
    CUT                 = new VAction("CutAction");
    DELETEPARAMETER     = new VAction("DeleteParameterAction");
    DELETESTATEVARIABLE = new VAction("DeleteStateVariableAction");
    GENERATEJAVACLASS   = new VAction("GenerateJavaClassAction");
    NEWASSEMBLY         = new VAction("NewAssemblyAction");
    NEWEVENTGRAPH       = new VAction("NewEventGraphAction");
    OPEN                = new VAction("OpenAction");
    PASTE               = new VAction("PasteAction");
    QUIT                = new VAction("QuitAction");
    SAVE                = new VAction("SaveAction");
    SAVEAS              = new VAction("SaveAsAction");
    EVENTLIST           = new VAction("EventListAction");
    SELECTMODE          = new VAction("SelectModeAction");
    NEWNODEMODE         = new VAction("NewNodeModeAction");
    ARCMODE             = new VAction("ArcModeAction");
    CANCELARCMODE       = new VAction("CancelArcModeAction");
  }

  // Note: These handler classes should be private.  We're trying to access them through
  // the nested VAction class below.  But access from "methods of classes
  // within the class" doesn't seem to work, although I think it should.
  //   See VAction.actionPerformed().

  //Introspection Action scheme handlers:

  private JFileChooser jfc;        // cache it so it comes up in last dir.
  /**
   * Handler for a user request to open a (saved event graph) file
   * @param ev ActionEvent, not used here
   */
  public void OpenAction(ActionEvent ev)
  //-------------------------------------
  {
    // To do this right (mvc-wise, put the view stuff in the view, and call it
    if(getModel() == null)
      return;             // Model is the only guy to do anything with this
    Model mod = (Model)getModel();
    if(mod.isDirty()) {
      // Ask to save
     int yn = JOptionPane.showConfirmDialog(
          getView() instanceof Component ? (Component)getView():null,"Save current graph?");
      switch(yn) {
        case JOptionPane.YES_OPTION:
          mod.saveModel(null);       // null means use original file
          break;
        case JOptionPane.NO_OPTION:
          break;
        case JOptionPane.CANCEL_OPTION:
        default:
          return;
      }
    }
    if(jfc == null)
      jfc = new JFileChooser(System.getProperty("user.dir"));

    int retv = jfc.showOpenDialog(getView() instanceof JFrame ? (JFrame)getView() : null);
    if (retv == JFileChooser.APPROVE_OPTION) {
      File file = jfc.getSelectedFile();
      Model model = (Model)getModel();
      model.newModel(file);
    }
  }

  // Change these to return the added object, then hand off to model
  public void AddParameterAction(ActionEvent ev)
  {
    ((EventGraphViewFrame)getView()).addParameterDialog();
    /*
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter parameter name:",
        "Add Parameter",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).newParameter(ret);
    */
    ((Model)getModel()).newParameter("mikesBigTest");
  }

  public void AddStateVariableAction(ActionEvent ev)
  {
    ((EventGraphViewFrame)getView()).addStateVariableDialog();
    /*
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter variable name:",
        "Add Variable",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).newStateVariable(ret);
    */
  }

  public void CopyAction(ActionEvent ev)
  {
    System.out.println("CopyAction in "+this);
  }

  public void CutAction(ActionEvent ev)
  { System.out.println("CutAction in "+this); }

  public void DeleteParameterAction(ActionEvent ev)
  {
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter parameter name:",
        "Delete Parameter",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).deleteParameter(ret);
  }
  public void DeleteStateVariableAction(ActionEvent ev)
  {
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter variable name:",
        "Delete State Variable",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).deleteStateVariable(ret);
  }
  public void GenerateJavaClassAction(ActionEvent ev)   { System.out.println("GenerateJavaClassAction in "+this); }
  public void NewAssemblyAction(ActionEvent ev)         { System.out.println("NewAssemblyAction in "+this); }
  public void NewEventGraphAction(ActionEvent ev)       { System.out.println("NewEventGraphAction in "+this); }
  public void PasteAction(ActionEvent ev)               { System.out.println("PasteAction in "+this); }
  public void QuitAction(ActionEvent ev)                { System.exit(0); }
  public void SaveAction(ActionEvent ev)                { System.out.println("SaveAction in "+this); }
  public void SaveAsAction(ActionEvent ev)              { System.out.println("SaveAsAction in "+this); }
  public void EventListAction(ActionEvent ev)           { System.out.println("EventListAction in "+this); }
  public void SelectModeAction(ActionEvent ev)          { System.out.println("SelectModeAction in "+this); }

  public void NewNodeModeAction(ActionEvent ev)
  {
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter event name:",
        "Add Event",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).newEvent(ret);
  }
  public void ArcModeAction(ActionEvent ev)
  {
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter edge name:",
        "Add Edge",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).newEdge(ret);
  }

  public void CancelArcModeAction(ActionEvent ev)
  {
    String ret = JOptionPane.showInputDialog(
        getView() instanceof Component? (Component)getView() : null,
        "Enter cancelling edge name:",
        "Add Cancelling Edge",
        JOptionPane.PLAIN_MESSAGE);

    if(ret == null || ret.trim().length() <= 0)
      return;
    ((Model)getModel()).newEdge(ret);
  }
  //=======================================

  class VAction extends AbstractAction
  {
    private Method method;

    VAction(String handler)
    //=====================
    {
      try {
        Class actionEvClass = Class.forName("java.awt.event.ActionEvent");
        method = Controller.this.getClass().getDeclaredMethod(handler,new Class[]{actionEvClass});
      }
      catch (Exception e) {
        //assert false : "Reflection error ("+handler+") in Controller.java" ;
        System.out.println("assert false : \"Reflection error ("+handler+") in Controller.java\"");
      }
    }

    /**
     * Each event comes in here and is vectored to the appropriate method in this class
     * @param e ActionEvent
     */
    public void actionPerformed(ActionEvent e)
    //----------------------------------------
    {
      try {
        method.invoke(Controller.this,new Object[]{e});
      }
      catch (IllegalAccessException e1) {
        e1.printStackTrace();
      }
      catch (InvocationTargetException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * Unit test entry.
   * @param args
   */
  public static void main(String[] args)
  {
    new Controller();
  }
}
