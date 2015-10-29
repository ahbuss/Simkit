package simkit.test;

import java.io.IOException;
import java.util.Arrays;
import simkit.random.RandomVector;
import simkit.random.RandomVectorFactory;
import simkit.stat.BivariateSimpleStatsTally;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestBivariateNormalVector {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        RandomVectorFactory.addSearchPackage("simkit.ext.random");
        double[] mean = {1.0, 2.0};
        double[] stdDev = {0.5, 0.75};
        double correlation = -0.3;

        RandomVector rv = RandomVectorFactory.getInstance("BivariateNormalVector",
                mean, stdDev, correlation);

        System.out.println(rv);

        SimpleStatsTally[] sst = new SimpleStatsTally[]{
            new SimpleStatsTally("mean[0]"),
            new SimpleStatsTally("mean[1]"),};

        double cross = 0.0;

        int number = 1000000;
        for (int i = 0; i < number; ++i) {
            double[] gen = rv.generate();
            for (int j = 0; j < 2; ++j) {
                sst[j].newObservation(gen[j]);
            }
            cross += gen[0] * gen[1];
        }

        double cov = cross - sst[0].getCount() * sst[0].getMean() * sst[1].getMean();
        double corr = cov / ((sst[0].getCount() - 1) * (sst[0].getStandardDeviation() * sst[1].getStandardDeviation()));
        System.out.println(sst[0]);
        System.out.println(sst[1]);
        System.out.printf("Correlation = %.4f%n", corr);

        Object[] params = new Object[]{
            new double[]{1.0, 2.0},
            new double[]{1.5, 0.75},
            0.3
        };

        RandomVector randVect
                = RandomVectorFactory.getInstance("BivariateNormalVector",
                        params);

        System.out.println(randVect);

        cross = 0.0;
        for (int i = 0; i < sst.length; ++i) {
            sst[i].reset();
        }
        for (int i = 0; i < number; ++i) {
            double[] gen = randVect.generate();
            for (int j = 0; j < 2; ++j) {
                sst[j].newObservation(gen[j]);
            }
            cross += gen[0] * gen[1];
        }

        cov = cross - sst[0].getCount() * sst[0].getMean() * sst[1].getMean();
        corr = cov / ((sst[0].getCount() - 1) * (sst[0].getStandardDeviation() * sst[1].getStandardDeviation()));
        System.out.println(sst[0]);
        System.out.println(sst[1]);
        System.out.printf("Correlation = %.4f%n", corr);

        randVect.setParameters(-3.0, 4.5, 1.5, 2.0, -0.3);
        System.out.println(randVect);
        cross = 0.0;
        for (int i = 0; i < sst.length; ++i) {
            sst[i].reset();
        }
        for (int i = 0; i < number; ++i) {
            double[] gen = randVect.generate();
            for (int j = 0; j < 2; ++j) {
                sst[j].newObservation(gen[j]);
            }
            cross += gen[0] * gen[1];
        }

        cov = cross - sst[0].getCount() * sst[0].getMean() * sst[1].getMean();
        corr = cov / ((sst[0].getCount() - 1) * (sst[0].getStandardDeviation() * sst[1].getStandardDeviation()));
        System.out.println(sst[0]);
        System.out.println(sst[1]);
        System.out.printf("Correlation = %.4f%n", corr);
        
        
        randVect.setParameters(mean, stdDev, correlation);
        number = 10000000;
        cross = 0.0;
        BivariateSimpleStatsTally bsst = new BivariateSimpleStatsTally();
        for (int i = 0; i < sst.length; ++i) {
            sst[i].reset();
        }
        for (int i = 0; i < number; ++i) {
            double[] gen = randVect.generate();
            bsst.newObservation(gen);
            for (int j = 0; j < 2; ++j) {
                sst[j].newObservation(gen[j]);
            }
            cross += gen[0] * gen[1];
        }

        cov = cross - sst[0].getCount() * sst[0].getMean() * sst[1].getMean();
        corr = cov / ((sst[0].getCount() - 1) * (sst[0].getStandardDeviation() * sst[1].getStandardDeviation()));
        System.out.println(sst[0]);
        System.out.println(sst[1]);
        System.out.printf("Correlation = %.4f%n", corr);
        System.out.println(bsst);
        System.out.printf("Bivariate SST Correlation = %.4f%n", bsst.getCorrelation());
        double[] diff = new double[5];
        diff[0] = Math.abs(sst[0].getMean() - mean[0]);
        diff[1] = Math.abs(sst[1].getMean() - mean[1]);
        diff[2] = Math.abs(sst[0].getStandardDeviation() - stdDev[0]);
        diff[3] = Math.abs(sst[1].getStandardDeviation() - stdDev[1]);
        diff[4] = Math.abs(correlation - corr);
        
        System.out.printf("%s%n", Arrays.toString(diff));
    }

}
