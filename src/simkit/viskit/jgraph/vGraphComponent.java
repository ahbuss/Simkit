package simkit.viskit.jgraph;

import simkit.viskit.ModelEvent;
import org.jgraph.JGraph;
import org.jgraph.graph.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 * OPNAV N81-NPS World-Class-Modeling (WCM) 2004 Projects
 * MOVES Institute.
 * Naval Postgraduate School, Monterey, CA
 * User: mike
 * Date: Feb 19, 2004
 * Time: 2:54:31 PM
 */
public class vGraphComponent extends JGraph
/**********************************************/
{
  vGraphModel model;

  public vGraphComponent(vGraphModel model)
  {
    super(model);
    ToolTipManager.sharedInstance().registerComponent(this);
    //super.setDoubleBuffered(false); // test for mac
    this.model = model;
    this.setModel(model);
    this.setBendable(true);
    this.setGridVisible(true);
    //this.setGridMode(JGraph.CROSS_GRID_MODE);
    //this.setGridMode(JGraph.DOT_GRID_MODE);
    this.setGridMode(JGraph.LINE_GRID_MODE);
  }
  
  public void viskitModelChanged(ModelEvent ev)
  {
    System.out.println("in vGraphComponent.viskitModelChanged() with ModelEvent "+ev);
  }

  public String getToolTipText(MouseEvent event)
  {
    if(event != null) {
      Object c = this.getFirstCellForLocation(event.getX(),event.getY());
      if(c != null) {
        String s = this.convertValueToString(c);
        if(c instanceof vEdgeCell) {
          return "edge: "+s;
        }
        else if (c instanceof CircleCell) {
        return "<html><font size=2><b><font size=3>ev: "+s+"</b></font>"+
            "<table border=1><tr><td>state 1 delta</td><td>...</td></tr>"+
                  "<tr><td>state 2 delta</td><td>...</td></tr>"+
                  "<tr><td>state 3 delta</td><td>...</td></tr>"+
                  "<tr><td><i>param 1</i></td><td>xxx</td></tr>"+
                  "<tr><td><i>param 2</i></td><td>yyy</td></tr>"+
            "</table></font></html>";
        }
      }
    }
    return null;
  }

  // To use circles, from the tutorial
  protected VertexView createVertexView(Object v, CellMapper cm)
  {
    if(v instanceof CircleCell)
      return new CircleView(v,this,cm);
    // else
    return super.createVertexView(v, cm);
  }

  // To customize my edges
  protected EdgeView createEdgeView(Object e, CellMapper cm)
  {
    if(e instanceof vEdgeCell)
      return new vEdgeView(e,this,cm);
    // else
    return super.createEdgeView(e, cm);
  }
}

/***********************************************/

/**
 * To mark our edges.
 */
class vEdgeCell extends DefaultEdge
{
  public vEdgeCell()
  {
    this(null);
  }
  public vEdgeCell(Object userObject)
  {
    super(userObject);
    GraphConstants.setRouting(attributes,new DefaultRouting());
  }
}

/**
 * Sub class EdgeView to install our own renderer.
 */
class vEdgeView extends EdgeView
{
  public static vEdgeRenderer renderer = new vEdgeRenderer();

  public vEdgeView(Object cell, JGraph gr, CellMapper cm)
  {
    super(cell,gr,cm);
  }

  public CellViewRenderer getRenderer()
  {
    return renderer;
  }
}

/**
 * To mark our nodes.
 */
class CircleCell extends DefaultGraphCell
{
  public CircleCell()
  {
    this(null);
  }
  public CircleCell(Object userObject)
  {
    super(userObject);
  }
}

/**
 * Sub class VertexView to install our own renderer.
 */
class CircleView extends VertexView
{
  static vVertexRenderer renderer = new vVertexRenderer();

  public CircleView(Object cell, JGraph gr, CellMapper cm)
  {
    super(cell,gr,cm);
  }

  public CellViewRenderer getRenderer()
  {
    return renderer;
  }
}

