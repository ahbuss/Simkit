package simkit.viskit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A dialog class that lets the user add a new parameter to the document.
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
  
  /** Editable combo box that lets us select a type */
  private JComboBox  parameterTypeCombo;
  
  /** The button the user picked */
  private int buttonChosen = CANCEL_CHOSEN;
  

  
  
  public ParameterDialog(JFrame parentFrame)
  {
    super(parentFrame, "New Parameter", true);  // modal dialog with "new parameter" as title and tied to our parent frame
    
    JPanel buttonPanel;
    JPanel fieldsPanel;
    JButton okButton = new JButton("OK");
    
    this.parentFrame = parentFrame;
    
    this.setSize(300, 200);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(okButton);
    buttonPanel.add(new JButton("Cancel"));
    
    okButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ParameterDialog.this.preflightData();
        ParameterDialog.this.buttonChosen = OK_CHOSEN;
      }
    });
    
    
    topPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    fieldsPanel = new JPanel();
    fieldsPanel.add(new JLabel("Parameter Name"));
    parameterNameField = new JTextField(15);
    fieldsPanel.add(parameterNameField);
    fieldsPanel.add(new JLabel("Type"));
    
    parameterTypeCombo = new JComboBox();
    parameterTypeCombo.setEditable(true);
    parameterTypeCombo.addItem("int");
    parameterTypeCombo.addItem("float");
    parameterTypeCombo.addItem("double");
    parameterTypeCombo.addItem("short");
    parameterTypeCombo.addItem("java.lang.String");
    
    fieldsPanel.add(parameterTypeCombo);
    
    topPanel.add(fieldsPanel, BorderLayout.CENTER);
    
    this.setContentPane(topPanel);
    
  }
  
  /**
   * Returns true if the data is valid, eg we have a valid parameter name
   * and a valid type.
   */
  private boolean preflightData()
  {
    String parameterName =  parameterNameField.getText();
    
    System.out.println("Got parameter name of " +  parameterNameField.getText());
    
    System.out.println("Got type of " + parameterTypeCombo.getSelectedItem().toString());
    
    if(parameterName.indexOf(" ") != -1)
    {
      System.out.println("Parameter name has a space in it.");
      return false;
    }
    
    // Check to make sure the class or type exists
    if(!ClassUtility.classExists(parameterTypeCombo.getSelectedItem().toString()))
    {
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
  
}


