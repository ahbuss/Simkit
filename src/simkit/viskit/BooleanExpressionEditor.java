package simkit.viskit;

import simkit.viskit.model.EventNode;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 15, 2004
 * Time: 11:47:09 AM
 */

public class BooleanExpressionEditor extends JDialog
{
  private static BooleanExpressionEditor dialog;
  private Component locationComp;
  private EventNode node;
  private static boolean modified = false;
  private JButton canButt,okButt;
  private JTextField name, delay;
  private JList transitions, parameters;

  /**
   * Set up and show the dialog.  The first Component argument
   * determines which frame the dialog depends on; it should be
   * a component in the dialog's controlling frame. The second
   * Component argument should be null if you want the dialog
   * to come up with its left corner in the center of the screen;
   * otherwise, it should be the component on top of which the
   * dialog should appear.
   */
  public static boolean showDialog(JFrame f, Component comp, EventNode node)
  {
    if(dialog == null)
      dialog = new BooleanExpressionEditor(f,comp,node);
    else
      dialog.setParams(comp,node);

    dialog.setVisible(true);
      // above call blocks
    return modified;
  }

  private BooleanExpressionEditor(Frame frame,
                              Component locationComp,
                              EventNode node)
  {
    super(frame, "Event -- "+node.getName(), true);
    this.node = node;
    this.locationComp = locationComp;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    Container cont = getContentPane();
    cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));
    JPanel con = new JPanel();
    con.setLayout(new BoxLayout(con,BoxLayout.Y_AXIS));
    con.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    con.add(Box.createVerticalStrut(5));

      // name
      JPanel namePan = new JPanel();
      namePan.setLayout(new BoxLayout(namePan,BoxLayout.X_AXIS));
      namePan.setOpaque(false);
      namePan.setBorder(BorderFactory.createTitledBorder("Event name"));
        name = new JTextField();
        name.setOpaque(true);
        name.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      namePan.add(name);
    con.add(namePan);
    con.add(Box.createVerticalStrut(5));

      // delay
      JPanel delayPan = new JPanel();
      delayPan.setLayout(new BoxLayout(delayPan,BoxLayout.X_AXIS));
      delayPan.setOpaque(false);
      delayPan.setBorder(BorderFactory.createTitledBorder("Time delay"));
        delay = new JTextField();
        delay.setOpaque(true);
        delay.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      delayPan.add(delay);
    con.add(delayPan);
    con.add(Box.createVerticalStrut(5));

      // state transitions
      transitions = new JList();
      transitions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      JScrollPane condSp = new JScrollPane(transitions);
      condSp.setBorder(BorderFactory.createTitledBorder("State Transitions"));
      condSp.setOpaque(false);
    con.add(condSp);

      // buttons
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
    sizeAndPosition();

    // attach listeners
    canButt.addActionListener(new BooleanExpressionEditor.cancelButtonListener());
    okButt.addActionListener(new BooleanExpressionEditor.applyButtonListener());
    BooleanExpressionEditor.myChangeActionListener chlis = new BooleanExpressionEditor.myChangeActionListener();
    //name.addActionListener(chlis);
    name.addKeyListener(new BooleanExpressionEditor.myKeyListener());
    delay.addActionListener(chlis);
    //todo transitions.add
  }

  private void sizeAndPosition()
  {
    pack();     // do this prior to next
    // little check to add some extra space to always include the node name in title bar w/out dotdotdots
    if(getWidth()<350)
      setSize(350,getHeight());
    this.setLocationRelativeTo(locationComp);
  }

  public void setParams(Component c, EventNode en)
  {
    node=en;
    locationComp = c;

    fillWidgets();
    sizeAndPosition();
  }

  private void fillWidgets()
  {
    setTitle("Event -- "+node.getName());
    name.setText(node.getName());
    transitions.setListData(new String[]{"a>b", "b>c", "c>a","fill from Event"});

    modified = false;
// test    okButt.setEnabled(false);
    getRootPane().setDefaultButton(canButt);
  }
  private void unloadWidgets()
  {
    node.setName(name.getText());
    // todo the rest
  }
  class cancelButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      modified = false;
      setVisible(false);
    }
  }
  class applyButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      // test
      modified = true;
      if(modified)
        unloadWidgets();
      setVisible(false);
    }
  }
  class myKeyListener extends KeyAdapter
  {
    public void keyTyped(KeyEvent e)
    {
      modified = true;
      okButt.setEnabled(true);
      getRootPane().setDefaultButton(okButt);
    }
  }
  class myChangeActionListener implements ChangeListener,ActionListener
  {
    public void stateChanged(ChangeEvent event)
    {
      System.out.println("stateChanged");
      modified = true;
      okButt.setEnabled(true);
      getRootPane().setDefaultButton(okButt);

    }

    public void actionPerformed(ActionEvent event)
    {
      stateChanged(null);
    }
  }
}
