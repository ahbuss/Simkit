package simkit.smdx;

import java.awt.geom.Point2D;
import simkit.SimEntity;

public interface Weapon extends Moveable, SimEntity {

    public void reload(int numberRounds);

    public int getRemainingRounds();

    public void doShoot(Munition munition, Point2D aimPoint);
    
    public void setMover(Moveable mover);
    
    public Moveable getMover();
    
    public WeaponFireType getFireType();
    
}
