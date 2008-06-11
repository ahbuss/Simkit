package simkit.test;
import simkit.random.*;
/**
 * @version $Id: TestMersenneTwisterDC.java 793 2005-06-24 01:14:26Z ahbuss $
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
        
        System.out.println("New Seeds:");
        
        data = new long[33];
        data[1] = 0x610d0001;
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
        data[13] = 0x56aa7b80;
        data[14] = 0x6fda8000;
        data[15] = 0;
        data[16 + 0] = 69070;
        data[16 + 1] = 475628535;
        data[16 + 2] = 1129920460;
        data[16 + 3] = 772999773;
        data[16 + 4] = 1730348410;
        data[16 + 5] = 1674351795;
        data[16 + 6] = 1662200408;
        data[16 + 7] = 2044158073;
        data[16 + 8] = 1641506278;
        data[16 + 9] = 797919023;
        data[16 + 10] = 596140964;
        data[16 + 11] = 1156259413;
        data[16 + 12] = 1059494674;
        data[16 + 13] = 584849259;
        data[16 + 14] = 786050992;
        data[16 + 15] = 1221861361;
        data[16 + 16] = 929943806;
        
        rng.setSeeds(data);
        System.out.println(rng);
        /* generated values should be:
            600403500
            849934661
            1092073882
            1113803125
            1586446825
         */
        for (int i = 0; i < 5; ++i) {
            System.out.println(rng.drawLong());
        }
        
        System.out.println(rng);

    }
    
}
