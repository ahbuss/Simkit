package simkit.viskit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;

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
  private Edge edge,edgeCopy;
  private static boolean modified = false;
  private JButton canButt,okButt,reverse;
  private JRadioButton schrb,canrb;
  private ButtonGroup bGroup;
  private JTextField srcEvent,targEvent;
  private JTextField delay;
  private ParametersPanel parameters;
  private ConditionalsPanel conditionals;
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

  private EdgeInspectorDialog(Frame frame,
                              Component locationComp,
                              Edge edge)
  {
    super(frame, "Edge Inspector", true);
    this.edge = edge;
    this.edgeCopy = (Edge)edge.copyShallow();

    this.locationComp = locationComp;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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
          srcEvent = new JTextField();
          srcPan.add(srcEvent);
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
          targEvent = new JTextField();
          targPan.add(targEvent);
        dirPSub.add(targPan);
      dirP.add(dirPSub);
        reverse = new JButton("reverse");
      dirP.add(reverse);
    con.add(dirP);
    con.add(Box.createVerticalStrut(5));

      JPanel delayPan = new JPanel();
      delayPan.setLayout(new BoxLayout(delayPan,BoxLayout.X_AXIS));
      delayPan.setOpaque(false);
      delayPan.setBorder(BorderFactory.createTitledBorder("Time Delay"));
        delay = new JTextField();
        delay.setOpaque(true);
        delay.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      delayPan.add(delay);
    con.add(delayPan);
    con.add(Box.createVerticalStrut(5));

      conditionals = new ConditionalsPanel(edge); //new JList();
      JScrollPane condSp = new JScrollPane(conditionals);
      condSp.setBorder(BorderFactory.createTitledBorder("Conditional Expression"));
      condSp.setOpaque(false);
    con.add(condSp);
    con.add(Box.createVerticalStrut(5));

      parameters = new ParametersPanel();
      JScrollPane paramSp = new JScrollPane(parameters);
      paramSp.setBorder(BorderFactory.createTitledBorder("Edge Parameters"));
      paramSp.setOpaque(false);
    con.add(paramSp);
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
  }
  public void setParams(Component c, Edge e)
  {
    edge = e;
    edgeCopy = (Edge)e.copyShallow();
    locationComp = c;

    fillWidgets();
    modified = false;
    okButt.setEnabled(false);
    getRootPane().setDefaultButton(canButt);

    this.setLocationRelativeTo(c);
  }

  private void fillWidgets()
  {
    if(edgeCopy instanceof SchedulingEdge) {
      schrb.setSelected(true);
      canrb.setSelected(false);
    }
    else {
      canrb.setSelected(true);
      schrb.setSelected(false);
    }
    srcEvent.setText(edgeCopy.from.name);
    targEvent.setText(edgeCopy.to.name);
    delay.setText("0");
  }

  private void unloadWidgets()
  {
    edge.from = edgeCopy.from;
    edge.to   = edgeCopy.to;
    //edge.delay = ...
    //edge.conditionals = ....
    //edge.parameters = ....    
  }
  class reverseButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      EventNode en = edgeCopy.from;
      edgeCopy.from = edgeCopy.to;
      edgeCopy.to = en;
      srcEvent.setText(edgeCopy.from.name);
      targEvent.setText(edgeCopy.to.name);
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
      System.out.println("stateChanged");
      modified = true;
      okButt.setEnabled(true);
      getRootPane().setDefaultButton(okButt);

    }
  }
}
