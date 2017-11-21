package simkit.smdx;

import java.awt.geom.Point2D;

import simkit.SimEntity;

/**
 * Represents a weapon system that can fire Munitions.
 *
 * @version $Id$
 *
 */
public interface Weapon extends Moveable, SimEntity {

    /**
     * Adds the given number of rounds to this Weapons inventory.
     *
     * @param numberRounds given number of rounds
     */
    public void reload(int numberRounds);

    /**
     *
     * @return The number of rounds remaining in this Weapons inventory.
     */
    public int getRemainingRounds();

    /**
     * An event that occurs when this Weapon fires the Munition at a point.
     *
     * @param munition Given Munition
     * @param aimPoint given aim point
     */
    public void doShoot(Munition munition, Point2D aimPoint);

    /**
     *
     * @param mover the Moveable asset on which this Weapon resides.
     */
    public void setMover(Moveable mover);

    /**
     *
     * @return The Moveable asset on which this Weapon resides.
     */
    public Moveable getMover();

    /**
     * @return the type of fire for this Weapon.
     */
    public WeaponFireType getFireType();

}
