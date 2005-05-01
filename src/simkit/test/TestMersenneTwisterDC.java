package simkit.test;
import simkit.random.*;
/**
 * @version $Id$
 * @author  ahbuss
 */
public class TestMersenneTwisterDC {
    /*
mts->aaa = 71938707
mts->mm = 8
mts->nn = 17
mts->rr = 6
mts->ww = 31
mts->wmask = 7fffffff
mts->umask = 7fffffc0
mts->lmask = 3f
mts->shift0 = 12
mts->shift1 = 18
mts->shiftB = 7
mts->shiftC = 15
mts->maskB = 64a95b80
mts->maskC = 3fd78000
     */
    public static void main(String[] args) {
        long[] data = new long[15];
        data[0] = 3241;
        data[1] = 0x71938707;
        data[2] = 8;
        data[3] = 17;
        data[4] = 6;
        data[5] = 31;
        data[6] = 0x7fffffff;
        data[7] = 0x7fffffc0;
        data[8] = 0x3f;
        data[9] = 12;
        data[10] = 18;
        data[11] = 7;
        data[12] = 15;
        data[13] = 0x64a95b80;
        data[14] = 0x3fd78000;
        
        RandomNumber rng = RandomNumberFactory.getInstance("MersenneTwisterDC", data);
        System.out.println(rng);
        
        for (int i = 0; i < 100; ++i) {
            System.out.println( rng.drawLong());
        }
        System.out.println(rng);
        long[] seeds = rng.getSeeds();
        System.out.println(rng.drawLong());
        rng.setSeeds(seeds);
        System.out.println(rng);
        System.out.println(rng.drawLong());
    }
    
}
