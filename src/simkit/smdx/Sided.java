package simkit.smdx;
/* Note the @version tag with $Id$ is a test to see what it looks 
*  like in javadoc. (JLR)
*/
/**
 * Something that has a Side associated with it.
 * @see Side
 * @version $Id$
 * @author  Arnold Buss
 */
public interface Sided {
    
/**
* Return the Side associated with this Sided.
**/
    public Side getSide();
    
}
