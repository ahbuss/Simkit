package simkit.smdx;
/**
 * Something that has a Side associated with it.
 * @see Side
 * @version $Id: Sided.java 476 2003-12-09 00:27:33Z jlruck $
 * @author  Arnold Buss
 */
public interface Sided {
    
/**
* Return the Side associated with this Sided.
**/
    public Side getSide();
    
}
