/*
 * WeaponAimType.java
 *
 * Created on February 20, 2002, 1:00 AM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class WeaponFireType {
    
    public static final WeaponFireType aimed = new WeaponFireType("AIMED");
    public static final WeaponFireType area = new WeaponFireType("AREA");
    public static final WeaponFireType aimedOrArea = new WeaponFireType("AIMED_OR_AREA");

    private String type;
    
    /** Creates new WeaponAimType */
    protected WeaponFireType(String type) {
        this.type = type;
    }
    
    public String toString() { return type; }
   

}
