package simkit.viskit;

import bsh.Interpreter;
import bsh.EvalError;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ArrayList;

import simkit.viskit.model.EventStateTransition;
import simkit.viskit.model.EventArgument;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 8, 2004
 * Time: 2:56:21 PM
 */

public class TransitionsPanel extends JPanel implements ActionListener
{
  JTextArea jta;
  JList lis;
  JButton testButt,minusButt,plusButt;
  myListModel model;
  JButton edButt;

  public TransitionsPanel()
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
    p.add(Box.createHorizontalGlue());
    JLabel instructions = new JLabel("Double click a row to ");//edit.");
    int bigSz = getFont().getSize();
    instructions.setFont(getFont().deriveFont(Font.ITALIC,(float)(bigSz-2)));
    p.add(instructions);
    edButt = new JButton("edit.");
      edButt.setFont(getFont().deriveFont(Font.ITALIC,(float)(bigSz-2)));
      edButt.setBorder(null);
      edButt.setEnabled(false);
      p.add(edButt);
    p.add(Box.createHorizontalGlue());
    add(p);

    //jta = new JTextArea(5,1);
    //JScrollPane jsp = new JScrollPane(jta);
    //add(jsp);
    model = new myListModel();
    model.addElement("1");model.addElement("2"); model.addElement("3");
    lis = new JList(model);
    lis.setVisibleRowCount(3);

    JScrollPane jsp = new JScrollPane(lis);
    Dimension dd = jsp.getPreferredSize();
    dd.width = Integer.MAX_VALUE;
    jsp.setMinimumSize(dd);
    add(jsp);

    add(Box.createVerticalStrut(5));

    JPanel buttPan = new JPanel();
    buttPan.setLayout(new BoxLayout(buttPan, BoxLayout.X_AXIS));
    buttPan.add(Box.createHorizontalGlue());
    plusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/plus.png")));
    plusButt.setBorder(null);
    plusButt.setText(null);
    Dimension d = plusButt.getPreferredSize();
    //plusButt.setFont(plusButt.getFont().deriveFont(plusButt.getFont().getSize2D() + 5f));
    plusButt.setMinimumSize(d);plusButt.setMaximumSize(d);
    buttPan.add(plusButt);
    minusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/minus.png")));
    minusButt.setDisabledIcon(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/minusGrey.png")));
    d = plusButt.getPreferredSize();
    //minusButt.setFont(plusButt.getFont()); //.deriveFont(plusButt.getFont().getSize2D()+5f));
    minusButt.setMinimumSize(d);minusButt.setMaximumSize(d);
    minusButt.setBorder(null);
    minusButt.setText(null);
    minusButt.setActionCommand("m");
    minusButt.setEnabled(false);
    buttPan.add(minusButt);
    buttPan.add(Box.createHorizontalGlue());
    add(buttPan);

    add(Box.createVerticalStrut(5));

    dd = this.getPreferredSize();
    this.setMinimumSize(dd);
/*   do test on "apply changes"
    JPanel bp = new JPanel();
    bp.setLayout(new BoxLayout(bp,BoxLayout.X_AXIS));
    bp.add(Box.createHorizontalGlue());

    testButt = new JButton("test");
    bp.add(testButt);
    bp.add(Box.createHorizontalGlue());

    add(bp);
    testButt.addActionListener(this);
*/
    plusButt.addActionListener(new plusButtonListener());
    minusButt.addActionListener(new minusButtonListener());
    lis.addListSelectionListener(new myListSelectionListener());
    lis.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent event)
      {
          if(event.getClickCount() == 2)
            if(myMouseLis != null) {
            int idx = lis.getSelectedIndex();
              EventStateTransition est = (EventStateTransition)arLis.get(idx);
              //EventStateTransition est = (EventStateTransition)model.get(lis.getSelectedIndex()); //.getSelectedRow());
              event.setSource(est);
              myMouseLis.mouseClicked(event);
            }
      }
    });
  }
  ArrayList arLis = new ArrayList(); //todo combine with list model
  public void setTransitions(java.util.List tLis)
  {
    clearTransitions();
    for(Iterator itr = tLis.iterator(); itr.hasNext();) {
      addTransition((EventStateTransition)itr.next());
    }
  }
  public ArrayList getTransitions()
  {
    return (ArrayList)arLis.clone();
  }

  public void addTransition(EventStateTransition est)
  {
    model.addElement(transitionString(est));
    arLis.add(est);
  }
  private String transitionString(EventStateTransition est)
  {
    String tstring = "";
    if(est.isOperation()) {
      //tstring += "(" + est.getStateVarType() +") ";
      tstring += est.getStateVarName();
      tstring += "." + est.getOperationOrAssignment() + "\n";
    }
    else {
      //tstring += "(" + est.getStateVarType() + ") ";
      tstring += est.getStateVarName() + " = ";
      tstring += est.getOperationOrAssignment() + "\n";
    }
    return tstring;
  }
  public void clearTransitions()
  {
    model.removeAllElements();
    arLis.clear();
  }
  public void xsetString(String s)
  {
    model.removeAllElements();
    if(s.length() <= 0) return;   // this is important.  Don't put an empty string on a list and expect normal behavior
    String[] elems = s.split("\n");
    for(int i=0;i<elems.length;i++) {
      model.addElement(elems[i]);
    }
    //lis.setVisibleRowCount(Math.max(elems.length,3));
  }
  public String getString()
  {
    String s = "";
    for(Enumeration en = model.elements(); en.hasMoreElements();) {
      s += (String)en.nextElement();
/*
      if(s.charAt(s.length()-1) != ';')
        s += ";";
*/
      s += "\n";
    }
    return s.substring(0,s.length()-1);     // lose last cr
  }
  public void actionPerformed(ActionEvent event)
  {
    try {
      Interpreter interpreter= new Interpreter();
      interpreter.setStrictJava(true);       // no loose typeing
      // set globals;
      interpreter.set("simParam1",5);
      interpreter.set("simParam2","yellow");

      //interpreter.eval(jta.getText());
      String s = "";
      for(int i=0;i<lis.getModel().getSize();i++) {
        s += lis.getModel().getElementAt(i);
        s += ";\n";
      }
      interpreter.eval(s);
    }
    catch (EvalError evalError) {
      String s = ""; //"Error in line "+evalError.getErrorLineNumber()+":  ";
      //s += evalError.getErrorText() + "\n";
      s += evalError.getMessage();
      JOptionPane.showMessageDialog(TransitionsPanel.this,s,"Java Error",JOptionPane.ERROR_MESSAGE);
      return;
    }

  }
  public void addDoubleClickedListener(MouseListener ml)
  {
    myMouseLis = ml;
  }
  private MouseListener myMouseLis;

  public void updateTransition(EventStateTransition est)
  {
    int idx = arLis.indexOf(est);
    model.setElementAt(transitionString(est),idx);
  }


  class myListModel extends DefaultListModel
  {
    myListModel()
    {
      super();
    }
  }
  class plusButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      model.addElement("todo: transition statement");
      lis.setVisibleRowCount(Math.max(model.getSize(),3));
      lis.ensureIndexIsVisible(model.getSize()-1);
      TransitionsPanel.this.invalidate();
      minusButt.setEnabled(true);
      arLis.add(new EventStateTransition());
      //testButt.setEnabled(true);
    }
  }
  class minusButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if(lis.getSelectionModel().getValueIsAdjusting())
        return;
      int[] sel = lis.getSelectedIndices();
      if(sel.length != 0)
        for(int i=0;i<sel.length;i++) {
          model.remove(sel[i]-i);
          arLis.remove(sel[i]-i);
        }
      if(lis.getModel().getSize() <= 0) {
        minusButt.setEnabled(false);
        //testButt.setEnabled(false);
      }
      lis.setVisibleRowCount(model.getSize());
      TransitionsPanel.this.invalidate();
    }
  }
  class myListSelectionListener implements ListSelectionListener
  {
    public void valueChanged(ListSelectionEvent event)
    {
      if(event.getValueIsAdjusting())
        return;
      if(lis.getModel().getSize() != 0) {
        boolean b = lis.getSelectedValue() != null;
        minusButt.setEnabled(b);
        //testButt.setEnabled(b);
      }
    }
  }

  public boolean testJavaCode()
  {
    actionPerformed(null);
    return true;            // todo putup a JOptionPane w/ ok/nogo etc.
  }
}
