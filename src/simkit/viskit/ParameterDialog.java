package simkit.viskit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
 
public class ParameterDialog extends JDialog
{
  /** User picked the "Cancel" button on dialog */
  public static final int CANCEL_CHOSEN = 0;
  
  /** The user picked the "OK" button */
  public static final int OK_CHOSEN     = 1;
  
  /** Frame from which we are a dialog */
  private JFrame parentFrame;
  
  /** Text field that holds the parameter name */
  private JTextField parameterNameField;

  /** Text field that holds the expression */
  private JTextField expressionField;

  /** Text field that holds the comment */
  private JTextField commentField;

  /** Editable combo box that lets us select a type */
  private JComboBox  parameterTypeCombo;
  
  /** The button the user picked */
  private int buttonChosen = CANCEL_CHOSEN;
  
  /** Array of state variable and parameter names. Any new parameter names must not exist in this list. */
  private java.util.List existingNames;

  
  /**
   * constructor for the parameter dialog. We pass in the parent frame and
   * run in modal mode.<p>
   *
   * @param parentFrame frame that called this as a modal dialog
   * @param pExistingNames list of existing parameter and state variable names (in string format)
   * @param isParameter whether we're adding a state variable or parameter
   */
  public ParameterDialog(JFrame parentFrame, java.util.List pExistingNames)
  {
    super(parentFrame, "New Parameter", true);  // modal dialog with "new parameter" as title and tied to our parent frame
    
    JPanel buttonPanel;
    JPanel fieldsPanel;
    JButton okButton     = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");
    
    existingNames = pExistingNames;
    
    this.parentFrame = parentFrame;
    
    this.setSize(300, 200);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);
    
    okButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        // Check to make sure the data is valid. Alert panels are thrown
        // up by preflightData if not, and the user is prohibited from
        // selecting "OK".
        if(ParameterDialog.this.preflightData() == false)
        {
          return;
        }
        ParameterDialog.this.buttonChosen = OK_CHOSEN;
        ParameterDialog.this.dispose();
      }
    });
    
    // User hit the cancel button. Bail, no preflight of data (since
    // the user may be wedged and not know how to enter correct
    // data, and by saying cancel we're bailing on all the data anyway).
    
    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ParameterDialog.this.buttonChosen = CANCEL_CHOSEN;
        ParameterDialog.this.dispose();
      }
    });
    
    
    topPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new BoxLayout(fieldsPanel,BoxLayout.Y_AXIS));

    fieldsPanel.add(new JLabel("Parameter Name"));
    parameterNameField = new JTextField(15);
    fieldsPanel.add(parameterNameField);

    fieldsPanel.add(new JLabel("Expression"));
    expressionField = new JTextField(25);
    fieldsPanel.add(expressionField);

    fieldsPanel.add(new JLabel("Type"));
    parameterTypeCombo = new JComboBox();
    parameterTypeCombo.setEditable(true);
    parameterTypeCombo.addItem("int");
    parameterTypeCombo.addItem("float");
    parameterTypeCombo.addItem("double");
    parameterTypeCombo.addItem("short");
    parameterTypeCombo.addItem("java.lang.String");
    fieldsPanel.add(parameterTypeCombo);

    fieldsPanel.add(new JLabel("Comment"));
    commentField = new JTextField(25);
    fieldsPanel.add(commentField);

    topPanel.add(fieldsPanel, BorderLayout.CENTER);
    
    this.setContentPane(topPanel);
    pack(); 
    this.setLocationRelativeTo(parentFrame);

  }
  
  /**
   * Returns true if the data is valid, eg we have a valid parameter name
   * and a valid type.
   */
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
                                    "Parameter names must start with a lower case letter and conform to the Java variable naming conventions",
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
                                    "Parameter names must be unique and not match any existing parameter or state variable name",
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
  
  /**
   * Used by the caller after the modal dialog has finished. The caller runs
   * the dialog, the dialog has an "ok" or "cancel" button hit, and then we
   * can get the parameter name by querying this method.
   */
  public String getParameterName()
  {
    return parameterNameField.getText();
  }
  
  /**
   * Returns the parameter type specified by the user. You should first check that
   * the user has hit the "OK" button before retrieving this; that ensures the
   * data has been correctly preflighted to ensure the class or type really exists.
   */
  public String getParameterType()
  {
    return parameterTypeCombo.getSelectedItem().toString();
  }
  
  public int getButtonChosen()
  { return buttonChosen;
  }
  
}


