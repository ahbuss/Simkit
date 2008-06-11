/*
 * DiscreteRandomVariate.java
 *
 * Created on August 31, 2001, 7:14 PM
 */

package simkit.random;

/**
 * A RandomVariate that can only take on discrete values.
 * 
 * @author  Arnold Buss
 * @version $Id$
 */
public interface DiscreteRandomVariate extends RandomVariate {
    
/**
* Generate the next value as an integer.
**/
    public int generateInt();

}

