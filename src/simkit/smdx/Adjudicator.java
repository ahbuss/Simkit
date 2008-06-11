/*
 * Adjudicator.java
 *
 * Created on July 29, 2002, 2:36 PM
 */

package simkit.smdx;

/**
 * Used to determine the result of an interaction between a
 * Target and a Munition.
 * @author  Arnold Buss
 * @version $Id: Adjudicator.java 476 2003-12-09 00:27:33Z jlruck $
 */
public interface Adjudicator {
    
/**
* Determines the result of an interaction between a Target and a Munition.
* The implementation is responsible for either calling <CODE>kill</CODE>
* or <CODE>hit</CODE> on the Target or carrying out the effects of the 
* interaction in some other way.
**/
    public void adjudicate(Munition munition, Target target);
}
