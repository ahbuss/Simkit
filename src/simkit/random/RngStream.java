package simkit.random;

import java.util.Arrays;

/**
 * Title:          RngStream.java<br>
 * Description:    Multiple Streams and Substreams of Random Numbers<br>
 * Copyright:      Pierre L'Ecuyer, University of Montreal<br>
 * Notice:         This code can be used freely for personal, academic,
 *                 or non-commercial purposes. For commercial purposes, 
 *                 please contact P. L'Ecuyer at: lecuyer@iro.UMontreal.ca<br>
 * Version         1.0<br>
 * Date:           14 August 2001<br>
 * 
 * Modified slightly to conform to <code>RandomNumber</code> interface
 * for use in Simkit by Arnold Buss.
 * 
 * @version $Id$
 * @author Pierre L'Ecuyer
 */
public class RngStream implements RandomNumber {

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private constants.

// Note: since these are static final, it is okay that they be public
    public static final double norm = 2.328306549295727688e-10;
    public static final double m1 = 4294967087.0;
    public static final double m2 = 4294944443.0;
    public static final double a12 = 1403580.0;
    public static final double a13n = 810728.0;
    public static final double a21 = 527612.0;
    public static final double a23n = 1370589.0;
    public static final double two17 = 131072.0;
    public static final double two53 = 9007199254740992.0;
    public static final double invtwo24 = 5.9604644775390625e-8;
    
    private int streamID;
    private int substreamID;
    
    private static final double InvA1[][] = { // Inverse of A1p0
        {184888585.0, 0.0, 1945170933.0},
        {1.0, 0.0, 0.0},
        {0.0, 1.0, 0.0}};
    private static final double InvA2[][] = { // Inverse of A2p0
        {0.0, 360363334.0, 4225571728.0},
        {1.0, 0.0, 0.0},
        {0.0, 1.0, 0.0}};
    private static final double A1p0[][] = {
        {0.0, 1.0, 0.0},
        {0.0, 0.0, 1.0},
        {-810728.0, 1403580.0, 0.0}};
    private static final double A2p0[][] = {
        {0.0, 1.0, 0.0},
        {0.0, 0.0, 1.0},
        {-1370589.0, 0.0, 527612.0}};
    private static final double A1p76[][] = {
        {82758667.0, 1871391091.0, 4127413238.0},
        {3672831523.0, 69195019.0, 1871391091.0},
        {3672091415.0, 3528743235.0, 69195019.0}};
    private static final double A2p76[][] = {
        {1511326704.0, 3759209742.0, 1610795712.0},
        {4292754251.0, 1511326704.0, 3889917532.0},
        {3859662829.0, 4292754251.0, 3708466080.0}};
    private static final double A1p127[][] = {
        {2427906178.0, 3580155704.0, 949770784.0},
        {226153695.0, 1230515664.0, 3580155704.0},
        {1988835001.0, 986791581.0, 1230515664.0}};
    private static final double A2p127[][] = {
        {1464411153.0, 277697599.0, 1610723613.0},
        {32183930.0, 1464411153.0, 1022607788.0},
        {2824425944.0, 32183930.0, 2093834863.0}};
    
    private static int NEXT_ID = 0;
    
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private variables (fields) for each stream.
    // Master seed for the RngStream system
    private static double streamsOrigin[] = {12345, 12345, 12345, 12345, 12345, 12345};

    private double nextSeed[] = {12345, 12345, 12345, 12345, 12345, 12345};
    // Default seed of the package and seed for the next stream to be created.
    private double Cg[] = new double[6];
    private double Bg[] = new double[6];
    private double Ig[] = new double[6];
    // The arrays {\tt Cg}, {\tt Bg}, and {\tt Ig} contain the current state, 
    // the starting point of the current substream,
    // and the starting point of the stream, respectively.
    private boolean anti;
    // This stream generates antithetic variates if 
    // and only if {\tt anti = true}.
    private boolean prec53;
    // The precision of the output numbers is ``increased'' (see
    // {\tt increasedPrecis}) if and only if {\tt prec53 = true}.
    private String descriptor;
    // Describes the stream (for writing the state, error messages, etc.).

    private int id;
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
// Private methods

    //--------------------------------------------------------------
    /* Compute (a*s + c) MOD m ; m must be < 2^35 */
    /* Works also for s, c < 0.                   */
    private static double multModM(double a, double s, double c, double m) {
        double v;
        int a1;
        v = a * s + c;
        if (v >= two53 || v <= -two53) {
            a1 = (int) (a / two17);
            a -= a1 * two17;
            v = a1 * s;
            a1 = (int) (v / m);
            v -= a1 * m;
            v = v * two17 + a * s + c;
        }
        a1 = (int) (v / m);
        if ((v -= a1 * m) < 0.0) {
            return v += m;
        } else {
            return v;
        }
    }


    //-----------------------------------------------------------
    /* Returns v = A*s MOD m.  Assumes that -m < s[i] < m. */
    /* Works even if v = s.                                */
    private static void matVecModM(double A[][], double s[],
            double v[], double m) {
        int i;
        double x[] = new double[3];
        for (i = 0; i < 3; ++i) {
            x[i] = multModM(A[i][0], s[0], 0.0, m);
            x[i] = multModM(A[i][1], s[1], x[i], m);
            x[i] = multModM(A[i][2], s[2], x[i], m);
        }
        for (i = 0; i < 3; ++i) {
            v[i] = x[i];
        }
    }

    //------------------------------------------------------------
    /* Returns C = A*B MOD m */
    /* Note: work even if A = C or B = C or A = B = C.         */
    private static void matMatModM(double A[][], double B[][],
            double C[][], double m) {
        int i, j;
        double V[] = new double[3], W[][] = new double[3][3];
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                V[j] = B[j][i];
            }
            matVecModM(A, V, V, m);
            for (j = 0; j < 3; ++j) {
                W[j][i] = V[j];
            }
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                C[i][j] = W[i][j];
            }
        }
    }

    //-------------------------------------------------------------
    /* Compute matrix B = (A^(2^e) Mod m);  works even if A = B */
    private static void matTwoPowModM(double A[][], double B[][],
            double m, int e) {
        int i, j;
        /* initialize: B = A */
        if (A != B) {
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; ++j) {
                    B[i][j] = A[i][j];
                }
            }
        }
        /* Compute B = A^{2^e} */
        for (i = 0; i < e; i++) {
            matMatModM(B, B, B, m);
        }
    }

    //-------------------------------------------------------------
    /* Compute matrix D = A^c Mod m ;  works even if A = B */
    private static void matPowModM(double A[][], double B[][],
            double m, int c) {
        int i, j;
        int n = c;
        double W[][] = new double[3][3];

        /* initialize: W = A; B = I */
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; ++j) {
                W[i][j] = A[i][j];
                B[i][j] = 0.0;
            }
        }
        for (j = 0; j < 3; ++j) {
            B[j][j] = 1.0;
        }

        /* Compute B = A^c mod m using the binary decomp. of c */
        while (n > 0) {
            if ((n % 2) == 1) {
                matMatModM(W, B, B, m);
            }
            matMatModM(W, W, W, m);
            n /= 2;
        }
    }

    //-------------------------------------------------------------
    // Generate a uniform random number, with 32 bits of resolution.
    private double U01() {
        int k;
        double p1, p2, u;
        /* Component 1 */
        p1 = a12 * Cg[1] - a13n * Cg[0];
        k = (int) (p1 / m1);
        p1 -= k * m1;
        if (p1 < 0.0) {
            p1 += m1;
        }
        Cg[0] = Cg[1];
        Cg[1] = Cg[2];
        Cg[2] = p1;
        /* Component 2 */
        p2 = a21 * Cg[5] - a23n * Cg[3];
        k = (int) (p2 / m2);
        p2 -= k * m2;
        if (p2 < 0.0) {
            p2 += m2;
        }
        Cg[3] = Cg[4];
        Cg[4] = Cg[5];
        Cg[5] = p2;
        /* Combination */
        u = ((p1 > p2) ? (p1 - p2) * norm : (p1 - p2 + m1) * norm);
        return (anti) ? (1 - u) : u;
    }

    //-------------------------------------------------------------
    // Generate a uniform random number, with 52 bits of resolution.
    private double U01d() {
        double u = U01();
        if (anti) {
            // Antithetic case: note that U01 already returns 1-u.
            u += (U01() - 1.0) * invtwo24;
            return (u < 0.0) ? u + 1.0 : u;
        } else {
            u += U01() * invtwo24;
            return (u < 1.0) ? u : (u - 1.0);
        }
    }

    /**
     * Returns an independent instance of Stream[0:0], that is, a stream that
     * has no offsets from the system seeds {@code streamsOrigin}. This is 
     * Stream 0, Substream 0
     * 
     * Unlike the L'Ecuyer implementation, the default constructor in
     * simkit always returns the same stream.  Like other places in
     * simkitk, streams instantiated in this way are not linked
     * but generate the same stream of random numbers.  The seed
     * values that define this default stream are presently hard-coded
     * as they are in the L'Ecuyer implementation.
     * <p>
     * This changes the meaing of the instance variable {@code id) to
     * one that is independent of the stream or substream the instance
     * represents, and, rather, simply gives a unique number to the
     * instance, which might be useful to client code or readers of
     * output.
     * <p>
     * The id concept has thus been replaced with values indicating which
     * stream and substream offsets from the default this RngStream
     * represents.
     * 
     */
    
    public RngStream() {
        anti = false;
        prec53 = false;
        for (int i = 0; i < 6; ++i) {
            Bg[i] = Cg[i] = Ig[i] = nextSeed[i] = streamsOrigin[i];
        }
        matVecModM(A1p127, nextSeed, nextSeed, m1);
        double temp[] = new double[3];
        for (int i = 0; i < 3; ++i) {
            temp[i] = nextSeed[i + 3];
        }
        matVecModM(A2p127, temp, temp, m2);
        for (int i = 0; i < 3; ++i) {
            nextSeed[i + 3] = temp[i];
        }
        id = ++NEXT_ID;
        streamID=0;
        substreamID=0;
        descriptor = "Stream[" + streamID + ":" + substreamID + "]";
    }
    
    /**
     * Creates a new independent Stream[0:0] and advances it to the stream
     * and substream offsets given.
     * 
     * @param stream
     * @param substream
     */
    public RngStream(int stream, int substream) {
        // TODO Study the usefulness of caching stream states, which would be
        // most useful if many substreams are going to be requested from a 
        // high-index main stream.
        this();
        for(int i=0;i<stream;i++){
            this.resetNextStream();
        }
        for(int i=0; i < substream; i++ ){
            this.resetNextSubstream();
        }
        descriptor = "Stream[" + streamID + ":" + substreamID + "]";
    }
    
    @Deprecated
    public RngStream(String name) {
        this();
        descriptor = name;
    }


    /**
     * Advances this instance to the next stream based on the current
     * stream baseline.  That is, calling this method will position the
     * stream at the same location regardless of the current state of
     * the generator.
     */
    public void resetNextStream() {
        for (int i = 0; i < 6; ++i) {
            Bg[i] = Cg[i] = Ig[i] = nextSeed[i];
        }
        matVecModM(A1p127, nextSeed, nextSeed, m1);
        double temp[] = new double[3];
        for (int i = 0; i < 3; ++i) {
            temp[i] = nextSeed[i + 3];
        }
        matVecModM(A2p127, temp, temp, m2);
        for (int i = 0; i < 3; ++i) {
            nextSeed[i + 3] = temp[i];
        }
        streamID++;
    }
    
    /**
     * Set the system stream origin.
     * 
     * @param seed Array of longs that will subsequently represent generator
     * state for Stream[0:0]
     * @return
     */
    public static boolean setPackageSeed(long seed[]) {
        // Must use long because there is no unsigned int type.
        if (CheckSeed(seed) != 0) {
            return false;
        }                   // FAILURE     
        for (int i = 0; i < 6; ++i) {
            streamsOrigin[i] = seed[i];
        }
        return true;                     // SUCCESS
    }

    /**
     * Reset the stream to its own individual starting point at substream 0.
     * <p>
     * Warning: this always implies the generator will be returned to
     * Stream[x:0], even if it was instantiated at Stream[x:y]
     */
    public void resetStartStream() {
        for (int i = 0; i < 6; ++i) {
            Cg[i] = Bg[i] = Ig[i];
        }
    }

    /**
     * Reset the stream to its own individual starting point.
     */
    public void resetStartSubstream() {
        for (int i = 0; i < 6; ++i) {
            Cg[i] = Bg[i];
        }
    }

    /**
     *advances generator to the next substream
     */
    public void resetNextSubstream() {
        int i;
        matVecModM(A1p76, Bg, Bg, m1);
        double temp[] = new double[3];
        for (i = 0; i < 3; ++i) {
            temp[i] = Bg[i + 3];
        }
        matVecModM(A2p76, temp, temp, m2);
        for (i = 0; i < 3; ++i) {
            Bg[i + 3] = temp[i];
        }
        for (i = 0; i < 6; ++i) {
            Cg[i] = Bg[i];
        }
        substreamID++;
    }

    public void setAntithetic(boolean a) {
        anti = a;
    }

    public void increasedPrecis(boolean incp) {
        prec53 = incp;
    }

    public void advanceState(int e, int c) {
        double B1[][] = new double[3][3], C1[][] = new double[3][3];
        double B2[][] = new double[3][3], C2[][] = new double[3][3];

        if (e > 0) {
            matTwoPowModM(A1p0, B1, m1, e);
            matTwoPowModM(A2p0, B2, m2, e);
        } else if (e < 0) {
            matTwoPowModM(InvA1, B1, m1, -e);
            matTwoPowModM(InvA2, B2, m2, -e);
        }

        if (c >= 0) {
            matPowModM(A1p0, C1, m1, c);
            matPowModM(A2p0, C2, m2, c);
        } else if (c < 0) {
            matPowModM(InvA1, C1, m1, -c);
            matPowModM(InvA2, C2, m2, -c);
        }

        if (e != 0) {
            matMatModM(B1, C1, C1, m1);
            matMatModM(B2, C2, C2, m2);
        }

        matVecModM(C1, Cg, Cg, m1);
        double[] cg3 = new double[3];
        for (int i = 0; i < 3; i++) {
            cg3[i] = Cg[i + 3];
        }
        matVecModM(C2, cg3, cg3, m2);
        for (int i = 0; i < 3; i++) {
            Cg[i + 3] = cg3[i];
        }
    }

    private static int CheckSeed(long seed[]) {
        /* Check that the seeds are legitimate values. Returns 0 if legal seeds,
        -1 otherwise. */
        int i;
        for (i = 0; i < 3; ++i) {
            if (seed[i] >= m1 || seed[i] < 0) {
                throw new IllegalArgumentException(
                        "Bad seed value - seed[" + i +
                        "] >= " + m1  + " or < 0 :" +
                        Arrays.toString(seed));
            }
        }
        for (i = 3; i < 6; ++i) {
            if (seed[i] >= m2 || seed[i] < 0) {
                throw new IllegalArgumentException(
                        "Bad seed value - seed[" + i +
                        "] >= " + m2  + " or < 0 :" +
                        Arrays.toString(seed));
            }
        }
        if (seed[0] == 0 && seed[1] == 0 && seed[2] == 0) {
                throw new IllegalArgumentException(
                        "Bad seed  - first three values are zero: " + 
                        Arrays.toString(seed));
        }
        if (seed[3] == 0 && seed[4] == 0 && seed[5] == 0) {
                throw new IllegalArgumentException(
                        "Bad seed  - last three values are zero: " + 
                        Arrays.toString(seed));
        }
        return 0;
    }

    /**
     * Position this stream at the indicated state.  The given state will
     * be the baseline state for this generator which is returned to if
     * {@code resetStartStream} or {@code resetStartSubstream} is called.
     * <p>
     * The state of this generator loses any internal sense of its offset
     * fromt the system Stream[0:0].
     * 
     * @param seed
     * @return
     */
    public boolean setSeed(long seed[]) {
        int i;
        if (CheckSeed(seed) != 0) {
            return false;
        }                   // FAILURE     
        for (i = 0; i < 6; ++i) {
            Cg[i] = Bg[i] = Ig[i] = seed[i];
        }
        return true;                        // SUCCESS
    }

    public double[] getState() {
        return Cg;
    }

    public void writeState() {
        System.out.print("The current state of the RngStream");
        if (descriptor.length() > 0) {
            System.out.print(" " + descriptor);
        }
        System.out.print(":\n   Cg = { ");
        for (int i = 0; i < 5; i++) {
            System.out.print((long) Cg[i] + ", ");
        }
        System.out.println((long) Cg[5] + " }\n");
    }

    public void writeStateFull() {
        System.out.print("The RngStream");
        if (descriptor.length() > 0) {
            System.out.print(" " + descriptor);
        }
        System.out.println(":\n   anti = " + (anti ? "true" : "false"));
        System.out.println("   prec53 = " + (prec53 ? "true" : "false"));

        System.out.print("   Ig = { ");
        for (int i = 0; i < 5; i++) {
            System.out.print((long) Ig[i] + ", ");
        }
        System.out.println((long) Ig[5] + " }");

        System.out.print("   Bg = { ");
        for (int i = 0; i < 5; i++) {
            System.out.print((long) Bg[i] + ", ");
        }
        System.out.println((long) Bg[5] + " }");

        System.out.print("   Cg = { ");
        for (int i = 0; i < 5; i++) {
            System.out.print((long) Cg[i] + ", ");
        }
        System.out.println((long) Cg[5] + " }\n");
    }

    public double randU01() {
        if (prec53) {
            return this.U01d();
        } else {
            return this.U01();
        }
    }

    public int randInt(int i, int j) {
        return (i + (int) (randU01() * (j - i + 1.0)));
    }

    public void setSeed(long arg0) {
        throw new UnsupportedOperationException("Do not use - " +
                "use setSeed(long[]) or (preferably) " +
                "ressetNextSubstream()");
    }

    public long getSeed() {
        throw new UnsupportedOperationException("Do not use");
    }

    public void resetSeed() {
        this.resetStartStream();
    }

    public void setSeeds(long[] seed) {
        boolean success = this.setSeed(seed);
        if (!success) {
            throw new IllegalArgumentException(
                    "Bad seed array");
        }
    }

    public long[] getSeeds() {
        double[] doubleSeed = getState();
        long[] seed = new long[doubleSeed.length];
        for (int i = 0; i < seed.length; ++i) {
            seed[i] = (long) doubleSeed[i];
        }
        return seed;
    }

    public double draw() {
        return randU01();
    }

    public long drawLong() {
        return (long) (randU01() / norm);
    }

    public double getMultiplier() {
        throw new UnsupportedOperationException("Not relevant to this generator");
    }
    
    public String toString() {
        return descriptor;
    }

    public int getStreamID() {
        return streamID;
    }

    public int getSubstreamID() {
        return substreamID;
    }
}

