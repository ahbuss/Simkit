package simkit.viskit.jgraph;

import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Map;
import java.util.Hashtable;

import simkit.viskit.*;
import simkit.viskit.model.EventNode;
import simkit.viskit.model.Edge;
import simkit.viskit.model.CancellingEdge;
import simkit.viskit.model.SchedulingEdge;

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
 * This code is a copy of the appropriate part of EditorGraph.java, which is
 * part of JGraph examples.
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

  /**
   * Stops the editing session. If messageStop is true the editor
   * is messaged with stopEditing, if messageCancel is true the
   * editor is messaged with cancelEditing. If messageGraph is true
   * the graphModel is messaged with valueForCellChanged.
   */
  protected void completeEditing(boolean messageStop,
                                 boolean messageCancel,
                                 boolean messageGraph)
  {
    if (stopEditingInCompleteEditing
        && editingComponent != null
        && editDialog != null) {
      Component oldComponent = editingComponent;
      Object oldCell = editingCell;
      GraphCellEditor oldEditor = cellEditor;
      Object newValue = oldEditor.getCellEditorValue();
      Rectangle editingBounds = graph.getCellBounds(editingCell);
      boolean requestFocus =
          (graph != null
          && (graph.hasFocus() || editingComponent.hasFocus()));
      editingCell = null;
      editingComponent = null;
      if (messageStop)
        oldEditor.stopCellEditing();
      else if (messageCancel)
        oldEditor.cancelCellEditing();
      editDialog.dispose();
      if (requestFocus)
        graph.requestFocus();
      if (messageGraph) {
        Map map = GraphConstants.createMap();
        GraphConstants.setValue(map, newValue);
        Map nested = new Hashtable();
        nested.put(oldCell, map);
        graphLayoutCache.edit(nested, null, null, null);
      }
      updateSize();
      // Remove Editor Listener
      if (oldEditor != null && cellEditorListener != null)
        oldEditor.removeCellEditorListener(cellEditorListener);
      cellEditor = null;
      editDialog = null;
    }
  }

}
