package simkit.smd;

/**
  * An exception thrown by a moving entity when the destination given
  * is unreachable.  The two possibilities are:
  *
  * <UL> <LI> The entity has a maximum speed of 0 (i.e. cannot move)
  * <LI> The destination is null
  * </UL>
  *
  * @author Arnold H. Buss
  * @version 0.2
  * 
**/

public class MovingException extends RuntimeException {

/**
  * Construct a <CODE>MovingException</CODE> with no detail message
**/
   public MovingException() { super(); }

/**
  * Construct a <CODE>MovingException</CODE> with specified detail message
  * @param detail Message for MovingException
**/
   public MovingException(String detail) { super(detail); }

}