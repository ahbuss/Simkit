
package simkit.viskit.model;

/**
 * Represents the information about one state variable. This
 * includes its name, type, initial value, and current value.
 *
 * @author DMcG
 */
public class vStateVariable extends ViskitElement
{
  /** Name of the state variable */
  private String variableName;
  
  /** The variable type. This can be a primitive or a class name. */
  private String variableType;
  
  /** Object that represents its current value */
  private Object currentValue;

  private String comment = "";
  public String toString()
  {
    return "("+variableType+") "+variableName;
  }
  /**
   * Constructor
   */
  vStateVariable(String pVariableName,         // package-accessible
                       String pVariableType)
  {
   variableName = pVariableName;
   variableType = pVariableType;
   currentValue = null;
  }

  public vStateVariable (String nm, String typ, String comment)
  {
    this(nm,typ);
    this.comment = comment;
  }
  /**
   * Returns the name of the state variable.
   *
   * @return name of state variable
   */
  public String getName()
  { return variableName;
  }
  
  /**
   * Sets the state variable name.
   *
   * @param pVariableName what the state variable name will become
   */
  public void setName(String pVariableName)
  { variableName = pVariableName;
  }
  
  /**
   * Returns a string representation of the type of the variable. This may
   * be a primitive type (int, double, float, etc) or an Object (String,
   * container, etc.).
   *
   * @return string represenatation of the type of the variable
   */
  public String getType()
  { return variableType;
  }
  
  /**
   * Sets the type of the state variable. There is no checking that the
   * type is valid; this will happily accept a class name string that
   * does not exist.
   *
   * @param pVariableType represenation of the type of the state variable
   */
  public void setType(String pVariableType)
  { variableType = pVariableType;
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

  public String getComment()
  {
    return comment;
  }
  public void setComment(String comment)
  {
    this.comment=comment;
  }
 }
