/*
 * NewSides.java
 *
 * Created on March 7, 2002, 7:50 AM
 */

package simkit.test;
import simkit.smdx.Side;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class NewSide extends simkit.smdx.Side {

    public static final Side CHARTRUESE = new NewSide("Chartruese");
    public static final Side MAGENTA = new NewSide("Magenta");
    
    /** Creates new NewSides */
    protected NewSide(String name) {
        super(name);
    }

    public static void main(String[] args) {
        System.out.println(Side.allSides);
    }
}
