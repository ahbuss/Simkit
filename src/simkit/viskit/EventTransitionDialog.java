package simkit.viskit;

import simkit.viskit.model.EventStateTransition;
import simkit.viskit.model.vStateVariable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A dialog class that lets the user add a new parameter to the document.
 * After the user clicks "OK", "Cancel", or closes the dialog via the
 * close box, the caller retrieves the "buttonChosen" variable from
 * the object to determine the choice. If the user clicked "OK", the
 * caller can retrieve various choices from the object.
 *
 * @author DMcG, Mike Bailey
 */

public class EventTransitionDialog extends JDialog
{
  private JTextField nameField;    // Text field that holds the parameter name
  private JTextField actionField;
  private JTextField commentField;          // Text field that holds the comment
  private JComboBox  parameterTypeCombo;    // Editable combo box that lets us select a type
  private JComboBox  testVars;
  private JRadioButton assTo,opOn;

  private static EventTransitionDialog dialog;
  private static boolean modified = false;
  private EventStateTransition param;
  private Component locationComp;
  private JButton okButt, canButt;

  public static String newName, newType, newComment;

  public static boolean showDialog(JFrame f, Component comp, EventStateTransition parm)
  {
    if(dialog == null)
      dialog = new EventTransitionDialog(f,comp,parm);
    else
      dialog.setParams(comp,parm);

    dialog.setVisible(true);
      // above call blocks
    return modified;
  }

  private EventTransitionDialog(JFrame parent, Component comp, EventStateTransition param)
  {
    super(parent, "State Transition", true);
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

        JLabel nameLab = new JLabel("state variable");
        //JLabel initLab = new JLabel("initial value");
        assTo = new JRadioButton("assign to (\"=\")");
        opOn  = new JRadioButton("invoke on (\".\")");
        ButtonGroup bg = new ButtonGroup();
        bg.add(assTo);bg.add(opOn);

        JLabel actionLab = new JLabel("action");
        JLabel commLab = new JLabel("state var. comment");
        int w = maxWidth(new JComponent[]{nameLab,assTo,opOn,actionLab,commLab});

        nameField = new JTextField(15);   setMaxHeight(nameField);

 testVars = new JComboBox(VGlobals.instance().getStateVarsCBModel());
        setMaxHeight(testVars);
    testVars.setBackground(Color.white);

        commentField       = new JTextField(25);   setMaxHeight(commentField);
          commentField.setEditable(false);
        actionField        = new JTextField(25);   setMaxHeight(actionField);

        fieldsPanel.add(new OneLinePanel(actionLab,w,actionField));
        fieldsPanel.add(new OneLinePanel(null,w,assTo));
        fieldsPanel.add(new OneLinePanel(null,w,opOn));
        //fieldsPanel.add(new OneLinePanel(nameLab,w,nameField));
   fieldsPanel.add(new OneLinePanel(nameLab,w,testVars));
        fieldsPanel.add(new OneLinePanel(commLab,w,commentField));

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
    this.nameField.addCaretListener(lis);
    actionField.addCaretListener(lis);
    this.commentField.      addCaretListener(lis);
    //this.parameterTypeCombo.addActionListener(lis);
    testVars.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JComboBox cb = (JComboBox)e.getSource();
        vStateVariable sv = (vStateVariable)cb.getSelectedItem();
        commentField.setText(sv.getComment());
        okButt.setEnabled(true);
        modified=true;
      }
    });
    // to start off:
    commentField.setText(((vStateVariable)testVars.getItemAt(0)).getComment());

    RadButtListener rbl = new RadButtListener();
    this.assTo.addActionListener(rbl);
    this.opOn.addActionListener(rbl);
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
  public void setParams(Component c, EventStateTransition p)
  {
    param = p;
    locationComp = c;

    fillWidgets();

    modified        = (p==null?true:false);
    okButt.setEnabled((p==null?true:false));

    getRootPane().setDefaultButton(canButt);

    this.setLocationRelativeTo(c);
  }

  private void fillWidgets()
  {
    if(param != null) {
      //nameField.setText(param.getStateVarName());//getName());
      actionField.setText(param.getOperationOrAssignment());

      opOn.setSelected(param.isOperation());
      assTo.setSelected(!param.isOperation());

      setVarNameComboBox(param);
      if(param.getComments().size() > 0)
        commentField.setText((String)param.getComments().get(0));
      else
        commentField.setText("");
    }
    else {
      nameField.setText("param name");
      actionField.setText("action here");
      testVars.setSelectedIndex(0);
      commentField.setText("comments here");
      assTo.setSelected(true);
    }
  }

  private void setVarNameComboBox(EventStateTransition est)
  {
    for(int i = 0; i< testVars.getItemCount();i++) {
      vStateVariable sv = (vStateVariable)testVars.getItemAt(i);
      if(est.getStateVarName().equalsIgnoreCase(sv.getName())) {
        testVars.setSelectedIndex(i);
        return;
      }
    }
    if(!est.getStateVarName().equals(""))     // for first time
      JOptionPane.showMessageDialog(this,"State variable "+est.getStateVarName() +"not found.",
                                         "alert", JOptionPane.ERROR_MESSAGE);
    testVars.setSelectedIndex(0);
  }
  private void unloadWidgets()
  {
    if(param != null) {
      param.setStateVarName(((vStateVariable)testVars.getSelectedItem()).getName()); //nameField.getText().trim());
      param.setOperationOrAssignment(actionField.getText().trim());
      param.setOperation(opOn.isSelected());
      param.getComments().clear();
      String cs = commentField.getText().trim();
      if(cs.length() > 0)
        param.getComments().add(0,cs);
    }
    else {
      newName    = nameField.getText().trim();
      // todo complete this
      newComment = commentField.getText().trim();
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
  class RadButtListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      modified = true;
      okButt.setEnabled(true);
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
      if(lab == null) lab = new JLabel("");
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
    String parameterName =  nameField.getText();
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


