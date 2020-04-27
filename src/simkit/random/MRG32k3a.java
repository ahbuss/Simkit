package simkit.random;

/**
 * An ateempt to implement L'Ecuyer's MRG32k3a algorithm from
 * "Good Parameters and Implementations for Combined Multiple Recursive 
 * Random Number Generators"
 * 
 * 
 * @author ahbuss
 */
public class MRG32k3a implements RandomNumber {

    public static final double norm = 2.3283163396834613E-10;
    public static final double m1 = 4294967087.0;
    public static final double m2 = 4294944443.0;
    public static final double a12 = 1403580.0;
    public static final double a13n = 810728.0;
    public static final double a21 = 527612.0;
    public static final double a23n = 1370589.0;
    
    private long seed;
    
    private long[] originalSeeds;
    
    
    public static final long DIVISOR = 4294967088L;
    public static final long M1 = 4294967087L;
    public static final long M2 = 4294944443L;
    public static final long A12 = 1403580L;
    public static final long A13N = 810728L;
    public static final long A21 = 527612L;
    public static final long A23N = 1370589L;
//    
    private static final double MULTIPLIER = 1.0 / DIVISOR;
    
    private boolean initialized;
    
    private long[] s;
    
    private long s10, s11, s12, s20, s21, s22;
    
    private double[] Cg;
    
    public MRG32k3a() {
        initialized = false;
        Cg = new double[6];
        setSeeds(new long[] {12345L, 12345L, 12345L, 12345L,
            12345L, 12345L} );
    }
    
    public long drawLong() {
        long draw = 0L;
        
        long x1 = A12 * s10 - A13N * s10 % M1;
        x1 = x1 >= 0L ? x1 : x1 + M1;
        long x2 = A21 * s22 - A23N * s20 % M2;
        x2 = x2 >= 0L ? x2 : x2 + M2;
        
        s10 = s11;
        s11 = s12;
        s12 = x1;
        s20 = s21;
        s21 = s22;
        s22 = x2;
        
        draw = (x1 - x2) % M1;
        draw = draw > 0 ? draw : draw + M1;
        
        return draw;
    }

    public double draw() {
                int k;
        double p1, p2, u;
        /* Component 1 */
        p1 = a12 * Cg[1] - a13n * Cg[0];
        k = (int)(p1 / m1);
        p1 -= k * m1;
        if (p1 < 0.0) p1 += m1;
        Cg[0] = Cg[1];   Cg[1] = Cg[2];   Cg[2] = p1;
        /* Component 2 */
        p2 = a21 * Cg[5] - a23n * Cg[3];
        k  = (int)(p2 / m2);
        p2 -= k * m2;
        if (p2 < 0.0) p2 += m2;
        Cg[3] = Cg[4];   Cg[4] = Cg[5];   Cg[5] = p2;
        /* Combination */
        u = ((p1 > p2) ? (p1 - p2) * norm : (p1 - p2 + m1) * norm);
        return u;
    }

    public double getMultiplier() {
        return MULTIPLIER;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }
    
    public void resetSeed() {
        setSeeds(originalSeeds);
    }

    public void setSeeds(long[] seeds) {
        if (seeds.length != 6) {
            throw new IllegalArgumentException(
                    "Need 6 elements in seed array: " + seeds.length);
        }
        if (!initialized) {
            initialized = true;
            originalSeeds = (long[]) seeds.clone();
        }
        this.s = (long[]) seeds.clone();
        s10 = seeds[0];
        s11 = seeds[1];
        s12 = seeds[2];
        s20 = seeds[3];
        s21 = seeds[4];
        s22 = seeds[5];
        
        for (int i = 0; i < Cg.length; ++i) {
            Cg[i] = seeds[i];
        }
    }

    public long[] getSeeds() {
        return (long[]) s.clone();
    }


}
