package simkit.smdx;
import simkit.smd.Mover;
import simkit.smd.Coordinate;
import simkit.SimEntity;

public interface Weapon extends Moveable, SimEntity {

    public void fireAt(Target target);

    public void reload(int numberRounds);

    public int getRemainingRounds();

    public void doShoot(Weapon weapon, Coordinate aimPoint);
    
    public void doShoot(Weapon weapon, Target target);

    public void setMover(Moveable mover);
    
    public Moveable getMover();
    
    public WeaponFireType getFireType();
    
}
