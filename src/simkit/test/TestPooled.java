package simkit.test;
import simkit.random.*;
/**
 *
 * @author  Arnold Buss
 */
public class TestPooled {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomNumber original = RandomNumberFactory.getInstance("PooledGenerator");
        original.setSeeds(new long[] { 42L, 42L });
        RandomNumber tausworthe = RandomNumberFactory.getInstance("Tausworthe");
        tausworthe.setSeed(42L);
        RandomNumber congruential = RandomNumberFactory.getInstance("Congruential");
        congruential.setSeed(42L);
        RandomNumber pooled = RandomNumberFactory.getInstance("PooledXORGenerator",
        tausworthe, congruential);
        System.out.println(original);
        System.out.println(pooled);
        
        for (int i = 0; i < 20; i++) {
            System.out.println(original.drawLong() + "\t" + pooled.drawLong());
        }
    }
}
