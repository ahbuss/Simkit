package simkit.viskit.model;

/**
 * Class describes a "vParameter" to an event graph--something
 * that is passed into the event graph at runtime. This has
 * a name and type.
 *
 * @author DMcG
 */
 
public class vParameter extends ViskitElement
{
  private String name;
  private String type;
  private String value = "";
  private String comment="";

  vParameter(String pName, String pType)         //package-accessible
  {
    name = pName;
    type = pType;
  }
  public vParameter(String pName, String pType, String comment)         //todo make package-accessible
  {
    this(pName,pType);
    this.comment = comment;
  }

  public String getName()              { return name;}
  public void   setName(String pName)  { name = pName;}
  
  public String getType()              { return type;}
  public void   setType(String pType)  { type = pType;}

  public String getValue()             { return value; }
  public void   setValue(String value) { this.value = value; }

  public String getComment()           { return comment; }
  public void   setComment(String cmt) { this.comment = cmt; }
}
