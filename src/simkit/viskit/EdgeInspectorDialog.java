package simkit.viskit;

import simkit.viskit.model.*;
import simkit.xsd.bindings.Schedule;
import simkit.xsd.bindings.Cancel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collections;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.*;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 2:56:21 PM
 */

public class EdgeInspectorDialog extends JDialog
{
  private static EdgeInspectorDialog dialog;
  private Component locationComp;
  private Edge edge,xedgeCopy;
  private static boolean modified = false;
  private JButton canButt,okButt,reverse;
  private JRadioButton schrb,canrb;
  private ButtonGroup bGroup;
  private JComboBox srcEvent,targEvent;
  private JTextField delay;
  private EdgeParametersPanel parameters;
  private ConditionalsPanel conditionals;
  private JPanel delayPan;
  private Border delayPanBorder,delayPanDisabledBorder;
  private JFrame myFrame;
  /**
   * Set up and show the dialog.  The first Component argument
   * determines which frame the dialog depends on; it should be
   * a component in the dialog's controlling frame. The second
   * Component argument should be null if you want the dialog
   * to come up with its left corner in the center of the screen;
   * otherwise, it should be the component on top of which the
   * dialog should appear.
   */
  public static boolean showDialog(JFrame f, Component comp, Edge edge)
  {
    if(dialog == null)
      dialog = new EdgeInspectorDialog(f,comp,edge);
    else
      dialog.setParams(comp,edge);

    dialog.setVisible(true);
      // above call blocks
    return modified;
  }

  Vector nodeList;
  Model mod; //todo fix
  private EdgeInspectorDialog(JFrame frame,
                              Component locationComp,
                              Edge edge)
  {
    super(frame, "Edge Inspector", true);
    myFrame = frame;
    this.edge = edge;
    //this.edgeCopy = (Edge)edge; //.copyShallow();                   // todo jmb comfirm

    this.locationComp = locationComp;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    mod = (Model)((EventGraphViewFrame)frame).getModel();       // todo fix this
   // nodeList = mod.getAllNodes();

    //Collections.sort(nodeList);             // todo get working

    Container cont = getContentPane();
    cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));
    JPanel con = new JPanel();
    con.setLayout(new BoxLayout(con,BoxLayout.Y_AXIS));
    con.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    con.add(Box.createVerticalStrut(5));
      // edge type
      JPanel typeP = new JPanel();
      typeP.setLayout(new BoxLayout(typeP,BoxLayout.X_AXIS));
      JLabel lab = new JLabel("Type: ");
      typeP.add(lab);
      schrb = new JRadioButton("Scheduling");
      schrb.setSelected(true);
      canrb = new JRadioButton("Cancelling");
      bGroup = new ButtonGroup();
      bGroup.add(schrb);
      bGroup.add(canrb);
      typeP.add(schrb);
      typeP.add(canrb);
    con.add(typeP);
    con.add(Box.createVerticalStrut(5));

      JPanel dirP = new JPanel();
      dirP.setLayout(new BoxLayout(dirP,BoxLayout.X_AXIS));
        JPanel dirPSub = new JPanel();
        dirPSub.setLayout(new BoxLayout(dirPSub,BoxLayout.Y_AXIS));
          JPanel srcPan = new JPanel();
          srcPan.setLayout(new BoxLayout(srcPan,BoxLayout.X_AXIS));
          JLabel lab1 = new JLabel("Source event:");
          srcPan.add(lab1);
          srcPan.add(Box.createHorizontalStrut(5));
          srcEvent = new JComboBox(); //nodeList);
          srcEvent.setBackground(Color.white);
          srcPan.add(srcEvent);
          Dimension d = srcPan.getPreferredSize();
          d.width = Integer.MAX_VALUE;
          srcPan.setMaximumSize(d);
        dirPSub.add(srcPan);
          JPanel targPan = new JPanel();
          targPan.setLayout(new BoxLayout(targPan,BoxLayout.X_AXIS));
          lab = new JLabel("Target event:");
          // line it up with one above
          lab.setMinimumSize(lab1.getMinimumSize());
          lab.setMaximumSize(lab1.getMaximumSize());
          lab.setPreferredSize(lab1.getPreferredSize());
          targPan.add(lab);
          targPan.add(Box.createHorizontalStrut(5));
          targEvent = new JComboBox(); //nodeList); //JTextField();
          targEvent.setBackground(Color.white);
          targPan.add(targEvent);
          d = targPan.getPreferredSize();
          d.width = Integer.MAX_VALUE;
          targPan.setMaximumSize(d);
        dirPSub.add(targPan);
      dirP.add(dirPSub);
        reverse = new JButton("reverse");
      dirP.add(reverse);
    con.add(dirP);
    con.add(Box.createVerticalStrut(5));

    delayPan = new JPanel();
      delayPan.setLayout(new BoxLayout(delayPan,BoxLayout.X_AXIS));
      delayPan.setOpaque(false);
      delayPan.setBorder(BorderFactory.createTitledBorder("Time Delay"));
        delay = new JTextField();
        delay.setOpaque(true);
        delay.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        d = delay.getPreferredSize();      // only expand in horiz direction
        d.width = Integer.MAX_VALUE;
        delay.setMaximumSize(d);
      delayPan.add(delay);
      this.delayPanBorder = delayPan.getBorder();
      this.delayPanDisabledBorder = BorderFactory.createTitledBorder(new LineBorder(Color.gray),"Time Delay",
                                    TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,
                                    null,Color.gray);
    con.add(delayPan);
    con.add(Box.createVerticalStrut(5));

      conditionals = new ConditionalsPanel(edge); //new JList();
      //JScrollPane condSp = new JScrollPane(conditionals);
      //condSp.setBorder(BorderFactory.createTitledBorder("Conditional Expression"));
      //condSp.setOpaque(false);
    //con.add(condSp)
    con.add(conditionals);
    con.add(Box.createVerticalStrut(5));

     JPanel myParmPanel = new JPanel();
     myParmPanel.setLayout(new BoxLayout(myParmPanel,BoxLayout.Y_AXIS));
     myParmPanel.setBorder(BorderFactory.createTitledBorder("Edge Parameters"));

       parameters = new EdgeParametersPanel(300);
       JScrollPane paramSp = new JScrollPane(parameters);
       paramSp.setBorder(null);
       paramSp.setOpaque(false);

     myParmPanel.add(paramSp);

    con.add(myParmPanel);
    con.add(Box.createVerticalStrut(5));


      JPanel buttPan = new JPanel();
      buttPan.setLayout(new BoxLayout(buttPan,BoxLayout.X_AXIS));
      canButt = new JButton("Cancel");
      okButt = new JButton("Apply changes");
      buttPan.add(Box.createHorizontalGlue());
      buttPan.add(canButt);
      buttPan.add(okButt);
    con.add(buttPan);
    cont.add(con);

    fillWidgets();     // put the data into the widgets

    modified = false;
    okButt.setEnabled(false);
    getRootPane().setDefaultButton(canButt);

    pack();     // do this prior to next
    this.setLocationRelativeTo(locationComp);

    // attach listeners
    canButt.addActionListener(new cancelButtonListener());
    okButt.addActionListener(new applyButtonListener());
    ChangeListener chlis = new myChangeListener();
    schrb.addChangeListener(chlis);
    canrb.addChangeListener(chlis);
    reverse.addActionListener(new reverseButtonListener());
    conditionals.addChangeListener(chlis);
    ActionListener ttArgLis = new ArgumentsToolTipUpdater();
    targEvent.addActionListener(ttArgLis);
    

    parameters.addDoubleClickedListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        vEdgeParameter ep = (vEdgeParameter)event.getSource();
        boolean modified = EdgeParameterDialog.showDialog(myFrame,EdgeInspectorDialog.this,ep);
        if(modified) {
          parameters.updateRow(ep);
        }
      }
    });
  }
  public void setParams(Component c, Edge e)
  {
    edge = e;
    //edgeCopy = (Edge)e; //.copyShallow();       // todo jmb confirm
    locationComp = c;

    fillWidgets();
    modified = false;
    okButt.setEnabled(false);
    getRootPane().setDefaultButton(canButt);

    this.setLocationRelativeTo(c);
  }

  private void fillWidgets()
  {
    nodeList = mod.getAllNodes();                  // todo fix
    srcEvent.setModel(new DefaultComboBoxModel(nodeList));
    targEvent.setModel(new DefaultComboBoxModel(nodeList));
    setTargEventTT(targEvent);
    if(edge instanceof SchedulingEdge) {
      schrb.setSelected(true);
      canrb.setSelected(false);
    }
    else {
      canrb.setSelected(true);
      schrb.setSelected(false);
    }
    srcEvent.setSelectedItem(edge.from);
    targEvent.setSelectedItem(edge.to);

    parameters.setData(edge.parameters);

    if(edge instanceof SchedulingEdge) {
      conditionals.setText(edge.conditional);
      //conditionals.setText(((Schedule)((SchedulingEdge)edgeCopy).opaqueModelObject).getCondition());
      delay.setText(""+edge.delay);
      //delay.setText((((Schedule)((SchedulingEdge)edgeCopy).opaqueModelObject).getDelay()));
      delay.setEnabled(true);
      delayPan.setBorder(delayPanBorder);
  }
    else {
      conditionals.setText(edge.conditional);
      //conditionals.setText(((Cancel)((CancellingEdge)edgeCopy).opaqueModelObject).getCondition());
      delay.setText("n/a");
      delay.setEnabled(false);
      delayPan.setBorder(delayPanDisabledBorder);
    }
  }

  private void unloadWidgets()
  {
    edge.from = (EventNode)srcEvent.getSelectedItem(); //edgeCopy.from;
    edge.to   = (EventNode)targEvent.getSelectedItem(); //edgeCopy.to;
    edge.delay = delay.getText();
    edge.conditional = conditionals.getText();

    edge.parameters.clear();
    for(Iterator itr = parameters.getData().iterator(); itr.hasNext(); ) {
      edge.parameters.add(itr.next());
    }
  }

  class reverseButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      EventNode en = edge.from;
      edge.from = edge.to;
      edge.to = en;
      srcEvent.setSelectedItem(edge.from);
      targEvent.setSelectedItem(edge.to);
      modified = true;
      okButt.setEnabled(true);
      getRootPane().setDefaultButton(okButt);
    }
  }
  class cancelButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      modified = false;    // for the caller
      setVisible(false);
    }
  }
  class applyButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if(modified)
        unloadWidgets();
      setVisible(false);
    }
  }
  class myChangeListener implements ChangeListener
  {
    public void stateChanged(ChangeEvent event)
    {
      modified = true;
      okButt.setEnabled(true);
      getRootPane().setDefaultButton(okButt);
    }
  }
  class ArgumentsToolTipUpdater implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      JComboBox jcb = (JComboBox)e.getSource();
      setTargEventTT(jcb);
    }
  }
  private void setTargEventTT(JComboBox jcb)
  {
    EventNode en = (EventNode)jcb.getSelectedItem();
    StringBuffer sb = new StringBuffer("<html><center>arguments</center>");
    int idx = 0;
    for(Iterator itr = en.getArguments().iterator(); itr.hasNext();) {
      EventArgument ea = (EventArgument)itr.next();
      sb.append(""+idx++);
      sb.append(" ");
      sb.append(ea.getName());
      sb.append(" (");
      sb.append(ea.getType());
      sb.append(")<br>");
    }
    if(idx == 0) // no arguments
      targEvent.setToolTipText(null);
    else {
      sb.setLength(sb.length()-4);   // lose last <br>
      sb.append("</html>");
      targEvent.setToolTipText(sb.toString());
    }

  }
}
