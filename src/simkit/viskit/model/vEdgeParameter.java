package simkit.viskit.model;

/**
 * Created by IntelliJ IDEA.
 * User: mike
 * Date: Apr 7, 2004
 * Time: 3:19:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class vEdgeParameter extends ViskitElement
{
  private String type;
  private String value;
  public vEdgeParameter(String value, String type)
  {
    this.type = type;
    this.value = value;
  }
  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }


}
