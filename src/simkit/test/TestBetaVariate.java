/*
 * Main.java
 *
 * Created on March 8, 2002, 3:38 PM
 */
package simkit.test;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 *
 */
public class TestBetaVariate {

    public static void main(String args[]) {
        double alpha = 2.0;
        double beta = 3.0;
        RandomVariate rv = RandomVariateFactory.getInstance("Beta",
                alpha, beta);

        System.out.println(rv);

        int numberReps = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        SimpleStatsTally sst = new SimpleStatsTally("Beta Variate Test");

        for (int i = 0; i < numberReps; i++) {
            double next = rv.generate();
            sst.newObservation(next);
        }

        System.out.println(sst);
        System.out.println("true mean = " + alpha / (alpha + beta));
        System.out.println("true variance = " + alpha * beta / (Math.pow((alpha + beta), 2) * (alpha + beta + 1)));

        alpha = 3;
        beta = 4;
        double min = 1;
        double max = 5;

        rv = RandomVariateFactory.getInstance("Beta", alpha, beta, min, max);
        System.out.println(rv);

        double trueMean = min + (max - min) * alpha / (alpha + beta);
        double trueVar = alpha * beta / (Math.pow((alpha + beta), 2) * (alpha + beta + 1));
        trueVar *= (max - min) * (max - min);

        sst.reset();
        for (int i = 0; i < numberReps; i++) {
            double next = rv.generate();
            sst.newObservation(next);
        }
        System.out.println(sst);
        System.out.printf("true mean: %,.3f%n", trueMean);
        System.out.printf("true var: %,.3f%n", trueVar);

        rv = RandomVariateFactory.getInstance("Beta", alpha, beta, max);
        System.out.println(rv);

        trueMean = max * alpha / (alpha + beta);
        trueVar = alpha * beta / (Math.pow((alpha + beta), 2) * (alpha + beta + 1));
        trueVar *= max * max;

        sst.reset();
        for (int i = 0; i < numberReps; i++) {
            double next = rv.generate();
            sst.newObservation(next);
        }
        System.out.println(sst);
        System.out.printf("true mean: %,.3f%n", trueMean);
        System.out.printf("true var: %,.3f%n", trueVar);

    }

}
