package simkit.smd;

public interface Weapon {

    public void fireAt(Mover target);

    public void reload(int numberRounds);

    public int getRemainingRounds();

    public void doShoot(Mover weapon, Coordinate aimPoint);

    public void doImpact(Mover weapon, Coordinate impactPoint);

}
