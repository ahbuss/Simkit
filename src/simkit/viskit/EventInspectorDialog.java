package simkit.viskit;

import bsh.Interpreter;
import bsh.EvalError;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EtchedBorder;

import simkit.viskit.model.*;
import simkit.xsd.bindings.*;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 2:56:21 PM
 */

public class EventInspectorDialog extends JDialog
{
  private static EventInspectorDialog dialog;
  private Component locationComponent;
  private EventNode node;
  private static boolean modified = false;
  private JButton canButt,okButt,testButt;
  private JTextField name, delay;
  private JList parameters;
  private TransitionsPanel transitions;
  private ArgumentsPanel arguments;
  private LocalVariablesPanel localVariables;
  private JFrame fr;
  private JButton lvPlus,lvMinus;

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
      dialog = new EventInspectorDialog(f,comp,node);
    else
      dialog.setParams(comp,node);

    dialog.setVisible(true);
      // above call blocks
    return modified;
  }

  private EventInspectorDialog(JFrame frame,
                              Component locationComp,
                              EventNode node)
  {
    super(frame, "Event -- "+node.getName(), true);
    this.fr = frame;
    this.node = node;
    this.locationComponent = locationComp;
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
        name = new JTextField("Junk");
        name.setOpaque(true);
        name.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

      namePan.add(name);
    // make the field expand only horiz.
     Dimension d = namePan.getPreferredSize();
     d.width = Integer.MAX_VALUE;
     namePan.setMaximumSize(d);

    con.add(namePan);
    con.add(Box.createVerticalStrut(5));

/*
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
*/

      // state transitions
      transitions = new TransitionsPanel();
      transitions.setBorder(BorderFactory.createTitledBorder("State transitions"));
    con.add(transitions);
    con.add(Box.createVerticalStrut(5));

      // local vars
      localVariables = new LocalVariablesPanel(300);
      localVariables.setBorder(BorderFactory.createTitledBorder("Local variables"));
    con.add(localVariables);
    con.add(Box.createVerticalStrut(5));

      // Event arguments
      arguments = new ArgumentsPanel(300);
      arguments.setBorder(BorderFactory.createTitledBorder("Event arguments"));
    con.add(arguments);
    con.add(Box.createVerticalStrut(5));

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
    canButt.addActionListener(new cancelButtonListener());
    okButt.addActionListener(new applyButtonListener());
    myChangeActionListener chlis = new myChangeActionListener();
    //name.addActionListener(chlis);
    name.addKeyListener(new myKeyListener());
    arguments.addDoubleClickedListener( new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        EventArgument ea = (EventArgument)e.getSource();
        boolean modified = EventArgumentDialog.showDialog(fr,locationComponent,ea);
        if(modified) {
          arguments.updateRow(ea);
        }
      }
    });
    transitions.addDoubleClickedListener( new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        EventStateTransition est = (EventStateTransition)e.getSource();
        boolean modified = EventTransitionDialog.showDialog(fr,locationComponent,est);
        if(modified) {
          transitions.updateTransition(est);
        }
      }
    });
    this.localVariables.addDoubleClickedListener( new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        EventLocalVariable elv = (EventLocalVariable)e.getSource();
        boolean modified = LocalVariableDialog.showDialog(fr,locationComponent,elv);
        if(modified) {
          localVariables.updateRow(elv);
        }
      }
    });
  }

  private void sizeAndPosition()
  {
    pack();     // do this prior to next
    // little check to add some extra space to always include the node name in title bar w/out dotdotdots
    if(getWidth()<350)
      setSize(350,getHeight());
    this.setLocationRelativeTo(locationComponent);
  }

  public void setParams(Component c, EventNode en)
  {
    node=en;
    locationComponent = c;

    fillWidgets();
    sizeAndPosition();
  }

  private void fillWidgets()
  {
    setTitle("Event -- "+node.getName());
    name.setText(node.getName());
    transitions.setTransitions(node.getTransitions());
    arguments.setData(node.getArguments());
    localVariables.setData(node.getLocalVariables());
    modified = false;
// test    okButt.setEnabled(false);
    getRootPane().setDefaultButton(canButt);
  }

  private void unloadWidgets()
  {
    if(modified) {
      node.setName(name.getText());
      node.setTransitions(transitions.getTransitions());
      node.setArguments(arguments.getData());
      node.setLocalVariables(new Vector(localVariables.getData()));
    }
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
