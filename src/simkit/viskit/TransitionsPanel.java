package simkit.viskit;

import bsh.Interpreter;
import bsh.EvalError;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Mar 15, 2004
 * Time: 2:35:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransitionsPanel extends JPanel implements ActionListener
{
  JTextArea jta;
  JList lis;
  JButton testButt,minusButt,plusButt;
  myListModel model;
  public TransitionsPanel()
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    //jta = new JTextArea(5,1);
    //JScrollPane jsp = new JScrollPane(jta);
    //add(jsp);
    model = new myListModel();
    model.addElement("1");model.addElement("2"); model.addElement("3");
    lis = new JList(model);
    JScrollPane jsp = new JScrollPane(lis);
    add(jsp);
    add(Box.createVerticalStrut(5));

    JPanel buttPan = new JPanel();
    buttPan.setLayout(new BoxLayout(buttPan, BoxLayout.X_AXIS));
    buttPan.add(Box.createHorizontalGlue());
    plusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/plus.png")));
    Dimension d = plusButt.getPreferredSize();

    plusButt.setFont(plusButt.getFont().deriveFont(plusButt.getFont().getSize2D() + 5f));
    plusButt.setPreferredSize(d);plusButt.setMinimumSize(d);plusButt.setMaximumSize(d);
    plusButt.setActionCommand("p");
    buttPan.add(plusButt);
    minusButt = new JButton(new ImageIcon(ClassLoader.getSystemResource("simkit/viskit/images/minus.png")));
    minusButt.setFont(plusButt.getFont()); //.deriveFont(plusButt.getFont().getSize2D()+5f));
    minusButt.setPreferredSize(d);minusButt.setMinimumSize(d);minusButt.setMaximumSize(d);

    minusButt.setActionCommand("m");
    minusButt.setEnabled(false);
    buttPan.add(minusButt);
    buttPan.add(Box.createHorizontalGlue());
    add(buttPan);

    add(Box.createVerticalStrut(5));

    JPanel bp = new JPanel();
    bp.setLayout(new BoxLayout(bp,BoxLayout.X_AXIS));
    bp.add(Box.createHorizontalGlue());

    testButt = new JButton("test");
    bp.add(testButt);
    bp.add(Box.createHorizontalGlue());

    add(bp);

    testButt.addActionListener(this);
    plusButt.addActionListener(new plusButtonListener());
    minusButt.addActionListener(new minusButtonListener());
    lis.addListSelectionListener(new myListSelectionListener());
  }
  public void setString(String s)
  {
    model.removeAllElements();
    String[] elems = s.split(";");
    for(int i=0;i<elems.length;i++) {
      model.addElement(elems[i]);
    }
  }
  public String getString()
  {
    String s = "";
    for(Enumeration en = model.elements(); en.hasMoreElements();) {
      s += (String)en.nextElement();
      if(s.charAt(s.length()-1) != ';')
        s += ";";
    }
    return s;
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
      model.addElement("blah");
      minusButt.setEnabled(true);
      testButt.setEnabled(true);
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
        for(int i=0;i<sel.length;i++)
          model.remove(sel[i]-i);
      if(lis.getModel().getSize() <= 0) {
        minusButt.setEnabled(false);
        testButt.setEnabled(false);
      }
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
        testButt.setEnabled(b);
      }
    }
  }
}
