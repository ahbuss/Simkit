package simkit.smd;
/**
  * An enumeration class for the type of motion of a Mover.  Currently,
  * only uniform linear motion (type UNIFORM_LINEAR) is supported.
  *
  * @author Arnold H. Buss
  * @version 0.2
  * 
**/


public class MovementType {
   
/**
  * The only elements of this class
**/
    public static MovementType UNIFORM_VELOCITY = new MovementType("UNIFORM_LINEAR");
    public static MovementType UNIFORM_ACCELERATION = new MovementType("UNIFORM_LINEAR");

    private String name;
   
/**
  * Only allow subclasses to create a new type.
  * @param name name of new MovementType
**/
   protected MovementType(String name){ this.name = name;}
   
/**
  * The type name is the same as its variable name.
**/ 
   public String toString() {
      return name;
   }
}