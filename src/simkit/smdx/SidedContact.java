/*
 * SidedContact.java
 *
 * Created on March 3, 2003, 11:22 AM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 */
public class SidedContact extends Contact implements Sided {
    
    protected Side side;
    
    public SidedContact(Moveable mover, Side side) {
        super(mover);
        this.side = side;
    }
    
    public Side getSide() { return side; }
        
}
