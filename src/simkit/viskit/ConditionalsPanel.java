package simkit.viskit;

import bsh.Interpreter;
import bsh.EvalError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;

/**
 * OPNAV N81 - NPS World Class Modeling (WCM) 2004 Projects
 * MOVES Institute
 * Naval Postgraduate School, Monterey CA
 * www.nps.navy.mil
 * By:   Mike Bailey
 * Date: Mar 15, 2004
 * Time: 11:53:36 AM
 */

public class ConditionalsPanel extends JPanel implements ActionListener
{
  JTextArea jta;
  Edge edge;
  public ConditionalsPanel(Edge edge)
  {
    this.edge = edge;
    setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    //setBorder(BorderFactory.createTitledBorder("Boolean expression"));
    JPanel tp = new JPanel();
    tp.setLayout(new BoxLayout(tp,BoxLayout.X_AXIS));
    tp.add(Box.createHorizontalStrut(5));
    JLabel leftParen = new JLabel("if (");
    leftParen.setFont(leftParen.getFont().deriveFont(Font.ITALIC | Font.BOLD));
    leftParen.setAlignmentX(Box.LEFT_ALIGNMENT);
    tp.add(leftParen);
    tp.add(Box.createHorizontalGlue());
    add(tp);

    jta = new JTextArea(8,25);
    jta.setText(edge.conditional);
    jta.setEditable(true);
    JScrollPane jsp = new JScrollPane(jta);
    add(jsp);

    JPanel bp = new JPanel();
    bp.setLayout(new BoxLayout(bp,BoxLayout.X_AXIS));
    bp.add(Box.createHorizontalStrut(10));
    JLabel rightParen = new JLabel(") then schedule/cancel target event");
    rightParen.setFont(leftParen.getFont().deriveFont(Font.ITALIC | Font.BOLD));
    rightParen.setAlignmentX(Box.LEFT_ALIGNMENT);
    bp.add(rightParen);
    bp.add(Box.createHorizontalGlue());
    add(bp);

    add(Box.createVerticalStrut(5));

    JPanel buttPan = new JPanel();
    buttPan.setLayout(new BoxLayout(buttPan,BoxLayout.X_AXIS));
    buttPan.add(Box.createHorizontalGlue());
    JButton testButt = new JButton("test expression");
    buttPan.add(testButt);
    buttPan.add(Box.createHorizontalGlue());
    add(buttPan);
    add(Box.createVerticalStrut(5));

    JTextArea jtaComments = new JTextArea(2,25);
    jsp = new JScrollPane(jtaComments);
    jsp.setBorder(BorderFactory.createTitledBorder("Source comments"));
    add(jsp);

    testButt.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event)
  {
    Object o;
    try {
      Interpreter interpreter = new Interpreter();
      interpreter.setStrictJava(true);       // no loose typeing

      // set globals;
      for (Iterator itr = edge.parameters.iterator(); itr.hasNext();) {
        EdgeParameter ep = (EdgeParameter) itr.next();
        interpreter.set(ep.name, ep.type);
      }
 System.out.println(jta.getText());
      String noCRs = jta.getText().replace('\n',' ');
      noCRs = "if("+noCRs+")System.out.println();";
 System.out.println(noCRs);
      o = interpreter.eval(noCRs);
      //interpreter.eval("if(" + noCRs + ")System.out.println(\"ok\")");
    }

    catch (EvalError evalError) {
      String s = ""; //"Error in line " + evalError.getErrorLineNumber() + ":  ";
      //s += evalError.getErrorText() + "\n";
      s += evalError.getMessage();
      JOptionPane.showMessageDialog(ConditionalsPanel.this, s, "Java Error", JOptionPane.ERROR_MESSAGE);
    }
  }

}
