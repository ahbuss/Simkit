package simkit.viskit;

import simkit.viskit.model.Edge;
import simkit.viskit.model.vParameter;
import simkit.viskit.model.vStateVariable;
import simkit.viskit.model.vEdgeParameter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.border.EtchedBorder;
import java.util.regex.*;

/**
 * A dialog class that lets the user add a new parameter to the document.
 * After the user clicks "OK", "Cancel", or closes the dialog via the
 * close box, the caller retrieves the "buttonChosen" variable from
 * the object to determine the choice. If the user clicked "OK", the
 * caller can retrieve various choices from the object.
 *
 * @author DMcG
 */

public class EdgeParameterDialogOld extends JDialog
{
  private JTextField valueField;       // Text field that holds the expression
  //private JTextField commentField;          // Text field that holds the comment
  private JComboBox  parameterTypeCombo;    // Editable combo box that lets us select a type

  private static EdgeParameterDialogOld dialog;
  private static boolean modified = false;
  private vEdgeParameter param;
  private Component locationComp;
  private JButton okButt, canButt;

  public static String newValue, newType; //, newComment;

  public static boolean showDialog(JFrame f, Component comp, vEdgeParameter parm)
  {
    if(dialog == null)
      dialog = new EdgeParameterDialogOld(f,comp,parm);
    else
      dialog.setParams(comp,parm);

    dialog.setVisible(true);
      // above call blocks
    return modified;
  }

  private EdgeParameterDialogOld(JFrame parent, Component comp, vEdgeParameter param)
  {
    super(parent, "Edge Parameter", true);
    this.param = param;
    this.locationComp = comp;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    Container cont = getContentPane();
    cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));

     JPanel con = new JPanel();
     con.setLayout(new BoxLayout(con,BoxLayout.Y_AXIS));
     con.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

      con.add(Box.createVerticalStrut(5));
      JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel,BoxLayout.Y_AXIS));

        JLabel valueLab = new JLabel("value");
        JLabel typeLab = new JLabel("type");
        //JLabel commLab = new JLabel("comment");
        int w = maxWidth(new JComponent[]{valueLab,typeLab}); //commLab});

        valueField         = new JTextField(25);   setMaxHeight(valueField);
       // commentField       = new JTextField(25);   setMaxHeight(commentField);
        parameterTypeCombo = new JComboBox(VGlobals.instance().getTypeCBModel());
                                               setMaxHeight(parameterTypeCombo);

        parameterTypeCombo.setEditable(true);

        fieldsPanel.add(new OneLinePanel(valueLab,w,valueField));
        fieldsPanel.add(new OneLinePanel(typeLab,w,parameterTypeCombo));
        //fieldsPanel.add(new OneLinePanel(commLab,w,commentField));
       con.add(fieldsPanel);
       con.add(Box.createVerticalStrut(5));

       JPanel buttPan = new JPanel();
        buttPan.setLayout(new BoxLayout(buttPan,BoxLayout.X_AXIS));
        canButt = new JButton("Cancel");
        okButt = new JButton("Apply changes");
        buttPan.add(Box.createHorizontalGlue());     // takes up space when dialog is expanded horizontally
        buttPan.add(canButt);
        buttPan.add(okButt);
       con.add(buttPan);
       con.add(Box.createVerticalGlue());    // takes up space when dialog is expanded vertically
      cont.add(con);

    fillWidgets();     // put the data into the widgets

    modified        = (param==null?true:false);     // if it's a new param, they can always accept defaults with no typing
    okButt.setEnabled((param==null?true:false));

    getRootPane().setDefaultButton(canButt);

    pack();     // do this prior to next
    this.setLocationRelativeTo(locationComp);

    // attach listeners
    canButt.addActionListener(new cancelButtonListener());
    okButt .addActionListener(new applyButtonListener());

    enableApplyButtonListener lis = new enableApplyButtonListener();
    //this.commentField.      addCaretListener(lis);
    this.valueField.   addCaretListener(lis);
    this.parameterTypeCombo.addActionListener(lis);
  }

  private int maxWidth(JComponent[] c)
  {
    int tmpw=0,maxw=0;
    for(int j=0; j<c.length; j++) {
      tmpw = c[j].getPreferredSize().width;
      if(tmpw > maxw)
        maxw = tmpw;
    }
    return maxw;
  }
  private void setMaxHeight(JComponent c)
  {
    Dimension d = c.getPreferredSize();
    d.width = Integer.MAX_VALUE;
    c.setMaximumSize(d);
  }
  public void setParams(Component c, vEdgeParameter p)
  {
    param = p;
    locationComp = c;

    fillWidgets();

    modified        = (p==null?true:false);
    okButt.setEnabled((p==null?true:false));

    getRootPane().setDefaultButton(canButt);

    this.setLocationRelativeTo(c);
  }
  private void setType(vEdgeParameter p)
  {
    String nm = "noType" ; //p.getType();
    ComboBoxModel mod = parameterTypeCombo.getModel();
    for(int i=0;i<mod.getSize(); i++) {
      if(nm.equals(mod.getElementAt(0))) {
        parameterTypeCombo.setSelectedIndex(i);
        return;
      }
    }
    VGlobals.instance().addType(nm);
    mod = VGlobals.instance().getTypeCBModel();
    parameterTypeCombo.setModel(mod);
    parameterTypeCombo.setSelectedIndex(mod.getSize()-1);
  }

  private void fillWidgets()
  {
    if(param != null) {
      valueField.setText(param.getValue());
      setType(param);
      //this.commentField.setText(param.getComment());
    }
    else {
      valueField.setText("param name");
      //commentField.setText("comments here");
    }
  }

  private void unloadWidgets()
  {
    if(param != null) {
      param.setValue(valueField.getText().trim());
      //param.setType(((String)(this.parameterTypeCombo.getSelectedItem())).trim());
      //param.setComment(this.commentField.getText());
    }
    else {
      newValue = valueField.getText().trim();
      newType = ((String)(this.parameterTypeCombo.getSelectedItem())).trim();
      //newComment = commentField.getText().trim();
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

  class enableApplyButtonListener implements CaretListener,ActionListener
  {
    public void caretUpdate(CaretEvent event)
    {
      modified = true;
      okButt.setEnabled(true);
      getRootPane().setDefaultButton(okButt);
    }

    public void actionPerformed(ActionEvent event)
    {
      caretUpdate(null);
    }
  }

  class OneLinePanel extends JPanel
  {
    OneLinePanel(JLabel lab, int w, JComponent comp)
    {
      setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
      add(Box.createHorizontalStrut(5));
      add(Box.createHorizontalStrut(w-lab.getPreferredSize().width));
      add(lab);
      add(Box.createHorizontalStrut(5));
      add(comp);

      Dimension d = getPreferredSize();
      d.width = Integer.MAX_VALUE;
      setMaximumSize(d);
    }
  }
  /**
   * Returns true if the data is valid, eg we have a valid parameter name
   * and a valid type.
   */
/*
jmb..this is good regex checking.  put in a single utility class
  private boolean preflightData()
  {
    String parameterName =  parameterNameField.getText();
    String javaVariableNameRegExp;

    // Do a REGEXP to confirm that the variable name fits the criteria for
    // a Java variable. We don't want to allow something like "2f", which
    // Java will misinterpret as a number literal rather than a variable. This regexp
    // is slightly more restrictive in that it demands that the variable name
    // start with a lower case letter (which is not demanded by Java but is
    // a strong convention) and disallows the underscore. "^" means it
    // has to start with a lower case letter in the leftmost position.

    javaVariableNameRegExp = "^[a-z][a-zA-Z0-9]*$";
    if(!Pattern.matches(javaVariableNameRegExp, parameterName))
    {
      JOptionPane.showMessageDialog(this,
                                    "vParameter names must start with a lower case letter and conform to the Java variable naming conventions",
                                    "alert",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }

    // Check to make sure the name the user specified isn't already used by a state variable
    // or parameter.

    for(int idx = 0; idx < existingNames.size(); idx++)
    {
      if(parameterName.equals(existingNames.get(idx)))
      {
        JOptionPane.showMessageDialog(null,
                                    "vParameter names must be unique and not match any existing parameter or state variable name",
                                    "alert",
                                    JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }



    // Check to make sure the class or type exists
    if(!ClassUtility.classExists(parameterTypeCombo.getSelectedItem().toString()))
    {
      JOptionPane.showMessageDialog(null,
                                    "The class name " + parameterTypeCombo.getSelectedItem().toString() + "  does not exist on the classpath",
                                    "alert",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }

    return true;
  }

*/
}


