package simkit.viskit.jgraph;

import org.jgraph.graph.*;
import org.jgraph.JGraph;

import javax.swing.*;
import java.util.Map;
import java.util.Hashtable;
import java.awt.*;

import simkit.viskit.jgraph.vEdgeCell;
import simkit.viskit.EventNode;
import simkit.viskit.SchedulingEdge;
import simkit.viskit.CancellingEdge;

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
    addSampleData(this);
  }
 /*
 Make 3 styles: implement, extend, aggregate

 */
  Map viskitEdgeStyle, viskitCancelEdgeStyle;

  public void addSampleData(GraphModel model) {
    ConnectionSet cs = new ConnectionSet();
    Map attributes = new Hashtable();
    // Styles For Implement/Extend/Aggregation
    Map implementStyle = GraphConstants.createMap();
    GraphConstants.setLineBegin  (implementStyle,GraphConstants.ARROW_TECHNICAL);   // open arrow
    GraphConstants.setBeginSize  (implementStyle, 10);
    GraphConstants.setDashPattern(implementStyle, new float[] { 3, 3 });
    GraphConstants.setFont       (implementStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable   (implementStyle, true);
   // GraphConstants.setLineStyle  (implementStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineStyle  (implementStyle, GraphConstants.STYLE_ORTHOGONAL);
    GraphConstants.setLineWidth  (implementStyle, 1);
    GraphConstants.setOpaque     (implementStyle, true);
    GraphConstants.setBackground (implementStyle, new Color(255,255,255,150));
    GraphConstants.setBorderColor(implementStyle, Color.gray);
    GraphConstants.setForeground (implementStyle, Color.black);
    GraphConstants.setRouting    (implementStyle, GraphConstants.ROUTING_SIMPLE);

    Map extendStyle = GraphConstants.createMap();
    GraphConstants.setLineBegin(extendStyle,GraphConstants.ARROW_TECHNICAL);
    GraphConstants.setBeginFill(extendStyle, true);
    GraphConstants.setBeginSize(extendStyle, 10);
    GraphConstants.setFont     (extendStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable (extendStyle, true);
    //GraphConstants.setLineStyle(extendStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineStyle(extendStyle, GraphConstants.STYLE_QUADRATIC);
    GraphConstants.setLineWidth(extendStyle, 1);
    GraphConstants.setOpaque     (extendStyle, true);
    GraphConstants.setBackground (extendStyle, new Color(255,255,255,150));
    GraphConstants.setBorderColor(extendStyle, Color.gray);
    GraphConstants.setForeground (extendStyle, Color.black);
    GraphConstants.setRouting    (extendStyle, GraphConstants.ROUTING_SIMPLE);

    Map aggregateStyle = GraphConstants.createMap();
    //GraphConstants.setLineBegin    (aggregateStyle,GraphConstants.ARROW_DIAMOND);
    GraphConstants.setBeginFill    (aggregateStyle, true);
    GraphConstants.setBeginSize    (aggregateStyle, 6);
    GraphConstants.setLineEnd      (aggregateStyle, GraphConstants.ARROW_SIMPLE);   // 2 lines
    GraphConstants.setEndSize      (aggregateStyle, 8);
    GraphConstants.setLabelPosition(aggregateStyle, new Point(500, 1200));
    GraphConstants.setFont         (aggregateStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable     (aggregateStyle, true);
    GraphConstants.setLineStyle    (aggregateStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineWidth    (aggregateStyle, 1);
    GraphConstants.setOpaque       (aggregateStyle, true);
    GraphConstants.setBackground   (aggregateStyle, new Color(255,255,255,150));
    GraphConstants.setBorderColor  (aggregateStyle, Color.gray);
    GraphConstants.setForeground   (aggregateStyle, Color.black);
    GraphConstants.setRouting      (aggregateStyle, GraphConstants.ROUTING_SIMPLE);

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
    //GraphConstants.setRouting    (viskitEdgeStyle, GraphConstants.ROUTING_SIMPLE);

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
   //GraphConstants.setRouting    (viskitEdgeStyle, GraphConstants.ROUTING_SIMPLE);

    DefaultGraphCell run = new CircleCell("Run");
    EventNode run_en = new EventNode("Run");
    run.setUserObject(run_en);
    run_en.opaqueViewObject = run;
    attributes.put(run,createBounds(20,50,Color.black));
    run.add(new DefaultPort("Run/Center"));

    DefaultGraphCell arrival = new CircleCell("Arrival");
    EventNode arr_en = new EventNode("Arrival");
    arr_en.stateTrans = "Q++";
    arrival.setUserObject(arr_en);
    arr_en.opaqueViewObject = arrival;
    attributes.put(arrival,createBounds(120,50,Color.black)); // color a nop?
    arrival.add(new DefaultPort("Arrival/Center"));

    DefaultEdge runToArrE = new vEdgeCell(null);
    SchedulingEdge sEdge = new SchedulingEdge();
    runToArrE.setUserObject(sEdge);
    tempAddToFroms(runToArrE,(DefaultPort)arrival.getChildAt(0),(DefaultPort)run.getChildAt(0));
    sEdge.opaqueViewObject = runToArrE;
    cs.connect(runToArrE,arrival.getChildAt(0),run.getChildAt(0));
    attributes.put(runToArrE,viskitEdgeStyle);

    run_en.connections.add(sEdge);
    arr_en.connections.add(sEdge);


    DefaultGraphCell stSvc = new CircleCell("Start\nService");
    EventNode st_en = new EventNode("Start\nService");
    st_en.stateTrans = "Q--, S--";
    stSvc.setUserObject(st_en);
    st_en.opaqueViewObject = stSvc;
    attributes.put(stSvc,createBounds(270,50,Color.black));
    stSvc.add(new DefaultPort("StartService/Center"));

    DefaultEdge arrToSvcE = new vEdgeCell("(S > 0)");
    sEdge = new SchedulingEdge();
    arrToSvcE.setUserObject(sEdge);
    tempAddToFroms(arrToSvcE,(DefaultPort)stSvc.getChildAt(0),(DefaultPort)arrival.getChildAt(0));
    sEdge.opaqueViewObject = arrToSvcE;
    cs.connect(arrToSvcE,stSvc.getChildAt(0),arrival.getChildAt(0));
    attributes.put(arrToSvcE,viskitEdgeStyle);

    arr_en.connections.add(sEdge);
    st_en.connections.add(sEdge);

    DefaultGraphCell endSvc = new CircleCell("End\nService");
    EventNode endSvc_en = new EventNode("End\nService");
    endSvc_en.stateTrans = "S++";
    endSvc.setUserObject(endSvc_en);
    endSvc_en.opaqueViewObject = endSvc;
    attributes.put(endSvc,createBounds(420,50,Color.black));
    endSvc.add(new DefaultPort("EndService/Center"));

    DefaultEdge stToEndE = new vEdgeCell(null);
    sEdge = new SchedulingEdge();
    stToEndE.setUserObject(sEdge);
    tempAddToFroms(stToEndE,(DefaultPort)endSvc.getChildAt(0),(DefaultPort)stSvc.getChildAt(0));
    sEdge.opaqueViewObject = stToEndE;
    cs.connect(stToEndE,endSvc.getChildAt(0),stSvc.getChildAt(0));
    attributes.put(stToEndE,viskitEdgeStyle);

    endSvc_en.connections.add(sEdge);
    st_en.connections.add(sEdge);

    DefaultEdge endToStE = new vEdgeCell(null);
    sEdge = new SchedulingEdge();
    endToStE.setUserObject(sEdge);
    tempAddToFroms(endToStE,(DefaultPort)stSvc.getChildAt(0),(DefaultPort)endSvc.getChildAt(0));
    sEdge.opaqueViewObject = endToStE;
    cs.connect(endToStE,stSvc.getChildAt(0),endSvc.getChildAt(0));
    attributes.put(endToStE,viskitEdgeStyle);

    st_en.connections.add(sEdge);
    endSvc_en.connections.add(sEdge);

    DefaultGraphCell endRpr = new CircleCell("End\nRepair");
    EventNode endR_en = new EventNode("End\nRepair");
    endR_en.stateTrans = "F--";
    endRpr.setUserObject(endR_en);
    endR_en.opaqueViewObject = endRpr;
    attributes.put(endRpr,createBounds(270,200,Color.black));
    endRpr.add(new DefaultPort("EndRepair/Center"));

    DefaultEdge endRprToStSvcE = new vEdgeCell("(Q > 0)");
    sEdge = new SchedulingEdge();
    endRprToStSvcE.setUserObject(sEdge);
    tempAddToFroms(endRprToStSvcE,(DefaultPort)stSvc.getChildAt(0),(DefaultPort)endRpr.getChildAt(0));
    sEdge.opaqueViewObject = endRprToStSvcE;
    cs.connect(endRprToStSvcE,stSvc.getChildAt(0),endRpr.getChildAt(0));
    attributes.put(endRprToStSvcE,viskitEdgeStyle);

    endR_en.connections.add(sEdge);
    st_en.connections.add(sEdge);

    DefaultGraphCell fail = new CircleCell("Failure");
    EventNode fail_en = new EventNode("Failure");
    fail_en.stateTrans = "F++,<br>Q += 1-S,<br>S = 0";

    fail.setUserObject(fail_en);
    fail_en.opaqueViewObject = fail;
    attributes.put(fail,createBounds(420,200,Color.black));
    fail.add(new DefaultPort("Failure/Center"));

    DefaultEdge failToEndSvcE = new vEdgeCell(null);
    CancellingEdge cEdge = new CancellingEdge();
    failToEndSvcE.setUserObject(cEdge);
    tempAddToFroms(failToEndSvcE,(DefaultPort)endSvc.getChildAt(0),(DefaultPort)fail.getChildAt(0));
    cEdge.opaqueViewObject = failToEndSvcE;
    cs.connect(failToEndSvcE,endSvc.getChildAt(0),fail.getChildAt(0));
    attributes.put(failToEndSvcE,viskitCancelEdgeStyle);

    fail_en.connections.add(cEdge);
    endSvc_en.connections.add(cEdge);

    DefaultEdge endRprToFail = new vEdgeCell(null);
    sEdge = new SchedulingEdge();
    endRprToFail.setUserObject(sEdge);
    sEdge.opaqueViewObject = endRprToFail;
    cs.connect(endRprToFail,fail.getChildAt(0),endRpr.getChildAt(0));
    attributes.put(endRprToFail,viskitEdgeStyle);

    endR_en.connections.add(sEdge);
    fail_en.connections.add(sEdge);

    DefaultEdge failToEndRpt = new vEdgeCell(null);
    sEdge = new SchedulingEdge();
    failToEndRpt.setUserObject(sEdge);
 tempAddToFroms(failToEndRpt,(DefaultPort)endRpr.getChildAt(0),(DefaultPort)fail.getChildAt(0));
    sEdge.opaqueViewObject = failToEndRpt;
    cs.connect(failToEndRpt,endRpr.getChildAt(0),fail.getChildAt(0));
    attributes.put(failToEndRpt,viskitEdgeStyle);

    endR_en.connections.add(sEdge);
    fail_en.connections.add(sEdge);


    /*
    // begin experiments

    DefaultGraphCell gm = new CircleCell("Start");
    attributes.put(gm, createBounds(20, 400, Color.blue));
    gm.add(new DefaultPort("GraphModel/Center"));

    DefaultGraphCell dgm = new CircleCell("Arrive");
    attributes.put(dgm, createBounds(20, 480, Color.orange)); //blue));
    dgm.add(new DefaultPort("DefaultGraphModel/Center"));

    DefaultEdge dgmImplementsGm = new vEdgeCell("can edge a\nand next line");
    cs.connect(dgmImplementsGm, gm.getChildAt(0), dgm.getChildAt(0));
    attributes.put(dgmImplementsGm, implementStyle);


    //DefaultGraphCell modelGroup = new DefaultGraphCell("ModelGroup");
    //modelGroup.add(gm);
    //modelGroup.add(dgm);
    //modelGroup.add(dgmImplementsGm);

    // JComponent Column
    DefaultGraphCell jc = new CircleCell("Complete");
    attributes.put(jc, createBounds(150, 320, Color.green));
    jc.add(new DefaultPort("JComponent/Center"));

    DefaultGraphCell jg = new CircleCell("Restart");
    attributes.put(jg, createBounds(150, 400, Color.green));
    jg.add(new DefaultPort("JGraph/Center"));

    DefaultEdge jgExtendsJc = new vEdgeCell("sch edge b");
    cs.connect(jgExtendsJc, jc.getChildAt(0), jg.getChildAt(0));
    attributes.put(jgExtendsJc, extendStyle);

    DefaultGraphCell cu = new CircleCell("Backup");
    attributes.put(cu, createBounds(280, 320, Color.red));
    cu.add(new DefaultPort("ComponentUI/Center"));

    DefaultGraphCell gu = new CircleCell("Replace");
    attributes.put(gu, createBounds(280, 400, Color.red));
    gu.add(new DefaultPort("GraphUI/Center"));

    DefaultGraphCell dgu = new CircleCell("Commit");
    attributes.put(dgu, createBounds(280, 480, Color.red));
    dgu.add(new DefaultPort("BasicGraphUI/Center"));

    DefaultEdge guExtendsCu = new vEdgeCell("sch edge c");
    cs.connect(guExtendsCu, cu.getChildAt(0), gu.getChildAt(0));
    attributes.put(guExtendsCu, extendStyle);

    DefaultEdge dguImplementsDu = new vEdgeCell("can edge d");
    cs.connect(dguImplementsDu, gu.getChildAt(0), dgu.getChildAt(0));
    attributes.put(dguImplementsDu, implementStyle);

    //DefaultGraphCell uiGroup = new DefaultGraphCell("UIGroup");
    //uiGroup.add(cu);
    //uiGroup.add(gu);
    //uiGroup.add(dgu);
    //uiGroup.add(dguImplementsDu);
    //uiGroup.add(guExtendsCu);

    // Aggregations
    DefaultEdge jgAggregatesGm = new vEdgeCell("sch edge e");
    cs.connect(jgAggregatesGm, jg.getChildAt(0), gm.getChildAt(0));
    attributes.put(jgAggregatesGm, aggregateStyle);

    DefaultEdge jcAggregatesCu = new vEdgeCell("sch edge f");
    cs.connect(jcAggregatesCu, jc.getChildAt(0), cu.getChildAt(0));
    attributes.put(jcAggregatesCu, aggregateStyle);


    DefaultEdge baileySelfRefTest = new vEdgeCell("g");
    cs.connect(baileySelfRefTest,gu.getChildAt(0),gu.getChildAt(0));
    attributes.put(baileySelfRefTest,extendStyle);

    // Insert Cells into model

    Object[] xcells =
      new Object[] {
        jgAggregatesGm,
        jcAggregatesCu,
       // modelGroup,
        jc,
        jg,
        jgExtendsJc,
        //uiGroup
      };
    */

   Object[] cells = new Object[] {
     run,
     arrival,
     runToArrE,
     stSvc,
     arrToSvcE,
     endSvc,
     stToEndE,
     endToStE,
     endRpr,
     endRprToStSvcE,
     fail,
     failToEndSvcE,
     endRprToFail,
     failToEndRpt,

     //gm,
     //dgm,
     //dgmImplementsGm,
     //jc,
     //jg,
     //jgExtendsJc,
     //cu,
     //gu,
     //dgu,
     //guExtendsCu,
     //dguImplementsDu,
     //jgAggregatesGm,
     //jcAggregatesCu,
     //baileySelfRefTest
   };

   // Insert Cells into model

    model.insert(cells, attributes, cs, null, null);
  }

  private void tempAddToFroms(DefaultEdge edge, DefaultPort to, DefaultPort from)
  {
    simkit.viskit.Edge e = (simkit.viskit.Edge)edge.getUserObject();
    e.to = (EventNode)((CircleCell)to.getParent()).getUserObject();
    e.from = (EventNode)((CircleCell)from.getParent()).getUserObject();
  }

  public void changeEvent(EventNode en)
  {
    System.out.println("vGraphModel.changeEvent()");
    // only changes we worry about are connections and name
    // Since the name is gotten by the renderer from the userobject, and that remains set, we're good.
    // Just tell to repaint.
    // todo jmb here
    CircleCell c = (CircleCell)en.opaqueViewObject;
    c.setUserObject(en);// causes a repaint...?
    // doesn't do anything here
    graph.getUI().stopEditing(graph);
  }
  public void changeEdge(SchedulingEdge ed)
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

    graph.getUI().stopEditing(graph);    // this does it, but lable is screwed
    //graph.ge
    //this.edit(null,null,null,null);  // does this work to cause a redraw?
    // per doc
    // none of this works
/*
    Object[] v = graph.getRoots();

    graph.setPreferredSize(graph.getPreferredSize());
    graph.graphDidChange();
*/
  }
  public void changeCancellingEdge(CancellingEdge ed)
  {

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
  public void addEventNode(EventNode en, Point p)
  {
    DefaultGraphCell c = new CircleCell(en.name);
    en.opaqueViewObject = c;
    c.setUserObject(en);

    Map attributes = new Hashtable();
    attributes.put(c,createBounds(p.x,p.y,Color.black)); // color a nop?
    c.add(new DefaultPort(en.name+"/Center"));
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
  private void _addEdgeCommon(simkit.viskit.Edge se, Map edgeStyle)
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
