package simkit.viskit;

import simkit.viskit.model.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 18, 2004
 * Time: 12:06:11 PM
 */

/**
 * The MVC design of Viskit means that the ViskitModel and the ViskitView know about the
 * chosen view only as much as is described by this interface.
 */

public interface ViskitView
{
  // permit user to edit existing entities
  public boolean doEditCancelEdge   ( CancellingEdge edge );
  public boolean doEditEdge         ( SchedulingEdge edge );
  public boolean doEditNode         ( EventNode node );
  public boolean doEditParameter    ( vParameter param );
  public boolean doEditStateVariable( vStateVariable var);

  public int     genericAsk             ( String title, String prompt );      // returns JOptionPane constants
  public void    genericErrorReport     ( String title, String message );
  public String  promptForStringOrCancel( String title, String message, String initval);

  public File    openFileAsk();
  public File    saveFileAsk();

  public void    fileName(String s);    // informative, tells view what we're working on
  
  // The following 2 may be implemented by the view in someother way that an official GUI Dialog
  public void addParameterDialog();
  public void addStateVariableDialog();

}
