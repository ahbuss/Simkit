/*
 * SidedContact.java
 *
 * Created on March 3, 2003, 11:22 AM
 */

package simkit.smdx;

/**
 * A Contact that contains Side information.
 * @author  Arnold Buss
 * @version $Id$
 */
public class SidedContact extends SensorContact implements Sided {
    
/**
* The Side associated with this SidedContact.
**/
    protected Side side;
    
/**
* Constructs a new Contact to represent the given Movable.
**/
    public SidedContact(Moveable mover, Side side) {
        super(mover);
        this.side = side;
    }
    
/**
* Gets the Side associated with this SidedContact.
**/
    public Side getSide() { return side; }
        
}
