package simkit.viskit;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * An implementation of the TableModel interface for the state variables.
 * This feeds the state variables table with assorted information on
 * how many columns, how to fill in data, etc.
 *
 * @author DMcG
 */
 
 public class StateVariableTableModel extends AbstractTableModel
 {
   /** Number of columns in the table */
   private static final int STATE_VARIABLE_COLUMNS = 4;
   
   private static final int NAME_COLUMN          = 0;
   private static final int TYPE_COLUMN          = 1;
   private static final int INITIAL_VALUE_COLUMN = 2;
   private static final int CURRENT_VALUE_COLUMN = 3;

   
   /** Vector that holds data to be displayed */
   private Vector tableData;

   /**
    * Constructor that adds some bogus data for testing purposes
    */
   public StateVariableTableModel()
   {
     tableData = new Vector();
     
     // Bogus data for testing and mockups
     //tableData.add(new StateVariable("servers", "int", new Integer(2)));
     //tableData.add(new StateVariable("waitrons", "int", new Integer(3)));
     //tableData.add(new StateVariable("waiteresses", "int", new Integer(4)));
     //tableData.add(new StateVariable("waiters", "int", new Integer(5)));
     
   }

   
   /**
    * Number of columns in the table
    */
   public int getColumnCount()
   {
     return STATE_VARIABLE_COLUMNS;
   }
   
   public String getColumnName(int column)
   {
     switch(column)
     {
       case NAME_COLUMN: return "Name";
       case TYPE_COLUMN: return "Type";
       case INITIAL_VALUE_COLUMN: return "Initial Value";
       case CURRENT_VALUE_COLUMN: return "Current Value";
       default: return "Unknown";
     }
   }
   
   /**
    * Returns the number of rows in the table
    */
   public int getRowCount()
   { return tableData.size();
   }
   
   /**
    * Returns false (signifying that the cell cannot be edited)
    * if the cell should not be edited.
    * In this case the current value column is not
    * editable.
    */
   public boolean isCellEditable(int row, int col)
   {
     if(//(col == NAME_COLUMN) ||
        (col == CURRENT_VALUE_COLUMN))
        return false;

     return true;
  }

  /**
   * Returns the value at a given row and column, in Object format.
   */
  public Object getValueAt(int row, int column)
  {
    StateVariable stateVariable = (StateVariable)tableData.elementAt(row);
    
    if(column == NAME_COLUMN) return stateVariable.getVariableName();
    if(column == TYPE_COLUMN) return stateVariable.getVariableType();
    if(column == INITIAL_VALUE_COLUMN) return stateVariable.getInitialValue();
    if(column == CURRENT_VALUE_COLUMN) return stateVariable.getCurrentValue();
    
    return "Unknown";
  }

  /**
   * Sets the value (in Object format) at a given row and column in the table.
   */
  public void setValueAt(Object value, int row, int column)
  {
    StateVariable stateVariable = (StateVariable)tableData.elementAt(row);

    if(column == NAME_COLUMN) stateVariable.setVariableName((String)value);
    if(column == TYPE_COLUMN) stateVariable.setVariableType((String)value);
    if(column == INITIAL_VALUE_COLUMN) stateVariable.setInitialValue(value);
    if(column == CURRENT_VALUE_COLUMN) stateVariable.setCurrentValue(value);

    fireTableCellUpdated(row, column);
  }
  
  /**
   * Add a state variable to the table model. Redraws the table with
   * the new data.
   */
  public void addStateVariable(StateVariable stateVariable)
  {
    tableData.add(stateVariable);
    this.fireTableDataChanged();
  }

  
    /**
   * Returns an array of all the state variable names.
   */
  public java.util.List getNames()
  {
    List names = new Vector();

    for(int idx = 0; idx < tableData.size(); idx++)
    {
      names.add(((StateVariable)tableData.elementAt(idx)).getVariableName());
    }

    return names;
  }
    
} // end StateVariableTableModel
   
   
