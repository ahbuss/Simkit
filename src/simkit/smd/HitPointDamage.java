package simkit.smd;

/**
 * This Damage type holds a Number to indicate the amount of damage to the
 * Target from a hit. The meaning of the Number depends on the implementation of
 * the <CODE>hit</CODE> method in the Target.
 *
 * @author Arnold Buss
 * 
 */
public class HitPointDamage implements Damage<Number> {

    /**
     * A numerical value representing the amount that a hit damages a Target.
     *
     */
    private Number damage;

    /**
     * Constructs a new HitPointDamage with the damage Object set to null.
     *
     */
    public HitPointDamage() {
    }

    /**
     * Constructs a new HitPointDamage with the given damage Object.
     *
     * @param damage Given damage object
     */
    public HitPointDamage(Number damage) {
        this.setDamage(damage);
    }

    /**
     * Sets the numerical value representing the amount that a hit damages a
     * Target.
     *
     * @param damage The Number representing the damage.
     *
     */
    public void setDamage(Number damage) {
        this.damage = damage;
    }

    /**
     * 
     * @return A numerical value representing the amount that a hit damages a Target.
     */
    @Override
    public Number getDamage() {
        return damage;
    }

}
