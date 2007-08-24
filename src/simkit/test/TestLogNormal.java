/*
 * TestLogNormal.java
 *
 * Created on November 8, 2001, 9:30 AM
 */

package simkit.test;
import java.util.Arrays;
import simkit.random.LogNormalVariate;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

public class TestLogNormal {
    public static void main(String args[]) {
        
        double mean = 0.0;
        double stdDev = 1.0;
        
        int numberObservations = 1000000;
        
        System.out.println("[0.0, 1.0] => " +
                Arrays.toString(LogNormalVariate.getLogNormalParameters(mean, stdDev)));
        
        double[] desiredParams = new double[] { 2.5, 1.6 };
        double[] normalParams = LogNormalVariate.getNormalParameters(desiredParams[0], 
                desiredParams[1]);
        double[] roundTrip = LogNormalVariate.getLogNormalParameters(normalParams[0], 
                normalParams[1]);
        
        System.out.println("Desired: " + Arrays.toString(desiredParams) );
        System.out.println("Derived Normal params: " + Arrays.toString(normalParams) );
        System.out.println("Transformed back: " + Arrays.toString(roundTrip) );
        
        
        RandomVariate norm = RandomVariateFactory.getInstance("simkit.random.NormalVariate",
                normalParams[0], normalParams[1] );
        RandomVariate logNormal = RandomVariateFactory.getInstance("ExponentialTransform",
                norm );
        SimpleStatsTally simpleStatsTally = new SimpleStatsTally(logNormal.toString());//        for (int i = 0 ; i <100; i++) {
        for (int i = 0; i < numberObservations; ++i) {
            simpleStatsTally.newObservation(logNormal.generate());
        }
        System.out.println(simpleStatsTally);
        logNormal = RandomVariateFactory.getInstance("LogNormal", 0.0, 1.0);
        
        simpleStatsTally = new SimpleStatsTally(logNormal.toString());
        for (int i = 0; i < numberObservations; ++i) {
            simpleStatsTally.newObservation(logNormal.generate());
        }
        System.out.println(simpleStatsTally);
        
        double[] meanAndStd = LogNormalVariate.getNormalParameters(1.5, 2.5);
        logNormal.setParameters(meanAndStd[0], meanAndStd[1]);
        
        System.out.println(Arrays.toString(LogNormalVariate.getLogNormalParameters(
                meanAndStd[0], meanAndStd[1])));
        
        simpleStatsTally = new SimpleStatsTally(logNormal.toString());
        for (int i = 0; i < numberObservations; ++i) {
            simpleStatsTally.newObservation(logNormal.generate());
        }
        System.out.println(simpleStatsTally);
    }    
}