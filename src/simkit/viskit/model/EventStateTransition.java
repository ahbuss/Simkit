package simkit.viskit.model;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Apr 1, 2004
 * Time: 4:00:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventStateTransition extends ViskitElement
{
  private String stateVarName="";
  private String stateVarType="";
  private String operationOrAssignment="";
  private boolean isOperation=false;
  private ArrayList comments=new ArrayList();

  public ArrayList getComments()
  {
    return comments;
  }

  public void setComments(ArrayList comments)
  {
    this.comments = comments;
  }

  public boolean isOperation()
  {
    return isOperation;
  }

  public void setOperation(boolean operation)
  {
    isOperation = operation;
  }

  public String getOperationOrAssignment()
  {
    return operationOrAssignment;
  }

  public void setOperationOrAssignment(String operationOrAssignment)
  {
    this.operationOrAssignment = operationOrAssignment;
  }

  public String getStateVarName()
  {
    return stateVarName;
  }

  public void setStateVarName(String stateVarName)
  {
    this.stateVarName = stateVarName;
  }

  public String getStateVarType()
  {
    return stateVarType;
  }

  public void setStateVarType(String stateVarType)
  {
    this.stateVarType = stateVarType;
  }


}
