package simkit.viskit.jgraph;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import simkit.viskit.model.CancellingEdge;
import simkit.viskit.model.EventNode;
import simkit.viskit.model.SchedulingEdge;
import simkit.viskit.model.Edge;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * OPNAV N81-NPS World-Class-Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA
 * User: mike
 * Date: Feb 23, 2004
 * Time: 1:21:52 PM
 */
public class vGraphModel extends DefaultGraphModel
{
  public JGraph graph; // fix this
  public vGraphModel()
  {
    initViskitStyle();
  }

  Map viskitEdgeStyle, viskitCancelEdgeStyle;

  public void initViskitStyle() {

    viskitEdgeStyle = GraphConstants.createMap();
    GraphConstants.setLineBegin  (viskitEdgeStyle,GraphConstants.ARROW_TECHNICAL);
    GraphConstants.setBeginFill  (viskitEdgeStyle, true);
    GraphConstants.setBeginSize  (viskitEdgeStyle, 10);
    GraphConstants.setFont       (viskitEdgeStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable   (viskitEdgeStyle, true);
    GraphConstants.setLineStyle  (viskitEdgeStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineWidth  (viskitEdgeStyle, 1);
    GraphConstants.setOpaque     (viskitEdgeStyle, true);
    GraphConstants.setBackground (viskitEdgeStyle, new Color(255,255,255,225));
    // comment out for no border GraphConstants.setBorderColor(viskitEdgeStyle, Color.gray);
    GraphConstants.setForeground (viskitEdgeStyle, Color.black);
    //GraphConstants.setRouting    (viskitEdgeStyle, new vGraphComponent.testDefaultRouting()); //GraphConstants.ROUTING_SIMPLE);
    GraphConstants.setRouting    (viskitEdgeStyle, GraphConstants.ROUTING_SIMPLE);

    viskitCancelEdgeStyle = GraphConstants.createMap();
    GraphConstants.setLineBegin  (viskitCancelEdgeStyle,GraphConstants.ARROW_TECHNICAL);
    GraphConstants.setBeginFill  (viskitCancelEdgeStyle, true);
    GraphConstants.setBeginSize  (viskitCancelEdgeStyle, 10);
    GraphConstants.setFont       (viskitCancelEdgeStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable   (viskitCancelEdgeStyle, true);
    GraphConstants.setLineStyle  (viskitCancelEdgeStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineWidth  (viskitCancelEdgeStyle, 1);
    GraphConstants.setOpaque     (viskitCancelEdgeStyle, true);
    GraphConstants.setBackground (viskitCancelEdgeStyle, new Color(255,255,255,225));
    // comment out for no border GraphConstants.setBorderColor(viskitCancelEdgeStyle, Color.gray);
    GraphConstants.setForeground (viskitCancelEdgeStyle, Color.black);
    GraphConstants.setDashPattern(viskitCancelEdgeStyle, new float[] { 3, 3 });
    //GraphConstants.setRouting    (viskitEdgeStyle, new vGraphComponent.testDefaultRouting()); //GraphConstants.ROUTING_SIMPLE);
    GraphConstants.setRouting    (viskitEdgeStyle, GraphConstants.ROUTING_SIMPLE);
  }

  public void changeEvent(EventNode en)
  {
    CircleCell c = (CircleCell)en.opaqueViewObject;
    c.setUserObject(en);

    graph.getUI().stopEditing(graph);
    graph.graphDidChange(); // jmb try...yes, I thought the stopEditing would do the same thing
  }

  public void changeEdge(SchedulingEdge ed)
  {
    changeEitherEdge(ed);
  }
  public void changeCancellingEdge(CancellingEdge ed)
  {
    changeEitherEdge(ed);
  }

  private void changeEitherEdge(Edge ed)
  {
    CircleCell newTo = (CircleCell)ed.to.opaqueViewObject;
    CircleCell newFrom = (CircleCell)ed.from.opaqueViewObject;
    vEdgeCell  ec = (vEdgeCell)ed.opaqueViewObject;

    DefaultPort dpsrc = (DefaultPort)ec.getSource();
    Object o = dpsrc.getParent();
    if(o != newFrom)
      return; // no change
    ec.setSource(ec.getTarget());
    ec.setTarget(dpsrc);

    graph.getUI().stopEditing(graph);    // this does it, but label is screwed
    graph.graphDidChange(); // needed for redraw

  }

  public void deleteAll()
  {
    remove(getRoots(this));
  }

  public void deleteEdge(SchedulingEdge edge)
  {
    DefaultEdge e = (DefaultEdge)edge.opaqueViewObject;
    this.remove(new Object[]{e});
  }
  
  public void deleteCancellingEdge(CancellingEdge edge)
  {
    DefaultEdge e = (DefaultEdge)edge.opaqueViewObject;
    this.remove(new Object[]{e});
  }
  public void deleteEventNode(EventNode en)
  {
    DefaultGraphCell c = (DefaultGraphCell)en.opaqueViewObject;
    c.removeAllChildren();
    this.remove(new Object[]{c});
  }
  public void addEventNode(EventNode en)
  {
    DefaultGraphCell c = new CircleCell(en.getName());
    en.opaqueViewObject = c;
    c.setUserObject(en);

    Map attributes = new Hashtable();
    attributes.put(c,createBounds(en.getPosition().x, en.getPosition().y,Color.black));
    //attributes.put(c,createBounds(p.x,p.y,Color.black)); // color a nop?
    c.add(new DefaultPort(en.getName()+"/Center"));
    this.insert(new Object[]{c},attributes,null,null,null);
  }
  public void addEdge(SchedulingEdge se)
  {
    _addEdgeCommon(se,viskitEdgeStyle);
  }
  public void addCancelEdge(CancellingEdge se)
  {
    _addEdgeCommon(se,viskitCancelEdgeStyle);
  }
  private void _addEdgeCommon(simkit.viskit.model.Edge se, Map edgeStyle)
  {
    EventNode enfrom = se.from;
    EventNode ento   = se.to;
    DefaultGraphCell from = (DefaultGraphCell)enfrom.opaqueViewObject;
    DefaultGraphCell to   = (DefaultGraphCell)ento.opaqueViewObject;
    DefaultEdge edge = new vEdgeCell(null);
    se.opaqueViewObject = edge;
    edge.setUserObject(se);
    ConnectionSet cs = new ConnectionSet();
    cs.connect(edge, to.getChildAt(0), from.getChildAt(0));
    Map attributes = new Hashtable();
    attributes.put(edge, edgeStyle);
    this.insert(new Object[]{edge},attributes,cs,null,null);

  }
  public Map createBounds(int x, int y, Color c) {
    Map map = GraphConstants.createMap();
    GraphConstants.setBounds(map, new Rectangle(x, y, 54, 54)); //90, 30));
    GraphConstants.setBorder(map, BorderFactory.createRaisedBevelBorder());
    GraphConstants.setBackground(map, c.darker());
    GraphConstants.setForeground(map, Color.white);
    GraphConstants.setFont(map, GraphConstants.defaultFont.deriveFont(Font.BOLD, 12));
    GraphConstants.setOpaque(map, true);
    return map;
  }

}
