package simkit.viskit.jgraph;

import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.*;

import simkit.viskit.*;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 3:17:59 PM
 */

/**
 * BasicGraphUI must be overridden to allow in node and edge editting.
 */
public class vGraphUI extends BasicGraphUI
{
  private JDialog editDialog;

  private vGraphComponent parent;

  public vGraphUI(vGraphComponent parent)
  {
    this.parent = parent;
  }

  protected boolean startEditing(Object cell, MouseEvent event)
  {
    System.out.println("startEditing");
    //return super.startEditing(cell, event);    //Call superclass
    completeEditing();
    if (graph.isCellEditable(cell) && editDialog == null) {

      // Create Editing Component **** *****
      CellView tmp = graphLayoutCache.getMapping(cell, false);
      cellEditor = tmp.getEditor();
      editingComponent = cellEditor.getGraphCellEditorComponent(graph, cell, graph.isCellSelected(cell));
      if (cellEditor.isCellEditable(event)) {
        editingCell = cell;

        // Create Wrapper Dialog **** *****
        createEditDialog(cell, event);

        // Add Editor Listener
        if (cellEditorListener == null)
          cellEditorListener = createCellEditorListener();
        if (cellEditor != null && cellEditorListener != null)
          cellEditor.addCellEditorListener(cellEditorListener);

        if (cellEditor.shouldSelectCell(event)) {
          stopEditingInCompleteEditing = false;
          try {
            graph.setSelectionCell(cell);
          }
          catch (Exception e) {
            System.err.println("Editing exception: " + e);
          }
          stopEditingInCompleteEditing = true;
        }

        if (event instanceof MouseEvent) {
          /* Find the component that will get forwarded all the
          mouse events until mouseReleased. */
          Point componentPoint = SwingUtilities.convertPoint(graph,new Point(event.getX(), event.getY()), editingComponent);

          /* Create an instance of BasicTreeMouseListener to handle
          passing the mouse/motion events to the necessary
          component. */
          // We really want similiar behavior to getMouseEventTarget,
          // but it is package private.
          Component activeComponent = SwingUtilities.getDeepestComponentAt(editingComponent, componentPoint.x, componentPoint.y);
          if (activeComponent != null) {
            new MouseInputHandler(graph,activeComponent,event);
          }
        }
        return true;
      }
      else
        editingComponent = null;
    }
    return false;
  }


  protected void createEditDialog(Object c, MouseEvent event)
  {
    final Object cell = c;
    //MouseEvent ev = event;

    SwingUtilities.invokeLater( new Runnable()
    {
      public void run()
      {
        ViskitController cntl = (ViskitController) vGraphUI.this.parent.parent.getController();     // todo fix this
        if (cell instanceof vEdgeCell) {
          Edge e = (Edge) ((vEdgeCell) cell).getUserObject();
          if (e instanceof SchedulingEdge)
            //cntl.ARCEDIT.actionPerformed(new ActionEvent(e, 0, "Arc edit"));
            cntl.arcEdit((SchedulingEdge)e);
          else //if(e instanceof CancellingEdge)
            //cntl.CANARCEDIT.actionPerformed(new ActionEvent(e, 0, "Can arc edit"));
            cntl.canArcEdit((CancellingEdge)e);
        }
        else if (cell instanceof CircleCell) {
          EventNode en = (EventNode) ((CircleCell) cell).getUserObject();
          //cntl.NODEEDIT.actionPerformed(new ActionEvent(en, 0, "Node edit"));
          cntl.nodeEdit(en);
        }
      }
    });
  }

  /*
  public void startEditingAtCell(JGraph graph, Object cell)
  {
    System.out.println("startEditingAtCell");
    super.startEditingAtCell(graph, cell);    //Call superclass
  }
  */

  public boolean stopEditing(JGraph graph)
  {
    System.out.println("stopEditing");
    return super.stopEditing(graph);    //Call superclass
  }
}
