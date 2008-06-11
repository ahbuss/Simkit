package simkit.smdx;

import java.awt.geom.Point2D;

import simkit.SimEntity;

/**
* Represents a weapon system that can fire Munitions.
* @version $Id: Weapon.java 643 2004-04-25 22:59:33Z kastork $
**/
public interface Weapon extends Moveable, SimEntity {

/**
* Adds the given number of rounds to this Weapons inventory.
**/
    public void reload(int numberRounds);

/**
* The number of rounds remaining in this Weapons inventory.
**/
    public int getRemainingRounds();

/**
* An event that occurs when this Weapon fires the Munition at 
* a point. 
**/
    public void doShoot(Munition munition, Point2D aimPoint);
    
/**
* Sets the Moveable asset on which this Weapon resides.
**/
    public void setMover(Moveable mover);
    
/**
* The Moveable asset on which this Weapon resides.
**/
    public Moveable getMover();
    
/**
* Returns the type of fire for this Weapon.
**/
    public WeaponFireType getFireType();
    
}
