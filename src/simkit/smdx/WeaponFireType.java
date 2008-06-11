/*
 * WeaponAimType.java
 *
 * Created on February 20, 2002, 1:00 AM
 */

package simkit.smdx;

/**
 * A java enumeration that represents the type of fire produced by a weapon system.
 * The type are:<UL>
 * <LI>aimed - The weapon is fired at a particular Contact. </LI>
 * <LI>area - The weapon is fired at a geographic location.</LI>
 * <LI>aimedOrArea - The weapon can be fired by either method.</LI>
 * </UL>
 * @author  Arnold Buss
 * @version $Id: WeaponFireType.java 441 2003-11-01 00:08:27Z jlruck $
 */
public class WeaponFireType {
    
    public static final WeaponFireType aimed = new WeaponFireType("AIMED");
    public static final WeaponFireType area = new WeaponFireType("AREA");
    public static final WeaponFireType aimedOrArea = new WeaponFireType("AIMED_OR_AREA");

/**
* The name of this WeaponFireType.
**/
    private String type;
    
    /** Creates new WeaponAimType */
    protected WeaponFireType(String type) {
        this.type = type;
    }
    
/**
* Returns a String containing the name of this WeaponFireType.
**/
    public String toString() { return type; }
   

}
