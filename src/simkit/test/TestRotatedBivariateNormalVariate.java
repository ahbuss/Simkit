package simkit.test;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import simkit.random.RandomVector;
import simkit.random.RandomVectorFactory;
import simkit.stat.BivariateSimpleStatsTally;
import simkit.random.RotatedBivariateNormalVector;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestRotatedBivariateNormalVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        RandomVector rv = RandomVectorFactory.getInstance("RotatedBivariateNormal");
        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", Math.PI * 0.5);
        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", 3.4, 5.6);
        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", 3.4, 5.6, Math.PI * 0.5);
        System.out.println(rv);
//        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", -3.4, 5.6); 
//        System.out.println(rv);
        rv = RandomVectorFactory.getInstance("RotatedBivariateNormal", 3.4, 5.6, 7.8, 9.1, Math.PI * 0.5);
        System.out.println(rv);
        rv.setParameters(10.0, 50.0, 1.0, 4.0, -Math.PI * 0.0);
        System.out.println(rv);
        
        Point2D vector = new Point2D.Double(20.0, 10.0);
        double angle = Math.atan2(vector.getX(), vector.getY());
        
        rv.setParameters(vector.getX(), vector.getY(), 1.0, 4.0,  angle );
        System.out.println(rv);
                
        BivariateSimpleStatsTally bsst = new BivariateSimpleStatsTally(rv.toString());
        
        File outputFile = new File(System.getProperty("user.home"), "output.csv");
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.append("x,y");
        int number = 100000;
        for (int i = 0; i < number; ++i) {
            double[] gen = rv.generate();
            bufferedWriter.newLine();
            bufferedWriter.append(String.format("%f,%f", gen[0], gen[1]));
            bsst.newObservation(gen);
        }
        bufferedWriter.flush();
        fileWriter.close();
        
        System.out.println(bsst);
        
        RotatedBivariateNormalVector theRV = (RotatedBivariateNormalVector)rv;

        double[] actualVar = new double[] {
            Math.pow(theRV.getStandardDeviation(0) * Math.cos(angle), 2) +
            Math.pow(theRV.getStandardDeviation(1) * Math.sin(angle), 2),
            Math.pow(theRV.getStandardDeviation(0) * Math.sin(angle), 2) +
            Math.pow(theRV.getStandardDeviation(1) * Math.cos(angle), 2),
        };
        
        System.out.printf("Theoretical var: (%.3f, %.3f)%n", actualVar[0], actualVar[1]);
        
        double[] actualStd = new double[] {
            Math.sqrt(actualVar[0]), Math.sqrt(actualVar[1])
        };
        System.out.printf("Theoretical std dev: (%.3f, %.3f)%n", actualStd[0], actualStd[1]);
        
        double coeff = Math.pow(theRV.getStandardDeviation(1), 2) -
                Math.pow(theRV.getStandardDeviation(0), 2);
        
        double cov = coeff * 0.5 * Math.sin(2 * angle) ;
        System.out.printf("Theoretical cov: %.3f%n", cov);

        double theoreticalCorr = cov / (actualStd[0] * actualStd[1]);
        System.out.printf("Theoretical corr: %.3f%n", theoreticalCorr);
    }

}
