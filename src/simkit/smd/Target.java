package simkit.smd;

/**
 * A Mover that can be acted on by a Munition and is subject to being killed or
 * damaged.
 *
 * @author Arnold Buss
 * 
 */
public interface Target extends Mover {

    /**
     * Kills this Target.
     */
    public void doKill();

    /**
     * Causes this Target to be damaged.
     *
     * @param damage Defines how this Target is damaged.
     */
    public void doHit(Damage damage);

    /**
     * @return true if this Target has not been killed.
     */
    public boolean isAlive();
}
