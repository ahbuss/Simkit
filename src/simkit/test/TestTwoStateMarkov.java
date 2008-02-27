package simkit.test;
import java.text.DecimalFormat;

import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;
/**
 *
 * @author  Arnold Buss
 */
public class TestTwoStateMarkov {
    
    public static void main(String[] args) {
        double[] tp = new double[] { 0.4, 0.1 };
        double[] ss = new double[2];
        ss[0] = (1.0 - tp[1]) / (2.0 - tp[0] - tp[1]);
        ss[1] = (1.0 - tp[0]) / (2.0 - tp[0] - tp[1]);
               
        DiscreteRandomVariate rv = (DiscreteRandomVariate) 
            RandomVariateFactory.getInstance("TwoStateMarkov", tp, 1.2);
            
        System.out.println(rv);
        int numberObs = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        SimpleStatsTally stat = new SimpleStatsTally("Two State MC");
        int[] count = new int[2];
        
        for (int i = 0; i < numberObs; ++i) {
            int obs = rv.generateInt();
            if (i < 10) { System.out.print(obs); }
            count[obs]++;
            stat.newObservation(obs);
        }
        System.out.println();
        double[] oss = new double[2];
        oss[0] = (double) count[0] / numberObs;
        oss[1] = (double) count[1] / numberObs;
        
        DecimalFormat form = new DecimalFormat("0.0000");
        double theoreticalSSMean = (1.0 - tp[0]) / (2.0 - tp[0] - tp[1]); 
        System.out.println("Based on " + numberObs + " observations:");
        System.out.println("theoretical mean = " + form.format(theoreticalSSMean));
        System.out.println("observed mean = " + form.format(stat.getMean()));
        
        System.out.println("theoretical ss probs = [" + form.format(ss[0]) + ", " +
            form.format(ss[1]) + "]");
        System.out.println("observed ss probs = [" + form.format(oss[0]) + ", " +
            form.format(oss[1]) + "]");
        
    }
    
}
