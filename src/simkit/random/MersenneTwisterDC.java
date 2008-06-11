package simkit.random;

/**
 * A clean port of the Mersenne Twister from the C code.
 * Caution: setSeed(long) does <i>not</i> have the effect probably
 * intended.  It essentially re-initializes the state array starting
 * with the argument.  To synchronize with where another instance
 * left off, for example, use setSeeds(long[]), passing tn the result of
 * getSeeds() from the other instance.  So setSeed(getSeed()) is not the
 * identity, but setSeeds(getSeeds()) is.
 *
 * @version $Id: MersenneTwisterDC.java 802 2005-07-07 17:41:45Z jlruck $
 * @author  ahbuss
 */
public class MersenneTwisterDC implements RandomNumber {
    
    // values corresponding to mt_struct
    private int aaa;
    private int mm;
    private int nn;
    private int rr;
    private int ww;
    private int wmask;
    private int umask;
    private int lmask;
    private int shift0;
    private int shift1;
    private int shiftB;
    private int shiftC;
    private int maskB;
    private int maskC;
    
    private int i;
    private int[] state;
    
    private static final int UNSIGNED_MASK = 0xffffffff;
    private static final double MULTIPLIER = 1.0 / (1L << 32);
    
    private long originalSeed;
    
    public MersenneTwisterDC() {
    }
    
    public void sgenrand(long seed) {
        state = new int[nn];
        state[0] = (((int) seed * 69069 + 0x1) & wmask) ;
        for (int i = 1; i < state.length; ++i) {
            state[i] = ((69069 * state[i-1]  + 0x1) & wmask);
        }
        i = 0;
    }
    
    public double draw() {
        
        return (double) drawLong() * MULTIPLIER;
    }
    
    public long drawLong() {
        int x; 
        
        if (i >= nn) {
            int k;
            for (k = 0; k < nn - mm; k++) {
                x = (state[k] & umask) | (state[k+1] & lmask);
                state[k] = state[k + mm] ^ (x >>> 1) ^ ((x & 0x1) == 0x1 ? aaa : 0x0);
            }
            for ( ; k < nn - 1; k++) {
                x = (state[k] & umask) | (state[k+1] & lmask);
                state[k] = state[k + mm - nn] ^ (x >>> 1) ^ ((x & 0x1) == 0x1 ? aaa : 0x0);
            }
            x = (state[nn - 1] & umask) | (state[0] & lmask);
            state[nn - 1] = state[mm - 1] ^ ( x >>> 1) ^ ((x & 0x1) == 0x1 ? aaa : 0x0);
            i = 0;
        }
        x = state[i];
        i += 1;
        
        x ^= (x >>> shift0);
        x ^= (x << shiftB) & maskB;
        x ^= (x << shiftC) & maskC;
        x ^= (x >>> shift1);
        
        return (long) x & wmask;
    }
    
    public double getMultiplier() { return MULTIPLIER; }
    
    public long getSeed() { return (long) state[i]; }
    
    public long[] getSeeds() {
        long[] data = new long[16 + nn];
        data[1] = aaa;
        data[2] = mm;
        data[3] = nn;
        data[4] = rr;
        data[5] = ww;
        data[6] = wmask;
        data[7] = umask;
        data[8] = lmask;
        data[9] = shift0;
        data[10] = shift1;
        data[11] = shiftB;
        data[12] = shiftC;
        data[13] = maskB;
        data[14] = maskC;
        data[15] = i;
        for (int i = 0; i < state.length; ++i) {
            data[16 + i] = state[i];
        }
        return data;
    }
    
    public void resetSeed() {
        sgenrand(originalSeed);
    }
    
    public void setSeed(long seed) {
        sgenrand(seed);
    }
    
    /**
     * If data.length == 15, use sgenrand to initialize state data[0] must be > 0).
     * Otherwise, use data[16..data.length] as the state array.
     */
    public void setSeeds(long[] data) {
        if (data.length < 15) {
            throw new IllegalArgumentException(
            "At least 15 data items needed: " + data.length);
        }
        
        long seed = data[0];
        aaa = (int) data[1] & UNSIGNED_MASK;
        mm = (int) data[2];
        nn = (int) data[3];
        rr = (int) data[4];
        ww = (int) data[5];
        wmask = (int) data[6] & UNSIGNED_MASK;
        umask = (int) data[7] & UNSIGNED_MASK;
        lmask = (int) data[8] & UNSIGNED_MASK;
        shift0 = (int) data[9];
        shift1 = (int) data[10];
        shiftB = (int) data[11];
        shiftC = (int) data[12];
        maskB = (int) data[13] & UNSIGNED_MASK;
        maskC = (int) data[14] & UNSIGNED_MASK;
        
        if (data.length == 16 + nn) {
            i = (int) data[15];
            state = new int[nn];
            for (int k = 0; k < nn; ++k) {
                state[k] = (int) data[16 + k] & UNSIGNED_MASK;
            }
        }
        else if (data.length == 15) {
            if (seed > 0) {
                setSeed(seed);
            }
            else {
                throw new IllegalArgumentException("Seed must be > 0: " + seed);
            }
        }
        else {
            throw new IllegalArgumentException("data[] must be 15 or 16 + data[3]: " + data.length );
        }
        
        
    }
    
    public String paramString() {
        String NL = System.getProperty("line.separator");
        
        StringBuffer buf = new StringBuffer();
        buf.append("aaa = " + Integer.toHexString(aaa));
        buf.append(NL);
        buf.append("mm = " + mm);
        buf.append(NL);
        buf.append("nn = " + nn);
        buf.append(NL);
        buf.append("rr = " + rr);
        buf.append(NL);
        buf.append("ww = " + ww);
        buf.append(NL);
        buf.append("wmask = " + Integer.toHexString(wmask));
        buf.append(NL);
        buf.append("umask = " + Integer.toHexString(umask));
        buf.append(NL);
        buf.append("lmask = " + Integer.toHexString(lmask));
        buf.append(NL);
        buf.append("shift0 = " + shift0);
        buf.append(NL);
        buf.append("shift1 = " + shift1);
        buf.append(NL);
        buf.append("shiftB = " + shiftB);
        buf.append(NL);
        buf.append("shiftC = " + shiftC);
        buf.append(NL);
        buf.append("maskB = " + Integer.toHexString(maskB));
        buf.append(NL);
        buf.append("maskC = " + Integer.toHexString(maskC));
        buf.append(NL);
        buf.append("i = " + i);
        for (int k = 0; k < state.length; ++k) {
            buf.append(NL);
            buf.append("state[" + k +"] = " + state[k]);
        }
        
        return buf.toString();
    }
    
    public String toString() { return "Mersenne Twister (Dynamic Creator)\n" + paramString(); }
    
}
