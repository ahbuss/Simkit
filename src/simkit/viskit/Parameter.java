package simkit.viskit;

/**
 * Class describes a "Parameter" to an event graph--something
 * that is passed into the event graph at runtime. This has
 * a name and type.
 *
 * @author DMcG
 */
 
public class Parameter extends Object
{
  /** Name of the parameter being passed in */
  private String name;
  
  /** Type of the parameter. */
  private String type;
  
  public Parameter(String pName, String pType)
  {
    name = pName;
    type = pType;
  }
  
  public String getName()
  { return name;
  }
  
  public void setName(String pName)
  { name = pName;
  }
  
  public String getType()
  { return type;
  }
  
  public void setType(String pType)
  { type = pType;
  }
}
