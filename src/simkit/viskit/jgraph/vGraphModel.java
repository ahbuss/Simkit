package simkit.viskit.jgraph;

import org.jgraph.graph.*;

import javax.swing.*;
import java.util.Map;
import java.util.Hashtable;
import java.awt.*;

import simkit.viskit.jgraph.vEdgeCell;

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
  public vGraphModel()
  {
    addSampleData(this);
  }
 /*
 Make 3 styles: implement, extend, aggregate

 */
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

    Map viskitEdgeStyle = GraphConstants.createMap();
    GraphConstants.setLineBegin  (viskitEdgeStyle,GraphConstants.ARROW_TECHNICAL);
    GraphConstants.setBeginFill  (viskitEdgeStyle, true);
    GraphConstants.setBeginSize  (viskitEdgeStyle, 10);
    GraphConstants.setFont       (viskitEdgeStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable   (viskitEdgeStyle, true);
    GraphConstants.setLineStyle  (viskitEdgeStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineWidth  (viskitEdgeStyle, 1);
    GraphConstants.setOpaque     (viskitEdgeStyle, true);
    GraphConstants.setBackground (viskitEdgeStyle, new Color(255,255,255,225));
    GraphConstants.setBorderColor(viskitEdgeStyle, Color.gray);
    GraphConstants.setForeground (viskitEdgeStyle, Color.black);
    //GraphConstants.setRouting    (viskitEdgeStyle, GraphConstants.ROUTING_SIMPLE);

    Map viskitCancelEdgeStyle = GraphConstants.createMap();
    GraphConstants.setLineBegin  (viskitCancelEdgeStyle,GraphConstants.ARROW_TECHNICAL);
    GraphConstants.setBeginFill  (viskitCancelEdgeStyle, true);
    GraphConstants.setBeginSize  (viskitCancelEdgeStyle, 10);
    GraphConstants.setFont       (viskitCancelEdgeStyle, GraphConstants.defaultFont.deriveFont(10));
    GraphConstants.setBendable   (viskitCancelEdgeStyle, true);
    GraphConstants.setLineStyle  (viskitCancelEdgeStyle, GraphConstants.STYLE_BEZIER);
    GraphConstants.setLineWidth  (viskitCancelEdgeStyle, 1);
    GraphConstants.setOpaque     (viskitCancelEdgeStyle, true);
    GraphConstants.setBackground (viskitCancelEdgeStyle, new Color(255,255,255,225));
    GraphConstants.setBorderColor(viskitCancelEdgeStyle, Color.gray);
    GraphConstants.setForeground (viskitCancelEdgeStyle, Color.black);
    GraphConstants.setDashPattern(viskitCancelEdgeStyle, new float[] { 3, 3 });
   //GraphConstants.setRouting    (viskitEdgeStyle, GraphConstants.ROUTING_SIMPLE);

    DefaultGraphCell run = new CircleCell("Run");
    attributes.put(run,createBounds(20,50,Color.black));
    run.add(new DefaultPort("Run/Center"));

    DefaultGraphCell arrival = new CircleCell("Arrival");
    attributes.put(arrival,createBounds(120,50,Color.black)); // color a nop?
    arrival.add(new DefaultPort("Arrival/Center"));

    DefaultEdge runToArrE = new vEdgeCell(null);
    cs.connect(runToArrE,arrival.getChildAt(0),run.getChildAt(0));
    attributes.put(runToArrE,viskitEdgeStyle);

    DefaultGraphCell stSvc = new CircleCell("Start\nService");
    attributes.put(stSvc,createBounds(270,50,Color.black));
    stSvc.add(new DefaultPort("StartService/Center"));

    DefaultEdge arrToSvcE = new vEdgeCell("(S > 0)");
    cs.connect(arrToSvcE,stSvc.getChildAt(0),arrival.getChildAt(0));
    attributes.put(arrToSvcE,viskitEdgeStyle);

    DefaultGraphCell endSvc = new CircleCell("End\nService");
    attributes.put(endSvc,createBounds(420,50,Color.black));
    endSvc.add(new DefaultPort("EndService/Center"));

    DefaultEdge stToEndE = new vEdgeCell(null);
    cs.connect(stToEndE,endSvc.getChildAt(0),stSvc.getChildAt(0));
    attributes.put(stToEndE,viskitEdgeStyle);

    DefaultEdge endToStE = new vEdgeCell("(Q > 0)");
    cs.connect(endToStE,stSvc.getChildAt(0),endSvc.getChildAt(0));
    attributes.put(endToStE,viskitEdgeStyle);

    DefaultGraphCell endRpr = new CircleCell("End\nRepair");
    attributes.put(endRpr,createBounds(270,200,Color.black));
    endRpr.add(new DefaultPort("EndRepair/Center"));

    DefaultEdge endRprToStSvcE = new vEdgeCell("(Q > 0)");
    cs.connect(endRprToStSvcE,stSvc.getChildAt(0),endRpr.getChildAt(0));
    attributes.put(endRprToStSvcE,viskitEdgeStyle);

    DefaultGraphCell fail = new CircleCell("Failure");
    attributes.put(fail,createBounds(420,200,Color.black));
    fail.add(new DefaultPort("Failure/Center"));

    DefaultEdge failToEndSvcE = new vEdgeCell(null);
    cs.connect(failToEndSvcE,endSvc.getChildAt(0),fail.getChildAt(0));
    attributes.put(failToEndSvcE,viskitCancelEdgeStyle);

    DefaultEdge endRprToFail = new vEdgeCell(null);
    cs.connect(endRprToFail,fail.getChildAt(0),endRpr.getChildAt(0));
    attributes.put(endRprToFail,viskitEdgeStyle);

    DefaultEdge failToEndRpt = new vEdgeCell(null);
    cs.connect(failToEndRpt,endRpr.getChildAt(0),fail.getChildAt(0));
    attributes.put(failToEndRpt,viskitEdgeStyle);




    // jmb DefaultGraphCell gm = new DefaultGraphCell("GraphModel");
    DefaultGraphCell gm = new CircleCell("Start");
    attributes.put(gm, createBounds(20, 400, Color.blue));
    gm.add(new DefaultPort("GraphModel/Center"));

    //jmb DefaultGraphCell dgm = new DefaultGraphCell("DefaultGraphModel");
    DefaultGraphCell dgm = new CircleCell("Arrive");
    attributes.put(dgm, createBounds(20, 480, Color.orange)); //blue));
    dgm.add(new DefaultPort("DefaultGraphModel/Center"));

    DefaultEdge dgmImplementsGm = new vEdgeCell("can edge a\nand next line"); //DefaultEdge("can edge a");
    cs.connect(dgmImplementsGm, gm.getChildAt(0), dgm.getChildAt(0));
    attributes.put(dgmImplementsGm, implementStyle);

/*
    DefaultGraphCell modelGroup = new DefaultGraphCell("ModelGroup");
    modelGroup.add(gm);
    modelGroup.add(dgm);
    modelGroup.add(dgmImplementsGm);
*/

    // JComponent Column
    //DefaultGraphCell jc = new DefaultGraphCell("JComponent");
    DefaultGraphCell jc = new CircleCell("Complete");
    attributes.put(jc, createBounds(150, 320, Color.green));
    jc.add(new DefaultPort("JComponent/Center"));

    //DefaultGraphCell jg = new DefaultGraphCell("JGraph");
    DefaultGraphCell jg = new CircleCell("Restart");
    attributes.put(jg, createBounds(150, 400, Color.green));
    jg.add(new DefaultPort("JGraph/Center"));

    DefaultEdge jgExtendsJc = new vEdgeCell("sch edge b"); //DefaultEdge("sch edge b");
    cs.connect(jgExtendsJc, jc.getChildAt(0), jg.getChildAt(0));
    attributes.put(jgExtendsJc, extendStyle);

    // UI Column
    //DefaultGraphCell cu = new DefaultGraphCell("ComponentUI");
    DefaultGraphCell cu = new CircleCell("Backup");
    attributes.put(cu, createBounds(280, 320, Color.red));
    cu.add(new DefaultPort("ComponentUI/Center"));

    //DefaultGraphCell gu = new DefaultGraphCell("GraphUI");
    DefaultGraphCell gu = new CircleCell("Replace");
    attributes.put(gu, createBounds(280, 400, Color.red));
    gu.add(new DefaultPort("GraphUI/Center"));

    //DefaultGraphCell dgu = new DefaultGraphCell("BasicGraphUI");
    DefaultGraphCell dgu = new CircleCell("Commit");
    attributes.put(dgu, createBounds(280, 480, Color.red));
    dgu.add(new DefaultPort("BasicGraphUI/Center"));

    DefaultEdge guExtendsCu = new vEdgeCell("sch edge c"); //DefaultEdge("sch edge c");
    cs.connect(guExtendsCu, cu.getChildAt(0), gu.getChildAt(0));
    attributes.put(guExtendsCu, extendStyle);

    DefaultEdge dguImplementsDu = new vEdgeCell("can edge d"); //DefaultEdge("can edge d");
    cs.connect(dguImplementsDu, gu.getChildAt(0), dgu.getChildAt(0));
    attributes.put(dguImplementsDu, implementStyle);

/*
    DefaultGraphCell uiGroup = new DefaultGraphCell("UIGroup");
    uiGroup.add(cu);
    uiGroup.add(gu);
    uiGroup.add(dgu);
    uiGroup.add(dguImplementsDu);
    uiGroup.add(guExtendsCu);
*/

    // Aggregations
    DefaultEdge jgAggregatesGm = new vEdgeCell("sch edge e"); //DefaultEdge("sch edge e");
    cs.connect(jgAggregatesGm, jg.getChildAt(0), gm.getChildAt(0));
    attributes.put(jgAggregatesGm, aggregateStyle);

    DefaultEdge jcAggregatesCu = new vEdgeCell("sch edge f"); //DefaultEdge("sch edge f");
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

     gm,
     dgm,
     dgmImplementsGm,
     jc,
     jg,
     jgExtendsJc,
     cu,
     gu,
     dgu,
     guExtendsCu,
     dguImplementsDu,
     jgAggregatesGm,
     jcAggregatesCu,
     baileySelfRefTest
   };

   // Insert Cells into model

    model.insert(cells, attributes, cs, null, null);
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
