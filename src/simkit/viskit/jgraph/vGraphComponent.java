package simkit.viskit.jgraph;

import simkit.viskit.*;
import simkit.viskit.model.Edge;
import simkit.viskit.model.EventNode;
import simkit.viskit.model.CancellingEdge;
import simkit.viskit.model.SchedulingEdge;
import simkit.xsd.bindings.*;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.plaf.basic.BasicGraphUI;
import org.jgraph.graph.*;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Map;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.List;

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
  EventGraphViewFrame parent;
  public vGraphComponent(vGraphModel model, EventGraphViewFrame frame)
  {
    super(model);
    parent = frame;
    ToolTipManager.sharedInstance().registerComponent(this);
    //super.setDoubleBuffered(false); // test for mac
    this.model = model;
    this.setModel(model);
    this.setBendable(true);
    this.setSizeable(false);
    this.setGridVisible(true);
    //this.setGridMode(JGraph.CROSS_GRID_MODE);
    //this.setGridMode(JGraph.DOT_GRID_MODE);
    this.setGridMode(JGraph.LINE_GRID_MODE);
    this.setMarqueeHandler(new MyMarqueeHandler());
    this.setAntiAliased(true);
    this.addGraphSelectionListener(new myGraphSelectionListener());
  }

  public void updateUI()
  {
    // Install a new UI
    setUI(new vGraphUI(this));    // we use our own for node/edge inspector editting
    //setUI(new BasicGraphUI());   // test
    invalidate();
  }


  public void viskitModelChanged(ModelEvent ev)
  {
    //System.out.println("in vGraphComponent.viskitModelChanged() with ModelEvent "+ev);
    switch (ev.getID()) {
      case ModelEvent.NEWMODEL:
        model.deleteAll();
        break;
      case ModelEvent.EVENTADDED:
        Object[] oa = (Object[])ev.getSource();
        model.addEventNode((EventNode)oa[0],(Point)oa[1]);
        break;
      case ModelEvent.EDGEADDED:
        model.addEdge((SchedulingEdge)ev.getSource());
        break;
      case ModelEvent.CANCELLINGEDGEADDED:
        model.addCancelEdge((CancellingEdge)ev.getSource());
        break;
      case ModelEvent.EVENTCHANGED:
        model.changeEvent((EventNode)ev.getSource());
        break;
      case ModelEvent.EVENTDELETED:
        model.deleteEventNode((EventNode)ev.getSource());
        break;
      case ModelEvent.EDGECHANGED:
        model.changeEdge((SchedulingEdge)ev.getSource());
        break;
      case ModelEvent.EDGEDELETED:
        model.deleteEdge((SchedulingEdge)ev.getSource());
        break;
      case ModelEvent.CANCELLINGEDGECHANGED:
        model.changeCancellingEdge((CancellingEdge)ev.getSource());
        break;
      case ModelEvent.CANCELLINGEDGEDELETED:
        model.deleteCancellingEdge((CancellingEdge)ev.getSource());
        break;
      default:
        //System.out.println("duh")
        ;
    }
  }
  private String getEdgePs(List edgLis)
  {
    String params = "";
    for(Iterator itr = edgLis.iterator(); itr.hasNext();) {
      EdgeParameter edPar = (EdgeParameter)itr.next();
      params+="<LI>"+edPar.getValue()+" ("+edPar.getType()+")</LI>";
    }
    if(params.length()<=0)
      return null;
    return params;
  }
  public String getToolTipText(MouseEvent event)
  {
    String tt = "";
    if(event != null) {
      Object c = this.getFirstCellForLocation(event.getX(),event.getY());
      if(c != null) {
        String s = this.convertValueToString(c);
        if(c instanceof vEdgeCell) {
          //return "edge: "+s;
          vEdgeCell vc = (vEdgeCell)c;
          if(vc.getUserObject() instanceof SchedulingEdge) {
            SchedulingEdge se = (SchedulingEdge)vc.getUserObject();
            Schedule sch =  (Schedule)se.opaqueModelObject;

            String cond = "";
            if(sch.getCondition() != null && sch.getCondition().length()>0) {
              cond += sch.getCondition();
            }
            if(cond.length() > 0)
              tt += "if( " + cond + " )";

            List edPLis = sch.getEdgeParameter();
            StringBuffer epSt = new StringBuffer();
            int idx = 1;
            for(Iterator itr = edPLis.iterator();itr.hasNext();) {
              EdgeParameter ep = (EdgeParameter)itr.next();
              epSt.append("&nbsp;"+idx++);
              epSt.append("(");
              epSt.append(ep.getType());
              epSt.append(")");
              epSt.append("&nbsp;");
              epSt.append(ep.getValue());
              epSt.append("<br>");
            }
            if(epSt.length()>0) {
              epSt.setLength(epSt.length()-4); // lose the last <br>
              tt += "<u>edge parameters</u><br>"+epSt.toString();
            }
          }
          else {
            CancellingEdge ce = (CancellingEdge)vc.getUserObject();
            Cancel can = (Cancel)ce.opaqueModelObject;
            String cond = "";
            if(can.getCondition() != null && can.getCondition().length()>0) {
              cond += can.getCondition();
            }
            if(cond.length() >0)
              tt += "if( " + cond + " )";

            List edPLis = can.getEdgeParameter();
             StringBuffer epSt = new StringBuffer();
             int idx = 1;
             for(Iterator itr = edPLis.iterator();itr.hasNext();) {
               EdgeParameter ep = (EdgeParameter)itr.next();
               epSt.append("&nbsp;"+idx++);
               epSt.append("(");
               epSt.append(ep.getType());
               epSt.append(")");
               epSt.append("&nbsp;");
               epSt.append(ep.getValue());
               epSt.append("<br>");
             }
             if(epSt.length()>0) {
               epSt.setLength(epSt.length()-4); // lose the last <br>
               tt += "<u>edge parameters</u><br>"+epSt.toString();
             }
          }

          if(tt.length() <= 0)

            return "true";
          //else
            return "<HTML>" + tt + "</HTML>";

        }
        else if (c instanceof CircleCell) {
          CircleCell cc = (CircleCell)c;
        /*
        return "<html><font size=2><b><font size=3>ev: "+s+"</b></font>"+
            "<table border=1><tr><td>state 1 delta</td><td>...</td></tr>"+
                  "<tr><td>state 2 delta</td><td>...</td></tr>"+
                  "<tr><td>state 3 delta</td><td>...</td></tr>"+
                  "<tr><td><i>param 1</i></td><td>xxx</td></tr>"+
                  "<tr><td><i>param 2</i></td><td>yyy</td></tr>"+
            "</table></font></html>";
        */
          EventNode en = (EventNode)cc.getUserObject();
          tt += "<center>"+en.getName()+"</center>";
          simkit.xsd.bindings.Event ev = (simkit.xsd.bindings.Event)en.opaqueModelObject;
          List st = ev.getStateTransition();
          String sttrans = "";
          for(Iterator itr = st.iterator();itr.hasNext();) {
            StateTransition str = (StateTransition)itr.next();
            Assignment assg = (Assignment)str.getAssignment();
            Operation opr   = (Operation)str.getOperation();
            StateVariable stt = (StateVariable)str.getState();
            if(assg != null)
              sttrans += "&nbsp;" + stt.getName() + "=" +assg.getValue();
            else
              sttrans += "&nbsp;" + stt.getName() + "." + opr.getMethod();
            sttrans += "<br>";
          }
          if(sttrans.length()>0) {
            sttrans = sttrans.substring(0,sttrans.length()-4);
            tt += "<u>state transitions</u><br>"+sttrans;
          }

          List argLis = ev.getArgument();
          String args = "";
          int n = 1;
          for(Iterator itr = argLis.iterator(); itr.hasNext();) {
            Argument arg = (Argument)itr.next();
            String as = arg.getName() + " ("+arg.getType()+")";
            args += "&nbsp;"+n + " " +as + "<br>";
          }
          if(args.length() > 0) {
            args = args.substring(0,args.length()-4);  // remove last <br>
            tt += "<br><u>arguments</u><br>"+args;
          }

          List locVarLis = ev.getLocalVariable();
          String lvs = "";
          for(Iterator itr = locVarLis.iterator(); itr.hasNext();) {
            LocalVariable lv = (LocalVariable)itr.next();
            String vs = lv.getName() + " ("+lv.getType()+") = ";
            String val = lv.getValue();
            vs += (val.length()<= 0? "<i><default></i>" : val);
            lvs += "&nbsp;"+vs+"<br>";
          }
          if(lvs.length() > 0) {
            lvs = lvs.substring(0,lvs.length()-4); // remove last <br>
            tt += "<br><u>local variables</u><br>"+lvs;
          }
          return "<HTML>"+ tt +"</HTML>";
        }
      }
    }
    return null;
  }

  public String convertValueToString(Object value)
  {
    if(value instanceof CircleView) {
      CircleCell cc = (CircleCell)((CellView)value).getCell();
      Object en = cc.getUserObject();
      if(en instanceof EventNode)  // should always be, except for our prototype examples
        return ((EventNode)en).getName();
    }
    else if(value instanceof vEdgeView) {
      vEdgeCell cc = (vEdgeCell)((CellView)value).getCell();
      Object e = cc.getUserObject();
      if(e instanceof SchedulingEdge)
        return "S";
      else if (e instanceof CancellingEdge) // should always be one of these 2 except for proto examples
        return null;
    }
    else if (value instanceof DefaultGraphCell) {
      Object e = ((DefaultGraphCell)value).getUserObject();
      if(e instanceof SchedulingEdge)
        return "S";
      else if (e instanceof CancellingEdge)
        return null;
      else if (e instanceof EventNode)
        return ((EventNode)e).getName();
    }
    else if(value instanceof String)      // jmb added
    {if(((String)value).length()<= 0) System.out.println("myfault3");
        return (String)value;         }

    return "S"; // todo make work neither of these 2 changes has any effect //super.convertValueToString(value);
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

/**
 * This class informs the controller that the selected set has changed.  Since we're only useing this
 * to (dis)able the cut and copy menu items, it could be argued that this functionality should be internal
 * to the View, and the controller needn't be involved.  Nevertheless, the round trip through the controller
 * remains in place.
 */
  class myGraphSelectionListener implements GraphSelectionListener
  {
    Vector selected = new Vector();
    public void valueChanged(GraphSelectionEvent e)
    {
      Object[] oa = e.getCells();
      if(oa == null || oa.length<=0)
        return;
      for(int i=0;i<oa.length;i++)
        if(e.isAddedCell(i))
           selected.add(((DefaultGraphCell)oa[i]).getUserObject());
        else
           selected.remove(((DefaultGraphCell)oa[i]).getUserObject());
      ((ViskitController)parent.getController()).selectNodeOrEdge(selected);
    }
  }

// MarqueeHandler that Connects Vertices and Displays PopupMenus
  public class MyMarqueeHandler extends BasicMarqueeHandler
  {

    // Holds the Start and the Current Point
    protected Point start, current;

    // Holds the First and the Current Port
    protected PortView port, firstPort;

    // Override to Gain Control (for PopupMenu and ConnectMode)
    public boolean isForceMarqueeEvent(MouseEvent e)
    {
      // If Right Mouse Button we want to Display the PopupMenu
      if (SwingUtilities.isRightMouseButton(e))
      // Return Immediately
        return true;
      // Find and Remember Port
      port = getSourcePortAt(e.getPoint());
      // If Port Found and in ConnectMode (=Ports Visible)
      if (port != null && vGraphComponent.this.isPortsVisible())
        return true;
      // Else Call Superclass
      return super.isForceMarqueeEvent(e);
    }

    // Display PopupMenu or Remember Start Location and First Port
    public void mousePressed(final MouseEvent e)
    {
      // If Right Mouse Button
      if (SwingUtilities.isRightMouseButton(e)) {
        // Scale From Screen to Model
        Point loc = vGraphComponent.this.fromScreen(e.getPoint());
        // Find Cell in Model Coordinates
        Object cell = vGraphComponent.this.getFirstCellForLocation(loc.x, loc.y);
        // Create PopupMenu for the Cell
        JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
        // Display PopupMenu
        menu.show(vGraphComponent.this, e.getX(), e.getY());

        // Else if in ConnectMode and Remembered Port is Valid
      }
      else if (port != null && !e.isConsumed() && vGraphComponent.this.isPortsVisible()) {
        // Remember Start Location
        start = vGraphComponent.this.toScreen(port.getLocation(null));
        // Remember First Port
        firstPort = port;
        // Consume Event
        e.consume();
      }
      else
      // Call Superclass
        super.mousePressed(e);
    }

    // Find Port under Mouse and Repaint Connector
    public void mouseDragged(MouseEvent e)
    {
      // If remembered Start Point is Valid
      if (start != null && !e.isConsumed()) {
        // Fetch Graphics from Graph
        Graphics g = vGraphComponent.this.getGraphics();
        // Xor-Paint the old Connector (Hide old Connector)
        paintConnector(Color.black, vGraphComponent.this.getBackground(), g);
        // Reset Remembered Port
        port = getTargetPortAt(e.getPoint());
        // If Port was found then Point to Port Location
        if (port != null)
          current = vGraphComponent.this.toScreen(port.getLocation(null));
        // Else If no Port was found then Point to Mouse Location
        else
          current = vGraphComponent.this.snap(e.getPoint());
        // Xor-Paint the new Connector
        paintConnector(vGraphComponent.this.getBackground(), Color.black, g);
        // Consume Event
        e.consume();
      }
      // Call Superclass
      super.mouseDragged(e);
    }

    public PortView getSourcePortAt(Point point)
    {
      // Scale from Screen to Model
      Point tmp = vGraphComponent.this.fromScreen(new Point(point));
      // Find a Port View in Model Coordinates and Remember
      return vGraphComponent.this.getPortViewAt(tmp.x, tmp.y);
    }

    // Find a Cell at point and Return its first Port as a PortView
    protected PortView getTargetPortAt(Point point)
    {
      // Find Cell at point (No scaling needed here)
      Object cell = vGraphComponent.this.getFirstCellForLocation(point.x, point.y);
      // Loop Children to find PortView
      for (int i = 0; i < vGraphComponent.this.getModel().getChildCount(cell); i++) {
        // Get Child from Model
        Object tmp = vGraphComponent.this.getModel().getChild(cell, i);
        // Get View for Child using the Graph's View as a Cell Mapper
      //jmb fix  tmp = graphPane.getView().getMapping(tmp, false);
        // If Child View is a Port View and not equal to First Port
        if (tmp instanceof PortView && tmp != firstPort)
        // Return as PortView
          return (PortView) tmp;
      }
      // No Port View found
      return getSourcePortAt(point);
    }

    // Connect the First Port and the Current Port in the Graph or Repaint
    public void mouseReleased(MouseEvent e)
    {
      // If Valid Event, Current and First Port
      if (e != null && !e.isConsumed() && port != null && firstPort != null &&
          firstPort != port) {
        // Then Establish Connection
        connect((Port) firstPort.getCell(), (Port) port.getCell());
        // Consume Event
        e.consume();
        // Else Repaint the Graph
      }
      else
        vGraphComponent.this.repaint();
      // Reset Global Vars
      firstPort = port = null;
      start = current = null;
      // Call Superclass
      super.mouseReleased(e);
    }

    // Show Special Cursor if Over Port
    public void mouseMoved(MouseEvent e)
    {
      // Check Mode and Find Port
      if (e != null && getSourcePortAt(e.getPoint()) != null &&
          !e.isConsumed() && vGraphComponent.this.isPortsVisible()) {
        // Set Cusor on Graph (Automatically Reset)
        vGraphComponent.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Consume Event
        e.consume();
      }
      // Call Superclass
      super.mouseReleased(e);
    }

    // Use Xor-Mode on Graphics to Paint Connector
    protected void paintConnector(Color fg, Color bg, Graphics g)
    {
      // Set Foreground
      g.setColor(fg);
      // Set Xor-Mode Color
      g.setXORMode(bg);
      // Highlight the Current Port
      paintPort(vGraphComponent.this.getGraphics());
      // If Valid First Port, Start and Current Point
      if (firstPort != null && start != null && current != null)
      // Then Draw A Line From Start to Current Point
        g.drawLine(start.x, start.y, current.x, current.y);
    }

    // Use the Preview Flag to Draw a Highlighted Port
    protected void paintPort(Graphics g)
    {
      // If Current Port is Valid
      if (port != null) {
        // If Not Floating Port...
        boolean o = (GraphConstants.getOffset(port.getAttributes()) != null);
        // ...Then use Parent's Bounds
        Rectangle r = (o) ? port.getBounds() : port.getParentView().getBounds();
        // Scale from Model to Screen
        r = vGraphComponent.this.toScreen(new Rectangle(r));
        // Add Space For the Highlight Border
        //r.setBounds(r.x - 3, r.y - 3, r.width + 6, r.height + 6);
        r.setBounds(r.x - 5, r.y - 5, r.width + 10, r.height + 10);
        // Paint Port in Preview (=Highlight) Mode
        vGraphComponent.this.getUI().paintCell(g, port, r, true);
      }
    }
    // Insert a new Vertex at point
    public void insert(Point point)
    {
      // Construct Vertex with no Label
      DefaultGraphCell vertex = new DefaultGraphCell();
      // Add one Floating Port
      vertex.add(new DefaultPort());
      // Snap the Point to the Grid
      point = vGraphComponent.this.snap(new Point(point));
      // Default Size for the new Vertex
      Dimension size = new Dimension(25, 25);
      // Create a Map that holds the attributes for the Vertex
      Map map = GraphConstants.createMap();
      // Add a Bounds Attribute to the Map
      GraphConstants.setBounds(map, new Rectangle(point, size));
      // Add a Border Color Attribute to the Map
      GraphConstants.setBorderColor(map, Color.black);
      // Add a White Background
      GraphConstants.setBackground(map, Color.white);
      // Make Vertex Opaque
      GraphConstants.setOpaque(map, true);
      // Construct a Map from cells to Maps (for insert)
      Hashtable attributes = new Hashtable();
      // Associate the Vertex with its Attributes
      attributes.put(vertex, map);

      System.out.println("!!!!!!!!!insert");
      // Insert the Vertex and its Attributes
   //   graphPane.getModel().insert(new Object[]{vertex}, null, null, attributes);
    }

    // Insert a new Edge between source and target
    public void connect(Port source, Port target)
    {
      DefaultGraphCell src = (DefaultGraphCell)vGraphComponent.this.getModel().getParent(source);
      DefaultGraphCell tar = (DefaultGraphCell)vGraphComponent.this.getModel().getParent(target);
      Object[] oa = new Object[]{src,tar};
      ViskitController controller = (ViskitController)parent.getController();
      if(parent.getCurrentMode() == EventGraphViewFrame.CANCEL_ARC_MODE)
        controller.newCancelArc(oa);
      else
        controller.newArc(oa);

    /*
      // Connections that will be inserted into the Model
      ConnectionSet cs = new ConnectionSet();
      // Construct Edge with no label
      DefaultEdge edge = new DefaultEdge();
      // Create Connection between source and target using edge
      cs.connect(edge, source, target);
      // Create a Map thath holds the attributes for the edge
      Map map = GraphConstants.createMap();
      // Add a Line End Attribute
    // jmb what her  GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);
      // Construct a Map from cells to Maps (for insert)
      Hashtable attributes = new Hashtable();
      // Associate the Edge with its Attributes
      attributes.put(edge, map);
      // Insert the Edge and its Attributes
      //graphPane.getModel().insert(new Object[]{edge}, cs, null, attributes);
      System.out.println("!!!!!!!!!!!connect");
    */
    }
    public JPopupMenu createPopupMenu(final Point pt, final Object cell)
      {
        JPopupMenu menu = new JPopupMenu();
        if (cell != null) {
          // Edit
          menu.add(new AbstractAction("Edit")
          {
            public void actionPerformed(ActionEvent e)
            {
              vGraphComponent.this.startEditingAtCell(cell);
            }
          });
        }
        // Remove
        if (!vGraphComponent.this.isSelectionEmpty()) {
          menu.addSeparator();
          menu.add(new AbstractAction("Remove")
          {
            public void actionPerformed(ActionEvent e)
            {
             // jmb fix remove.actionPerformed(e);
              // remove is an Action
              System.out.println("!!!!!!!!!!!!!!remove");
            }
          });
        }
        menu.addSeparator();
        // Insert
        menu.add(new AbstractAction("Insert")
        {
          public void actionPerformed(ActionEvent ev)
          {
            insert(pt);
          }
        });
        return menu;
      }



  } // End of Editor.MyMarqueeHandler
  public static class testDefaultRouting implements org.jgraph.graph.Edge.Routing {

    public void route(EdgeView edge, java.util.List points) {
      //System.out.println(edge.toString() + points);
      int n = points.size();
      Point from = edge.getPoint(0);
      if (edge.getSource() instanceof PortView)
        from = ((PortView) edge.getSource()).getLocation(null);
      else if (edge.getSource() != null)
        from = edge.getSource().getBounds().getLocation();
      Point to = edge.getPoint(n - 1);
      if (edge.getTarget() instanceof PortView)
        to = ((PortView) edge.getTarget()).getLocation(null);
      else if (edge.getTarget() != null)
        to = edge.getTarget().getBounds().getLocation();
      if (from != null && to != null) {
        Point[] routed;
        // Handle self references
        if (edge.getSource() == edge.getTarget()
          && edge.getSource() != null) {

System.out.println(edge.toString() + " /// " +points);

          Rectangle bounds =
            edge.getSource().getParentView().getBounds();
          int height = 30; //edge.getGraph().getGridSize();
          int width = 20; //(int) (bounds.getWidth() / 3);
          routed = new Point[4];
          routed[0] =
            new Point(
              bounds.x + width,
              bounds.y + bounds.height);
          routed[1] =
            new Point(
              bounds.x + width,
              bounds.y + bounds.height + height);
          routed[2] =
            new Point(
              bounds.x + 2 * width,
              bounds.y + bounds.height + height);
          routed[3] =
            new Point(
              bounds.x + 2 * width,
              bounds.y + bounds.height);
          System.out.println("...... "+routed[0].x+","+routed[0].y+" "
                                      +routed[1].x+","+routed[1].y+" "
                                      +routed[2].x+","+routed[2].y+" "
                                      +routed[3].x+","+routed[3].y+" ");
        } else {
          int dx = Math.abs(from.x - to.x);
          int dy = Math.abs(from.y - to.y);
          int x2 = from.x + ((to.x - from.x) / 2);
          int y2 = from.y + ((to.y - from.y) / 2);
          routed = new Point[2];
          if (dx > dy) {
            routed[0] = new Point(x2, from.y);
            //new Point(to.x, from.y)
            routed[1] = new Point(x2, to.y);
          } else {
            routed[0] = new Point(from.x, y2);
            // new Point(from.x, to.y)
            routed[1] = new Point(to.x, y2);
          }
        }
        // Set/Add Points
        for (int i=0; i<routed.length; i++)
          if (points.size() > i+2)
            points.set(i+1, routed[i]);
          else
            points.add(i+1, routed[i]);
        // Remove spare points
        while (points.size() > routed.length+2) {
          points.remove(points.size()-2);
        }
      }
    }
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
    GraphConstants.setRouting(attributes,GraphConstants.ROUTING_SIMPLE);
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

  CircleCell()
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
