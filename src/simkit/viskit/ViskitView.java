package simkit.viskit;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 18, 2004
 * Time: 12:06:11 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * The MVC design of Viskit means that the ViskitModel and the ViskitView know about the
 * chosen view only as much as is described by this interface.
 */

public interface ViskitView
{
  public boolean doEditCancelEdge( CancellingEdge edge );
  public boolean doEditEdge( SchedulingEdge edge );
  public boolean doEditNode( EventNode node );

  public int     genericAsk             ( String title, String prompt );      //JOptionPane constants
  public void    genericErrorReport     ( String title, String message );
  public String  promptForStringOrCancel( String title, String message, String initval);

  public File    openFileAsk();
  public File    saveFileAsk();

  public void    fileName(String s);
  
  // The following 2 may be implemented by the view in someother way that an official GUI Dialog
  public Parameter     addParameterDialog();
  public StateVariable addStateVariableDialog();

}
