
package simkit.viskit;

import java.util.*;

/**
 * Represents the information about one state variable. This
 * includes its name, type, initial value, and current value.
 *
 * @author DMcG
 */
public class StateVariable extends Object
{
  /** Name of the state variable */
  private String variableName;
  
  /** The variable type. This can be a primitive or a class name. */
  private String variableType;
  
  /** Object that represents its initial value. */
  private Object initialValue;
  
  /** Object that represents its current value */
  private Object currentValue;
  
  
  /**
   * Constructor
   */
  public StateVariable(String pVariableName,
                       String pVariableType,
                       Object pInitialValue)
 {
   variableName = pVariableName;
   variableType = pVariableType;
   initialValue = pInitialValue;
   currentValue = null;
 }

  /**
   * Returns the name of the state variable.
   *
   * @return name of state variable
   */
  public String getVariableName()
  { return variableName;
  }
  
  /**
   * Sets the state variable name.
   *
   * @param pVariableName what the state variable name will become
   */
  public void setVariableName(String pVariableName)
  { variableName = pVariableName;
  }
  
  /**
   * Returns a string representation of the type of the variable. This may
   * be a primitive type (int, double, float, etc) or an Object (String,
   * container, etc.).
   *
   * @return string represenatation of the type of the variable
   */
  public String getVariableType()
  { return variableType;
  }
  
  /**
   * Sets the type of the state variable. There is no checking that the
   * type is valid; this will happily accept a class name string that
   * does not exist.
   *
   * @param string represenation of the type of the state variable
   */
  public void setVariableType(String pVariableType)
  { variableType = pVariableType;
  }
  
  /**
   * Returns an object that represents the initial value of the state
   * variable. If this is a primitive type, you should retrieve the
   * primitive value from the object yourself.
   *
   * @return Object representing the intial state variable value
   */
  public Object getInitialValue()
  { return initialValue;
  }
  
  /**
   * Set the inital value via an object. If the type is a primitive
   * other code must retrieve the primitive value from the object.
   *
   * @param pInitialValue the object representatation of the initial value
   */
  public void setInitialValue(Object pInitialValue)
  { initialValue = pInitialValue;
  }
  
  /**
   * Returns an object that represents the current value of the object.
   */
  public Object getCurrentValue()
  { return currentValue;
  }
  
  /**
   * Sets the current value of the state variable
   */
  public void setCurrentValue(Object pCurrentValue)
  { currentValue = pCurrentValue;
  }
  
 }
