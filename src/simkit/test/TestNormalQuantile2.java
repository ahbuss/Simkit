package simkit.test;

import simkit.stat.NormalQuantile;

/**
 * Expected output:
 * <pre>
p		0.00	0.01	0.02	0.03	0.04	0.05	0.06	0.07	0.08	0.09
0.5		0.000	0.025	0.050	0.075	0.100	0.126	0.151	0.176	0.202	0.228
0.6		0.253	0.279	0.305	0.332	0.358	0.385	0.412	0.440	0.468	0.496
0.7		0.524	0.553	0.583	0.613	0.643	0.674	0.706	0.739	0.772	0.806
0.8		0.842	0.878	0.915	0.954	0.994	1.036	1.080	1.126	1.175	1.227
* </pre>
* @author ahbuss
 */
public class TestNormalQuantile2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print("p\t");
        for (double second = 0.00; second <= 0.09; second += 0.01) {
            System.out.printf("\t%.2f", second);
        }
        for (double first = 0.5; first <= 0.8; first += 0.1) {
            System.out.printf("%n%.1f\t", first);
            for (double second = 0.00; second <= 0.09; second += 0.01) {
                System.out.printf("\t%.3f", NormalQuantile.getQuantile(first + second));
            }
        }
        System.out.println();
    }

}
